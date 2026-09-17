package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.nuts.command.NExec;
import net.thevpc.nuts.io.NCp;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.text.NMsg;

import java.net.URL;
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
        List<String> args = new ArrayList<>();
        args.add("docker");
        args.add("run");
        args.add("--rm");
        args.add("-v");
        args.add(hostWorkDir.toAbsolute().toString() + ":" + containerWorkDir);
        args.add("-w");
        args.add(containerWorkDir);
        args.add(dockerImage);
        args.addAll(command);

        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[%s] Running in Docker: %s", logPrefix, String.join(" ", command)));
        }

        NExec exec = NExec.ofSystem(args.toArray(new String[0]))
                .directory(hostWorkDir)
                .failFast(false);
        return exec.run().exitCode();
    }
}
