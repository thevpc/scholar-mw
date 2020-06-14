package net.vpc.scholar.hadrumaths.test.removeme;

//package net.vpc.scholar.math.integration;
//
//import static java.lang.Math.abs;
//import static java.lang.Math.max;
//import net.vpc.scholar.math.IDDx;
//
//import net.vpc.scholar.math.functions.dfx.DDx;
//
///**
// * Created by IntelliJ IDEA.
// * User: vpc
// * Date: 29 juil. 2005
// * Time: 18:55:20
// * To change this template use File | Settings | File Templates.
// */
//public class DQuadIntegralX implements DIntegralX {
//    private static final int ERR_MAX_STEP_REACHED = 1;
//    private static final int ERR_MAX_FCT_COUNT = 2;
//    private static final int ERR_NAN_OR_INFINITE = 3;
//    private double tol = 1E-6;
//    private double hminCoeff = 1E-52 / 1024.0;
//    private double hmaxCoeff = 1.0 / 32.0;
//    private int maxfcnt = 10000;
//
//    public DQuadIntegralX() {
//    }
//
//    public DQuadIntegralX(double tol, double minWindow, double maxWindow, int maxfcnt) {
//        if (tol > 0) {
//            this.tol = tol;
//        }
//        if (tol > 0) {
//            this.hminCoeff = minWindow;
//        }
//        if (tol > 0) {
//            this.hmaxCoeff = maxWindow;
//        }
//        if (maxfcnt > 0) {
//            this.maxfcnt = maxfcnt;
//        }
//    }
//
//    public double integrateX(IDDx f, double a, double b) {
//
//        double[] x = new double[]{a, (a + b) / 2.0, b};
//        double[] y = f.computeDouble(x, null,null);
//        int fcnt = 3;
//        double hmin = hminCoeff * Math.abs(b - a);
//        double hmax = hmaxCoeff * Math.abs(b - a);
//
//        if (Double.isInfinite(y[0]) || Double.isNaN(y[0])) {
//            y[0] = f.computeDouble(a + hmin);
//            fcnt = fcnt + 1;
//        }
//        if (Double.isInfinite(y[2]) || Double.isNaN(y[2])) {
//            y[0] = f.computeDouble(a - hmin);
//            fcnt = fcnt + 1;
//        }
//
//
//        // Call the recursive core integrator.
//        double[] o = quadstep(f, a, b, y[0], y[1], y[2], fcnt, hmin, hmax);
//        switch ((int) o[2]) {
//            case ERR_MAX_STEP_REACHED:
//                {
//                    System.out.println("Minimum step size reached; singularity possible.");
//                    //throw new IllegalArgumentException("Minimum step size reached; singularity possible.");
//                    break;
//                }
//            case ERR_MAX_FCT_COUNT:
//                {
//                    System.out.println("Maximum function count exceeded; singularity likely.");
////                    throw new IllegalArgumentException("Maximum function count exceeded; singularity likely.");
//                    break;
//                }
//            case ERR_NAN_OR_INFINITE:
//                {
//                    System.out.println("Infinite or Not-a-Number function value encountered.");
//                    //throw new IllegalArgumentException("Infinite or Not-a-Number function value encountered.");
//                    break;
//                }
//        }
//        return o[0];
//    }
//
//    private double[] quadstep(IDDx f, double a, double b, double fa, double fc, double fb, int fcnt, double hmin, double hmax) {
////QUADSTEP  Recursive core routine for function QUAD.
//
//
////% Evaluate integrand twice in interior of subinterval [a,b].
//        double h = b - a;
//        double c = (a + b) / 2;
//        if (abs(h) < hmin || c == a || c == b) {
//            //% Minimum step size reached; singularity possible.
//            double Q = h * fc;
//            double warn = ERR_MAX_STEP_REACHED;
//            return new double[]{Q, fcnt, warn};
//
//        }
//        double[] x = {(a + c) / 2, (c + b) / 2};
//        double[] y = f.computeDouble(x, null,null);
//
//        fcnt = fcnt + 2;
//        if (fcnt > maxfcnt) {//% Maximum function count exceeded; singularity likely.
//            double Q = h * fc;
//            double warn = ERR_MAX_FCT_COUNT;
//            return new double[]{Q, fcnt, warn};
//        }
//        double fd = y[0];
//        double fe = y[1];
//
//        //% Three point Simpson's rule.
//        double Q1 = (h / 6) * (fa + 4 * fc + fb);
//
//        //% Five point double Simpson's rule.
//        double Q2 = (h / 12) * (fa + 4 * fd + 2 * fc + 4 * fe + fb);
//
//        //% One step of Romberg extrapolation.
//        double Q = Q2 + (Q2 - Q1) / 15;
//
//        if (Double.isInfinite(Q) || Double.isNaN(Q)) { //% Infinite or Not-aNumber function value encountered.
//            double warn = ERR_NAN_OR_INFINITE;
//            return new double[]{Q, fcnt, warn};
//        }
//        //disp(sprintf('%8.0f %16.10f %18.8e %16.10f', fcnt, a, h, Q))
//        //% Check accuracy of integral over this subinterval.
//        if (absdbl(h) <= hmax && absdbl(Q2 - Q) <= tol) {
//            double warn = 0;
//            return new double[]{Q, fcnt, warn};
//        } else {//% Subdivide into two subintervals.
//            double[] o1 = quadstep(f, a, c, fa, fd, fc, fcnt, hmin, hmax);
//            double[] o2 = quadstep(f, c, b, fc, fe, fb, fcnt, hmin, hmax);
//            Q = o1[0] + o2[0];
//            int warn = max((int) o1[2], (int) o2[2]);
//            return new double[]{Q, fcnt, warn};
//        }
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 23 * hash + (int) (Double.doubleToLongBits(this.tol) ^ (Double.doubleToLongBits(this.tol) >>> 32));
//        hash = 23 * hash + (int) (Double.doubleToLongBits(this.hminCoeff) ^ (Double.doubleToLongBits(this.hminCoeff) >>> 32));
//        hash = 23 * hash + (int) (Double.doubleToLongBits(this.hmaxCoeff) ^ (Double.doubleToLongBits(this.hmaxCoeff) >>> 32));
//        hash = 23 * hash + this.maxfcnt;
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final DQuadIntegralX other = (DQuadIntegralX) obj;
//        if (Double.doubleToLongBits(this.tol) != Double.doubleToLongBits(other.tol)) {
//            return false;
//        }
//        if (Double.doubleToLongBits(this.hminCoeff) != Double.doubleToLongBits(other.hminCoeff)) {
//            return false;
//        }
//        if (Double.doubleToLongBits(this.hmaxCoeff) != Double.doubleToLongBits(other.hmaxCoeff)) {
//            return false;
//        }
//        if (this.maxfcnt != other.maxfcnt) {
//            return false;
//        }
//        return true;
//    }
//
//
//}
//
//
//
