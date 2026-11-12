package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.nuts.command.NExec;
import net.thevpc.nuts.io.NCp;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.text.NMsg;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reusable Docker provisioning and container execution helper for NTexUp
 * simulation extensions (OpenEMS, GetDP, Qucs, etc.).
 */
public class NTxDockerProvisioner {

    public static boolean isDockerCommandAvailable() {
        try {
            return NExec.ofSystem("docker").which() != null;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean isDockerDaemonRunning() {
        if (!isDockerCommandAvailable()) {
            return false;
        }
        try {
            int code = NExec.ofSystem("docker", "info").failFast(false).grabAll().run().exitCode();
            return code == 0;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean isImageAvailable(String dockerImage) {
        try {
            int code = NExec.ofSystem("docker", "image", "inspect", dockerImage).failFast(false).grabAll().run().exitCode();
            return code == 0;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean ensureDockerImage(String dockerImage, String defaultDockerfile, URL dockerfileResource,
                                            NTxRendererContext rendererContext, String logPrefix) {
        if (!isDockerCommandAvailable()) {
            return false;
        }
        if (!isDockerDaemonRunning()) {
            if (rendererContext != null) {
                rendererContext.log(NMsg.ofC("[%s] Docker command is available, but Docker daemon is not running.", logPrefix));
            }
            return false;
        }

        if (isImageAvailable(dockerImage)) {
            return true;
        }

        // Image not present locally: try pulling, or build from Dockerfile
        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[%s] Docker image '%s' not found locally. Attempting pull...", logPrefix, dockerImage));
        }
        try {
            int pullCode = NExec.ofSystem("docker", "pull", dockerImage).failFast(false).run().exitCode();
            if (pullCode == 0) {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[%s] Docker image '%s' successfully pulled.", logPrefix, dockerImage));
                }
                return true;
            }
        } catch (Exception ex) {
            if (rendererContext != null) {
                rendererContext.log(NMsg.ofC("[%s] Could not pull image '%s' (%s). Building locally...", logPrefix, dockerImage, ex.getMessage()));
            }
        }

        // Pull failed or image not on registry: build locally from Dockerfile
        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[%s] Building Docker image '%s' from embedded Dockerfile...", logPrefix, dockerImage));
        }
        boolean built = buildDockerImage(dockerImage, defaultDockerfile, dockerfileResource, rendererContext, logPrefix);
        if (built) {
            return true;
        }
        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[%s] Failed to build docker image '%s'.", logPrefix, dockerImage));
        }
        return false;
    }

    public static boolean buildDockerImage(String dockerImage, String defaultDockerfile, URL dockerfileResource,
                                           NTxRendererContext rendererContext, String logPrefix) {
        NPath buildDir = null;
        try {
            buildDir = NPath.ofTempFolder("ntexup-docker-build-");
            NPath dockerfile = buildDir.resolve("Dockerfile");
            if (dockerfileResource != null) {
                NCp.of().from(dockerfileResource).to(dockerfile).run();
            } else if (defaultDockerfile != null) {
                dockerfile.writeString(defaultDockerfile);
            } else {
                throw new IllegalArgumentException("No Dockerfile content or resource provided");
            }

            NExec buildCmd = NExec.ofSystem("docker", "build", "-t", dockerImage, ".")
                    .directory(buildDir)
                    .failFast(false);
            int buildCode = buildCmd.run().exitCode();
            if (buildCode == 0) {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[%s] Successfully built Docker image '%s'.", logPrefix, dockerImage));
                }
                return true;
            }
        } catch (Exception ex) {
            if (rendererContext != null) {
                rendererContext.log(NMsg.ofC("[%s] Exception building Docker image '%s': %s", logPrefix, dockerImage, ex.getMessage()));
            }
        } finally {
            if (buildDir != null) {
                try {
                    buildDir.deleteTree();
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }

    public static int runInDocker(String dockerImage, NPath hostWorkDir, String containerWorkDir,
                                  List<String> command, NTxRendererContext rendererContext, String logPrefix) {
        return runInDocker(dockerImage, hostWorkDir, containerWorkDir, command, false, rendererContext, logPrefix);
    }

    /**
     * Needed for images that declare a CLI ENTRYPOINT (thevpc/scuff-em), which would otherwise
     * consume the requested program as one of their own subcommands.
     */
    public static int runInDockerViaEntrypoint(String dockerImage, NPath hostWorkDir, String containerWorkDir,
                                               List<String> command, NTxRendererContext rendererContext, String logPrefix) {
        return runInDocker(dockerImage, hostWorkDir, containerWorkDir, command, true, rendererContext, logPrefix);
    }

    private static int runInDocker(String dockerImage, NPath hostWorkDir, String containerWorkDir,
                                   List<String> command, boolean useCommandAsEntrypoint,
                                   NTxRendererContext rendererContext, String logPrefix) {
        NTxSolverCost cost = NTxSolverCost.current();
        Path cidFile = null;
        if (cost != null) {
            try {
                cidFile = Files.createTempFile("ntx-docker-", ".cid");
                Files.delete(cidFile); // docker refuses an existing cid file
            } catch (IOException e) {
                cidFile = null;
            }
        }
        List<String> args = new ArrayList<>();
        args.add("docker");
        args.add("run");
        args.add("--rm");
        if (cidFile != null) {
            args.add("--cidfile");
            args.add(cidFile.toString());
        }
        // every solver container is capped, swap included: a solver that needs more is
        // killed (exit 137) instead of taking the machine down
        args.add("--memory=" + solverMemoryLimit());
        args.add("--memory-swap=" + solverMemoryLimit());
        args.add("-v");
        args.add(hostWorkDir.toAbsolute().toString() + ":" + containerWorkDir);
        args.add("-w");
        args.add(containerWorkDir);
        List<String> tail = command;
        if (useCommandAsEntrypoint && !command.isEmpty()) {
            args.add("--entrypoint");
            args.add(command.get(0));
            tail = command.subList(1, command.size());
        }
        args.add(dockerImage);
        args.addAll(tail);

        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[%s] Running in Docker: %s", logPrefix, String.join(" ", command)));
        }

        NExec exec = NExec.ofSystem(args.toArray(new String[0]))
                .directory(hostWorkDir)
                .failFast(false);
        if (cidFile == null) {
            return logMemoryKill(exec.run().exitCode(), rendererContext, logPrefix);
        }
        PeakMemoryPoller poller = new PeakMemoryPoller(cidFile, cost);
        poller.start();
        try {
            return logMemoryKill(exec.run().exitCode(), rendererContext, logPrefix);
        } finally {
            poller.stopPolling();
        }
    }

    /** Docker memory limit of every solver container; NTX_SOLVER_MEMORY overrides the 20g default. */
    public static String solverMemoryLimit() {
        String v = System.getenv("NTX_SOLVER_MEMORY");
        return v == null || v.trim().isEmpty() ? "20g" : v.trim();
    }

    private static int logMemoryKill(int code, NTxRendererContext rendererContext, String logPrefix) {
        if (code == 137 && rendererContext != null) {
            rendererContext.log(NMsg.ofC("[%s] solver container killed (exit 137): most likely it exceeded the %s memory limit",
                    logPrefix, solverMemoryLimit()).asError());
        }
        return code;
    }

    /**
     * Samples the container's cgroup v2 working set every two seconds: {@code memory.current}
     * less the reclaimable file cache ({@code inactive_file} in {@code memory.stat}), as
     * {@code docker stats} does. {@code memory.peak} is not used: it counts that cache, which
     * grows until the limit, so every long run would report the cap. Sampling can miss a
     * spike shorter than two seconds.
     */
    private static class PeakMemoryPoller extends Thread {
        private final Path cidFile;
        private final NTxSolverCost cost;
        private volatile boolean running = true;

        PeakMemoryPoller(Path cidFile, NTxSolverCost cost) {
            super("ntx-docker-mem");
            setDaemon(true);
            this.cidFile = cidFile;
            this.cost = cost;
        }

        @Override
        public void run() {
            String cid = null;
            while (running) {
                try {
                    if (cid == null && Files.exists(cidFile)) {
                        String s = new String(Files.readAllBytes(cidFile), StandardCharsets.UTF_8).trim();
                        cid = s.isEmpty() ? null : s;
                    }
                    if (cid != null) {
                        for (String p : new String[]{
                                "/sys/fs/cgroup/system.slice/docker-" + cid + ".scope",
                                "/sys/fs/cgroup/docker/" + cid}) {
                            Path current = Paths.get(p, "memory.current");
                            if (Files.isReadable(current)) {
                                long used = Long.parseLong(new String(Files.readAllBytes(current), StandardCharsets.UTF_8).trim());
                                cost.recordPeak(used - inactiveFile(Paths.get(p, "memory.stat")));
                                break;
                            }
                        }
                    }
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                } catch (Exception ignored) {
                    // the container may exit between the check and the read
                }
            }
        }

        private static long inactiveFile(Path stat) throws IOException {
            for (String line : Files.readAllLines(stat, StandardCharsets.UTF_8)) {
                if (line.startsWith("inactive_file ")) {
                    return Long.parseLong(line.substring("inactive_file ".length()).trim());
                }
            }
            return 0;
        }

        void stopPolling() {
            running = false;
            interrupt();
            try {
                Files.deleteIfExists(cidFile);
            } catch (IOException ignored) {
            }
        }
    }
}
