package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.common.util.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.PlotType;

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
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure,monitor,p);
    }

    protected NamedMatrix computeMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
        ProgressMonitor emonitor = ProgressMonitorFactory.enhance(monitor);
//        emonitor.startm(getClass().getSimpleName());
        NamedMatrix namedMatrix = new NamedMatrix(structure.matrixX().monitor(monitor).computeMatrix().getArray());
//        emonitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

}
