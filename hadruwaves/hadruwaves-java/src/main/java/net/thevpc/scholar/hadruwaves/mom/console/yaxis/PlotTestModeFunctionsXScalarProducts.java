package net.thevpc.scholar.hadruwaves.mom.console.yaxis;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.PlotMatrix;

import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.common.mon.ProgressMonitor;

public class PlotTestModeFunctionsXScalarProducts extends PlotAxisSeries implements Cloneable {
    public PlotTestModeFunctionsXScalarProducts(YType... type) {
        this(type, PlotType.HEATMAP);
    }

    public PlotTestModeFunctionsXScalarProducts(YType[] type, PlotType graphix) {
        super("GpxFnxScalarProducts", type, graphix);
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
        ModeFunctions fnModeFunctions = structure.modeFunctions();
        TestFunctions gpTestFunctions = structure.testFunctions();
        DoubleToVector[] cache_essai = gpTestFunctions.arr();
//        emonitor.startm(getClass().getSimpleName());
        Complex[][] gfps = new Complex[cache_essai.length][fnModeFunctions.count()];
        ModeInfo[] indexes = structure.getModes();
        int max= fnModeFunctions.count();
//        int progress=0;
        for (int q = 0; q < cache_essai.length; q++) {
            for (int n = 0; n < max; n++) {
                gfps[q][n] = Maths.scalarProduct(
                        indexes[n].fn.getComponent(Axis.X),
                        cache_essai[q].getComponent(Axis.X)
                ).toComplex();
//                progress++;
//                monitor.setProgress(1.0*progress/(cache_essai.length*max));
                ProgressMonitors.setProgress(monitor,q,n,gfps.length,max, getClass().getSimpleName());
            }
        }
        PlotMatrix namedMatrix = new PlotMatrix(gfps);
//        emonitor.terminatem(getClass().getSimpleName());
        return namedMatrix;
    }

    public String getName() {
        return "<Gpx,Fnx>";
    }
}
