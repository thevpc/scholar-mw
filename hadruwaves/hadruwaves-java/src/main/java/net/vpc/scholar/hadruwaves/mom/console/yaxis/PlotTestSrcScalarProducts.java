package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class PlotTestSrcScalarProducts extends PlotAxisSeries implements Cloneable {
    public PlotTestSrcScalarProducts(YType... type) {
        this(type, PlotType.HEATMAP);
    }

    public PlotTestSrcScalarProducts(YType[] type, PlotType graphix) {
        super("GpSrcScalarProducts", type, graphix);
        if (graphix != PlotType.MESH && graphix != PlotType.HEATMAP) {
            throw new IllegalArgumentException();
        }
    }
    @Override
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ComputationMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure,monitor,p);
    }

    protected NamedMatrix computeMatrix(MomStructure structure, ComputationMonitor cmonitor, ConsoleActionParams p) {
        EnhancedComputationMonitor monitor = ComputationMonitorFactory.enhance(cmonitor);
//        monitor.startm(getClass().getSimpleName());
        NamedMatrix namedMatrix = new NamedMatrix(structure.getTestSourceScalarProducts(monitor).toMatrix());
//        monitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

    public String getName() {
        return "<Gp,Src>";
    }
//
//    public double[] getY(AbstractStructure2D direct, AbstractStructure2D modele, ParamSet x) {
//        int l = compute(direct, modele, x, null)[0].length;
//        return Math2.dsteps(1, l, 1.0);
//    }
}
