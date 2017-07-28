//package net.vpc.scholar.tmwlib.mom.modes;
//
//import net.vpc.scholar.math.Complex;
//import net.vpc.scholar.math.Maths;
//import net.vpc.scholar.math.symbolic.DoubleToVector;
//import net.vpc.scholar.tmwlib.*;
//import net.vpc.scholar.math.symbolic.AbstractDoubleToDouble;
//import net.vpc.scholar.math.symbolic.CosXCosY;
//
//import java.util.ArrayList;
//import net.vpc.scholar.math.util.ComputationMonitor;
//
//public class FnMagneticXYOld extends ModeFunctionsBase {
//
//
//    public FnMagneticXYOld() {
//        super(false, WallBorders.MMMM);
//    }
//
//    public ModeInfo[] getIndexesImpl(ComputationMonitor par0) {
//        ArrayList<ModeInfo> next = new ArrayList<ModeInfo>();
//        int max=getFnMax();
//        for (int n = 0; n < max && next.size()<max; n++) {
//            ModeType[] modes = new ModeType[]{ModeType.TM, ModeType.TE};// TE prioritaire
//            for (int h = 0; h < 2; h++) {
//                for (int i = 0; i < n; i++) {
//                    for (int k = 0; k < modes.length && next.size() < max; k++) {
//                        ModeInfo o = h == 0 ?
//                                new ModeInfo(modes[k], i, n, next.size())
//                                : new ModeInfo(modes[k], n, i, next.size())
//                                ;
//                        if (modes[k] == ModeType.TM) {
//                            if (o.mode.m != 0 && o.mode.n != 0) {
//                                next.add(o);
//                            }
//                        } else if (modes[k] == ModeType.TE) {
//                            if (o.mode.m != 0 || o.mode.n != 0) {
//                                next.add(o);
//                            }
//                        } else {
//                            throw new RuntimeException("impossible");
//                        }
//                    }
//                }
//            }
//            for (int k = 0; k < modes.length && next.size() < max; k++) {
//                ModeInfo o = new ModeInfo(modes[k], n, n, next.size());
//
//                if (modes[k] == ModeType.TM) {
//                    if (o.mode.m != 0 && o.mode.n != 0) {
//                        next.add(o);
//                    }
//                } else if (modes[k] == ModeType.TE) {
//                    if (o.mode.m != 0 || o.mode.n != 0) {
//                        next.add(o);
//                    }
//                } else {
//                    throw new RuntimeException("impossible");
//                }
//            }
//        }
//        return next.toArray(new ModeInfo[next.size()]);
//    }
//
//    public double getCutoffFrequency(ModeIndex i) {
//        int m = i.m;
//        int n = i.n;
//        //Mode mode = i.mode;
//        double a = domain.xwidth();
//        double b = domain.ywidth();
//        double k0 = Math.sqrt(Maths.sqr(m * Math.PI / a) + Maths.sqr(n * Math.PI / b));
//        return Physics.freqByK0(k0);
//    }
//
//    public Complex getGammaImpl(ModeIndex i, BoxSpace bs) {
//        return new Complex(Maths.sqr(i.m * Math.PI / domain.xwidth()) + Maths.sqr(i.n * Math.PI / domain.ywidth()) - Maths.sqr(cachedk0)*bs.getEpsr()).sqrt();
//    }
//
//
//    public Complex getImpedanceImpl(ModeInfo i) {
//        if (i.mode.type == ModeType.TM) {//tm
//            return i.firstBoxSpaceGamma.div(Complex.I(cachedOmega * Maths.EPS0));
//        } else {//te
//            return new Complex(0, cachedOmega * Maths.U0).div(i.firstBoxSpaceGamma);
//        }
//    }
//
//    public DoubleToVector getFnImpl(ModeIndex i) {
//        int m = i.m;
//        int n = i.n;
//        ModeType mode = i.type;
//        double dw = domain.xwidth();
//        double dh = domain.ywidth();
//        double u = 1;
//        if (m != 0 && n != 0) {
//            u = 2;
//        }
//
//        double cached_PI_divide_WIDTH = Math.PI / dw;
//        double cached_PI_divide_HEIGHT = Math.PI / dh;
//        double n1 = 0;
//        double n2 = 0;
//        if (m != 0 || n != 0) {
//            double n_height = n / dh;
//            double m_width = m / dw;
//            double coeff = Math.sqrt(2 * u / dw / dh
//                    / (n_height * n_height + m_width * m_width));
//            n1 = n_height * coeff;
//            n2 = m_width * coeff;
//        }
//        double nx;
//        double ny;
//        if (mode == ModeType.TE) {
//            nx = n1;
//            ny = -n2;
//        } else {
//            nx = n2;
//            ny = n1;
//        }
//        AbstractDoubleToDouble fx = new CosXCosY(nx,
//                m * cached_PI_divide_WIDTH,
//                0,
//                n * cached_PI_divide_HEIGHT,
//                n * Maths.HALF_PI - Maths.HALF_PI,
//                domain);
//        AbstractDoubleToDouble fy = new CosXCosY(ny,
//                m * cached_PI_divide_WIDTH,
//                0 - Maths.HALF_PI,
//                n * cached_PI_divide_HEIGHT,
//                n * Maths.HALF_PI,
//                domain);
//        DoubleToVector ff = Maths.vector((fx), (fy));
//        ff=(DoubleToVector) ff.setName("Magn" + i);
//        return ff;
//    }
//
//}
