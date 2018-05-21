/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.project.gpmesher;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.vpc.scholar.hadruwaves.mom.project.AbstractMomProjectItem;
import net.vpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public abstract class RectGpMesher extends AbstractMomProjectItem implements GpMesher{
    private Axis invariance;
    private TestFunctionsSymmetry symmetry;
    public RectGpMesher() {
    }

    public void setInvariance(Axis invariance) {
        this.invariance = invariance;
    }

    public void setSymmetry(TestFunctionsSymmetry symmetry) {
        this.symmetry = symmetry;
    }

    public Axis getInvariance() {
        return invariance;
    }

    public TestFunctionsSymmetry getSymmetry() {
        return symmetry;
    }
    

    public void load(Configuration conf, String key) {
        invariance = conf.getString(key + ".invariance")==null?null:Axis.valueOf(conf.getString(key + ".invariance"));
        symmetry = conf.getString(key + ".symmetry")==null?null: TestFunctionsSymmetry.valueOf(conf.getString(key + ".symmetry"));
    }

    public void store(Configuration c, String key) {
        c.setString(key + ".invariance", invariance==null?null:invariance.toString());
        c.setString(key + ".symmetry", symmetry==null?null:symmetry.toString());
    }

}
