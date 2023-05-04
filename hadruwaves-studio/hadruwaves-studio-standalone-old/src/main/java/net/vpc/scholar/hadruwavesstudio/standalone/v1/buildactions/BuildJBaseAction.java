package net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigData;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;

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
        return helper.evalCurrent(x, y, this);
    }
    
}
