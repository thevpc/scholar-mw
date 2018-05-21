package net.vpc.scholar.hadruwaves.studio.standalone.buildactions;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigData;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public class BuildEBaseAction extends BuildCCubeVectorAction {
    double[] x;
    double[] y;
    double[] z;
    public BuildEBaseAction(MomStrHelper helper, PlotConfigData config, double[] x, double[] y,double[] z) {
        super(helper, config);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public VDiscrete run(MomStrHelper helper) {
        return helper.computeElectricField(x, y, z,this);
    }


}
