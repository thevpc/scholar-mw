package net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public class BuildCapacityAction extends RunAction {
    private MomStrHelper jxy;

    public BuildCapacityAction(MomStrHelper jxy) {
        this.jxy = jxy;
        
    }


    public ComplexMatrix go0() {
        return (ComplexMatrix) go();
    }

    public ComplexMatrix run() {
        return jxy.evalCapacity(this);
    }
    

}
