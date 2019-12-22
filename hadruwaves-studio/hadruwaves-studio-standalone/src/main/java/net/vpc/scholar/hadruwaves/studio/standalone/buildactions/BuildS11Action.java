package net.vpc.scholar.hadruwaves.studio.standalone.buildactions;

import net.vpc.common.log.Log;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;


/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public class BuildS11Action extends RunAction {
    private MomStrHelper jxy;

    public BuildS11Action(MomStrHelper jxy) {
        this.jxy = jxy;
    }


    public double getProgress() {
        return 1;
    }

    public ComplexMatrix go0() {
        return (ComplexMatrix) go();
    }

    public Object run() {
        ComplexMatrix s = jxy.computeS(net.vpc.common.mon.ProgressMonitorFactory.none());
        Log.trace("[S (f=" + jxy.getStructureConfig().getFrequency()+ " Hz)] S= " + s);
        return s;
    }

}
