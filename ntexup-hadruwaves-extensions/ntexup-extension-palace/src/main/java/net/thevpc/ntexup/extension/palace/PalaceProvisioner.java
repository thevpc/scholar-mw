package net.thevpc.ntexup.extension.palace;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxDockerProvisioner;

public class PalaceProvisioner {

    /** Palace v0.18.1 built with its own CMake superbuild (Dockerfile next to this class) */
    public static final String PALACE_DOCKER_IMAGE = "thevpc/palace:0.18.1";
    /** gmsh, to mesh the model (Dockerfile-gmsh) */
    public static final String GMSH_DOCKER_IMAGE = "thevpc/gmsh:4.8";

    public static boolean ensureDocker(NTxRendererContext rendererContext, String logPrefix) {
        return NTxDockerProvisioner.ensureDockerImage(GMSH_DOCKER_IMAGE, null,
                PalaceProvisioner.class.getResource("Dockerfile-gmsh"), rendererContext, "Palace][" + logPrefix)
                && NTxDockerProvisioner.ensureDockerImage(PALACE_DOCKER_IMAGE, null,
                PalaceProvisioner.class.getResource("Dockerfile"), rendererContext, "Palace][" + logPrefix);
    }
}
