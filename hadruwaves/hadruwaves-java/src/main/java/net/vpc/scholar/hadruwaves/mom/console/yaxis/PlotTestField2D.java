package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.params.XParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.MomParamFactory;

public class PlotTestField2D extends PlotAxisSeries implements Cloneable {
    private Axis axis;
    private int fnstep = 500;
    private int threshold = 10;
    private double epsilon = 1E-3;
    private boolean convergenceFn = false;

    public PlotTestField2D(Axis axis, YType... type) {
        super("TestField2D" + axis, type, PlotType.HEATMAP);
        this.axis = axis;
    }

    public PlotTestField2D(double epsilon, int threshold, int fnstep, Axis axis, YType... type) {
        super("TestField2D" + axis, type, PlotType.HEATMAP);
        this.axis = axis;
        this.convergenceFn = true;
        this.fnstep = fnstep;
        this.epsilon = epsilon;
        this.threshold = threshold;
    }

    @Override
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure, monitor, p);
    }

//    public PlotTestField2D(Axis axis, YType[] type, PlotType plotType) {
//        super("TestField" + axis, type, plotType);
//        this.axis = axis;
//        setPlotType(PlotType.HEATMAP);
//    }

    protected NamedMatrix computeMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
//        XParamSet xAxis = (XParamSet) p.getAxis().getX();
//        double[] y = structure.toYForDomainCoeff(xAxis.getY());
//        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
//        Complex[][] c = structure.computeTestField(x, y, monitor).getCube(axis).getMatrix(Axis.Z, 0);
//        return new NamedMatrix(c, x, y);


        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        VDiscrete j = null;
        if (convergenceFn) {
            int fnMax_start = structure.getModeFunctionsCount();
            int[] fnMax_all = Maths.isteps(fnMax_start, fnMax_start + fnstep * 10000, fnstep);
            j = structure.testField().monitor(monitor).converge(
                    ConvergenceEvaluator.create(MomParamFactory.params.modesCount(), fnMax_all)
                            .setThreshold(epsilon).setStabilityIterations(threshold).setMaxIterations(1000)
            ).computeVDiscrete(x, y);
        } else {
            j = structure.testField().monitor(monitor).computeVDiscrete(x, y);
        }
        Complex[][] c = j.getComponent(axis).getArray(Axis.Z, 0);
        if (x.length == 1 && y.length > 0) {
            //inverser les axes
            return new NamedMatrix(Maths.matrix(c).transpose().getArray(), y, x);
        } else {
            return new NamedMatrix(c, x, y);
        }
    }

}
