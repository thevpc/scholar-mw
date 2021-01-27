package net.thevpc.scholar.hadruwaves.mom.console.yaxis;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruplot.PlotMatrix;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.XParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.MomParamFactory;

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
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return evalMatrix((MomStructure) structure, monitor, p);
    }

//    public PlotTestField2D(Axis axis, YType[] type, PlotType plotType) {
//        super("TestField" + axis, type, plotType);
//        this.axis = axis;
//        setPlotType(PlotType.HEATMAP);
//    }

    protected PlotMatrix evalMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
//        XParamSet xAxis = (XParamSet) p.getAxis().getX();
//        double[] y = structure.toYForDomainCoeff(xAxis.getY());
//        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
//        Complex[][] c = structure.computeTestField(x, y, monitor).getCube(axis).getMatrix(Axis.Z, 0);
//        return new PlotMatrix(c, x, y);


        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        VDiscrete j = null;
        if (convergenceFn) {
            int fnMax_start = structure.modeFunctions().getSize();
            int[] fnMax_all = Maths.isteps(fnMax_start, fnMax_start + fnstep * 10000, fnstep);
            j = structure.testField().monitor(monitor).converge(
                    ConvergenceEvaluator.of(MomParamFactory.params.modesCount(), fnMax_all)
                            .setThreshold(epsilon).setStabilityIterations(threshold).setMaxIterations(1000)
            ).evalVDiscrete(x, y);
        } else {
            j = structure.testField().monitor(monitor).evalVDiscrete(x, y);
        }
        Complex[][] c = j.getComponentDiscrete(axis).getArray(Axis.Z, 0);
        if (x.length == 1 && y.length > 0) {
            //inverser les axes
            return new PlotMatrix(Maths.matrix(c).transpose().getArray(), y, x);
        } else {
            return new PlotMatrix(c, x, y);
        }
    }

}
