package net.vpc.scholar.hadrumaths.integration;

import net.vpc.scholar.hadrumaths.symbolic.DDyIntegralX;
import net.vpc.scholar.hadrumaths.symbolic.DDzIntegralXY;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.dump.Dumpable;
import net.vpc.scholar.hadrumaths.dump.Dumper;

import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 29 juil. 2005
 * Time: 18:55:20
 * To change this template use File | Settings | File Templates.
 */
public class DQuadIntegralXY implements DIntegralXY, Dumpable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final int ERR_MAX_STEP_REACHED = 1;
    private static final int ERR_MAX_FCT_COUNT = 2;
    private static final int ERR_NAN_OR_INFINITE = 3;
    private double tolerance = 1E-6;
    private double hminCoeff = 1E-52 / 1024.0;
    private double hmaxCoeff = 1.0 / 32.0;
    private int maxfcnt = 10000;

    public DQuadIntegralXY() {

    }


    @Override
    public double integrateX(DoubleToDouble f, double xmin, double xmax) {
        return integrateX(f, 0, xmin, xmax);
    }

    public DQuadIntegralXY(double tolerance, double minWindow, double maxWindow, int maxfcnt) {
        if (tolerance > 0) {
            this.tolerance = tolerance;
        }
        if (tolerance > 0) {
            this.hminCoeff = minWindow;
        }
        if (tolerance > 0) {
            this.hmaxCoeff = maxWindow;
        }
        if (maxfcnt > 0) {
            this.maxfcnt = maxfcnt;
        }
    }

    public double integrateXYZ(DoubleToDouble f, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
//        DFunctionXY simp=FunctionFactory.simplify(f);
//        if(simp==null){
//            simp=f;
//        }else{
//            System.out.println("simplification");
//        }
        //g(z)= intx_(z)
        DDzIntegralXY f2 = new DDzIntegralXY(f, this, xmin, xmax, ymin, ymax);
        return integrateX(f2, 0, zmin, zmax);
    }

    public double integrateXY(DoubleToDouble f, double xmin, double xmax, double ymin, double ymax) {
//        DFunctionXY simp=FunctionFactory.simplify(f);
//        if(simp==null){
//            simp=f;
//        }else{
//            System.out.println("simplification");
//        }

        DDyIntegralX f2 = new DDyIntegralX(f, this, xmin, xmax);
        return integrateX(f2, 0, ymin, ymax);
    }

    public double integrateX(DoubleToDouble f, double y0, double xmin, double xmax) {
        double[] x = new double[]{xmin, (xmin + xmax) / 2.0, xmax};
        if (f.isZero()) {
            return 0;
        }
        double[] y = f.computeDouble(x, y0, null, null);
        if (Double.isInfinite(y[2])) {
            y = f.computeDouble(x, y0, null, null);
        }
        int fcnt = 3;
        double hmin = hminCoeff * Math.abs(xmax - xmin);
        double hmax = hmaxCoeff * Math.abs(xmax - xmin);

        if (Double.isInfinite(y[0]) || Double.isNaN(y[0])) {
            y[0] = f.computeDouble(xmin + hmin, y0);
            fcnt = fcnt + 1;
        }
        if (Double.isInfinite(y[2]) || Double.isNaN(y[2])) {
            y[0] = f.computeDouble(xmin - hmin, y0);
            fcnt = fcnt + 1;
        }


        // Call the recursive core integrator.
        double[] o = quadstepX(f, y0, xmin, xmax, y[0], y[1], y[2], fcnt, hmin, hmax);
        switch ((int) o[2]) {
            case ERR_MAX_STEP_REACHED: {
//                System.out.println("Minimum step size reached; singularity possible.");
                //throw new IllegalArgumentException("Minimum step size reached; singularity possible.");
                break;
            }
            case ERR_MAX_FCT_COUNT: {
                System.out.println("Maximum function count exceeded; singularity likely.");
//                    throw new IllegalArgumentException("Maximum function count exceeded; singularity likely.");
                break;
            }
            case ERR_NAN_OR_INFINITE: {
//                System.out.println("Infinite or Not-a-Number function value encountered.");
                //throw new IllegalArgumentException("Infinite or Not-a-Number function value encountered.");
                break;
            }
        }
        return o[0];
    }


    public double integrateY(DoubleToDouble f, double x0, double ymin, double ymax) {

        double[] yy = new double[]{ymin, (ymin + ymax) / 2.0, ymax};
        if (f.isZero()) {
            return 0;
        }
        double[] y = f.computeDouble(x0, yy, null, null);
        int fcnt = 3;
        double hmin = hminCoeff * Math.abs(ymax - ymin);
        double hmax = hmaxCoeff * Math.abs(ymax - ymin);

        if (Double.isInfinite(y[0]) || Double.isNaN(y[0])) {
            y[0] = f.computeDouble(x0, ymin + hmin);
            fcnt = fcnt + 1;
        }
        if (Double.isInfinite(y[2]) || Double.isNaN(y[2])) {
            y[0] = f.computeDouble(x0, ymin - hmin);
            fcnt = fcnt + 1;
        }


        // Call the recursive core integrator.
        double[] o = quadstepY(f, x0, ymin, ymax, y[0], y[1], y[2], fcnt, hmin, hmax);
        switch ((int) o[2]) {
            case ERR_MAX_STEP_REACHED: {
//                System.out.println("Minimum step size reached; singularity possible.");
                //throw new IllegalArgumentException("Minimum step size reached; singularity possible.");
                break;
            }
            case ERR_MAX_FCT_COUNT: {
                System.out.println("Maximum function count exceeded; singularity likely.");
//                    throw new IllegalArgumentException("Maximum function count exceeded; singularity likely.");
                break;
            }
            case ERR_NAN_OR_INFINITE: {
//                System.out.println("Infinite or Not-a-Number function value encountered.");
                //throw new IllegalArgumentException("Infinite or Not-a-Number function value encountered.");
                break;
            }
        }
        return o[0];
    }

    private double[] quadstepX(DoubleToDouble f, double y0, double a, double b, double fa, double fc, double fb, int fcnt, double hmin, double hmax) {
//QUADSTEP  Recursive core routine for function QUAD.


//% Evaluate integrand twice in interior of subinterval [a,b].
        double h = b - a;
        double c = (a + b) / 2;
        if (Math.abs(h) < hmin || c == a || c == b) {
            //% Minimum step size reached; singularity possible.
            double Q = h * fc;
            double warn = ERR_MAX_STEP_REACHED;
            return new double[]{Q, fcnt, warn};

        }
        double[] x = {(a + c) / 2, (c + b) / 2};
        double[] y = f.computeDouble(x, y0, null, null);

        fcnt = fcnt + 2;
        if (fcnt > maxfcnt) {//% Maximum function count exceeded; singularity likely.
            double Q = h * fc;
            double warn = ERR_MAX_FCT_COUNT;
            return new double[]{Q, fcnt, warn};
        }
        double fd = y[0];
        double fe = y[1];

        //% Three point Simpson's rule.
        double Q1 = (h / 6) * (fa + 4 * fc + fb);

        //% Five point double Simpson's rule.
        double Q2 = (h / 12) * (fa + 4 * fd + 2 * fc + 4 * fe + fb);

        //% One step of Romberg extrapolation.
        double Q = Q2 + (Q2 - Q1) / 15;

        if (Double.isInfinite(Q) || Double.isNaN(Q)) { //% Infinite or Not-aNumber function value encountered.
            double warn = ERR_NAN_OR_INFINITE;
            return new double[]{Q, fcnt, warn};
        }
        //disp(sprintf('%8.0f %16.10f %18.8e %16.10f', fcnt, a, h, Q))
        //% Check accuracy of integral over this subinterval.
        if (Math.abs(h) <= hmax && Math.abs(Q2 - Q) <= tolerance) {
            double warn = 0;
            return new double[]{Q, fcnt, warn};
        } else {//% Subdivide into two subintervals.
            double[] o1 = quadstepX(f, y0, a, c, fa, fd, fc, fcnt, hmin, hmax);
            double[] o2 = quadstepX(f, y0, c, b, fc, fe, fb, fcnt, hmin, hmax);
            Q = o1[0] + o2[0];
            int warn = Math.max((int) o1[2], (int) o2[2]);
            return new double[]{Q, fcnt, warn};
        }
    }

    private double[] quadstepY(DoubleToDouble f, double x0, double a, double b, double fa, double fc, double fb, int fcnt, double hmin, double hmax) {
//QUADSTEP  Recursive core routine for function QUAD.


//% Evaluate integrand twice in interior of subinterval [a,b].
        double h = b - a;
        double c = (a + b) / 2;
        if (Math.abs(h) < hmin || c == a || c == b) {
            //% Minimum step size reached; singularity possible.
            double Q = h * fc;
            double warn = ERR_MAX_STEP_REACHED;
            return new double[]{Q, fcnt, warn};

        }
        double[] y = {(a + c) / 2, (c + b) / 2};
        double[] v = f.computeDouble(x0, y, null, null);

        fcnt = fcnt + 2;
        if (fcnt > maxfcnt) {//% Maximum function count exceeded; singularity likely.
            double Q = h * fc;
            double warn = ERR_MAX_FCT_COUNT;
            return new double[]{Q, fcnt, warn};
        }
        double fd = v[0];
        double fe = v[1];

        //% Three point Simpson's rule.
        double Q1 = (h / 6) * (fa + 4 * fc + fb);

        //% Five point double Simpson's rule.
        double Q2 = (h / 12) * (fa + 4 * fd + 2 * fc + 4 * fe + fb);

        //% One step of Romberg extrapolation.
        double Q = Q2 + (Q2 - Q1) / 15;

        if (Double.isInfinite(Q) || Double.isNaN(Q)) { //% Infinite or Not-aNumber function value encountered.
            double warn = ERR_NAN_OR_INFINITE;
            return new double[]{Q, fcnt, warn};
        }
        //disp(sprintf('%8.0f %16.10f %18.8e %16.10f', fcnt, a, h, Q))
        //% Check accuracy of integral over this subinterval.
        if (Math.abs(h) <= hmax && Math.abs(Q2 - Q) <= tolerance) {
            double warn = 0;
            return new double[]{Q, fcnt, warn};
        } else {//% Subdivide into two subintervals.
            double[] o1 = quadstepY(f, x0, a, c, fa, fd, fc, fcnt, hmin, hmax);
            double[] o2 = quadstepY(f, x0, c, b, fc, fe, fb, fcnt, hmin, hmax);
            Q = o1[0] + o2[0];
            int warn = Math.max((int) o1[2], (int) o2[2]);
            return new double[]{Q, fcnt, warn};
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DQuadIntegralXY)) return false;

        DQuadIntegralXY that = (DQuadIntegralXY) o;

        if (Double.compare(that.hmaxCoeff, hmaxCoeff) != 0) return false;
        if (Double.compare(that.hminCoeff, hminCoeff) != 0) return false;
        if (maxfcnt != that.maxfcnt) return false;
        if (Double.compare(that.tolerance, tolerance) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(tolerance);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(hminCoeff);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(hmaxCoeff);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + maxfcnt;
        return result;
    }

    @Override
    public String dump() {
        Dumper h = new Dumper(getClass().getSimpleName());
        h.add("tolerance", tolerance);
        h.add("hminCoeff", hminCoeff);
        h.add("hmaxCoeff", hmaxCoeff);
        h.add("maxfcnt", maxfcnt);
        return h.toString();
    }

    @Override
    public String toString() {
        return "DQuadIntegralXY";
    }
}



