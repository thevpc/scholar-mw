package net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions;

import net.thevpc.common.log.Log;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;


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
        ComplexMatrix s = jxy.evalS(ProgressMonitors.none());
        Log.trace("[S (f=" + jxy.getStructureConfig().getFrequency()+ " Hz)] S= " + s);
        return s;
    }

}
