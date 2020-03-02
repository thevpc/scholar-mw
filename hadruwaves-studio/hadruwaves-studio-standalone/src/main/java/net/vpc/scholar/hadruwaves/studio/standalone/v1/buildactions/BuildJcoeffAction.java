package net.vpc.scholar.hadruwaves.studio.standalone.v1.buildactions;

import net.vpc.scholar.hadrumaths.ComplexVector;
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

    public ComplexVector go0() {
        return (ComplexVector) go();
    }

    @Override
    protected Object run() {
        return jxy.evalXMatrix(this);
    }
    
}
