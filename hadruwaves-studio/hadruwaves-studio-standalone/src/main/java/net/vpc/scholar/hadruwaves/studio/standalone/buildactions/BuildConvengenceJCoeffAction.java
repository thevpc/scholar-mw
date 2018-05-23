package net.vpc.scholar.hadruwaves.studio.standalone.buildactions;


import java.util.ArrayList;
import net.vpc.common.log.Log;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.common.util.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;


/**
 * User: vpc
 * Date: 21 mars 2005
 * Time: 13:15:57
 */
public class BuildConvengenceJCoeffAction extends AbstractBuildJCoeffSerieAction {
    private double precision;
    Matrix value;
    ArrayList<Double> relativeError;
    public BuildConvengenceJCoeffAction(MomStrHelper jxy, String iterVarName, double iterVarNameMinValue, double iterVarNameMaxValue, int iterVarNameCountValue, double precision) {
        super(jxy, iterVarName, iterVarNameMinValue, iterVarNameMaxValue, iterVarNameCountValue);
        this.precision=precision;
    }


    protected void initIterations() {
        value = null;
        relativeError=new ArrayList<Double>();
    }

    protected boolean iteration(double i) {
        Matrix newVal=getJxy().computeTestcoeff(ProgressMonitorFactory.none());
        if(value!=null){
            double diff=(value.sub(newVal)).normInf();
            double oldVal=newVal.normInf();
            double err=(oldVal!=0) ?diff/oldVal:diff;
            Log.trace("[BuildConvengenceJCoeffAction] "+err);
            relativeError.add(err);
            if(err<precision){
                return true;
            }
        }
        value=newVal;
        return false;
    }
    protected Object returnValue() {
        return relativeError.toArray(new Double[relativeError.size()]);
    }
}
