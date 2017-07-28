package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.vpc.scholar.hadruwaves.mom.str.MatrixBEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:09:27
 */
public class MatrixBPlanarSerialParallelEvaluator implements MatrixBEvaluator {

    public Matrix evaluate(MomStructure str, ComputationMonitor monitor) {
        String monitorMessage = getClass().getSimpleName();
        TestFunctions gpTestFunctions = str.getTestFunctions();
        PlanarSources planarSources1 = (PlanarSources) str.getSources();
        if (planarSources1 == null) {
            throw new IllegalArgumentException("Missing Planar Sources");
        }
        DoubleToVector[] _g = gpTestFunctions.arr();
        DoubleToVector[] _src = planarSources1.getSourceFunctions();
        if (_src.length != 1) {
            throw new IllegalArgumentException("Unsupported Sources count " + _src.length);
        }
        EnhancedComputationMonitor[] mon = ComputationMonitorFactory.split(monitor, new double[]{2, 8});
        ScalarProductCache sp = str.getTestSourceScalarProducts(mon[0]);

        EnhancedComputationMonitor m = ComputationMonitorFactory.createIncrementalMonitor(mon[1], (_g.length * _src.length));
        return Maths.invokeMonitoredAction(m, monitorMessage, new MonitoredAction<Matrix>() {
            @Override
            public Matrix process(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                Complex[][] b = new Complex[_g.length][_src.length];
                for (int n = 0; n < _src.length; n++) {
                    Vector cc = sp.getColumn(n);
                    for (int p = 0; p < _g.length; p++) {
                        //[vpc/20140801] removed neg, don't know why i used to use it
//                b[p][n] = sp.gf(p, n).neg();
                        //b[p][n] = sp.gf(p, n);
                        b[p][n] = cc.get(p);
                        monitor.inc(monitorMessage);
                    }
                }
                return Maths.matrix(b);
            }
        });
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

    public String dump() {
        return getClass().getName();
    }
}
