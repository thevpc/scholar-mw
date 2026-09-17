package net.thevpc.ntexup.extension.getdp;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxDockerProvisioner;
import net.thevpc.nuts.io.NPath;

import java.net.URL;
import java.util.List;

public class GetDPProvisioner {

    public static final String DEFAULT_DOCKER_IMAGE = "thevpc/getdp:3.2.0";

    public static final String DEFAULT_DOCKERFILE =
            "FROM ubuntu:22.04\n" +
            "ENV DEBIAN_FRONTEND=noninteractive\n" +
            "RUN apt-get update && apt-get install -y --no-install-recommends \\\n" +
            "    ca-certificates getdp gmsh \\\n" +
            "    && rm -rf /var/lib/apt/lists/*\n" +
            "CMD [\"getdp\", \"--help\"]\n";

    public static boolean ensureDocker(String dockerImage, NTxRendererContext rendererContext, String logPrefix) {
        URL dockerfileRes = GetDPProvisioner.class.getResource("Dockerfile");
        return NTxDockerProvisioner.ensureDockerImage(
                dockerImage != null ? dockerImage : DEFAULT_DOCKER_IMAGE,
                DEFAULT_DOCKERFILE,
                dockerfileRes,
                rendererContext,
                "GetDP][" + logPrefix
        );
    }

    public static int runInDocker(String dockerImage, NPath hostWorkDir, String containerWorkDir,
                                  List<String> command, NTxRendererContext rendererContext, String logPrefix) {
        return NTxDockerProvisioner.runInDocker(
                dockerImage != null ? dockerImage : DEFAULT_DOCKER_IMAGE,
                hostWorkDir,
                containerWorkDir,
                command,
                rendererContext,
                "GetDP][" + logPrefix
        );
    }
}
