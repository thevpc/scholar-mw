package net.thevpc.scholar.hadruwaves.mom.mpie;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;

/**
 * Evaluates scalar and vector potential interactions between two planar triangles
 * in the Spatial-Domain Mixed Potential Integral Equation (MPIE).
 *
 * <p>Uses closed-form Wilton singularity extraction for self and near triangle pairs,
 * and high-order Gaussian quadrature for the smooth remainder and far pairs.</p>
 */
public final class MpiePotentialHelper {

    // 7-point symmetric Gauss-Legendre quadrature on simplex (Cowper 1973)
    private static final double SQ15 = Math.sqrt(15.0);
    private static final double A1 = (9.0 - 2.0 * SQ15) / 21.0;
    private static final double B1 = (6.0 + SQ15) / 21.0;
    private static final double A2 = (9.0 + 2.0 * SQ15) / 21.0;
    private static final double B2 = (6.0 - SQ15) / 21.0;

    private static final double[][] GAUSS_7_POINTS = {
            {1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0},
            {A1, B1, B1},
            {B1, A1, B1},
            {B1, B1, A1},
            {A2, B2, B2},
            {B2, A2, B2},
            {B2, B2, A2}
    };

    private static final double[] GAUSS_7_WEIGHTS = {
            0.225,
            (155.0 - SQ15) / 1200.0,
            (155.0 - SQ15) / 1200.0,
            (155.0 - SQ15) / 1200.0,
            (155.0 + SQ15) / 1200.0,
            (155.0 + SQ15) / 1200.0,
            (155.0 + SQ15) / 1200.0
    };

    // 4-point Gauss quadrature on simplex (degree 3)
    private static final double[][] GAUSS_4_POINTS = {
            {1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0},
            {3.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0},
            {1.0 / 5.0, 3.0 / 5.0, 1.0 / 5.0},
            {1.0 / 5.0, 1.0 / 5.0, 3.0 / 5.0}
    };

    private static final double[] GAUSS_4_WEIGHTS = {
            -9.0 / 16.0,
            25.0 / 48.0,
            25.0 / 48.0,
            25.0 / 48.0
    };

    public static class InteractionResult {
        public final Complex scalarIntegral;
        public final Complex vectorIntegral;

        public InteractionResult(Complex scalarIntegral, Complex vectorIntegral) {
            this.scalarIntegral = scalarIntegral;
            this.vectorIntegral = vectorIntegral;
        }
    }

    private MpiePotentialHelper() {
    }

    /**
     * Computes the scalar and vector potential interaction between triangle Ta
     * (with free vertex va) and triangle Tb (with free vertex vb).
     *
     * @param ta observation triangle
     * @param va free vertex associated with ta
     * @param tb source triangle
     * @param vb free vertex associated with tb
     * @param k  wavenumber in dielectric/effective medium
     * @param h  substrate thickness (image theory at 2h; if &lt;= 0, free space)
     * @return interaction result containing scalar and vector integrals
     */
    public static InteractionResult computeTriangleInteraction(
            HTriangle ta, HPoint va,
            HTriangle tb, HPoint vb,
            double k, double h) {
        return computeTriangleInteraction(ta, va, tb, vb, k, k, h);
    }

    /**
     * Computes the scalar and vector potential interaction between triangle Ta
     * (with free vertex va) and triangle Tb (with free vertex vb).
     *
     * @param ta observation triangle
     * @param va free vertex associated with ta
     * @param tb source triangle
     * @param vb free vertex associated with tb
     * @param k  wavenumber in dielectric/effective medium (reactive near fields)
     * @param k0 free-space wavenumber in air (far-field radiation damping)
     * @param h  substrate thickness (image theory at 2h; if &lt;= 0, free space)
     * @return interaction result containing scalar and vector integrals
     */
    public static InteractionResult computeTriangleInteraction(
            HTriangle ta, HPoint va,
            HTriangle tb, HPoint vb,
            double k, double k0, double h) {

        double areaA = ta.area();
        double areaB = tb.area();
        if (areaA <= 1e-15 || areaB <= 1e-15) {
            return new InteractionResult(Complex.ZERO, Complex.ZERO);
        }

        HPoint ca = ta.getBarycenter();
        HPoint cb = tb.getBarycenter();
        double distCent = ca.distance(cb);
        double sizeA = ta.longestEdge();
        double sizeB = tb.longestEdge();

        // Check if near or far
        boolean isNear = distCent < 1.5 * (sizeA + sizeB);

        if (isNear) {
            return computeNearInteraction(ta, va, tb, vb, k, k0, h, areaA, areaB);
        } else {
            return computeFarInteraction(ta, va, tb, vb, k, k0, h, areaA, areaB);
        }
    }

