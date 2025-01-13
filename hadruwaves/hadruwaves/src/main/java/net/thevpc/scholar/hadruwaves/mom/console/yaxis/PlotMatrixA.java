package net.thevpc.scholar.hadruwaves.mom.console.yaxis;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadruplot.PlotMatrix;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;

import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.common.mon.ProgressMonitor;

public class PlotMatrixA extends PlotAxisSeries implements Cloneable {
    public PlotMatrixA(YType... type) {
        this(type, PlotType.HEATMAP);
    }

    public PlotMatrixA(YType type[], PlotType graphix) {
        super("Amatrix", type, graphix);
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
        PlotMatrix namedMatrix = new PlotMatrix(structure.matrixA().monitor(monitor).evalMatrix().getArrayCopy());
//        emonitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

//
//    public double[] getY(AbstractStructure2D direct, AbstractStructure2D modele, ParamSet x) {
//        int max = compute(direct, modele, x, null)[0].length;
//        return Math2.dsteps(1, max, 1.0);
//    }
}
