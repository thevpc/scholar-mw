package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.scalarproducts.DoubleScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
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

    public Matrix evaluate(MomStructure str, ProgressMonitor monitor) {
        TestFunctions gpTestFunctions = str.getTestFunctions();
        DoubleToVector[] _g = gpTestFunctions.arr();
        Complex[][] b = new Complex[_g.length][_g.length];
        ModeFunctions fn = str.getModeFunctions();
//        ModeInfo[] n_eva = str.isParameter(AbstractStructure2D.HINT_REGULAR_ZN_OPERATOR) ? fn.getModes() : fn.getVanishingModes();
        ModeInfo[] n_eva = str.getModes();
        TMatrix<Complex> sp = str.getTestModeScalarProducts(ProgressMonitorFactory.none());
        boolean complex = fn.isComplex() || gpTestFunctions.isComplex();
        boolean symMatrix = !complex;
        //boolean hermMatrix=complex;
        String monMessage = getClass().getSimpleName();
        if (symMatrix) {
            if(sp.isConvertibleTo(Maths.$DOUBLE)){
                DoubleMatrix dsp=(DoubleMatrix) sp.to(Maths.$DOUBLE);
                EnhancedProgressMonitor m = ProgressMonitorFactory.createIncrementalMonitor(monitor, (_g.length * _g.length));
                Maths.invokeMonitoredAction(m, monMessage, new VoidMonitoredAction() {
                    @Override
                    public void invoke(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
                        MutableComplex c = MutableComplex.Zero();

                        //copied to local to enhance performance!
                        int glength = _g.length;
                        Complex[][] cb = b;
                        DoubleMatrix csp = dsp;
                        EnhancedProgressMonitor cm = m;
                        String cmonMessage = monMessage;
                        ModeInfo[] cn_eva = n_eva;

                        for (int p = 0; p < glength; p++) {
                            double[] psp = csp.getRowDouble(p);
                            for (int q = p; q < glength; q++) {
                                double[] qsp = csp.getRowDouble(q);
                                c.setZero();
                                for (ModeInfo n : cn_eva) {
                                    Complex zn = n.impedance;
                                    double sp1 = psp[n.index];
                                    double sp2 = qsp[n.index];
                                    c.add(zn.mul(sp1*sp2));
                                }
                                cb[p][q] = c.toComplex();
                                cm.inc(cmonMessage);
                            }
                        }
                        for (int p = 0; p < glength; p++) {
                            for (int q = 0; q < p; q++) {
                                cb[p][q] = cb[q][p];
                                cm.inc(cmonMessage);
                            }
                        }
                    }
                });
            }else {
                EnhancedProgressMonitor m = ProgressMonitorFactory.createIncrementalMonitor(monitor, (_g.length * _g.length));
                Maths.invokeMonitoredAction(m, monMessage, new VoidMonitoredAction() {
                    @Override
                    public void invoke(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
                        MutableComplex c = MutableComplex.Zero();

                        //copied to local to enhance performance!
                        int glength = _g.length;
                        Complex[][] cb = b;
                        TMatrix<Complex> csp = sp;
                        EnhancedProgressMonitor cm = m;
                        String cmonMessage = monMessage;
                        ModeInfo[] cn_eva = n_eva;

                        for (int p = 0; p < glength; p++) {
                            TVector<Complex> psp = csp.getRow(p);
                            for (int q = p; q < glength; q++) {
                                TVector<Complex> qsp = csp.getRow(q);
                                c.setZero();
                                for (ModeInfo n : cn_eva) {
                                    Complex zn = n.impedance;
//                            Complex sp1 = sp.gf(p, n.index);
//                            Complex sp2 = sp.fg(n.index, q);
                                    Complex sp1 = psp.get(n.index); //sp.gf(p, n.index);
                                    Complex sp2 = qsp.get(n.index);//both are real, no complex//.conj();//sp.fg(n.index, q);
                                    c.addProduct(zn, sp1, sp2);
                                }
                                cb[p][q] = c.toComplex();
                                cm.inc(cmonMessage);
                            }
                        }
                        for (int p = 0; p < glength; p++) {
                            for (int q = 0; q < p; q++) {
                                cb[p][q] = cb[q][p];
                                cm.inc(cmonMessage);
                            }
                        }
                    }
                });
            }
        } else {// non symmetric
            EnhancedProgressMonitor m = ProgressMonitorFactory.createIncrementalMonitor(monitor, (_g.length * _g.length));
            Maths.invokeMonitoredAction(m, monMessage, new VoidMonitoredAction() {
                @Override
                public void invoke(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
                    for (int p = 0; p < _g.length; p++) {
                        TVector<Complex> psp = sp.getRow(p);
                        for (int q = 0; q < _g.length; q++) {
                            TVector<Complex> qsp = sp.getRow(q);
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