    private static InteractionResult computeNearInteraction(
            HTriangle ta, HPoint va,
            HTriangle tb, HPoint vb,
            double k, double k0, double h,
            double areaA, double areaB) {

        Complex totalPhi = Complex.ZERO;
        Complex totalA = Complex.ZERO;

        // Outer integral over Ta using 7-point Gauss quadrature
        for (int i = 0; i < GAUSS_7_POINTS.length; i++) {
            double wa = GAUSS_7_WEIGHTS[i];
            double rx = GAUSS_7_POINTS[i][0] * ta.p1().x + GAUSS_7_POINTS[i][1] * ta.p2().x + GAUSS_7_POINTS[i][2] * ta.p3().x;
            double ry = GAUSS_7_POINTS[i][0] * ta.p1().y + GAUSS_7_POINTS[i][1] * ta.p2().y + GAUSS_7_POINTS[i][2] * ta.p3().y;

            // 1. Singular 1/R part via Wilton closed-form integrals
            double singPhi = WiltonPotentialIntegrals.integrateScalarPotential(rx, ry, tb);
            double[] singA = WiltonPotentialIntegrals.integrateLinearPotential(rx, ry, vb.x, vb.y, tb);

            // 2. Smooth remainder: (cos(kR) - 1)/R - cos(k R_img)/R_img - j sin(k0 R)/R
            // Integrated over Tb using 7-point Gauss quadrature
            Complex smoothPhi = Complex.ZERO;
            Complex smoothAx = Complex.ZERO;
            Complex smoothAy = Complex.ZERO;

            for (int j = 0; j < GAUSS_7_POINTS.length; j++) {
                double wb = GAUSS_7_WEIGHTS[j];
                double px = GAUSS_7_POINTS[j][0] * tb.p1().x + GAUSS_7_POINTS[j][1] * tb.p2().x + GAUSS_7_POINTS[j][2] * tb.p3().x;
                double py = GAUSS_7_POINTS[j][0] * tb.p1().y + GAUSS_7_POINTS[j][1] * tb.p2().y + GAUSS_7_POINTS[j][2] * tb.p3().y;

                double dx = rx - px;
                double dy = ry - py;
                double R = Math.hypot(dx, dy);

                Complex gSmooth = evalSmoothGreen(R, k, k0, h);

                smoothPhi = smoothPhi.plus(gSmooth.mul(wb));

                double vbx = px - vb.x;
                double vby = py - vb.y;
                smoothAx = smoothAx.plus(gSmooth.mul(wb * vbx));
                smoothAy = smoothAy.plus(gSmooth.mul(wb * vby));
            }
            smoothPhi = smoothPhi.mul(areaB);
            smoothAx = smoothAx.mul(areaB);
            smoothAy = smoothAy.mul(areaB);

            // Full inner potential at (rx, ry) = (sing + smooth) / (4 * PI)
            double inv4Pi = 1.0 / (4.0 * Math.PI);
            Complex innerPhi = Complex.of(singPhi, 0).plus(smoothPhi).mul(inv4Pi);
            Complex innerAx = Complex.of(singA[0], 0).plus(smoothAx).mul(inv4Pi);
            Complex innerAy = Complex.of(singA[1], 0).plus(smoothAy).mul(inv4Pi);

            // Dot with (r - va) for vector potential
            double drx = rx - va.x;
            double dry = ry - va.y;
            Complex dotA = innerAx.mul(drx).plus(innerAy.mul(dry));

            totalPhi = totalPhi.plus(innerPhi.mul(wa));
            totalA = totalA.plus(dotA.mul(wa));
        }

        totalPhi = totalPhi.mul(areaA);
        totalA = totalA.mul(areaA);

        return new InteractionResult(totalPhi, totalA);
    }

