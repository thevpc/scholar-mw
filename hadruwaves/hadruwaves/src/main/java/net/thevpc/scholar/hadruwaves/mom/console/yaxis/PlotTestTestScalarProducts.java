package net.thevpc.scholar.hadruwaves.mom.console.yaxis;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruplot.PlotMatrix;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.common.mon.ProgressMonitor;

import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;

public class PlotTestTestScalarProducts extends PlotAxisSeries implements Cloneable {
    public PlotTestTestScalarProducts(YType... type) {
        this(type, PlotType.HEATMAP);
    }

    public PlotTestTestScalarProducts(YType[] type, PlotType graphix) {
        super("GpGqScalarProducts", type, graphix);
        if (graphix != PlotType.MESH && graphix != PlotType.HEATMAP) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return evalMatrix((MomStructure) structure, monitor, p);
    }

    protected PlotMatrix evalMatrix(final MomStructure structure, ProgressMonitor cmonitor, ConsoleActionParams p) {
        return Maths.invokeMonitoredAction(cmonitor, getClass().getSimpleName(), new MonitoredAction<PlotMatrix>() {
            @Override
            public PlotMatrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                TestFunctions _testFunctions = structure.testFunctions();
                Complex[][] gfps = new Complex[_testFunctions.count()][_testFunctions.count()];
                int max = _testFunctions.count();
                for (int q = 0; q < gfps.length; q++) {
                    for (int n = 0; n < max; n++) {
                        gfps[q][n] = Maths.scalarProduct(
                                _testFunctions.gp(n),
                                _testFunctions.gp(q)).toComplex();
                        ProgressMonitors.setProgress(monitor, q, n, gfps.length, max, getClass().getSimpleName());
                    }
                }
                PlotMatrix namedMatrix = new PlotMatrix(gfps);
                return namedMatrix;
            }
        });
    }

    public String getName() {
        return "<Gp,Gq>";
    }

}
