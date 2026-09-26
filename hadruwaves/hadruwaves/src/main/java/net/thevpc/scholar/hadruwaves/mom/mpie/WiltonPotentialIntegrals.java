package net.thevpc.scholar.hadruwaves.mom.mpie;

import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;

/**
 * Analytical closed-form potential integrals over planar triangles based on:
 * D. R. Wilton, S. M. Rao, A. W. Glisson, D. H. Schaubert, O. M. Al-Bundak, and C. M. Butler,
 * "Potential integrals for uniform and linear source distributions on polygonal and polyhedral domains",
 * IEEE Transactions on Antennas and Propagation, vol. AP-32, no. 3, pp. 276-281, March 1984.
 *
 * <p>Evaluates:
 * <pre>
 *   I_scalar(r) = \iint_T (1 / |r - r'|) dS'
 *   I_linear(r) = \iint_T ((r' - v) / |r - r'|) dS'
 * </pre>
 * in exact closed form for any observation point r in the triangle plane (z=0).
 */
public final class WiltonPotentialIntegrals {

    private WiltonPotentialIntegrals() {
    }

    /**
     * Evaluates \iint_T (1 / |r - r'|) dS' in exact closed-form.
     *
     * @param rx x-coordinate of observation point r
     * @param ry y-coordinate of observation point r
     * @param t  planar triangle in z=0
     * @return potential integral value in meters
     */
    public static double integrateScalarPotential(double rx, double ry, HTriangle t) {
        HPoint[] pts = getCcwVertices(t);
        HPoint p1 = pts[0], p2 = pts[1], p3 = pts[2];

        double sum = 0.0;
        sum += edgeScalarTerm(rx, ry, p1, p2);
        sum += edgeScalarTerm(rx, ry, p2, p3);
        sum += edgeScalarTerm(rx, ry, p3, p1);
        return Math.max(0.0, sum);
    }

    /**
     * Evaluates \iint_T ((r' - v) / |r - r'|) dS' in exact closed-form,
     * returning the 2D vector [x, y].
     *
     * @param rx x-coordinate of observation point r
     * @param ry y-coordinate of observation point r
     * @param vx x-coordinate of vertex v
     * @param vy y-coordinate of vertex v
     * @param t  planar triangle in z=0
     * @return array of length 2: [result_x, result_y] in m^2
     */
    public static double[] integrateLinearPotential(double rx, double ry, double vx, double vy, HTriangle t) {
        HPoint[] pts = getCcwVertices(t);
        HPoint p1 = pts[0], p2 = pts[1], p3 = pts[2];

        double iScalar = 0.0;
        double vecSumX = 0.0;
        double vecSumY = 0.0;

        // Edge 1
        double[] e1 = edgeVectorTerm(rx, ry, p1, p2);
        iScalar += e1[0];
        vecSumX += e1[1];
        vecSumY += e1[2];

        // Edge 2
        double[] e2 = edgeVectorTerm(rx, ry, p2, p3);
        iScalar += e2[0];
        vecSumX += e2[1];
        vecSumY += e2[2];

        // Edge 3
        double[] e3 = edgeVectorTerm(rx, ry, p3, p1);
        iScalar += e3[0];
        vecSumX += e3[1];
        vecSumY += e3[2];

        // (r' - v) = (r' - r) + (r - v)
        // \iint_T ((r' - v)/R) dS' = (r - v) * I_scalar + \iint_T ((r' - r)/R) dS'
        // where \iint_T ((r' - r)/R) dS' = 0.5 * \sum u_i [ d_i^2 P_i + s_i^+ R_i^+ - s_i^- R_i^- ]
        double drx = rx - vx;
        double dry = ry - vy;

        return new double[]{
                drx * iScalar + 0.5 * vecSumX,
                dry * iScalar + 0.5 * vecSumY
        };
    }

