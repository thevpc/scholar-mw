package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.XParamSet;
import net.thevpc.scholar.hadruplot.PlotMatrix;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;

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
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return evalMatrix((MomStructure) structure,monitor,p);
    }


    protected PlotMatrix evalMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
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
                return new PlotMatrix(Maths.matrix(v).transpose().getArray(), yval, xval);
            }else{
                return new PlotMatrix(v, xval, zval.length>1?zval:yval);
            }
        } else {
            throw new IllegalArgumentException();
//            return super.evalMatrix(structure, x);
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