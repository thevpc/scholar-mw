package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadrumaths.util.VoidMonitoredAction;

import java.io.Serializable;

public class MemComplexScalarProductCache extends AbstractScalarProductCache implements Serializable {
    private Complex[/** p index **/][/** n index **/] cache=new Complex[0][0];
    private boolean hermitian;
    private boolean doubleValue;
    private boolean scalarValue;

    public MemComplexScalarProductCache(boolean hermitian, boolean doubleValue, boolean scalarValue) {
        this.hermitian = hermitian;
        this.doubleValue = doubleValue;
        this.scalarValue = scalarValue;
    }

    private static Expr[] simplifyAll(Expr[] e, EnhancedProgressMonitor mon) {
        Expr[] all = new Expr[e.length];
        Maths.invokeMonitoredAction(mon, "Simplify All", new VoidMonitoredAction() {
            @Override
            public void invoke(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
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

    public TVector<Complex> getColumn(int column) {
        Complex[] vmatrix = new Complex[cache.length];
        for (int i = 0; i < vmatrix.length; i++) {
            vmatrix[i] = cache[i][column];
        }
        return Maths.columnVector(vmatrix);
    }

    public TVector<Complex> getRow(int row) {
//        Complex[] vmatrix = new Complex[cache[0].length];
//        System.arraycopy(cache[row], 0, vmatrix, 0, vmatrix.length);
//        return Maths.columnVector(vmatrix);
        return Maths.columnVector(cache[row]);
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
        Complex complex = cache[p][n];
        return hermitian ? complex.conj() : complex;
    }

    public ScalarProductCache evaluate(ScalarProductOperator sp, Expr[] fn, Expr[] gp, boolean hermitian, AxisXY axis, ProgressMonitor monitor) {
        EnhancedProgressMonitor emonitor = ProgressMonitorFactory.enhance(monitor);
        String monMessage = getClass().getSimpleName();
        if (sp == null) {
            sp = Maths.Config.getDefaultScalarProductOperator();
        }
//        EnhancedProgressMonitor[] hmon = emonitor.split(new double[]{2, 1, 3});
//        if (doSimplifyAll) {
//            Expr[] finalFn = fn;
//            Expr[] finalGp = gp;
//            Expr[][] fg = Maths.invokeMonitoredAction(emonitor, "Simplify All", new MonitoredAction<Expr[][]>() {
//                @Override
//                public Expr[][] process(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
//                    Expr[][] fg = new Expr[2][];
//                    fg[0] = simplifyAll(finalFn, hmon[0]);
//
//                    fg[1] = simplifyAll(finalGp, hmon[1]);
//                    return fg;
//                }
//            });
//            fn = fg[0];
//            gp = fg[1];
//        }
        int maxF = fn.length;
//        int maxG = gp.length;
        Complex[][] gfps = new Complex[gp.length][maxF];
        if (scalarValue) {
            switch (axis) {
                case XY:
                case X: {
                    EnhancedProgressMonitor mon = ProgressMonitorFactory.createIncrementalMonitor(monitor, (gp.length * maxF));
                    boolean finalDoubleValue = doubleValue;
                    Expr[] finalGp = gp;
                    ScalarProductOperator finalSp = sp;
                    Expr[] finalFn = fn;
                    Maths.invokeMonitoredAction(mon, monMessage, new VoidMonitoredAction() {
                        @Override
                        public void invoke(EnhancedProgressMonitor monitor, String monMessage) throws Exception {
                            String _monMessage = monMessage + "({0,number,#},{1,number,#})";
                            if (!finalDoubleValue) {
                                for (int q = 0; q < finalGp.length; q++) {
                                    DoubleToComplex gpq = finalGp[q].toDC();
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = finalSp.evalDC(hermitian, gpq, finalFn[n].toDC());
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
                    EnhancedProgressMonitor mon = ProgressMonitorFactory.createIncrementalMonitor(monitor, (gp.length * maxF));
                    Expr[] finalGp1 = gp;
                    Maths.invokeMonitoredAction(mon, monMessage, new VoidMonitoredAction() {
                        @Override
                        public void invoke(EnhancedProgressMonitor monitor, String monMessage) throws Exception {
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
                    EnhancedProgressMonitor mon = ProgressMonitorFactory.createIncrementalMonitor(monitor, (gp.length * maxF));
                    boolean finalDoubleValue1 = doubleValue;
                    Expr[] finalGp2 = gp;
                    ScalarProductOperator finalSp1 = sp;
                    Expr[] finalFn1 = fn;
                    Maths.invokeMonitoredAction(mon, monMessage, new VoidMonitoredAction() {
                        @Override
                        public void invoke(EnhancedProgressMonitor monitor, String monMessage) throws Exception {
                            String _monMessage = monMessage + "({0,number,#},{1,number,#})";

                            if (!finalDoubleValue1) {
                                for (int q = 0; q < finalGp2.length; q++) {
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = finalSp1.eval(hermitian, finalGp2[q], finalFn1[n]);
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
                                                finalSp1.evalDD(gpqv.getComponent(Axis.X).toDC().getRealDD(), fnndv.getComponent(Axis.X).toDC().getRealDD())
                                                        + finalSp1.evalDD(gpqv.getComponent(Axis.Y).toDC().getRealDD(), fnndv.getComponent(Axis.Y).toDC().getRealDD()));
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
                    EnhancedProgressMonitor mon = ProgressMonitorFactory.createIncrementalMonitor(monitor, (gp.length * maxF));
                    boolean finalDoubleValue2 = doubleValue;
                    Expr[] finalGp3 = gp;
                    ScalarProductOperator finalSp2 = sp;
                    Expr[] finalFn2 = fn;
                    Maths.invokeMonitoredAction(mon, monMessage, new VoidMonitoredAction() {
                        @Override
                        public void invoke(EnhancedProgressMonitor monitor, String monMessage) throws Exception {
                            if (!finalDoubleValue2) {
                                    for (int q = 0; q < finalGp3.length; q++) {
                                        for (int n = 0; n < maxF; n++) {
                                            gfps[q][n] = finalSp2.eval(hermitian, finalGp3[q].toDV().getComponent(Axis.X), finalFn2[n].toDV().getComponent(Axis.X));
                                            mon.inc(monMessage);
                                        }
                                    }
                            } else {
                                if (true) {
                                    DoubleToDouble[] df = new DoubleToDouble[maxF];
                                    DoubleToDouble[] dg = new DoubleToDouble[finalGp3.length];
                                    for (int n = 0; n < maxF; n++) {
                                        df[n] = finalFn2[n].toDV().getComponent(Axis.X).toDD();
                                    }
                                    for (int q = 0; q < finalGp3.length; q++) {
                                        dg[q] = finalGp3[q].toDV().getComponent(Axis.X).toDD();
                                    }
                                    for (int n = 0; n < maxF; n++) {
                                        double[] doubles = finalSp2.evalDD(null, df[n], dg);
                                        for (int q = 0; q < finalGp3.length; q++) {
                                            gfps[q][n] = Complex.valueOf(doubles[q]);
                                            mon.inc(monMessage);
                                        }
                                    }
                                } else {
                                    for (int q = 0; q < finalGp3.length; q++) {
                                        for (int n = 0; n < maxF; n++) {
                                            gfps[q][n] = Complex.valueOf(finalSp2.evalDD(finalGp3[q].toDV().getComponent(Axis.X).toDC().getRealDD(), finalFn2[n].toDV().getComponent(Axis.X).toDC().getRealDD()));
                                            mon.inc(monMessage);
                                        }
                                    }
                                }
                            }
                        }
                    });

                    break;
                }
                case Y: {
                    EnhancedProgressMonitor mon = ProgressMonitorFactory.createIncrementalMonitor(monitor, (gp.length * maxF));
                    boolean finalDoubleValue3 = doubleValue;
                    Expr[] finalGp4 = gp;
                    ScalarProductOperator finalSp3 = sp;
                    Expr[] finalFn3 = fn;
                    Maths.invokeMonitoredAction(mon, monMessage, new VoidMonitoredAction() {
                        @Override
                        public void invoke(EnhancedProgressMonitor monitor, String monMessage) throws Exception {
                            if (!finalDoubleValue3) {
                                for (int q = 0; q < finalGp4.length; q++) {
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = finalSp3.eval(hermitian, finalGp4[q].toDV().getComponent(Axis.Y), finalFn3[n].toDV().getComponent(Axis.Y));
                                        mon.inc(monMessage);
                                    }
                                }
                            } else {
                                for (int q = 0; q < finalGp4.length; q++) {
                                    for (int n = 0; n < maxF; n++) {
                                        gfps[q][n] = Complex.valueOf(finalSp3.evalDD(finalGp4[q].toDV().getComponent(Axis.Y).toDC().getRealDD(), finalFn3[n].toDV().getComponent(Axis.Y).toDC().getRealDD()));
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
        return this;
    }

//    public Complex zop(int p,int q,FnIndexes[] n_evan){
//        Complex c = Complex.ZERO;
//        for (FnIndexes n : n_evan) {
//            c = c.add(n.zn.multiply(gf(p, n)).multiply(fg(n, q)));
//        }
//        return c;
//    }

//    private void writeObject(ObjectOutputStream oos)
//            throws IOException {
//        // default serialization
//        oos.defaultWriteObject();
//        int rowCount = cache.length;
//        oos.writeBoolean(hermitian);
//        oos.writeBoolean(doSimplifyAll);
//        oos.writeInt(rowCount);
//        int columnCount = cache.length ==0?0:cache[0].length;
//        oos.writeInt(columnCount);
//        for (int i = 0; i < rowCount; i++) {
//            for (int j = 0; j < columnCount; j++) {
//                Complex.writeObjectHelper(cache[i][j],oos);
//            }
//        }
//    }
//
//    private void readObject(ObjectInputStream ois)
//            throws ClassNotFoundException, IOException {
//        // default deserialization
//        ois.defaultReadObject();
//        hermitian=ois.readBoolean();
//        doSimplifyAll=ois.readBoolean();
//        int rowCount = ois.readInt(); // Replace with real deserialization
//        int columnCount = ois.readInt(); // Replace with real deserialization
//        cache=new Complex[rowCount][columnCount];
//        for (int i = 0; i < rowCount; i++) {
//            for (int j = 0; j < columnCount; j++) {
//                cache[i][j]=Complex.readObjectResolveHelper(ois);
//            }
//        }
//    }


}
