package net.vpc.scholar.hadruwaves.studio.standalone.buildactions;

import net.vpc.common.log.Log;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.common.util.mon.ProgressMonitorFactory;
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

    public Matrix go0() {
        return (Matrix) go();
    }

    public Object run() {
        Matrix s = jxy.computeS(ProgressMonitorFactory.none());
        Log.trace("[S (f=" + jxy.getStructureConfig().getFrequency()+ " Hz)] S= " + s);
        return s;
    }

}
