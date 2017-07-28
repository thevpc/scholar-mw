//package net.vpc.scholar.tmwlib.mom.modes;
//
//import net.vpc.scholar.math.Complex;
//import net.vpc.scholar.math.Maths;
//import net.vpc.scholar.math.symbolic.DoubleToVector;
//import net.vpc.scholar.tmwlib.*;
//import net.vpc.scholar.math.FunctionFactory;
//import net.vpc.scholar.math.symbolic.CosXCosY;
//
//import java.util.ArrayList;
//import net.vpc.scholar.math.util.ComputationMonitor;
//
//@Deprecated
//public class FnElectricXY extends ModeFunctionsBase {
////    public static void main(String[] args) {
////        System.out.println(Physics.C/(2*Math.PI)*Math.sqrt(Math.PI/(23E-3)));
////    }
//
//    public FnElectricXY() {
//        super(false, WallBorders.EEEE);
//    }
//
//    @Override
//    public ModeInfo[] getIndexesImpl(ComputationMonitor par0) {
//        ArrayList<ModeInfo> next = new ArrayList<ModeInfo>();
//        int max = getFnMax();
//        for (int n = 0; n < max && next.size() < max; n++) {
//            ModeType[] modes = new ModeType[]{ModeType.TE, ModeType.TM};// TE prioritaire
//
//            for (int h = 0; h < 2; h++) {
//                for (int i = 0; i < n; i++) {
//                    for (int k = 0; k < modes.length && next.size() < max; k++) {
//                        ModeInfo o = h == 0 ?
//                                new ModeInfo(modes[k], i, n, next.size())
//                                : new ModeInfo(modes[k], n, i, next.size());
//                        if (acceptIndexes(o)) {
//                            next.add(o);
//                        }
//                    }
//                }
//            }
//            for (int k = 0; k < modes.length && next.size() < max; k++) {
//                ModeInfo o = new ModeInfo(modes[k], n, n, next.size());
//                if (acceptIndexes(o)) {
//                    next.add(o);
//                }
//            }
//        }
//        return next.toArray(new ModeInfo[next.size()]);
//    }
//
//    private boolean acceptIndexes(ModeInfo o) {
//       if(!super.doAcceptModeIndex(o.getMode())){
//           return false;
//       }
//        if (o.mode.type == ModeType.TM) {
//            return (o.mode.m != 0 && o.mode.n != 0);
//        } else if (o.mode.type == ModeType.TE) {
//            return (o.mode.m != 0 || o.mode.n != 0);
//        } else {
//            throw new RuntimeException("impossible");
//        }
//    }
//
//    @Override
//    public double getCutoffFrequency(ModeIndex i) {
//        int m = i.m;
//        int n = i.n;
//        double a = domain.xwidth();
//        double b = domain.ywidth();
//        double k0Coupure = Math.sqrt(Maths.sqr(m * Math.PI / a) + Maths.sqr(n * Math.PI / b));
//        return Physics.freqByK0(k0Coupure);
//    }
//
//    @Override
//    public Complex getGammaImpl(ModeIndex i, BoxSpace bs) {
//        return new Complex(Maths.sqr(i.m * Math.PI / domain.xwidth()) + Maths.sqr(i.n * Math.PI / domain.ywidth()) - Maths.sqr(cachedk0)*bs.getEpsr()).sqrt();
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
//        double u = 1;
//        if (m != 0 && n != 0) {
//            u = 2;
//        }
//
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
//        CosXCosY fx = FunctionFactory.cosXsinY(
//                nx,
//                (m * Math.PI / a), -(m * Math.PI / a) * domain.xmin(),
//                (n * Math.PI / b), -(n * Math.PI / b) * domain.ymin(),
//                domain
//        );
//
//        CosXCosY fy = FunctionFactory.sinXcosY(
//                ny,
//                (m * Math.PI / a), -(m * Math.PI / a) * domain.xmin(),
//                (n * Math.PI / b), -(n * Math.PI / b) * domain.ymin(),
//                domain
//        );
//        DoubleToVector ff = Maths.vector((fx), (fy));
//        ff=(DoubleToVector) ff.setName("Elec" + i);
//        return ff;
//    }
//
//}