    private static double edgeScalarTerm(double rx, double ry, HPoint a, HPoint b) {
        double lx = b.x - a.x;
        double ly = b.y - a.y;
        double L = Math.hypot(lx, ly);
        if (L < 1e-15) return 0.0;

        // Tangent unit vector
        double tx = lx / L;
        double ty = ly / L;

        // Outward unit normal: u = t x z = (ty, -tx)
        double ux = ty;
        double uy = -tx;

        // Perpendicular signed distance from r to edge line
        // d = (a - r) . u
        double d = (a.x - rx) * ux + (a.y - ry) * uy;

        // Projected coordinates along edge line from r
        double sMinus = (a.x - rx) * tx + (a.y - ry) * ty;
        double sPlus = (b.x - rx) * tx + (b.y - ry) * ty;

        double rMinus = Math.hypot(a.x - rx, a.y - ry);
        double rPlus = Math.hypot(b.x - rx, b.y - ry);

        double P = edgeLogTerm(sMinus, sPlus, rMinus, rPlus, d);
        return d * P;
    }

    private static double[] edgeVectorTerm(double rx, double ry, HPoint a, HPoint b) {
        double lx = b.x - a.x;
        double ly = b.y - a.y;
        double L = Math.hypot(lx, ly);
        if (L < 1e-15) return new double[]{0, 0, 0};

        double tx = lx / L;
        double ty = ly / L;
        double ux = ty;
        double uy = -tx;

        double d = (a.x - rx) * ux + (a.y - ry) * uy;
        double sMinus = (a.x - rx) * tx + (a.y - ry) * ty;
        double sPlus = (b.x - rx) * tx + (b.y - ry) * ty;

        double rMinus = Math.hypot(a.x - rx, a.y - ry);
        double rPlus = Math.hypot(b.x - rx, b.y - ry);

        double P = edgeLogTerm(sMinus, sPlus, rMinus, rPlus, d);
        double term = d * d * P + sPlus * rPlus - sMinus * rMinus;

        return new double[]{
                d * P,
                ux * term,
                uy * term
        };
    }

    /**
     * Numerically stable evaluation of P = \int_{s^-}^{s^+} (1 / \sqrt{s^2 + d^2}) ds
     * = \ln( (R^+ + s^+) / (R^- + s^-) ).
     */
    public static double edgeLogTerm(double sMinus, double sPlus, double rMinus, double rPlus, double d) {
        // If sMinus and sPlus have the same sign and |d| is small,
        // use standard algebraic identity to avoid catastrophic cancellation:
        // (R - s)(R + s) = d^2
        if (sPlus >= 0 && sMinus >= 0) {
            double denom = rMinus + sMinus;
            double numer = rPlus + sPlus;
            if (denom > 1e-15 && numer > 1e-15) {
                return Math.log(numer / denom);
            }
        } else if (sPlus <= 0 && sMinus <= 0) {
            // Both negative: (R + s) can cancel, use (R - s) in denominator
            double denom = rPlus - sPlus;
            double numer = rMinus - sMinus;
            if (denom > 1e-15 && numer > 1e-15) {
                return Math.log(numer / denom);
            }
        } else {
            // Straddling 0 (sMinus < 0 < sPlus)
            double num = rPlus + sPlus;
            double den = rMinus + sMinus;
            if (num > 1e-15 && den > 1e-15) {
                return Math.log(num / den);
            }
        }
        // Fallback for near-zero d
        double d2 = d * d;
        if (d2 > 1e-30) {
            double num = (rPlus + sPlus) * (rMinus - sMinus);
            return Math.log(Math.max(1e-30, num / d2));
        }
        return 0.0;
    }

    /**
     * Ensures vertices are in Counter-Clockwise (CCW) order.
     */
    public static HPoint[] getCcwVertices(HTriangle t) {
        HPoint p1 = t.p1();
        HPoint p2 = t.p2();
        HPoint p3 = t.p3();
        double signedArea = (p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y);
        if (signedArea < 0) {
            // Clockwise -> swap p2 and p3 to make CCW
            return new HPoint[]{p1, p3, p2};
        }
        return new HPoint[]{p1, p2, p3};
    }
}
