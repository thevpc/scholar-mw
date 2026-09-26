package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.GeometryFactory;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadruwaves.mom.mpie.WiltonPotentialIntegrals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestMpieWiltonSingularity {

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    @Test
    public void testScalarPotentialAgainstQuadrature() {
        // Triangle in mm: (0, 0) - (2mm, 0) - (1mm, 2mm)
        HTriangle t = new net.thevpc.scholar.hadrumaths.geom.DefaultHTriangle(
                HPoint.create(0.0, 0.0),
                HPoint.create(0.002, 0.0),
                HPoint.create(0.001, 0.002)
        );

        // Observation point at (5mm, 5mm) - sufficiently far that 7-point Gauss quadrature is accurate
        double rx = 0.005;
        double ry = 0.005;

        double wiltonVal = WiltonPotentialIntegrals.integrateScalarPotential(rx, ry, t);

        // 7-point symmetric Gauss-Legendre quadrature weights and barycentric coordinates on simplex
        double[][] points = {
                {1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0},
                {(6.0 + Math.sqrt(15.0)) / 21.0, (6.0 + Math.sqrt(15.0)) / 21.0, (9.0 - 2.0 * Math.sqrt(15.0)) / 21.0},
                {(6.0 + Math.sqrt(15.0)) / 21.0, (9.0 - 2.0 * Math.sqrt(15.0)) / 21.0, (6.0 + Math.sqrt(15.0)) / 21.0},
                {(9.0 - 2.0 * Math.sqrt(15.0)) / 21.0, (6.0 + Math.sqrt(15.0)) / 21.0, (6.0 + Math.sqrt(15.0)) / 21.0},
                {(6.0 - Math.sqrt(15.0)) / 21.0, (6.0 - Math.sqrt(15.0)) / 21.0, (9.0 + 2.0 * Math.sqrt(15.0)) / 21.0},
                {(6.0 - Math.sqrt(15.0)) / 21.0, (9.0 + 2.0 * Math.sqrt(15.0)) / 21.0, (6.0 - Math.sqrt(15.0)) / 21.0},
                {(9.0 + 2.0 * Math.sqrt(15.0)) / 21.0, (6.0 - Math.sqrt(15.0)) / 21.0, (6.0 - Math.sqrt(15.0)) / 21.0}
        };
        double[] weights = {
                0.225,
                (155.0 - Math.sqrt(15.0)) / 1200.0,
                (155.0 - Math.sqrt(15.0)) / 1200.0,
                (155.0 - Math.sqrt(15.0)) / 1200.0,
                (155.0 + Math.sqrt(15.0)) / 1200.0,
                (155.0 + Math.sqrt(15.0)) / 1200.0,
                (155.0 + Math.sqrt(15.0)) / 1200.0
        };

        double area = t.area();
        double quadVal = 0.0;
        for (int i = 0; i < points.length; i++) {
            double qx = points[i][0] * t.p1().x + points[i][1] * t.p2().x + points[i][2] * t.p3().x;
            double qy = points[i][0] * t.p1().y + points[i][1] * t.p2().y + points[i][2] * t.p3().y;
            double dist = Math.hypot(rx - qx, ry - qy);
            quadVal += weights[i] * (1.0 / dist);
        }
        quadVal *= area;

        System.out.printf("Wilton: %.8e, Quad: %.8e, diff: %.4e%n", wiltonVal, quadVal, Math.abs(wiltonVal - quadVal));
        Assertions.assertEquals(quadVal, wiltonVal, 1e-6, "Wilton integral must match 7-point quadrature in far field");

        // Self-point: observation at centroid of triangle
        double cx = (t.p1().x + t.p2().x + t.p3().x) / 3.0;
        double cy = (t.p1().y + t.p2().y + t.p3().y) / 3.0;
        double selfVal = WiltonPotentialIntegrals.integrateScalarPotential(cx, cy, t);
        System.out.printf("Wilton self-potential at centroid: %.8e m%n", selfVal);
        Assertions.assertTrue(selfVal > 0, "Self-potential must be strictly positive");
        Assertions.assertTrue(!Double.isNaN(selfVal) && !Double.isInfinite(selfVal), "Self-potential must be finite");
    }
}
