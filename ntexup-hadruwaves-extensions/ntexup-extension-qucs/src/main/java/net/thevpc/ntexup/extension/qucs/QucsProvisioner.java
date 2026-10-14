package net.thevpc.ntexup.extension.qucs;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxDockerProvisioner;
import net.thevpc.nuts.io.NPath;

import java.net.URL;
import java.util.List;

public class QucsProvisioner {

    public static final String DEFAULT_DOCKER_IMAGE = "thevpc/qucs-core:0.0.20";

    public static final String DEFAULT_DOCKERFILE =
            "FROM ubuntu:22.04\n" +
            "ENV DEBIAN_FRONTEND=noninteractive\n" +
            "RUN apt-get update -qq && \\\n" +
            "    apt-get install -y -qq --no-install-recommends \\\n" +
            "        build-essential cmake flex bison gperf dos2unix git ca-certificates && \\\n" +
            "    git clone --depth 1 https://github.com/ra3xdh/qucsator_rf.git /tmp/qucsator_rf && \\\n" +
            "    mkdir -p /tmp/qucsator_rf/build && cd /tmp/qucsator_rf/build && \\\n" +
            "    cmake -DCMAKE_BUILD_TYPE=Release .. && make -j$(nproc) && make install && \\\n" +
            "    ln -s /usr/local/bin/qucsator_rf /usr/local/bin/qucsator && \\\n" +
            "    rm -rf /tmp/qucsator_rf && \\\n" +
            "    apt-get purge -y --auto-remove build-essential cmake flex bison gperf dos2unix git && \\\n" +
            "    rm -rf /var/lib/apt/lists/*\n" +
            "ENTRYPOINT [\"qucsator\"]\n" +
            "CMD [\"--help\"]\n";

    public static boolean ensureDocker(String dockerImage, NTxRendererContext rendererContext, String logPrefix) {
        URL dockerfileRes = QucsProvisioner.class.getResource("Dockerfile");
        return NTxDockerProvisioner.ensureDockerImage(
                dockerImage != null ? dockerImage : DEFAULT_DOCKER_IMAGE,
                DEFAULT_DOCKERFILE,
                dockerfileRes,
                rendererContext,
                "Qucs][" + logPrefix
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
                "Qucs][" + logPrefix
        );
    }
}
