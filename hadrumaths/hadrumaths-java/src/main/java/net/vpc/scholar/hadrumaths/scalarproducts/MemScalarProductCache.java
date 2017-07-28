package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.VoidMonitoredAction;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;

import java.io.Serializable;

public class MemScalarProductCache extends AbstractScalarProductCache implements Serializable {
    private Complex[/** p index **/][/** n index **/] cache;
    private boolean doSimplifyAll;

    private static Expr[] simplifyAll(Expr[] e, EnhancedComputationMonitor mon) {
        Expr[] all = new Expr[e.length];
        Maths.invokeMonitoredAction(mon, "Simplify All", new VoidMonitoredAction() {
            @Override
            public void invoke(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                int length = all.length;
                for (int i = 0; i < length; i++) {
                    mon.setProgress(i, length, messagePrefix + " {0}/{1}", (i + 1), length);
                    all[i] = Maths.simplify(e[i]);
                }
            }
        });
        return all;
    }

    public Matrix toMatrix() {
        return Maths.matrix(cache);
    }

    public Vector getColumn(int column) {
        Complex[] vmatrix = new Complex[cache.length];
        for (int i = 0; i < vmatrix.length; i++) {
            vmatrix[i] = cache[i][column];
        }
        return Maths.columnVector(vmatrix);
    }

    public Vector getRow(int row) {
        Complex[] vmatrix = new Complex[cache[0].length];
        System.arraycopy(cache[row], 0, vmatrix, 0, vmatrix.length);
        return Maths.columnVector(vmatrix);
    }

    public Complex[][] toArray() {
        return cache;
    }

    public Complex apply(int p, int n) {
        return cache[p][n];
    }

    public Complex gf(int p, int n) {
        return cache[p][n];
    }

    public Complex fg(int n, int p) {
        return cache[p][n].conj();
    }

