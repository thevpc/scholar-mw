package net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions;


import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public class BuildEstimateMNAction extends RunAction {
    private MomStrHelper jxy;

    int progress_percent_build = 0;
    int progress_percent_build_max;


    int max_mn;
    double delta;
    int thresholdLength;
    int trialCount;
    public BuildEstimateMNAction(MomStrHelper jxy, int max_mn, double delta, int thresholdLength, int trialCount) {
        this.jxy = jxy;
        this.max_mn = max_mn;
        this.max_mn = max_mn;
        this.delta = delta;
        this.thresholdLength = thresholdLength;
        this.trialCount = trialCount;
    }


    public int go0() {
        return (Integer)go();
    }

    public Object run() {
        return jxy.estimateMn(max_mn, delta, thresholdLength, trialCount, this);
    }
    

}
