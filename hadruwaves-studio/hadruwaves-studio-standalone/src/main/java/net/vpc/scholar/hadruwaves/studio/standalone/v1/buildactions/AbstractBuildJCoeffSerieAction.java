package net.vpc.scholar.hadruwaves.studio.standalone.v1.buildactions;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruwaves.mom.project.common.RunAction;
import net.vpc.scholar.hadruwaves.mom.project.common.VariableExpression;
import net.vpc.scholar.hadruwaves.mom.str.RequiredRebuildException;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;


/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public abstract class AbstractBuildJCoeffSerieAction extends RunAction {
    private MomStrHelper jxy;
    private RunAction buildJcoeffMinimumMemoryAction;
    int progress_percent_build = 0;
    int progress_percent_build_max;

    String iterVarName;
    double iterVarNameMinValue;
    double iterVarNameMaxValue;
    int iterVarNameCountValue;
    RunAction innerAction;

    public AbstractBuildJCoeffSerieAction(MomStrHelper jxy,String iterVarName,double iterVarNameMinValue,double iterVarNameMaxValue,int iterVarNameCountValue) {
        this.jxy = jxy;
        this.iterVarName=iterVarName;
        this.iterVarNameMinValue=iterVarNameMinValue;
        this.iterVarNameMaxValue=iterVarNameMaxValue;
        this.iterVarNameCountValue=iterVarNameCountValue;
        progress_percent_build_max=iterVarNameCountValue;
    }


    public double getProgress() {
        return
                (((double) progress_percent_build) / ((double) progress_percent_build_max))+
                0.1*(buildJcoeffMinimumMemoryAction==null?
                (innerAction==null? 0 : innerAction.getProgress()) :
                (buildJcoeffMinimumMemoryAction.getProgress()*0.5+(innerAction==null? 0 : innerAction.getProgress()))
                );
    }

    public Complex go0() {
        return (Complex) go();
    }

    public Object run() {
        VariableExpression oldExpression = jxy.getStructureConfig().getVariableExpression(iterVarName);
        VariableExpression newExpression = (VariableExpression) oldExpression.clone();
        // compute
        double[] indices= Maths.dtimes(iterVarNameMinValue,iterVarNameMaxValue,iterVarNameCountValue);
        initIterations();
        for(int j=0;j<indices.length;j++){
            progress_percent_build=j;
            double i=indices[j];
            newExpression.setExpression(String.valueOf(i));
            jxy.getStructureConfig().setExpression(newExpression);
            jxy.recompile();
            buildJcoeffMinimumMemoryAction=null;
            try {
                jxy.checkBuildIsRequired();
            } catch (RequiredRebuildException e) {
                buildJcoeffMinimumMemoryAction=new BuildJcoeffAction(jxy);
                buildJcoeffMinimumMemoryAction.go();
            }
            if(iteration(i)){
                break;
            }
        }
        // restore old values
        jxy.getStructureConfig().setExpression(oldExpression);
        jxy.recompile();
        return returnValue();
    }

    public MomStrHelper getJxy() {
        return jxy;
    }

    public String getIterVarName() {
        return iterVarName;
    }

    public double getIterVarNameMinValue() {
        return iterVarNameMinValue;
    }

    public double getIterVarNameMaxValue() {
        return iterVarNameMaxValue;
    }

    public int getIterVarNameCountValue() {
        return iterVarNameCountValue;
    }

    public RunAction getInnerAction() {
        return innerAction;
    }

    public void setInnerAction(RunAction innerAction) {
        this.innerAction = innerAction;
    }

    public double[] getIterations(){
       return Maths.dtimes(iterVarNameMinValue,iterVarNameMaxValue,iterVarNameCountValue);
    }

    protected abstract void initIterations();

    /**
     *
     * @param i
     * @return true if wants to stop iterations
     */
    protected abstract boolean iteration(double i);
    protected abstract Object returnValue();
}
