//package net.vpc.scholar.tmwlib.mom.modes;
//
//import net.vpc.scholar.math.Complex;
//import static net.vpc.scholar.math.Maths.sqr;
//import static net.vpc.scholar.tmwlib.Physics.freqByK0;
//
//import net.vpc.scholar.math.Maths;
//import net.vpc.scholar.math.FunctionFactory;
//import net.vpc.scholar.math.symbolic.DoubleToVector;
//import net.vpc.scholar.math.symbolic.CosXCosY;
//import net.vpc.scholar.tmwlib.ModeType;
//import static java.lang.Math.PI;
//import static java.lang.Math.sqrt;
//import java.util.ArrayList;
//import java.util.Collection;
//import net.vpc.scholar.math.util.ComputationMonitor;
//import net.vpc.scholar.tmwlib.ModeIndex;
//import net.vpc.scholar.tmwlib.ModeInfo;
//import net.vpc.scholar.tmwlib.WallBorders;
//
//@Deprecated
//public class FnMagneticXY extends ModeFunctionsBase {
//
//    public FnMagneticXY() {
//        super(false, WallBorders.MMMM);
//    }
////    @Override
////    public ModeInfo[] getIndexesImpl() {
////        ArrayList<ModeInfo> next = new ArrayList<ModeInfo>();
////        int maxx = getFnMax();
////        int max = (int) (Math.ceil(Math.sqrt(maxx) / 2))+2;
////        if (max <= 0) {
////            max = 2;
////        }
////        Mode[] modes = new ModeType[]{ModeType.TM, ModeType.TE}; // TM prioritaire
////        int found = 0;
////        for (int i = 0; i < max; i++) {
////            for (int j = 0; j < max; j++) {
////                for (Mode mode : modes) {
////                    ModeInfo modeInfo = new ModeInfo(mode, i, j);
////                    if (found < maxx && acceptIndexes(modeInfo)) {
////                        next.add(modeInfo);
////                        found++;
////                    }
////                }
////            }
////        }
////        return next.toArray(new ModeInfo[next.size()]);
////    }
//
//    public ModeInfo[] getIndexesImpl(ComputationMonitor par0) {
//        ArrayList<ModeInfo> next = new ArrayList<ModeInfo>();
//        int max = getFnMax();
//        int count = 0;
//        ModeType[] modes = new ModeType[]{ModeType.TM, ModeType.TE}; // TM prioritaire
//        out:
//        for (int i = 0;; i++) {
//            Collection<ModeInfo> modeInfos = nextModeInfos(i, modes, true);
//            for (ModeInfo modeInfo : modeInfos) {
//                next.add(modeInfo);
//                count++;
//                if (count > max) {
//                    break out;
//                }
//            }
//        }
//
////        int iter = 0;
////        while (count < max) {
////            Collection<ModeInfo> modeInfos = nextModeInfos(iter, modes);
////            for (ModeInfo modeInfo : modeInfos) {
////                if (acceptIndexes(modeInfo)) {
////                    next.add(modeInfo);
////                    count++;
////                    if (count > max) {
////                        break;
////                    }
////                }
////            }
////            iter++;
////        }
//        return next.toArray(new ModeInfo[next.size()]);
//    }
////
////    public ModeInfo[] getIndexesImpl() {
////        ArrayList<ModeInfo> next = new ArrayList<ModeInfo>();
////        int max=getFnMax();
////        for (int n = 0; n < max && next.size()<max; n++) {
////            Mode[] modes = new ModeType[]{ModeType.TM,ModeType.TE};// TM prioritaire
////
////            for (int h = 0; h < 2; h++) {
////                for (int i = 0; i < n; i++) {
////                    for (int k = 0; k < modes.length && next.size() < max; k++) {
////                        ModeInfo o = h == 0 ?
////                                new ModeInfo(modes[k], i, n, next.size())
////                                : new ModeInfo(modes[k], n, i, next.size())
////                                ;
////                        if(acceptIndexes(o)){
////                            next.add(o);
////                        }
////                    }
////                }
////            }
////            for (int k = 0; k < modes.length && next.size() < max; k++) {
////                ModeInfo o = new ModeInfo(modes[k], n, n, next.size());
////                if(acceptIndexes(o)){
////                    next.add(o);
////                }
////            }
////        }
////        return next.toArray(new ModeInfo[next.size()]);
////    }
//
//    @Override
//    protected boolean doAcceptModeIndex(ModeIndex o) {
////        if(o.mode.equals(Mode.TM) && ((o.m==1 && o.n==0) || (o.m==2 && o.n==0) ||(o.m==3 && o.n==0))){
////            return false;
////        }
//        if (!super.doAcceptModeIndex(o)) {
//            return false;
//        }
////        return o.m!=0 || o.n!=0;
////        return true;
//        //TODO a corriger, please validate
//        if (o.type() == ModeType.TM) {
//            if (o.m() != 0 || o.n() != 0) {
//                return true;
//            }
//        } else if (o.type() == ModeType.TE) {
//            if (o.m() != 0 && o.n() != 0) {
//                return true;
//            }
//        } else {
//            throw new RuntimeException("impossible");
//        }
//        return false;
//    }
//
//    @Override
//    public double getCutoffFrequency(ModeIndex i) {
//        int m = i.m;
//        int n = i.n;
//        //Mode mode = i.mode;
//        double a = domain.xwidth();
//        double b = domain.ywidth();
//        double k0 = sqrt(sqr(m * PI / a) + sqr(n * PI / b));
//        return freqByK0(k0);
//    }
//
//    @Override
//    public Complex getGammaImpl(ModeIndex i, BoxSpace bs) {
//        return new Complex(sqr(i.m * PI / domain.xwidth()) + sqr(i.n * PI / domain.ywidth()) - sqr(cachedk0) * bs.getEpsr()).sqrt();
//    }
//
//    @Override
//    public DoubleToVector getFnImpl(ModeIndex i) {
//        int m = i.m;
//        int n = i.n;
//        ModeType mode = i.type;
//        double a = domain.xwidth();
//        double b = domain.ywidth();
//        double u = 1;
//        if (m != 0 && n != 0) {
//            u = 2;
//        }
//
////        double cached_PI_divide_WIDTH = Math.PI / a;
////        double cached_PI_divide_HEIGHT = Math.PI / b;
//        double n_bAmp = 0;
//        double m_aAmp = 0;
//        if (m != 0 || n != 0) {
//            double n_b = n * PI/ b;
//            double m_a = m * PI/ a;
//            double coeff = Math.sqrt(2 * u / (a * b * (n_b * n_b + m_a * m_a)));
//            n_bAmp = n_b * coeff;
//            m_aAmp = m_a * coeff;
//        }
//        double nx;
//        double ny;
//        if (mode == ModeType.TE) {
//            nx = n_bAmp;
//            ny = -m_aAmp;
//        } else {
//            nx = -m_aAmp;
//            ny = -n_bAmp;
//        }
//
//        CosXCosY fx = FunctionFactory.sinXcosY(nx, m * PI / a, -(m * PI / a) * domain.xmin(), n * PI / b, -(n * PI / b) * domain.ymin(), domain);
//
//        CosXCosY fy = FunctionFactory.cosXsinY(ny, m * PI / a, -(m * PI / a) * domain.xmin(), n * PI / b, -(n * PI / b) * domain.ymin(), domain);
//        /*
//        DFunctionXY fx = new DCosCosFunctionXY(
//        nx,
//        m * cached_PI_divide_WIDTH, //*x
//        0,
//        n * cached_PI_divide_HEIGHT, //*y
//        n * HALF_PI - HALF_PI,
//        domain);
//        DFunctionXY fy = new DCosCosFunctionXY(ny,
//        m * cached_PI_divide_WIDTH,
//        0 - HALF_PI,
//        n * cached_PI_divide_HEIGHT,
//        n * HALF_PI,
//        domain);
//         */
//        if (fx.isZero() && fy.isZero()) {
//            System.out.println("null for " + i);
//        }
//        DoubleToVector ff = Maths.vector((fx), (fy));
//        ff=(DoubleToVector) ff.setName("Magn" + i);
//        return ff;
//    }
//}