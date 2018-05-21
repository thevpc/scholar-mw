package net.vpc.scholar.hadruwaves.console.yaxis;

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
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomParamFactory;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.PlotAxisSeries;

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
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure, monitor, p);
    }


    protected NamedMatrix computeMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        VDiscrete j = null;
        if (convergenceFn) {
            int fnMax_start = structure.getModeFunctionsCount();
            int[] fnMax_all = Maths.isteps(fnMax_start, fnMax_start + fnstep * 10000, fnstep);
            j = structure.current().monitor(monitor)
                    .converge(
                            ConvergenceEvaluator.create(MomParamFactory.params.modesCount(), fnMax_all)
                                    .setThreshold(epsilon).setStabilityIterations(threshold).setMaxIterations(1000)
                    ).computeVDiscrete(x, y);
        } else {
            j = structure.current().monitor(monitor).computeVDiscrete(x, y);
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
