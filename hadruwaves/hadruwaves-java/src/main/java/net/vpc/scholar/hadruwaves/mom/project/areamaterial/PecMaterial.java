/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.project.areamaterial;

import net.vpc.scholar.hadruwaves.mom.project.AbstractMomProjectItem;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectFactory;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.GpMesher;
import net.vpc.scholar.hadrumaths.util.Configuration;

/**
 *
 * @author vpc
 */
public class PecMaterial extends AbstractMomProjectItem implements AreaMaterial {

    public PecMaterial() {
    }
    private GpMesher gpMesher;

    public void load(Configuration conf, String key) {
        gpMesher = (GpMesher) MomProjectFactory.INSTANCE.load(conf, key + ".gpMesher");
    }

    public void store(Configuration c, String key) {
        MomProjectFactory.INSTANCE.store(c, key + ".gpMesher", gpMesher);
    }

    public String getName() {
        return "PEC";
    }

    public String getId() {
        return "PecMaterial";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PecMaterial other = (PecMaterial) obj;
        return true;
    }

    @Override
    public void recompile() {
        super.recompile();
        if (gpMesher != null) {
            gpMesher.setContext(getContext());
            gpMesher.recompile();
        }
    }

    public GpMesher getGpMesher() {
        return gpMesher;
    }

    public void setGpMesher(GpMesher gpMesher) {
        this.gpMesher = gpMesher;
    }
}
