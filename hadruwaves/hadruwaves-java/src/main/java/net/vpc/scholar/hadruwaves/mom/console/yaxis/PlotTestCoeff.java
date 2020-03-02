package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadruplot.PlotMatrix;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.PlotType;

public class PlotTestCoeff extends PlotAxisSeries implements Cloneable {
    public PlotTestCoeff(YType... type) {
        this(type, PlotType.HEATMAP);
    }

    public PlotTestCoeff(YType type[], PlotType graphix) {
        super("TestCoeff", type, graphix);
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
        PlotMatrix namedMatrix = new PlotMatrix(structure.matrixX().monitor(monitor).evalMatrix().getArray());
//        emonitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

}
