package net.vpc.scholar.hadruwaves.solvers;

import net.vpc.scholar.hadruwaves.project.scene.HWScene;

public class AbstractHWSolver implements HWSolver{
    private HWScene scene;

    public HWScene getScene() {
        return scene;
    }

    public HWSolver setScene(HWScene scene) {
        this.scene = scene;
        return this;
    }
}
