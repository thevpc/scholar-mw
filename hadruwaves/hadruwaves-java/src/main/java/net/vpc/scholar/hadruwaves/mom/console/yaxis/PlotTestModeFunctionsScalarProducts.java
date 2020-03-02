package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadruplot.PlotMatrix;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;

public class PlotTestModeFunctionsScalarProducts extends PlotAxisSeries implements Cloneable {
    public PlotTestModeFunctionsScalarProducts(YType... type) {
        this(type, PlotType.HEATMAP);
    }

    public PlotTestModeFunctionsScalarProducts(YType[] type, PlotType graphix) {
        super("GpFnScalarProducts", type, graphix);
        if (graphix != PlotType.MESH && graphix != PlotType.HEATMAP) {
            throw new IllegalArgumentException();
        }
    }
    @Override
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return evalMatrix((MomStructure) structure,monitor,p);
    }

    protected PlotMatrix evalMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
        ProgressMonitor emonitor = ProgressMonitors.nonnull(monitor);
//        emonitor.startm(getClass().getSimpleName());
        PlotMatrix namedMatrix = new PlotMatrix(structure.getTestModeScalarProducts(monitor).getArray());
//        emonitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

    public String getName() {
        return "<Gp,Fn>";
    }
//
//    public double[] getY(AbstractStructure2D direct, AbstractStructure2D modele, ParamSet x) {
//        int l = compute(direct, modele, x, null)[0].length;
//        return Math2.dsteps(1, l, 1.0);
//    }
}
