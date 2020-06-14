package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruplot.PlotMatrix;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;

import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.common.mon.ProgressMonitor;

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
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return evalMatrix((MomStructure) structure,monitor,p);
    }

    protected PlotMatrix evalMatrix(MomStructure structure, ProgressMonitor cmonitor, ConsoleActionParams p) {
        ProgressMonitor monitor = ProgressMonitors.nonnull(cmonitor);
        ModeFunctions fnModeFunctions = structure.modeFunctions();
        TestFunctions gpTestFunctions = structure.testFunctions();
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
                ).toComplex();
//                progress++;
//                monitor.setProgress(1.0*progress/(cache_essai.length*max));
                ProgressMonitors.setProgress(monitor,q,n,cache_essai.length,max, getClass().getSimpleName());
            }
        }
        PlotMatrix namedMatrix = new PlotMatrix(gfps);
//        monitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

    public String getName() {
        return "<Gpy,Fny>";
    }
}
