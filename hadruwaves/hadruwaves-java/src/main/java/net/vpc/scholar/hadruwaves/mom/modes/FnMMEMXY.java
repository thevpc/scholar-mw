//package net.vpc.scholar.tmwlib.mom.modes;
//
//import net.vpc.scholar.math.Complex;
//import static net.vpc.scholar.math.Complex.I;
//import static net.vpc.scholar.math.Maths.sqr;
//import static net.vpc.scholar.tmwlib.Physics.*;
//import static net.vpc.scholar.math.FunctionFactory.*;
//
//import net.vpc.scholar.math.symbolic.DoubleToDouble;
//import net.vpc.scholar.math.Maths;
//import net.vpc.scholar.math.symbolic.DoubleToVector;
//import net.vpc.scholar.tmwlib.ModeType;
//
//import static java.lang.Math.PI;
//import static java.lang.Math.sqrt;
//import java.util.ArrayList;
//
//import net.vpc.scholar.math.util.ComputationMonitor;
//import net.vpc.scholar.tmwlib.ModeIndex;
//import net.vpc.scholar.tmwlib.ModeInfo;
//import net.vpc.scholar.tmwlib.WallBorders;
//
//import static net.vpc.scholar.math.Maths.*;
//
///**
// * TODO a valider
// */
//@Deprecated
//public class FnMMEMXY extends ModeFunctionsBase {
//
//    public FnMMEMXY() {
//        super(false, WallBorders.MMEM);
//    }
//
//    public ModeInfo[] getIndexesImpl(ComputationMonitor par0) {
//        ArrayList<ModeInfo> next = new ArrayList<ModeInfo>();
//        int max = getFnMax();
//        for (int n = 0; n < max && next.size() < max; n++) {
//            ModeType[] modes = new ModeType[]{ModeType.TEM, ModeType.TE, ModeType.TM};// TE prioritaire
//
//            for (int h = 0; h < 2; h++) {
//                for (int i = 0; i < n; i++) {
//                    for (int k = 0; k < modes.length && next.size() < max; k++) {
//                        ModeInfo o = h == 0 ? new ModeInfo(modes[k], i, n, next.size())
//                                : new ModeInfo(modes[k], n, i, next.size());
//                        if (doAcceptModeIndex(o.getMode())) {
//                            next.add(o);
//                        }
//                    }
//                }
//            }
//            for (int k = 0; k < modes.length && next.size() < max; k++) {
//                ModeInfo o = new ModeInfo(modes[k], n, n, next.size());
//                if (doAcceptModeIndex(o.getMode())) {
//                    next.add(o);
//                }
//            }
//        }
//        return next.toArray(new ModeInfo[next.size()]);
//    }
//
//    protected boolean doAcceptModeIndex(ModeIndex o) {
//        if (!super.doAcceptModeIndex(o)) {
//            return false;
//        }
//        //        if((o.m%2)!=0 || (o.n%2)!=0){
////            return false;
////        }
//        if (o.type() == ModeType.TM) {
//            return //                        getGammaImpl(o).getImag()==0 &&
//                    o.n() != 0;
//        } else if (o.type() == ModeType.TE) {
//            return //getGammaImpl(o).getImag()==0 &&
//                    o.m() != 0;
//        } else if (o.type() == ModeType.TEM) {
//            return o.m() == 0 && o.n() == 0;
//        } else {
//            throw new RuntimeException("impossible");
//        }
//    }
//
//    public double getCutoffFrequency(ModeIndex i) {
//        int m = i.m;
//        int n = i.n;
//        //Mode mode = i.mode;
//        double a = domain.xwidth();
//        double b = domain.ywidth();
//        double k0Coupure = sqrt(sqr(m * PI / a) + sqr(n * PI / b));
//        return freqByK0(k0Coupure);
//    }
//
//    public Complex getGammaImpl(ModeIndex n, BoxSpace bs) {
//        return new Complex(sqr((2.0 * n.m + 1) / 2.0 * PI / domain.xwidth()) + sqr(n.n * PI / domain.ywidth()) - sqr(cachedk0) * bs.getEpsr()).sqrt();
//    }
//
//    public Complex getImpedanceImpl(ModeInfo n) {
//        if (n.mode.type == ModeType.TM) {//tm
//            return n.firstBoxSpaceGamma.div(I(cachedOmega * Maths.EPS0));
//        } else if (n.mode.type == ModeType.TE) {//te
//            return I(cachedOmega * Maths.U0).div(n.firstBoxSpaceGamma);
//        } else {
//            return I(Maths.U0).div(Maths.EPS0);
//        }
//    }
//
//    public DoubleToVector getFnImpl(ModeIndex i) {
//        double m = (2.0 * i.m + 1) / 2.0;
//        int n = i.n;
//        ModeType mode = i.type;
//        double a = domain.xwidth();
//        double b = domain.ywidth();
//        DoubleToVector fn;
//        if (m == 0 && n == 0) {//mode TEM
//            fn = Maths.vector(
//                    (DZEROXY),
//                    (expr(1.0 / sqrt(a * b), domain)));
//            fn=(DoubleToVector) fn.setName("MMEM" + i);
//        } else {
//            double u = 1;
//            if (m != 0 && n != 0) {
//                u = 2;
//            }
//
//            //        double cached_PI_divide_WIDTH = Math.PI / a;
////        double cached_PI_divide_HEIGHT = Math.PI / b;
//            double n_bAmp = 0;
//            double m_aAmp = 0;
//            if (m != 0 || n != 0) {
//                double n_b = n * PI/ b;
//                double m_a = m * PI/ a;
//                double coeff = sqrt(2 * u / (a * b * (n_b * n_b + m_a * m_a)));
//                n_bAmp = n_b * coeff;
//                m_aAmp = m_a * coeff;
//            }
//            double nx;
//            double ny;
//            if (mode == ModeType.TE) {
//                nx = n_bAmp;
//                ny = -m_aAmp;
//            } else {
//                nx = -m_aAmp;
//                ny = -n_bAmp;
//            }
//            DoubleToDouble fx = sinXsinY(
//                    nx,
//                    /**
//                     * sin*
//                     */
//                    (m * PI / a), -(m * PI / a) * domain.xmin(),
//                    /**
//                     * sin*
//                     */
//                    (n * PI / b), -(n * PI / b) * domain.ymin(),
//                    domain
//            ).setName("nx.sin(" + m + "PIx/a)sin(" + n + "PIx/b)").toDD();
//
//            DoubleToDouble fy = cosXcosY(
//                    ny,
//                    /**
//                     * cos*
//                     */
//                    (m * PI / a), -(m * PI / a) * domain.xmin(),
//                    /**
//                     * cos*
//                     */
//                    (n * PI / b), -(n * PI / b) * domain.ymin(),
//                    domain
//            ).setName("nx.cos(" + m + "PIx/a)cos(" + n + "PIx/b)").toDD();
//
//            //if(fx.isNull() && fy.isNull()){
//            //    System.out.println("null for "+i);
//            //}
//            fn = Maths.vector((fx), (fy));
//            fn=(DoubleToVector) fn.setName("EMEM" + i);
//        }
//        return fn;
//    }
//}
