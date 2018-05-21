package net.vpc.scholar.hadruwaves.studio.standalone.buildactions;

import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;


/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public class BuildJcoeffAction extends RunAction{
    private MomStrHelper jxy;
    public BuildJcoeffAction(MomStrHelper jxy) {
        this.jxy = jxy;
    }

    public Vector go0() {
        return (Vector) go();
    }

    @Override
    protected Object run() {
        return jxy.computeTestcoeff(this);
    }
    
}
