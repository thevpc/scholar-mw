package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.scalarproducts.ScalarProductHelper;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;

import java.util.List;

/**
 * Exact closed-form analytical scalar product between a cavity box mode
 * ({@link CosXCosY}) and an {@link RWG} linear triangular basis function.
 *
 * <p>Uses Green's theorem in the plane to reduce the 2D surface integral
 * over each triangle simplex to three 1D line integrals along the boundary edges,
 * which evaluate in closed-form via elementary trigonometric and algebraic expressions.
 *
 * <p>Eliminates the expensive 7-point Gaussian numerical quadrature, achieving
 * orders-of-magnitude matrix assembly acceleration.
 *
 * @author thevpc
 */
public final class CosCosVsRWGScalarProduct implements FormalScalarProductHelper {

    public static final CosCosVsRWGScalarProduct INSTANCE = new CosCosVsRWGScalarProduct();

    @Override
    public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        CosXCosY cos = (CosXCosY) f1;
        RWG rwg = (RWG) f2;

        if (cos.isZero() || rwg.isZero()) return 0;

        double sum = 0;
        if (rwg.tr1 != null && rwg.tr1.area() > 1e-15) {
            sum += integrateTriangle(rwg.tr1, cos, rwg, true);
        }
        if (rwg.tr2 != null && rwg.tr2.area() > 1e-15) {
            sum += integrateTriangle(rwg.tr2, cos, rwg, false);
        }
        return sum;
    }

    /**
     * Integrates cos(x, y) * rwg(x, y) over triangle t in closed-form.
     */
    public static double integrateTriangle(HTriangle t, CosXCosY cos, RWG rwg, boolean isTr1) {
        Domain intersection = t.getDomain().intersect(cos.getDomain());
        if (intersection.isEmpty()) return 0;

        double aT = t.area();
        if (!(aT > 1e-15)) return 0;

        // Linear affine coefficients for RWG on triangle t: g(x, y) = c0 + cx*x + cy*y
        double factor;
        double cx = 0, cy = 0, c0 = 0;
        if (isTr1) {
            factor = rwg.max * (rwg.edgeLength / (2.0 * aT));
            HPoint p1 = t.p1();
            if (rwg.axis == Axis.X) {
                cx = factor;
                c0 = -factor * p1.x;
            } else {
                cy = factor;
                c0 = -factor * p1.y;
            }
        } else {
            factor = rwg.max * (rwg.edgeLength / (2.0 * aT));
            HPoint p1 = t.p1();
            if (rwg.axis == Axis.X) {
                cx = -factor;
                c0 = factor * p1.x;
            } else {
                cy = -factor;
                c0 = factor * p1.y;
            }
        }

        return integrateTriangleAffine(t, cos.getAmp(), cos.getA(), cos.getB(), cos.getC(), cos.getD(), c0, cx, cy);
    }

    /**
     * Exact analytical evaluation of:
     * \iint_T [amp * cos(a*x + b) * cos(c*y + d)] * (c0 + cx*x + cy*y) dx dy
     */
    public static double integrateTriangleAffine(HTriangle t, double amp,
                                                 double a, double b, double c, double d,
                                                 double c0, double cx, double cy) {
        if (amp == 0) return 0;

        List<HPoint> pts = t.getPoints();
        HPoint p1 = pts.get(0), p2 = pts.get(1), p3 = pts.get(2);

        // Signed area to determine boundary orientation (counter-clockwise vs clockwise)
        double signedArea2 = (p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y);
        if (Math.abs(signedArea2) < 1e-28) return 0;
        double orientationSign = Math.signum(signedArea2);

        // Case 1: both a == 0 and c == 0 (constant field)
        if (Math.abs(a) < 1e-12 && Math.abs(c) < 1e-12) {
            double cosConst = amp * Math.cos(b) * Math.cos(d);
            double centroidX = (p1.x + p2.x + p3.x) / 3.0;
            double centroidY = (p1.y + p2.y + p3.y) / 3.0;
            double gCentroid = c0 + cx * centroidX + cy * centroidY;
            return Math.abs(signedArea2) * 0.5 * cosConst * gCentroid;
        }

        // Case 2: a != 0 -> use Q(x, y) dy where Q = \int integrand dx
        if (Math.abs(a) >= 1e-12) {
            double sum = lineIntegralQ(p1, p2, amp, a, b, c, d, c0, cx, cy)
                       + lineIntegralQ(p2, p3, amp, a, b, c, d, c0, cx, cy)
                       + lineIntegralQ(p3, p1, amp, a, b, c, d, c0, cx, cy);
            return orientationSign * sum;
        }

        // Case 3: a == 0 and c != 0 -> use P(x, y) dx where P = -\int integrand dy
        double sum = lineIntegralP(p1, p2, amp, a, b, c, d, c0, cx, cy)
                   + lineIntegralP(p2, p3, amp, a, b, c, d, c0, cx, cy)
                   + lineIntegralP(p3, p1, amp, a, b, c, d, c0, cx, cy);
        return orientationSign * sum;
    }

    /**
     * Line integral \int_{pA}^{pB} Q(x, y) dy
     * where Q(x, y) = amp * cos(c*y + d) * [ (c0 + cx*x + cy*y)/a * sin(a*x + b) + cx/a^2 * cos(a*x + b) ]
     */
    private static double lineIntegralQ(HPoint pA, HPoint pB, double amp,
                                        double a, double b, double c, double d,
                                        double c0, double cx, double cy) {
        double dy = pB.y - pA.y;
        if (Math.abs(dy) < 1e-15) return 0;
        double dx = pB.x - pA.x;

        double omegaX = a * dx;
        double phiX = a * pA.x + b;
        double omegaY = c * dy;
        double phiY = c * pA.y + d;

        double gA = c0 + cx * pA.x + cy * pA.y;
        double deltaG = cx * dx + cy * dy;

        double omegaPlus = omegaX + omegaY;
        double phiPlus = phiX + phiY;
        double omegaMinus = omegaX - omegaY;
        double phiMinus = phiX - phiY;

        double sin0Plus = intSin0(omegaPlus, phiPlus);
        double sin0Minus = intSin0(omegaMinus, phiMinus);
        double sin1Plus = intSin1(omegaPlus, phiPlus);
        double sin1Minus = intSin1(omegaMinus, phiMinus);

        double cos0Plus = intCos0(omegaPlus, phiPlus);
        double cos0Minus = intCos0(omegaMinus, phiMinus);

        // Term 1: (gA + t*deltaG) / a * [sin(theta+) + sin(theta-)] * 0.5
        double term1 = (gA / a) * (sin0Plus + sin0Minus) + (deltaG / a) * (sin1Plus + sin1Minus);

        // Term 2: cx / a^2 * [cos(theta+) + cos(theta-)] * 0.5
        double term2 = (cx / (a * a)) * (cos0Plus + cos0Minus);

        return dy * 0.5 * amp * (term1 + term2);
    }

    /**
     * Line integral -\int_{pA}^{pB} P(x, y) dx (used when a == 0 and c != 0)
     * where P(x, y) = amp * cos(b) * [ (c0 + cx*x + cy*y)/c * sin(c*y + d) + cy/c^2 * cos(c*y + d) ]
     */
    private static double lineIntegralP(HPoint pA, HPoint pB, double amp,
                                        double a, double b, double c, double d,
                                        double c0, double cx, double cy) {
        double dx = pB.x - pA.x;
        if (Math.abs(dx) < 1e-15) return 0;
        double dy = pB.y - pA.y;

        double cosB = Math.cos(b);
        double omegaY = c * dy;
        double phiY = c * pA.y + d;

        double gA = c0 + cx * pA.x + cy * pA.y;
        double deltaG = cx * dx + cy * dy;

        double sin0 = intSin0(omegaY, phiY);
        double sin1 = intSin1(omegaY, phiY);
        double cos0 = intCos0(omegaY, phiY);

        double term = (gA / c) * sin0 + (deltaG / c) * sin1 + (cy / (c * c)) * cos0;
        return -dx * amp * cosB * term;
    }

    // --- Elementary 1D Integrals on t \in [0, 1] ---

    /** \int_0^1 sin(omega*t + phi) dt */
    private static double intSin0(double omega, double phi) {
        if (Math.abs(omega) < 1e-7) {
            return Math.sin(phi) + 0.5 * omega * Math.cos(phi) - (omega * omega / 6.0) * Math.sin(phi);
        }
        return (Math.cos(phi) - Math.cos(omega + phi)) / omega;
    }

    /** \int_0^1 cos(omega*t + phi) dt */
    private static double intCos0(double omega, double phi) {
        if (Math.abs(omega) < 1e-7) {
            return Math.cos(phi) - 0.5 * omega * Math.sin(phi) - (omega * omega / 6.0) * Math.cos(phi);
        }
        return (Math.sin(omega + phi) - Math.sin(phi)) / omega;
    }

    /** \int_0^1 t * sin(omega*t + phi) dt */
    private static double intSin1(double omega, double phi) {
        if (Math.abs(omega) < 1e-7) {
            return 0.5 * Math.sin(phi) + (omega / 3.0) * Math.cos(phi) - (omega * omega / 8.0) * Math.sin(phi);
        }
        return -Math.cos(omega + phi) / omega + intCos0(omega, phi) / omega;
    }

    /** \int_0^1 t * cos(omega*t + phi) dt */
    private static double intCos1(double omega, double phi) {
        if (Math.abs(omega) < 1e-7) {
            return 0.5 * Math.cos(phi) - (omega / 3.0) * Math.sin(phi) - (omega * omega / 8.0) * Math.cos(phi);
        }
        return Math.sin(omega + phi) / omega - intSin0(omega, phi) / omega;
    }
}
