package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.params.XParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.PlotAxisSeries;

@Deprecated
public abstract class PlotElectricFieldAbstract extends PlotAxisSeries implements Cloneable {
    private Axis axis;
    private int fnstep=500;
    private int threshold=10;
    private double epsilon=1E-3;
    private boolean convergenceFn=false;

    public PlotElectricFieldAbstract(String name, Axis axis, YType... type) {
        super(name, type, PlotType.HEATMAP);
        this.axis = axis;
    }

    public PlotElectricFieldAbstract(double epsilon, int threshold, int fnstep, String name, Axis axis, YType... type) {
        super(name, type, PlotType.HEATMAP);
        this.axis = axis;
        this.convergenceFn = true;
        this.fnstep = fnstep;
        this.epsilon = epsilon;
        this.threshold = threshold;
    }
    @Override
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure,monitor,p);
    }


    protected NamedMatrix computeMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
        if (p.getAxis().getX() instanceof XParamSet) {
            XParamSet xAxis = (XParamSet) p.getAxis().getX();
            double[] xval = structure.toXForDomainCoeff(xAxis.getValues());
            double[] zval = structure.toZForDomainCoeff(xAxis.getZ());
            double[] yval = structure.toYForDomainCoeff(xAxis.getY());
            if((zval.length>1 && yval.length>1) || (zval.length<=1 && yval.length<=1)){
                throw new IllegalArgumentException("Not supported");
            }
            Complex[][] v = resolveE(structure, xval, yval, zval,this)[0];
            if(xval.length==1 && yval.length>0){
                //inverser les axes
                return new NamedMatrix(Maths.matrix(v).transpose().getArray(), yval, xval);
            }else{
                return new NamedMatrix(v, xval, zval.length>1?zval:yval);
            }
        } else {
            throw new IllegalArgumentException();
//            return super.computeMatrix(structure, x);
        }
    }

    protected abstract Complex[][][] resolveE(MomStructure structure, double[] x, double[] y, double[] z, ProgressMonitor monitor);

    public Axis getAxis() {
        return axis;
    }

    public boolean isConvergenceFn() {
        return convergenceFn;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public int getFnstep() {
        return fnstep;
    }

    public int getThreshold() {
        return threshold;
    }
    
}