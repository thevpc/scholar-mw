package net.vpc.scholar.hadruwaves.studio.standalone.buildactions;

import java.util.ArrayList;
import net.vpc.common.log.Log;

import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: vpc
 * Date: 21 mars 2005
 * Time: 13:15:57
 */
public class BuildDurationJCoeffAction extends AbstractBuildJCoeffSerieAction {
    ArrayList<Long> durations;
    public BuildDurationJCoeffAction(MomStrHelper jxy, String iterVarName, double iterVarNameMinValue, double iterVarNameMaxValue, int iterVarNameCountValue) {
        super(jxy, iterVarName, iterVarNameMinValue, iterVarNameMaxValue, iterVarNameCountValue);
    }


    protected void initIterations() {
        durations=new ArrayList<Long>();
    }

    protected boolean iteration(double i) {
        long period=getJxy().getLoggedStatistics(BuildJcoeffAction.class.getName());
        Log.trace("[BuildDurationJCoeffAction] "+period);
        durations.add(period);
        return false;
    }
    protected Object returnValue() {
        return durations.toArray(new Long[durations.size()]);
    }
}