    public void evaluate(ScalarProductOperator sp, Expr[] fn, Expr[] gp, AxisXY axis, ComputationMonitor monitor) {
        EnhancedComputationMonitor emonitor = ComputationMonitorFactory.enhance(monitor);
        String monMessage = getClass().getSimpleName();
        if (sp == null) {
            sp = Maths.Config.getDefaultScalarProductOperator();
        }
        EnhancedComputationMonitor[] hmon = emonitor.split(new double[]{2, 1, 3});
        if (doSimplifyAll) {
            Expr[] finalFn = fn;
            Expr[] finalGp = gp;
            Expr[][] fg = Maths.invokeMonitoredAction(emonitor, "Simplify All", new MonitoredAction<Expr[][]>() {
                @Override
                public Expr[][] process(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                    Expr[][] fg = new Expr[2][];
                    fg[0] = simplifyAll(finalFn, hmon[0]);

                    fg[1] = simplifyAll(finalGp, hmon[1]);
                    return fg;
                }
            });
            fn = fg[0];
            gp = fg[1];
        }
        boolean doubleValue = true;
        boolean scalarValue = true;
        int maxF = fn.length;
//        int maxG = gp.length;
        for (Expr expr : fn) {
            if (!expr.isScalarExpr()) {
                scalarValue = false;
                break;
            }
        }
        if (scalarValue) {
            for (Expr expr : gp) {
                if (!expr.isScalarExpr()) {
                    scalarValue = false;
                    break;
                }
            }
        }
        for (Expr expr : fn) {
            if (!expr.isDoubleExpr()) {
                doubleValue = false;
                break;
            }
        }
        if (doubleValue) {
            for (Expr expr : gp) {
                if (!expr.isDoubleExpr()) {
                    doubleValue = false;
                    break;
                }
            }
        }
        Complex[][] gfps = new Complex[gp.length][maxF];
        if (scalarValue) {
            switch (axis) {
                case XY:
                case X: {
                    EnhancedComputationMonitor mon = ComputationMonitorFactory.createIncrementalMonitor(monitor, (gp.length * maxF));
                    boolean finalDoubleValue = doubleValue;
                    Expr[] finalGp = gp;
                    ScalarProductOperator finalSp = sp;
                    Expr[] finalFn = fn;
                    Maths.invokeMonitoredAction(mon, monMessage, new VoidMonitoredAction() {
                        @Override
                        public void invoke(EnhancedComputationMonitor monitor, String monMessage) throws Exception {
                            String _monMessage = monMessage + "({0,number,#},{1,number,#})";
                            if (!finalDoubleValue) {
                                for (int q = 0; q < finalGp.length; q++) {
                                    DoubleToComplex gpq = finalGp[q].toDC();
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = finalSp.eval(gpq, finalFn[n].toDC());
                                        mon.inc(_monMessage, q, n);
                                    }
                                }
                            } else {
                                for (int q = 0; q < finalGp.length; q++) {
                                    DoubleToDouble gpq = finalGp[q].toDD();
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = Complex.valueOf(finalSp.evalDD(gpq, finalFn[n].toDD()));
                                        mon.inc(_monMessage, q, n);
                                    }
                                }
                            }
                        }
                    });
//                System.out.println("c1 = " + c1);
                    break;
                }
                case Y: {
                    EnhancedComputationMonitor mon = ComputationMonitorFactory.createIncrementalMonitor(monitor, (gp.length * maxF));
                    Expr[] finalGp1 = gp;
                    Maths.invokeMonitoredAction(mon, monMessage, new VoidMonitoredAction() {
                        @Override
                        public void invoke(EnhancedComputationMonitor monitor, String monMessage) throws Exception {
                            String _monMessage = monMessage + "({0,number,#},{1,number,#})";
                            for (int q = 0; q < finalGp1.length; q++) {
                                for (int n = 0; n < maxF; n++) {
                                    gfps[q][n] = Complex.ZERO;
                                    mon.inc(_monMessage, q, n);
                                }
                            }
                        }
                    });
                    break;
                }
            }
        } else {
            switch (axis) {
                case XY: {
                    EnhancedComputationMonitor mon = ComputationMonitorFactory.createIncrementalMonitor(monitor, (gp.length * maxF));
                    boolean finalDoubleValue1 = doubleValue;
                    Expr[] finalGp2 = gp;
                    ScalarProductOperator finalSp1 = sp;
                    Expr[] finalFn1 = fn;
                    Maths.invokeMonitoredAction(mon, monMessage, new VoidMonitoredAction() {
                        @Override
                        public void invoke(EnhancedComputationMonitor monitor, String monMessage) throws Exception {
                            String _monMessage = monMessage + "({0,number,#},{1,number,#})";

                            if (!finalDoubleValue1) {
                                for (int q = 0; q < finalGp2.length; q++) {
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = finalSp1.eval(finalGp2[q], finalFn1[n]);
                                        mon.inc(_monMessage, q, n);
                                    }
                                }
                            } else {
                                for (int q = 0; q < finalGp2.length; q++) {
                                    Expr gpq = finalGp2[q];
                                    DoubleToVector gpqv = gpq.toDV();
                                    for (int n = 0; n < maxF; n++) {
                                        DoubleToVector fnndv = finalFn1[n].toDV();
                                        gfps[q][n] = Complex.valueOf(
                                                finalSp1.evalDD(gpqv.getComponent(Axis.X).toDC().getReal(), fnndv.getComponent(Axis.X).toDC().getReal())
                                                        + finalSp1.evalDD(gpqv.getComponent(Axis.Y).toDC().getReal(), fnndv.getComponent(Axis.Y).toDC().getReal()));
                                        mon.inc(_monMessage, q, n);
                                    }
                                }
                            }

                        }
                    });

//                System.out.println("c1 = " + c1);
                    break;
                }
                case X: {
                    EnhancedComputationMonitor mon = ComputationMonitorFactory.createIncrementalMonitor(monitor, (gp.length * maxF));
                    boolean finalDoubleValue2 = doubleValue;
                    Expr[] finalGp3 = gp;
                    ScalarProductOperator finalSp2 = sp;
                    Expr[] finalFn2 = fn;
                    Maths.invokeMonitoredAction(mon, monMessage, new VoidMonitoredAction() {
                        @Override
                        public void invoke(EnhancedComputationMonitor monitor, String monMessage) throws Exception {
                            if (!finalDoubleValue2) {
                                for (int q = 0; q < finalGp3.length; q++) {
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = finalSp2.eval(finalGp3[q].toDV().getComponent(Axis.X), finalFn2[n].toDV().getComponent(Axis.X));
                                        mon.inc(monMessage);
                                    }
                                }
                            } else {
                                for (int q = 0; q < finalGp3.length; q++) {
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = Complex.valueOf(finalSp2.evalDD(finalGp3[q].toDV().getComponent(Axis.X).toDC().getReal(), finalFn2[n].toDV().getComponent(Axis.X).toDC().getReal()));
                                        mon.inc(monMessage);
                                    }
                                }
                            }
                        }
                    });

                    break;
                }
                case Y: {
                    EnhancedComputationMonitor mon = ComputationMonitorFactory.createIncrementalMonitor(monitor, (gp.length * maxF));
                    boolean finalDoubleValue3 = doubleValue;
                    Expr[] finalGp4 = gp;
                    ScalarProductOperator finalSp3 = sp;
                    Expr[] finalFn3 = fn;
                    Maths.invokeMonitoredAction(mon, monMessage, new VoidMonitoredAction() {
                        @Override
                        public void invoke(EnhancedComputationMonitor monitor, String monMessage) throws Exception {
                            if (!finalDoubleValue3) {
                                for (int q = 0; q < finalGp4.length; q++) {
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = finalSp3.eval(finalGp4[q].toDV().getComponent(Axis.Y), finalFn3[n].toDV().getComponent(Axis.Y));
                                        mon.inc(monMessage);
                                    }
                                }
                            } else {
                                for (int q = 0; q < finalGp4.length; q++) {
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = Complex.valueOf(finalSp3.evalDD(finalGp4[q].toDV().getComponent(Axis.Y).toDC().getReal(), finalFn3[n].toDV().getComponent(Axis.Y).toDC().getReal()));
                                        mon.inc(monMessage);
                                    }
                                }
                            }

                        }
                    });
                    break;
                }
            }
        }
        cache = gfps;
    }

//    public Complex zop(int p,int q,FnIndexes[] n_evan){
//        Complex c = Complex.ZERO;
//        for (FnIndexes n : n_evan) {
//            c = c.add(n.zn.multiply(gf(p, n)).multiply(fg(n, q)));
//        }
//        return c;
//    }

}
