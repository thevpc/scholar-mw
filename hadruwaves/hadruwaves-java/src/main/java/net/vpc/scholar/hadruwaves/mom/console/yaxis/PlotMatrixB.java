package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;

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
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ComputationMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure,monitor,p);
    }

    protected NamedMatrix computeMatrix(MomStructure structure, ComputationMonitor monitor, ConsoleActionParams p) {
        EnhancedComputationMonitor emonitor = ComputationMonitorFactory.enhance(monitor);
//        emonitor.startm(getClass().getSimpleName());
        NamedMatrix namedMatrix = new NamedMatrix(structure.matrixB().monitor(monitor).computeMatrix().getArrayCopy());
//        emonitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

}
