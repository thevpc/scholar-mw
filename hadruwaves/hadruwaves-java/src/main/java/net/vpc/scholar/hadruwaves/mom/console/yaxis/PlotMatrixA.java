package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;

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
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure,monitor,p);
    }

    protected NamedMatrix computeMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
        EnhancedProgressMonitor emonitor = ProgressMonitorFactory.enhance(monitor);
//        emonitor.startm(getClass().getSimpleName());
        NamedMatrix namedMatrix = new NamedMatrix(structure.matrixA().monitor(monitor).computeMatrix().getArrayCopy());
//        emonitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

//
//    public double[] getY(AbstractStructure2D direct, AbstractStructure2D modele, ParamSet x) {
//        int max = compute(direct, modele, x, null)[0].length;
//        return Math2.dsteps(1, max, 1.0);
//    }
}
