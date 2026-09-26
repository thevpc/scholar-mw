package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.common.mon.VoidMonitoredAction;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.ComplexVector;
import net.thevpc.scholar.hadrumaths.DoubleMatrix;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.MutableComplex;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.Physics;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;
import net.thevpc.scholar.hadruwaves.mom.str.MatrixAEvaluator;
import net.thevpc.scholar.hadruwaves.util.AdmittanceValue;
import net.thevpc.scholar.hadruwaves.util.Impedance;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:55:23
 */
public class MatrixAPlanarSerialEvaluator implements MatrixAEvaluator {
    public static final MatrixAPlanarSerialEvaluator INSTANCE = new MatrixAPlanarSerialEvaluator();

    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor1) {
        TestFunctions gpTestFunctions = str.testFunctions();
        final DoubleToVector[] _g = gpTestFunctions.toArray();
        final Complex[][] b = new Complex[_g.length][_g.length];
        ProgressMonitor[] mons=monitor1.split(2,2,6);

        ModeFunctions fn = str.modeFunctions();
//        ModeInfo[] n_eva = str.isParameter(AbstractStructure2D.HINT_REGULAR_ZN_OPERATOR) ? fn.getModes() : fn.getVanishingModes();
        final ModeInfo[] n_eva = str.getModes(mons[0]);
        final ComplexMatrix sp = str.getTestModeScalarProducts(mons[1]);
        boolean complex = fn.isComplex() || gpTestFunctions.isComplex();
        boolean symMatrix = !complex;
        //boolean hermMatrix=complex;
        final String monMessage = getClass().getSimpleName();
        Impedance scalarSurfaceImpedance = str.getSerialZs()==null?Physics.impedance(Complex.ZERO):str.getSerialZs();
        ProgressMonitor monitor=mons[2];

        final Complex[] zn_all = new Complex[n_eva.length];
        for (int i = 0; i < n_eva.length; i++) {
            ModeInfo n = n_eva[i];
            AdmittanceValue yl = Physics.evalLayersAdmittance(str.getLayers(), n.firstBoxSpaceGamma, n.secondBoxSpaceGamma, n.impedance.impedanceValue());
            zn_all[i] = n.impedance.parallel(yl).serial(scalarSurfaceImpedance).impedanceValue();
        }

        if (symMatrix) {
            if (sp.isConvertibleTo(Maths.$DOUBLE)) {
                final DoubleMatrix dsp = (DoubleMatrix) sp.to(Maths.$DOUBLE);
                final ProgressMonitor m = ProgressMonitors.incremental(monitor, (_g.length * _g.length));
                Maths.invokeMonitoredAction(m, monMessage, new VoidMonitoredAction() {
                    @Override
                    public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                        MutableComplex c = MutableComplex.Zero();

                        //copied to local to nonnull performance!
                        int glength = _g.length;
                        Complex[][] cb = b;
                        DoubleMatrix csp = dsp;
                        ProgressMonitor cm = m;
                        String cmonMessage = monMessage;
                        ModeInfo[] cn_eva = n_eva;
                        Complex[] czn = zn_all;

                        for (int p = 0; p < glength; p++) {
                            double[] psp = csp.getRowDouble(p);
                            for (int q = p; q < glength; q++) {
                                double[] qsp = csp.getRowDouble(q);
                                c.setZero();
                                for (int i = 0; i < cn_eva.length; i++) {
                                    int nindex = cn_eva[i].index;
                                    double sp1 = psp[nindex];
                                    double sp2 = qsp[nindex];
                                    c.add(czn[i].mul(sp1 * sp2));
                                }
                                cb[p][q] = c.toComplex();
                            }
                            cm.inc(cmonMessage, glength - p);
                        }
                        for (int p = 0; p < glength; p++) {
                            for (int q = 0; q < p; q++) {
                                cb[p][q] = cb[q][p];
                            }
                            cm.inc(cmonMessage, p);
                        }
                    }
                });
            } else {
                final ProgressMonitor m = ProgressMonitors.incremental(monitor, (_g.length * _g.length));
                Maths.invokeMonitoredAction(m, monMessage, new VoidMonitoredAction() {
                    @Override
                    public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                        MutableComplex c = MutableComplex.Zero();

                        //copied to local to nonnull performance!
                        int glength = _g.length;
                        Complex[][] cb = b;
                        ComplexMatrix csp = sp;
                        ProgressMonitor cm = m;
                        String cmonMessage = monMessage;
                        ModeInfo[] cn_eva = n_eva;
                        Complex[] czn = zn_all;

                        for (int p = 0; p < glength; p++) {
                            ComplexVector psp = csp.getRow(p);
                            for (int q = p; q < glength; q++) {
                                ComplexVector qsp = csp.getRow(q);
                                c.setZero();
                                for (int i = 0; i < cn_eva.length; i++) {
                                    int nindex = cn_eva[i].index;
                                    Complex sp1 = psp.get(nindex);
                                    Complex sp2 = qsp.get(nindex);
                                    c.addProduct(czn[i], sp1, sp2);
                                }
                                cb[p][q] = c.toComplex();
                            }
                            cm.inc(cmonMessage, glength - p);
                        }
                        for (int p = 0; p < glength; p++) {
                            for (int q = 0; q < p; q++) {
                                cb[p][q] = cb[q][p];
                            }
                            cm.inc(cmonMessage, p);
                        }
                    }
                });
            }
        } else {// non symmetric
            final ProgressMonitor m = ProgressMonitors.incremental(monitor, (_g.length * _g.length));
            Maths.invokeMonitoredAction(m, monMessage, new VoidMonitoredAction() {
                @Override
                public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                    int glength = _g.length;
                    Complex[][] cb = b;
                    ComplexMatrix csp = sp;
                    ModeInfo[] cn_eva = n_eva;
                    Complex[] czn = zn_all;
                    int M = cn_eva.length;
                    int[] modeIndices = new int[M];
                    for (int i = 0; i < M; i++) {
                        modeIndices[i] = cn_eva[i].index;
                    }

                    // Extract S into primitive arrays
                    double[][] s_re = new double[glength][M];
                    double[][] s_im = new double[glength][M];
                    boolean anyImag = false;
                    for (int p = 0; p < glength; p++) {
                        ComplexVector pRow = csp.getRow(p);
                        double[] pre = s_re[p];
                        double[] pim = s_im[p];
                        for (int i = 0; i < M; i++) {
                            Complex c = pRow.get(modeIndices[i]);
                            pre[i] = c.getReal();
                            double im = c.getImag();
                            pim[i] = im;
                            if (im != 0.0) {
                                anyImag = true;
                            }
                        }
                    }

                    // Precompute T[p][i] = S[p][i] * czn[i]
                    double[][] t_re = new double[glength][M];
                    double[][] t_im = new double[glength][M];
                    for (int p = 0; p < glength; p++) {
                        double[] pre = s_re[p];
                        double[] pim = s_im[p];
                        double[] tre = t_re[p];
                        double[] tim = t_im[p];
                        for (int i = 0; i < M; i++) {
                            double zr = czn[i].getReal();
                            double zi = czn[i].getImag();
                            double sr = pre[i];
                            double si = pim[i];
                            tre[i] = sr * zr - si * zi;
                            tim[i] = sr * zi + si * zr;
                        }
                    }

                    if (!anyImag) {
                        for (int p = 0; p < glength; p++) {
                            double[] tp_re = t_re[p];
                            double[] tp_im = t_im[p];
                            for (int q = p; q < glength; q++) {
                                double[] sq_re = s_re[q];
                                double re = 0;
                                double im = 0;
                                for (int i = 0; i < M; i++) {
                                    double sr = sq_re[i];
                                    re += tp_re[i] * sr;
                                    im += tp_im[i] * sr;
                                }
                                Complex c = Complex.of(re, im);
                                cb[p][q] = c;
                                cb[q][p] = c;
                            }
                            m.inc(monMessage, glength);
                        }
                    } else {
                        for (int p = 0; p < glength; p++) {
                            double[] tp_re = t_re[p];
                            double[] tp_im = t_im[p];
                            for (int q = 0; q < glength; q++) {
                                double[] sq_re = s_re[q];
                                double[] sq_im = s_im[q];
                                double re = 0;
                                double im = 0;
                                for (int i = 0; i < M; i++) {
                                    double tr = tp_re[i];
                                    double ti = tp_im[i];
                                    double sr = sq_re[i];
                                    double msi = -sq_im[i];
                                    re += (tr * sr - ti * msi);
                                    im += (tr * msi + ti * sr);
                                }
                                cb[p][q] = Complex.of(re, im);
                            }
                            m.inc(monMessage, glength);
                        }
                    }
                }
            });
        }
        return Maths.matrix(b);
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public NElement toElement() {
        return NElement.ofObjectBuilder(getClass().getSimpleName()).build();
    }
}
