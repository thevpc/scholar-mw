package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.common.mon.VoidMonitoredAction;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.str.MatrixBEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:09:27
 */
public class MatrixBWaveguideSerialParallelEvaluator implements MatrixBEvaluator {

    @Override
    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        ProgressMonitor emonitor = ProgressMonitors.nonnull(monitor);
        final String monitorMessage = getClass().getSimpleName();
        TestFunctions gpTestFunctions = str.testFunctions();
        final DoubleToVector[] _g = gpTestFunctions.arr();
        final ModeInfo[] n_propa = str.modeFunctions().getPropagatingModes();
        if (n_propa.length == 0) {
            throw new IllegalArgumentException("WAVE_GUIDE Structure with no Propagative modes");
        }
        final Complex[][] b = new Complex[_g.length][n_propa.length];
        ProgressMonitor[] mon = ProgressMonitors.split(emonitor, new double[]{2, 8});
        final ComplexMatrix sp = str.getTestModeScalarProducts(mon[0]);
        ProgressMonitor m = ProgressMonitors.incremental(mon[1], (_g.length * n_propa.length));
        Maths.invokeMonitoredAction(m, monitorMessage, new VoidMonitoredAction() {
            @Override
            public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                for (int p = 0; p < _g.length; p++) {
                    ComplexVector spp = sp.getRow(p);
                    for (int n = 0; n < n_propa.length; n++) {
                        //[vpc/20140801] removed neg, don't know why i used to use it
//                b[p][n] = sp.gf(p, n_propa[n].index).neg();
                        b[p][n] = spp.get(n_propa[n].index);//.neg();
                        monitor.inc(monitorMessage);
                    }
                }
            }
        });



        ComplexMatrix cMatrix = Maths.matrix(b);
        double norm = cMatrix.norm1();
        if (norm == 0 || Double.isNaN(norm) || Double.isInfinite(norm)) {
            emonitor.setMessage("MatrixB is badly normed : " + norm);
            System.err.println("MatrixB is badly normed : " + norm);
        }
        for (int n = 0; n < n_propa.length; n++) {
            double normCol = cMatrix.getColumn(n).toMatrix().norm1();
            double x = normCol / norm;
            if (x < 1E-5) {
                emonitor.setMessage("MatrixB has column " + n + " badly normed : mode is " + n_propa[n].getMode() + "/" + str.getBorders() + " (error " + (x * 100) + "%)");
                System.err.println("MatrixB has column " + n + " badly normed : mode is " + n_propa[n].getMode() + "/" + str.getBorders() + " (error " + (x * 100) + "%)");
            }
        }
        return cMatrix;
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofFunction(getClass().getSimpleName()).build();
    }
}
