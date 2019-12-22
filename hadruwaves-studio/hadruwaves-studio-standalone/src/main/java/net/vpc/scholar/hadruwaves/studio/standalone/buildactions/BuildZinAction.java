package net.vpc.scholar.hadruwaves.studio.standalone.buildactions;

import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public class BuildZinAction extends RunAction {
    private MomStrHelper jxy;

    public BuildZinAction(MomStrHelper jxy) {
        this.jxy = jxy;

    }


    public ComplexMatrix go0() {
        return (ComplexMatrix) go();
    }

    public ComplexMatrix run() {
        return jxy.computeZin(this);
    }


}
