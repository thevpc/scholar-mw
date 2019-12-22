package net.vpc.scholar.hadruwaves.studio.standalone.buildactions;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.PlotConfigData;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public class BuildJBaseAction extends BuildCCubeVectorAction {
    double[] x;
    double[] y;
    public BuildJBaseAction(MomStrHelper helper, PlotConfigData config,double[] x, double[] y) {
        super(helper, config);
        this.x = x;
        this.y = y;
    }

    @Override
    public VDiscrete run(MomStrHelper helper) {
        return helper.computeCurrent(x, y, this);
    }
    
}
