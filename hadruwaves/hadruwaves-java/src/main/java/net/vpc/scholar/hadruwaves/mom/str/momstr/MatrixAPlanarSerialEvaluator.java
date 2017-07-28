package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.VoidMonitoredAction;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;
import net.vpc.scholar.hadruwaves.mom.str.MatrixAEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:55:23
 */
public class MatrixAPlanarSerialEvaluator implements MatrixAEvaluator {
    public static final MatrixAPlanarSerialEvaluator INSTANCE = new MatrixAPlanarSerialEvaluator();

    public Matrix evaluate(MomStructure str, ComputationMonitor monitor) {
        TestFunctions gpTestFunctions = str.getTestFunctions();
        DoubleToVector[] _g = gpTestFunctions.arr();
        Complex[][] b = new Complex[_g.length][_g.length];
        ModeFunctions fn = str.getModeFunctions();
//        ModeInfo[] n_eva = str.isParameter(AbstractStructure2D.HINT_REGULAR_ZN_OPERATOR) ? fn.getModes() : fn.getVanishingModes();
        ModeInfo[] n_eva = str.getModes();
        ScalarProductCache sp = str.getTestModeScalarProducts(ComputationMonitorFactory.none());
        boolean complex = fn.isComplex() || gpTestFunctions.isComplex();
        boolean symMatrix = !complex;
        //boolean hermMatrix=complex;
        String monMessage = getClass().getSimpleName();
        if (symMatrix) {
            EnhancedComputationMonitor m = ComputationMonitorFactory.createIncrementalMonitor(monitor, (_g.length * _g.length));
            Maths.invokeMonitoredAction(m, monMessage, new VoidMonitoredAction() {
                @Override
                public void invoke(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                    for (int p = 0; p < _g.length; p++) {
                        Vector psp = sp.getRow(p);
                        for (int q = p; q < _g.length; q++) {
                            Vector qsp = sp.getRow(q);
                            MutableComplex c = MutableComplex.Zero();
                            for (ModeInfo n : n_eva) {
                                Complex zn = n.impedance;
//                            Complex sp1 = sp.gf(p, n.index);
//                            Complex sp2 = sp.fg(n.index, q);
                                Complex sp1 = psp.get(n.index); //sp.gf(p, n.index);
                                Complex sp2 = qsp.get(n.index).conj();//sp.fg(n.index, q);
                                c.addProduct(zn, sp1, sp2);
                            }
                            b[p][q] = c.toComplex();
                            m.inc(monMessage);
                        }
                    }
                    for (int p = 0; p < _g.length; p++) {
                        for (int q = 0; q < p; q++) {
                            b[p][q] = b[q][p];
                            m.inc(monMessage);
                        }
                    }
                }
            });
        } else {//hermitienne si complex
            EnhancedComputationMonitor m = ComputationMonitorFactory.createIncrementalMonitor(monitor, (_g.length * _g.length));
            Maths.invokeMonitoredAction(m, monMessage, new VoidMonitoredAction() {
                @Override
                public void invoke(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                    for (int p = 0; p < _g.length; p++) {
                        Vector psp = sp.getRow(p);
                        for (int q = 0; q < _g.length; q++) {
                            Vector qsp = sp.getRow(q);
                            MutableComplex c = MutableComplex.Zero();
                            for (ModeInfo n : n_eva) {
                                Complex zn = n.impedance;
                                //Complex sp1 = sp.gf(p, n.index);
                                //Complex sp2 = sp.fg(n.index, q);
                                Complex sp1 = psp.get(n.index); //sp.gf(p, n.index);
                                Complex sp2 = qsp.get(n.index).conj();//sp.fg(n.index, q);
                                c.addProduct(zn, sp1, sp2);
                            }
                            b[p][q] = c.toComplex();
                            m.inc(monMessage);
                        }
                    }
                }
            });
        }
        return Maths.matrix(b);
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

    public String dump() {
        return getClass().getName();
    }
}
