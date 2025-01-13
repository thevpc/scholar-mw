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

public class PlotMatrixB extends PlotAxisSeries implements Cloneable {
    public PlotMatrixB(YType... type) {
        this(type, PlotType.HEATMAP);
    }

    public PlotMatrixB(YType type[], PlotType graphix) {
        super("Bmatrix", type, graphix);
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
        PlotMatrix namedMatrix = new PlotMatrix(structure.matrixB().monitor(monitor).evalMatrix().getArrayCopy());
//        emonitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

}
