package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.monitors.EnhancedProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;

public class PlotTestModeFunctionsYScalarProducts extends PlotAxisSeries implements Cloneable {
    public PlotTestModeFunctionsYScalarProducts(YType... type) {
        this(type, PlotType.HEATMAP);
    }

    public PlotTestModeFunctionsYScalarProducts(YType type[], PlotType graphix) {
        super("GpyFnyScalarProducts", type, graphix);
        if (graphix != PlotType.MESH && graphix != PlotType.HEATMAP) {
            throw new IllegalArgumentException();
        }
    }
    @Override
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure,monitor,p);
    }

    protected NamedMatrix computeMatrix(MomStructure structure, ProgressMonitor cmonitor, ConsoleActionParams p) {
        EnhancedProgressMonitor monitor = ProgressMonitorFactory.enhance(cmonitor);
        ModeFunctions fnModeFunctions = structure.getModeFunctions();
        TestFunctions gpTestFunctions = structure.getTestFunctions();
        DoubleToVector[] cache_essai = gpTestFunctions.arr();
//        monitor.startm(getClass().getSimpleName());
        Complex[][] gfps = new Complex[cache_essai.length][fnModeFunctions.count()];
        ModeInfo[] indexes= structure.getModes();
        int max= fnModeFunctions.count();
        int progress=0;
        for (int q = 0; q < cache_essai.length; q++) {
            for (int n = 0; n < max; n++) {
                gfps[q][n] = Maths.scalarProduct(
                        indexes[n].fn.getComponent(Axis.Y),
                        cache_essai[q].getComponent(Axis.Y)
                );
//                progress++;
//                monitor.setProgress(1.0*progress/(cache_essai.length*max));
                ProgressMonitorFactory.setProgress(monitor,q,n,cache_essai.length,max, getClass().getSimpleName());
            }
        }
        NamedMatrix namedMatrix = new NamedMatrix(gfps);
//        monitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

    public String getName() {
        return "<Gpy,Fny>";
    }
}
