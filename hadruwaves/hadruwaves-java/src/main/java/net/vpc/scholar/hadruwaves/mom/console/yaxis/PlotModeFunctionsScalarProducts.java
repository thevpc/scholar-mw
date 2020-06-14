package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruplot.PlotMatrix;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.vpc.scholar.hadruplot.console.yaxis.YType;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.MonitoredAction;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import static net.vpc.scholar.hadrumaths.Maths.scalarProduct;

public class PlotModeFunctionsScalarProducts extends PlotAxisSeries implements Cloneable {
    public PlotModeFunctionsScalarProducts(YType... type) {
        this(type, PlotType.HEATMAP);
    }

    public PlotModeFunctionsScalarProducts(YType[] type, PlotType graphix) {
        super("FmFnScalarProducts", type, graphix);
        if (graphix != PlotType.MESH && graphix != PlotType.HEATMAP) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return evalMatrix((MomStructure) structure, monitor, p);
    }

    protected PlotMatrix evalMatrix(final MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
        ProgressMonitor emonitor = ProgressMonitors.nonnull(monitor);
        return Maths.invokeMonitoredAction(
                emonitor,
                getClass().getSimpleName(),
                new MonitoredAction<PlotMatrix>() {
                    @Override
                    public PlotMatrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                        ModeFunctions fnModeFunctions = structure.modeFunctions();
                        Complex[][] gfps = new Complex[fnModeFunctions.count()][fnModeFunctions.count()];
                        ModeInfo[] indexes = structure.getModes();
                        int max = fnModeFunctions.count();
//        int progress=0;
                        for (int q = 0; q < gfps.length; q++) {
                            for (int n = 0; n < max; n++) {
                                gfps[q][n] = scalarProduct(indexes[n].fn, indexes[q].fn).toComplex();
//                progress++;
//                monitor.setProgress(1.0*progress/(gfps.length*max));
                                ProgressMonitors.setProgress(monitor, q, n, gfps.length, max, getClass().getSimpleName());
                            }
                        }
                        PlotMatrix namedMatrix = new PlotMatrix(gfps);
                        return namedMatrix;
                    }
                }
        );

    }

    public String getName() {
        return "<Fn,Fm>";
    }
//
//    public double[] getY(AbstractStructure2D direct, AbstractStructure2D modele, ParamSet x) {
//        int l = compute(direct, modele, x, null)[0].length;
//        return Math2.dsteps(1, l, 1.0);
//    }
}
