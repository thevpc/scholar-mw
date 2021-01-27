package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.thevpc.scholar.hadruwaves.mom.str.MatrixBEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:09:27
 */
public class MatrixBPlanarSerialParallelEvaluator implements MatrixBEvaluator {

    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        final String monitorMessage = getClass().getSimpleName();
        TestFunctions gpTestFunctions = str.testFunctions();
        PlanarSources planarSources1 = (PlanarSources) str.getSources();
        if (planarSources1 == null) {
            throw new IllegalArgumentException("Missing Planar Sources");
        }
        final DoubleToVector[] _g = gpTestFunctions.arr();
        final DoubleToVector[] _src = planarSources1.getSourceFunctions();
        if (_src.length != 1) {
            throw new IllegalArgumentException("Unsupported Sources count " + _src.length);
        }
        ProgressMonitor[] mon = ProgressMonitors.split(monitor, new double[]{2, 8});
        final ComplexMatrix sp = str.getTestSourceScalarProducts(mon[0]);

        ProgressMonitor m = ProgressMonitors.incremental(mon[1], (_g.length * _src.length));
        return Maths.invokeMonitoredAction(m, monitorMessage, new MonitoredAction<ComplexMatrix>() {
            @Override
            public ComplexMatrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                Complex[][] b = new Complex[_g.length][_src.length];
                for (int n = 0; n < _src.length; n++) {
                    ComplexVector cc = sp.getColumn(n);
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
        return dump();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }

}
