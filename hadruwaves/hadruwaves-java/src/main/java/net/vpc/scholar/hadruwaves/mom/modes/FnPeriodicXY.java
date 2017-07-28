//package net.vpc.scholar.tmwlib.mom.modes;
//
//import net.vpc.scholar.math.Axis;
//import net.vpc.scholar.math.Complex;
//import net.vpc.scholar.math.Maths;
//import static net.vpc.scholar.math.Maths.sqr;
//
//import net.vpc.scholar.math.symbolic.DoubleToVector;
//
//import static net.vpc.scholar.tmwlib.Physics.freqByK0;
//import net.vpc.scholar.math.symbolic.CExp;
//import net.vpc.scholar.math.symbolic.DoubleValue;
//import net.vpc.scholar.math.FunctionFactory;
//import net.vpc.scholar.tmwlib.ModeType;
//
//import static java.lang.Math.PI;
//import static java.lang.Math.sqrt;
//import java.util.ArrayList;
//import net.vpc.scholar.math.util.ComputationMonitor;
//import net.vpc.scholar.tmwlib.ModeIndex;
//import net.vpc.scholar.tmwlib.ModeInfo;
//import net.vpc.scholar.tmwlib.WallBorders;
//
//public class FnPeriodicXY extends ModeFunctionsBase {
//    private Axis axis;
//    private double alphax;
//    private double betay;
//
////    public FnPeriodicXY() {
////        this(Axis.Y, 0, 0);//TE
////    }
//
//    public FnPeriodicXY(Axis polarization) {
//        this(polarization,0,0);
//    }
//
//    public FnPeriodicXY(Axis polarization, double phasex, double phasey) {
//        super(true, WallBorders.PPPP);
//        this.axis = polarization;
//        this.alphax = phasex;
//        this.betay = phasey;
//    }
//
////    public FnIndexes[] getIndexesImpl() {
////        int maxIndex = getFnMax() + 1;
////        ArrayList<FnIndexes> mnTe1 = new ArrayList<FnIndexes>(maxIndex);
////        int count = 0;
////        int max = (int) (Math.sqrt(maxIndex / 2) / 2);
////        Mode[] modes = ModeType.values();
////        for (int i = -max; count < maxIndex && i <= max; i++) {
////            for (int j = -max; count < maxIndex && j <= max; j++) {
////                for (int k = 0; k < modes.length && count < maxIndex; k++) {
////                    FnIndexes o = new FnIndexes(i, j, modes[k], count);
////                    if (o.m != 0 || o.n != 0) {
////                        mnTe1.add(o);
////                        count++;
////                    }
////                }
////            }
////        }
////        return mnTe1.toArray(new FnIndexes[mnTe1.size()]);
////    }
//
//    @Override
//    public ModeInfo[] getIndexesImpl(ComputationMonitor par0) {
//        ArrayList<ModeInfo> next = new ArrayList<ModeInfo>();
//        int max = getFnMax();
//        for (int n = 0; n < max && next.size() < max; n++) {
//            ModeType[] modes = axis == Axis.X ? new ModeType[]{ModeType.TM, ModeType.TE} : new ModeType[]{ModeType.TE, ModeType.TM};// TE prioritaire
//            for (int h = 0; h < 2; h++) {
//                for (int i = 0; i < n; i++) {
//                    for (int s1 = -1; s1 <= 1; s1 += 2) {
//                        if (i == 0 && s1 == -1) {
//                            continue;
//                        }
//                        for (int s2 = -1; s2 <= 1; s2 += 2) {
//                            if (n == 0 && s2 == -1) {
//                                continue;
//                            }
//                            for (int k = 0; k < modes.length && next.size() < max; k++) {
//                                ModeInfo o = h == 0 ?
//                                        new ModeInfo(modes[k], s1 * i, n * s2, next.size())
//                                        : new ModeInfo(modes[k], n * s2, s1 * i, next.size());
//                                if (modes[k] == ModeType.TM) {
//                                    next.add(o);
//                                } else if (modes[k] == ModeType.TE) {
//                                    next.add(o);
//                                } else {
//                                    throw new RuntimeException("impossible");
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            for (int s1 = -1; s1 <= 1; s1 += 2) {
//                if (n == 0 && s1 == -1) {
//                    continue;
//                }
//                for (int s2 = -1; s2 <= 1; s2 += 2) {
//                    if (n == 0 && s2 == -1) {
//                        continue;
//                    }
//                    for (int k = 0; k < modes.length && next.size() < max; k++) {
//                        ModeInfo o = new ModeInfo(modes[k], s1 * n, s2 * n, next.size());
//
//                        if (modes[k] == ModeType.TM) {
//                            next.add(o);
//                        } else if (modes[k] == ModeType.TE) {
//                            next.add(o);
//                        } else {
//                            throw new RuntimeException("impossible");
//                        }
//                    }
//                }
//            }
//        }
//        return next.toArray(new ModeInfo[next.size()]);
//    }
//
//
//    @Override
//    public double getCutoffFrequency(ModeIndex i) {
//        int m = i.m;
//        int n = i.n;
//        if (m == 0 && n == 0) {
//            return 0;
//        }
//        //Mode mode = i.mode;
//        double a = domain.xwidth();
//        double b = domain.ywidth();
//        double k0 = sqrt(sqr(2 * m * PI / a) + sqr(2 * n * PI / b));
//        return freqByK0(k0);
//    }
//
//    @Override
//    public Complex getGammaImpl(ModeIndex i, BoxSpace bs) {
//        return new Complex(Maths.sqr(2 * i.m * Math.PI / domain.xwidth()) + Maths.sqr(2 * i.n * Math.PI / domain.ywidth()) - Maths.sqr(cachedk0)*bs.getEpsr()).sqrt();
//    }
//
//
//    @Override
//    public Complex getImpedanceImpl(ModeInfo i) {
//        if (i.mode.type == ModeType.TM) {//tm
//            return i.firstBoxSpaceGamma.div(Complex.I(cachedOmega * Maths.EPS0));
//        } else {//te
//            return new Complex(0, cachedOmega * Maths.U0).div(i.firstBoxSpaceGamma);
//        }
//    }
//
//    @Override
//    public DoubleToVector getFnImpl(ModeIndex i) {
//        int m = i.m;
//        int n = i.n;
//        ModeType mode = i.type;
//        double a = domain.xwidth();
//        double b = domain.ywidth();
//        double bx = 2 * m * PI / a;
//        double by = 2 * n * PI / b;
//        double Bx = bx / sqrt(bx * bx + by * by);
//        double By = by / sqrt(bx * bx + by * by);
//        double sqrt_ab = sqrt(a * b);
////        double c = 1 / (sqrt(a * b * ((m * m) / (a * a) + (n * n) / (b * b))));
//        String name = "Period" + i;
//        if (mode == ModeType.TE) {
//            if (m == n && n == 0) {
//                DoubleToVector ff= Maths.vector(
//                        FunctionFactory.CZEROXY,
//                        (new DoubleValue(-1 / sqrt_ab, domain))
//                );
//                ff=(DoubleToVector) ff.setName(name);
//                return ff;
//            } else {
//                //TODO v�rifier ca stp
//                DoubleToVector ff= Maths.vector(
//                        new CExp(By / sqrt_ab, -bx, -by, domain),
//                        new CExp(-Bx / sqrt_ab, -bx, -by, domain)
//                );
//                ff=(DoubleToVector) ff.setName(name);
//                return ff;
////                return new CFunctionXY2D(name,
////                        new CFunctionXY(new DCstFunctionXY(1 / sqrt_ab, domain)),
////                        CFunctionXY.ZERO
////                );
//            }
//        } else {
//            if (m == n && n == 0) {
//                DoubleToVector ff= Maths.vector(
//                        (new DoubleValue(1 / sqrt_ab, domain)),
//                        FunctionFactory.CZEROXY
//                );
//                ff=(DoubleToVector) ff.setName(name);
//                return ff;
//            } else {
//                DoubleToVector ff= Maths.vector(
//                        new CExp(-Bx / sqrt_ab + alphax, -bx, -by, domain),
//                        new CExp(-By / sqrt_ab + betay, -bx, -by, domain)
//                );
//                ff=(DoubleToVector) ff.setName(name);
//                return ff;
//            }
//        }
//    }
//
//
//    @Override
//    public String toString() {
//        return getClass().getSimpleName() + "[Polarization" + axis + "]" + (alphax != 0 ? ("[alphax=" + String.valueOf(alphax) + "]") : "") + (betay != 0 ? ("[alphax=" + String.valueOf(betay) + "]") : "");
//    }
//
//}
