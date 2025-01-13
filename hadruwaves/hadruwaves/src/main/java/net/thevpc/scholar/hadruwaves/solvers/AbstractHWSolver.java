package net.thevpc.scholar.hadruwaves.solvers;

import net.thevpc.scholar.hadruwaves.project.scene.HWProjectScene;

public abstract class AbstractHWSolver implements HWSolver {

    private HWProjectScene scene;

    public HWProjectScene getScene() {
        return scene;
    }

    public HWSolver setScene(HWProjectScene scene) {
        this.scene = scene;
        return this;
    }
}