    private static InteractionResult computeFarInteraction(
            HTriangle ta, HPoint va,
            HTriangle tb, HPoint vb,
            double k, double k0, double h,
            double areaA, double areaB) {

        Complex totalPhi = Complex.ZERO;
        Complex totalA = Complex.ZERO;

        for (int i = 0; i < GAUSS_4_POINTS.length; i++) {
            double wa = GAUSS_4_WEIGHTS[i];
            double rx = GAUSS_4_POINTS[i][0] * ta.p1().x + GAUSS_4_POINTS[i][1] * ta.p2().x + GAUSS_4_POINTS[i][2] * ta.p3().x;
            double ry = GAUSS_4_POINTS[i][0] * ta.p1().y + GAUSS_4_POINTS[i][1] * ta.p2().y + GAUSS_4_POINTS[i][2] * ta.p3().y;
            double dra_x = rx - va.x;
            double dra_y = ry - va.y;

            for (int j = 0; j < GAUSS_4_POINTS.length; j++) {
                double wb = GAUSS_4_WEIGHTS[j];
                double px = GAUSS_4_POINTS[j][0] * tb.p1().x + GAUSS_4_POINTS[j][1] * tb.p2().x + GAUSS_4_POINTS[j][2] * tb.p3().x;
                double py = GAUSS_4_POINTS[j][0] * tb.p1().y + GAUSS_4_POINTS[j][1] * tb.p2().y + GAUSS_4_POINTS[j][2] * tb.p3().y;
                double drb_x = px - vb.x;
                double drb_y = py - vb.y;

                double R = Math.hypot(rx - px, ry - py);
                Complex G = evalFullGreen(R, k, k0, h);
                double w = wa * wb;

                totalPhi = totalPhi.plus(G.mul(w));

                double dot = dra_x * drb_x + dra_y * drb_y;
                totalA = totalA.plus(G.mul(w * dot));
            }
        }

        double areaFactor = areaA * areaB;
        totalPhi = totalPhi.mul(areaFactor);
        totalA = totalA.mul(areaFactor);

        return new InteractionResult(totalPhi, totalA);
    }

    private static Complex evalSmoothGreen(double R, double k, double k0, double h) {
        // (cos(kR) - 1) / R - cos(k R_img) / R_img for real part
        // -sin(k0 R) / R for imaginary part (radiation damping into upper air half-space)
        double realTerm;
        if (R < 1e-10) {
            realTerm = -0.5 * k * k * R;
        } else {
            double kr = k * R;
            realTerm = (Math.cos(kr) - 1.0) / R;
        }

        if (h > 0) {
            double rImg = Math.sqrt(R * R + 4.0 * h * h);
            double krImg = k * rImg;
            realTerm -= Math.cos(krImg) / rImg;
        }

        double imagTerm;
        if (R < 1e-10) {
            imagTerm = -k0;
        } else {
            imagTerm = -Math.sin(k0 * R) / R;
        }

        return Complex.of(realTerm, imagTerm);
    }

    private static Complex evalFullGreen(double R, double k, double k0, double h) {
        double inv4Pi = 1.0 / (4.0 * Math.PI);
        if (R < 1e-15) {
            R = 1e-15;
        }
        double kr = k * R;
        double realTerm = Math.cos(kr) / R;

        if (h > 0) {
            double rImg = Math.sqrt(R * R + 4.0 * h * h);
            double krImg = k * rImg;
            realTerm -= Math.cos(krImg) / rImg;
        }

        double imagTerm = -Math.sin(k0 * R) / R;

        return Complex.of(realTerm, imagTerm).mul(inv4Pi);
    }
}

