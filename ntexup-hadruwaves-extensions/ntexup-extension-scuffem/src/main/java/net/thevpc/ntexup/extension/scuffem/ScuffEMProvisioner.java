package net.thevpc.ntexup.extension.scuffem;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxDockerProvisioner;
import net.thevpc.nuts.io.NPath;

import java.net.URL;
import java.util.List;

public class ScuffEMProvisioner {

    public static final String DEFAULT_DOCKER_IMAGE = "thevpc/scuff-em:latest";

    public static final String DEFAULT_DOCKERFILE =
            "FROM jfeist/scuff-em:latest\n" +
            "USER root\n" +
            "ENV DEBIAN_FRONTEND=noninteractive\n" +
            "RUN apt-get update && apt-get install -y --no-install-recommends gmsh \\\n" +
            "    && rm -rf /var/lib/apt/lists/*\n" +
            "ENTRYPOINT [\"/usr/local/bin/scuff\"]\n";

    public static boolean ensureDocker(String dockerImage, NTxRendererContext rendererContext, String logPrefix) {
        URL dockerfileRes = ScuffEMProvisioner.class.getResource("Dockerfile");
        return NTxDockerProvisioner.ensureDockerImage(
                dockerImage != null ? dockerImage : DEFAULT_DOCKER_IMAGE,
                DEFAULT_DOCKERFILE,
                dockerfileRes,
                rendererContext,
                "SCUFF-EM][" + logPrefix
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
                "SCUFF-EM][" + logPrefix
        );
    }
}
