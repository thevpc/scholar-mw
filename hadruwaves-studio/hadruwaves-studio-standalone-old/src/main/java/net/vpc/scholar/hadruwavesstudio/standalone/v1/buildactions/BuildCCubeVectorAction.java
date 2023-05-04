package net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigData;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;
import net.thevpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.thevpc.scholar.hadruwaves.mom.project.common.VarUnit;
import net.thevpc.scholar.hadruwaves.mom.project.common.VariableExpression;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;


/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public abstract class BuildCCubeVectorAction extends RunAction {
    private MomStrHelper helper;
    double[] varValues;
    private String varName;
    private int progress_percent_build;

    public BuildCCubeVectorAction(MomStrHelper helper, PlotConfigData config) {
        this.helper = helper;
        varName=config.getIteratorName();
        varValues=config.getIteratorValues();
    }

//    @Override
    public double getProgress() {
        double max=1;
        if(varName==null || varName.trim().length()==0 || varValues==null|| varValues.length==0){
            max=1;
        }else{
            max=varValues.length;
        }
        double d=super.getProgressValue();
        return (progress_percent_build+d)/max;
    }



    @Override
    public final Object run() {
        if(varName==null || varName.trim().length()==0 || varValues==null|| varValues.length==0){
            return new VDiscrete[]{run(helper)};
        }
        VDiscrete[] ret = new VDiscrete[varValues.length];
        MomProject  ctx = helper.getStructureConfig();
        VariableExpression expression = ctx.getVariableExpression(varName);
        if(expression==null){
            ctx.setExpression(new VariableExpression(varName,"0", VarUnit.NUMBER, "Default Iterator"));
        }
        for (int i = 0; i < ret.length; i++) {
            ctx = helper.getStructureConfig();
            ctx.updateExpression(varName,String.valueOf(varValues[i]));
            helper.recompile();
            ret[i] = run(helper);
            progress_percent_build++;
        }
        return ret;
    }

    public abstract VDiscrete run(MomStrHelper helper) ;
}