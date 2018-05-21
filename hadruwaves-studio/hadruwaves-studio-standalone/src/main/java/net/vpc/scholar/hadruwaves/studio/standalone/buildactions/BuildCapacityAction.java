package net.vpc.scholar.hadruwaves.studio.standalone.buildactions;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

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


    public Matrix go0() {
        return (Matrix) go();
    }

    public Matrix run() {
        return jxy.computeCapacity(this);
    }
    

}
