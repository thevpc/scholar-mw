package net.thevpc.ntexup.extension.openems;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.nuts.command.NExec;
import net.thevpc.nuts.command.NExecutableInformation;
import net.thevpc.nuts.io.NCp;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.io.NPathPermission;
import net.thevpc.nuts.io.NUncompress;
import net.thevpc.nuts.platform.NEnv;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NOptional;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OpenEMSProvisioner {

    public static final String DEFAULT_DOCKER_IMAGE = "thevpc/openems:0.0.36";

    public static final String DEFAULT_DOCKERFILE =
            "FROM ubuntu:22.04\n" +
            "ENV DEBIAN_FRONTEND=noninteractive\n" +
            "ENV INSTALL_DIR=/usr/local\n" +
            "ENV OPENEMS_VERSION=v0.0.36\n\n" +
            "RUN apt-get update && apt-get install -y --no-install-recommends \\\n" +
            "    ca-certificates \\\n" +
            "    git wget build-essential cmake pkg-config \\\n" +
            "    libhdf5-dev libvtk7-dev \\\n" +
            "    libboost-all-dev libcgal-dev libtinyxml-dev \\\n" +
            "    gengetopt help2man groff bison flex libtool \\\n" +
            "    && rm -rf /var/lib/apt/lists/*\n\n" +
            "WORKDIR /root/\n\n" +
            "RUN git clone --recursive \\\n" +
            "    --branch ${OPENEMS_VERSION} \\\n" +
            "    https://github.com/thliebig/openEMS-Project.git\n\n" +
            "# Allow failure — openEMS binary installs at 72%, rest are optional bindings\n" +
            "RUN cd openEMS-Project && \\\n" +
            "    bash update_openEMS.sh ${INSTALL_DIR} || true\n\n" +
            "# Verify the binary actually exists — fail here if it doesn't\n" +
            "RUN test -f /usr/local/bin/openEMS && echo \"openEMS binary OK\"\n\n" +
            "CMD [\"openEMS\", \"--help\"]\n";

    public static boolean ensureDocker(String dockerImage, NTxRendererContext rendererContext, String logPrefix) {
        boolean dockerCmdExists = false;
        try {
            dockerCmdExists = NExec.ofSystem("docker").which() != null;
        } catch (Exception ignored) {
        }
        if (!dockerCmdExists) {
            return false;
        }

        // Check if docker daemon is running
        try {
            int code = NExec.ofSystem("docker", "info").failFast(false).grabAll().run().exitCode();
            if (code != 0) {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Docker command is available, but Docker daemon is not running. Falling back to native openEMS.", logPrefix));
                }
                return false;
            }
        } catch (Exception ex) {
            return false;
        }

        // Check if image exists locally
        boolean imagePresent = false;
        try {
            int code = NExec.ofSystem("docker", "image", "inspect", dockerImage).failFast(false).grabAll().run().exitCode();
            imagePresent = (code == 0);
        } catch (Exception ignored) {
        }

        if (imagePresent) {
            return true;
        }

        // Image not present locally: try pulling, or build from Dockerfile
        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[OpenEMS][%s] Docker image '%s' not found locally. Attempting pull...", logPrefix, dockerImage));
        }
        try {
            int pullCode = NExec.ofSystem("docker", "pull", dockerImage).failFast(false).run().exitCode();
            if (pullCode == 0) {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Docker image '%s' successfully pulled.", logPrefix, dockerImage));
                }
                return true;
            }
        } catch (Exception ex) {
            if (rendererContext != null) {
                rendererContext.log(NMsg.ofC("[OpenEMS][%s] Could not pull image '%s' (%s). Building locally...", logPrefix, dockerImage, ex.getMessage()));
            }
        }

        // Pull failed or image not on registry: build locally from Dockerfile
        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[OpenEMS][%s] Building Docker image '%s' from embedded Dockerfile...", logPrefix, dockerImage));
        }
        boolean built = buildDockerImage(dockerImage, rendererContext, logPrefix);
        if (built) {
            return true;
        }
        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[OpenEMS][%s] Failed to build docker image '%s'. Falling back to native openEMS.", logPrefix, dockerImage));
        }
        return false;
    }

    public static boolean buildDockerImage(String dockerImage, NTxRendererContext rendererContext, String logPrefix) {
        NPath buildDir = null;
        try {
            buildDir = NPath.ofTempFolder("openems-docker-build-");
            NPath dockerfile = buildDir.resolve("Dockerfile");
            URL res = OpenEMSProvisioner.class.getResource("Dockerfile");
            if (res != null) {
                NCp.of().from(res).to(dockerfile).run();
            } else {
                dockerfile.writeString(DEFAULT_DOCKERFILE);
            }

            NExec buildCmd = NExec.ofSystem("docker", "build", "-t", dockerImage, ".")
                    .directory(buildDir)
                    .failFast(false);
            int buildCode = buildCmd.run().exitCode();
            if (buildCode == 0) {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Successfully built Docker image '%s'.", logPrefix, dockerImage));
                }
                return true;
            }
        } catch (Exception ex) {
            if (rendererContext != null) {
                rendererContext.log(NMsg.ofC("[OpenEMS][%s] Exception building Docker image '%s': %s", logPrefix, dockerImage, ex.getMessage()));
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

    public static NPath ensureNativeBinary(NTxRendererContext rendererContext, String logPrefix) {
        NPath found = findNativeBinary();
        if (found != null && found.exists()) {
            return found;
        }
        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[OpenEMS][%s] Native openEMS binary not found. Attempting to provision...", logPrefix));
        }
        found = provisionNativeBinary(rendererContext, logPrefix);
        if (found != null && found.exists()) {
            return found;
        }
        throw new IllegalStateException(buildPlatformHelpMessage(NEnv.of().osFamily()));
    }

    public static String buildPlatformHelpMessage(net.thevpc.nuts.platform.NOsFamily os) {
        StringBuilder sb = new StringBuilder();
        sb.append("OpenEMS solver could not be found or provisioned.\n");
        sb.append("To resolve this, please either:\n");
        sb.append("  1. Start Docker (ntexup will automatically build or pull 'thevpc/openems:0.0.36'), OR\n");
        if (os.isWindow()) {
            sb.append("  2. Install openEMS on Windows:\n");
            sb.append("     - Download prebuilt openEMS_v0.0.36.zip from https://github.com/thliebig/openEMS-Project/releases\n");
            sb.append("     - Extract to C:\\openEMS\\ or ~/.ntexup/tools/openems/\n");
            sb.append("     - Or configure the OPENEMS_BIN environment variable pointing to openEMS.exe.\n");
        } else if (os.isMacOs()) {
            sb.append("  2. Install openEMS on macOS:\n");
            sb.append("     - Install using Homebrew: brew install openems (or brew tap thliebig/openEMS && brew install openems)\n");
            sb.append("     - Or install build prerequisites (`xcode-select --install` and `brew install cmake git`) so ntexup can compile it\n");
            sb.append("     - Or configure the OPENEMS_BIN environment variable pointing to the openEMS binary.\n");
        } else {
            // Linux / Unix
            sb.append("  2. Install openEMS on Linux:\n");
            sb.append("     - On openSUSE:      sudo zypper in openEMS\n");
            sb.append("     - On Debian/Ubuntu: sudo apt-get install openems (or: sudo apt-get install build-essential cmake git)\n");
            sb.append("     - On Fedora:        sudo dnf install openems (or: sudo dnf install gcc-c++ make cmake git)\n");
            sb.append("     - On Arch Linux:    sudo pacman -S openems\n");
            sb.append("     - Or configure the OPENEMS_BIN environment variable pointing to the openEMS binary.\n");
        }
        return sb.toString();
    }

    public static boolean isCommandAvailable(String commandName) {
        try {
            NOptional<NExecutableInformation> which = NExec.ofSystem(commandName).which();
            return which.isPresent();
        } catch (Exception ignored) {
            return false;
        }
    }

    public static List<String> findMissingBuildTools() {
        List<String> missing = new ArrayList<>();
        if (!isCommandAvailable("git")) {
            missing.add("git");
        }
        if (!isCommandAvailable("cmake")) {
            missing.add("cmake");
        }
        boolean hasMake = isCommandAvailable("make") || isCommandAvailable("gmake") || isCommandAvailable("ninja");
        if (!hasMake) {
            missing.add("make (or ninja)");
        }
        boolean hasCompiler = isCommandAvailable("g++") || isCommandAvailable("clang++")
                || isCommandAvailable("gcc") || isCommandAvailable("clang");
        if (!hasCompiler) {
            missing.add("C++ compiler (g++ or clang++)");
        }
        return missing;
    }

    public static NPath findNativeBinary() {
        NEnv env = NEnv.of();
        net.thevpc.nuts.platform.NOsFamily os = env.osFamily();
        boolean isWindows = os.isWindow();
        String exeName = isWindows ? "openEMS.exe" : "openEMS";

        // 1. Check OPENEMS_BIN or openems.bin
        String envBin = env.getEnv("OPENEMS_BIN").orNull();
        if (envBin == null || envBin.trim().isEmpty()) {
            envBin = System.getProperty("openems.bin");
        }
        if (envBin != null && !envBin.trim().isEmpty()) {
            NPath p = NPath.of(envBin.trim());
            if (p.exists()) return p;
        }

        // 2. Check OPENEMS_HOME or openems.home
        String envHome = env.getEnv("OPENEMS_HOME").orNull();
        if (envHome == null || envHome.trim().isEmpty()) {
            envHome = System.getProperty("openems.home");
        }
        if (envHome != null && !envHome.trim().isEmpty()) {
            NPath p = NPath.of(envHome.trim()).resolve("bin").resolve(exeName);
            if (p.exists()) return p;
            p = NPath.of(envHome.trim()).resolve(exeName);
            if (p.exists()) return p;
        }

        // 3. System PATH
        try {
            NExecutableInformation which = NExec.ofSystem(exeName).which().orNull();
            if (which != null && which.value() != null) {
                NPath p = NPath.of(which.value());
                if (p.exists()) return p;
            }
        } catch (Exception ignored) {
        }

        // 4. User tool directory: ~/.ntexup/tools/openems
        try {
            NPath toolsDir = NPath.ofUserHome().resolve(".ntexup/tools/openems");
            NPath inTree = findExecutableInTree(toolsDir, exeName);
            if (inTree != null && inTree.exists()) {
                return inTree;
            }
        } catch (Exception ignored) {
        }

        // 5. Standard platform locations
        List<NPath> candidates = new ArrayList<>();
        if (isWindows) {
            candidates.add(NPath.of("C:\\openEMS\\openEMS.exe"));
            candidates.add(NPath.of("C:\\openEMS\\bin\\openEMS.exe"));
            candidates.add(NPath.of("D:\\openEMS\\openEMS.exe"));
            candidates.add(NPath.of("D:\\openEMS\\bin\\openEMS.exe"));
            String userProfile = env.getEnv("USERPROFILE").orNull();
            if (userProfile != null && !userProfile.trim().isEmpty()) {
                candidates.add(NPath.of(userProfile.trim()).resolve("openEMS\\openEMS.exe"));
                candidates.add(NPath.of(userProfile.trim()).resolve("openEMS\\bin\\openEMS.exe"));
                candidates.add(NPath.of(userProfile.trim()).resolve("AppData\\Local\\openEMS\\openEMS.exe"));
            }
            String localAppData = env.getEnv("LOCALAPPDATA").orNull();
            if (localAppData != null && !localAppData.trim().isEmpty()) {
                candidates.add(NPath.of(localAppData.trim()).resolve("openEMS\\openEMS.exe"));
                candidates.add(NPath.of(localAppData.trim()).resolve("openEMS\\bin\\openEMS.exe"));
                candidates.add(NPath.of(localAppData.trim()).resolve("Programs\\openEMS\\openEMS.exe"));
            }
            String progFiles = env.getEnv("ProgramFiles").orNull();
            if (progFiles != null && !progFiles.trim().isEmpty()) {
                candidates.add(NPath.of(progFiles.trim()).resolve("openEMS\\openEMS.exe"));
                candidates.add(NPath.of(progFiles.trim()).resolve("openEMS\\bin\\openEMS.exe"));
            }
            String progFilesX86 = env.getEnv("ProgramFiles(x86)").orNull();
            if (progFilesX86 != null && !progFilesX86.trim().isEmpty()) {
                candidates.add(NPath.of(progFilesX86.trim()).resolve("openEMS\\openEMS.exe"));
                candidates.add(NPath.of(progFilesX86.trim()).resolve("openEMS\\bin\\openEMS.exe"));
            }
        } else if (os.isMacOs()) {
            candidates.add(NPath.of("/opt/homebrew/bin/openEMS"));
            candidates.add(NPath.of("/usr/local/bin/openEMS"));
            candidates.add(NPath.of("/opt/local/bin/openEMS"));
            candidates.add(NPath.of("/Applications/openEMS/bin/openEMS"));
            candidates.add(NPath.of("/Applications/openEMS/openEMS"));
            NPath home = NPath.ofUserHome();
            candidates.add(home.resolve("Applications/openEMS/bin/openEMS"));
            candidates.add(home.resolve("opt/openEMS/bin/openEMS"));
            candidates.add(home.resolve("openEMS/bin/openEMS"));
            candidates.add(home.resolve(".local/bin/openEMS"));
        } else {
            // Linux / Unix
            candidates.add(NPath.of("/usr/bin/openEMS"));
            candidates.add(NPath.of("/usr/local/bin/openEMS"));
            candidates.add(NPath.of("/opt/openEMS/bin/openEMS"));
            NPath home = NPath.ofUserHome();
            candidates.add(home.resolve("opt/openEMS/bin/openEMS"));
            candidates.add(home.resolve("openEMS/bin/openEMS"));
            candidates.add(home.resolve(".local/bin/openEMS"));
        }

        for (NPath p : candidates) {
            if (p.exists()) {
                return p;
            }
        }

        return null;
    }

    public static NPath provisionNativeBinary(NTxRendererContext rendererContext, String logPrefix) {
        NEnv env = NEnv.of();
        net.thevpc.nuts.platform.NOsFamily os = env.osFamily();
        boolean isWindows = os.isWindow();
        String exeName = isWindows ? "openEMS.exe" : "openEMS";
        NPath toolsDir = NPath.ofUserHome().resolve(".ntexup/tools/openems");

        if (isWindows) {
            String customUrl = env.getEnv("OPENEMS_DOWNLOAD_URL").orNull();
            if (customUrl == null || customUrl.trim().isEmpty()) {
                customUrl = System.getProperty("openems.download.url");
            }
            String winZipUrl = (customUrl != null && !customUrl.trim().isEmpty())
                    ? customUrl.trim()
                    : "https://github.com/thliebig/openEMS-Project/releases/download/v0.0.36/openEMS_v0.0.36.zip";
            try {
                downloadAndExtractZip(winZipUrl, toolsDir, rendererContext, logPrefix);
                NPath found = findExecutableInTree(toolsDir, exeName);
                if (found != null && found.exists()) {
                    return found;
                }
            } catch (Exception ex) {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Failed to download/extract openEMS Windows binary from %s: %s", logPrefix, winZipUrl, ex.getMessage()));
                }
            }

            // If zip download failed on Windows, check build tools if attempting compilation
            List<String> missing = findMissingBuildTools();
            if (rendererContext != null) {
                if (!missing.isEmpty()) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Cannot build openEMS from source on Windows: missing build tools (%s).", logPrefix, String.join(", ", missing)));
                }
                rendererContext.log(NMsg.ofC("[OpenEMS][%s] On Windows, please download openEMS_v0.0.36.zip manually from https://github.com/thliebig/openEMS-Project/releases and extract it to %s or C:\\openEMS", logPrefix, toolsDir));
            }
            return null;
        }

        // Linux or macOS
        String customUrl = env.getEnv("OPENEMS_DOWNLOAD_URL").orNull();
        if (customUrl == null || customUrl.trim().isEmpty()) {
            customUrl = System.getProperty("openems.download.url");
        }
        if (customUrl != null && !customUrl.trim().isEmpty()) {
            try {
                downloadAndExtractZip(customUrl.trim(), toolsDir, rendererContext, logPrefix);
                NPath found = findExecutableInTree(toolsDir, exeName);
                if (found != null && found.exists()) {
                    return found;
                }
            } catch (Exception ex) {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Failed to download/extract openEMS from custom URL: %s", logPrefix, ex.getMessage()));
                }
            }
        }

        // Check required build tools
        List<String> missingTools = findMissingBuildTools();
        if (!missingTools.isEmpty()) {
            if (rendererContext != null) {
                String missingList = String.join(", ", missingTools);
                rendererContext.log(NMsg.ofC("[OpenEMS][%s] Cannot build openEMS from source: missing required build tool(s): %s.", logPrefix, missingList));
                if (os.isMacOs()) {
                    rendererContext.log(NMsg.ofC(
                            "[OpenEMS][%s] To compile on macOS, install Xcode Command Line Tools (`xcode-select --install`) and CMake (`brew install cmake git`).\n" +
                            "[OpenEMS][%s] Alternatively, install openEMS directly using Homebrew: `brew install openems`.",
                            logPrefix, logPrefix
                    ));
                } else {
                    rendererContext.log(NMsg.ofC(
                            "[OpenEMS][%s] To compile on Linux, install missing tools (%s) using your package manager:\n" +
                            "  - Ubuntu/Debian: sudo apt-get update && sudo apt-get install -y build-essential cmake git libhdf5-dev libvtk7-dev libboost-all-dev libcgal-dev libtinyxml-dev\n" +
                            "  - Fedora/RHEL:   sudo dnf install -y gcc-c++ make cmake git hdf5-devel boost-devel cgal-devel tinyxml-devel\n" +
                            "  - openSUSE:      sudo zypper in cmake gcc-c++ make git\n" +
                            "  - Arch Linux:    sudo pacman -S --needed base-devel cmake git\n" +
                            "[OpenEMS][%s] Alternatively, install openEMS directly using your package manager:\n" +
                            "  - Ubuntu/Debian: sudo apt-get install openems\n" +
                            "  - Fedora/RHEL:   sudo dnf install openems\n" +
                            "  - openSUSE:      sudo zypper in openEMS\n" +
                            "  - Arch Linux:    sudo pacman -S openems",
                            logPrefix, missingList, logPrefix
                    ));
                }
            }
            return null;
        }

        // All build tools are present
        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[OpenEMS][%s] All required build tools detected. Attempting to build openEMS from source into %s ...", logPrefix, toolsDir));
        }
        NPath clonePath = null;
        try {
            toolsDir.mkdirs();
            clonePath = NPath.ofTempFolder("openems-git-");
            if (rendererContext != null) {
                rendererContext.log(NMsg.ofC("[OpenEMS][%s] Cloning openEMS-Project (v0.0.36) into temporary directory %s ...", logPrefix, clonePath));
            }
            NExec cloneCmd = NExec.ofSystem("git", "clone", "--recursive", "--depth", "1", "--branch", "v0.0.36",
                    "https://github.com/thliebig/openEMS-Project.git", clonePath.toString())
                    .failFast(false)
                    .grabAll();
            int cloneExit = cloneCmd.run().exitCode();
            if (cloneExit != 0) {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Git clone failed (exit code %d): %s", logPrefix, cloneExit, cloneCmd.grabbedAll()));
                }
                return null;
            }

            if (rendererContext != null) {
                rendererContext.log(NMsg.ofC("[OpenEMS][%s] Running update_openEMS.sh %s ...", logPrefix, toolsDir));
            }
            NExec buildCmd = NExec.ofSystem("bash", "update_openEMS.sh", toolsDir.toString())
                    .directory(clonePath)
                    .failFast(false)
                    .grabAll();
            int buildExit = buildCmd.run().exitCode();
            if (buildExit != 0) {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] update_openEMS.sh finished with exit code %d (openEMS binary might still be generated).", logPrefix, buildExit));
                }
            }
            NPath found = findExecutableInTree(toolsDir, exeName);
            if (found != null && found.exists()) {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Successfully built openEMS binary: %s", logPrefix, found));
                }
                return found;
            } else {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Compilation finished but '%s' was not found in %s.", logPrefix, exeName, toolsDir));
                }
            }
        } catch (Exception ex) {
            if (rendererContext != null) {
                rendererContext.log(NMsg.ofC("[OpenEMS][%s] Build from source failed: %s", logPrefix, ex.getMessage()));
            }
        } finally {
            if (clonePath != null) {
                try {
                    clonePath.deleteTree();
                } catch (Exception ignored) {
                }
            }
        }

        return null;
    }

    public static void downloadAndExtractZip(String urlString, NPath targetDir, NTxRendererContext rendererContext, String logPrefix) {
        targetDir.mkdirs();
        if (rendererContext != null) {
            rendererContext.log(NMsg.ofC("[OpenEMS][%s] Downloading and extracting %s to %s ...", logPrefix, urlString, targetDir));
        }
        NPath tempZip = NPath.ofTempFile("openems-download.zip");
        try {
            NCp.of().from(NPath.of(urlString)).to(tempZip).run();
            NUncompress.of().from(tempZip).to(targetDir).run();
        } finally {
            try {
                tempZip.delete();
            } catch (Exception ignored) {
            }
        }
    }

    public static NPath findExecutableInTree(NPath root, String exeName) {
        if (root == null || !root.exists()) {
            return null;
        }
        try {
            NPath found = root.walk(5)
                    .filter(p -> p.isRegularFile() && p.name().equalsIgnoreCase(exeName))
                    .findFirst()
                    .orNull();
            if (found != null) {
                found.addPermissions(NPathPermission.CAN_EXECUTE);
                return found;
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
