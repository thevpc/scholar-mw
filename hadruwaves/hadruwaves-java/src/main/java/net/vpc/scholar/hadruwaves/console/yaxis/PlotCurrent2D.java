package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruplot.PlotMatrix;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.XParamSet;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomParamFactory;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;

public class PlotCurrent2D extends PlotAxisSeries implements Cloneable {
    private Axis axis;
    private int fnstep = 500;
    private int threshold = 10;
    private double epsilon = 1E-3;
    private boolean convergenceFn = false;

    public PlotCurrent2D(Axis axis, YType... type) {
        super("Current2D" + axis, type, PlotType.HEATMAP);
        this.axis = axis;
    }

    public PlotCurrent2D(double epsilon, int threshold, int fnstep, Axis axis, YType... type) {
        super("Current2D" + axis, type, PlotType.HEATMAP);
        this.axis = axis;
        this.convergenceFn = true;
        this.fnstep = fnstep < 1 ? 500 : fnstep;
        this.epsilon = epsilon < 0 ? 1E-3 : epsilon;
        this.threshold = threshold;
    }

    @Override
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return evalMatrix((MomStructure) structure, monitor, p);
    }


    protected PlotMatrix evalMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        VDiscrete j = null;
        if (convergenceFn) {
            int fnMax_start = structure.modeFunctions().getSize();
            int[] fnMax_all = Maths.isteps(fnMax_start, fnMax_start + fnstep * 10000, fnstep);
            j = structure.current().monitor(monitor)
                    .converge(
                            ConvergenceEvaluator.of(MomParamFactory.params.modesCount(), fnMax_all)
                                    .setThreshold(epsilon).setStabilityIterations(threshold).setMaxIterations(1000)
                    ).evalVDiscrete(x, y);
        } else {
            j = structure.current().monitor(monitor).evalVDiscrete(x, y);
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
