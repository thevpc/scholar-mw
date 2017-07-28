package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;

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
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ComputationMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure, monitor, p);
    }

    protected NamedMatrix computeMatrix(MomStructure structure, ComputationMonitor cmonitor, ConsoleActionParams p) {
        return Maths.invokeMonitoredAction(cmonitor, getClass().getSimpleName(), new MonitoredAction<NamedMatrix>() {
            @Override
            public NamedMatrix process(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                TestFunctions _testFunctions = structure.getTestFunctions();
                Complex[][] gfps = new Complex[_testFunctions.count()][_testFunctions.count()];
                int max = _testFunctions.count();
//        int progress=0;
                for (int q = 0; q < gfps.length; q++) {
                    for (int n = 0; n < max; n++) {
//                progress++;
//                if(q==n){
//                    Complex complex = ScalarProductFactory.scalarProduct(
//                            _testFunctions.gp(n),
//                            _testFunctions.gp(q));
//                    System.out.println("p=q="+q+" ==> " + complex);
//                    System.out.print("");
//                }
                        gfps[q][n] = Maths.scalarProduct(
                                _testFunctions.gp(n),
                                _testFunctions.gp(q));
                        ComputationMonitorFactory.setProgress(monitor, q, n, gfps.length, max, getClass().getSimpleName());
//                monitor.setProgress(1.0*progress/(gfps.length*max));
                    }
                }
                NamedMatrix namedMatrix = new NamedMatrix(gfps);
                return namedMatrix;
            }
        });
    }

    public String getName() {
        return "<Gp,Gq>";
    }

//
//    public double[] getY(AbstractStructure2D direct, AbstractStructure2D modele, ParamSet x) {
//        int l = compute(direct, modele, x, null)[0].length;
//        return Math2.dsteps(1, l, 1.0);
//    }
}
