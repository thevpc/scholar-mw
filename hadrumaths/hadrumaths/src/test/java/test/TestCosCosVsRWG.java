package test;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.GeometryFactory;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.geom.HPolygon;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.scalarproducts.ScalarProductHelper;
import net.thevpc.scholar.hadrumaths.scalarproducts.formal.CosCosVsRWGScalarProduct;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCosCosVsRWG {

    public static void main(String[] args) {
        new TestCosCosVsRWG().testAnalyticalVsQuadrature();
    }

    @Test
    public void testAnalyticalVsQuadrature() {
        // Triangle 1: (0, 0) - (2mm, 0) - (1mm, 3mm)
        HPolygon tri1 = GeometryFactory.createPolygon(
                HPoint.create(0.0, 0.0),
                HPoint.create(0.002, 0.0),
                HPoint.create(0.001, 0.003)
        );
        // Triangle 2: (0, 0) - (2mm, 0) - (1mm, -2.5mm)
        HPolygon tri2 = GeometryFactory.createPolygon(
                HPoint.create(0.001, -0.0025),
                HPoint.create(0.0, 0.0),
                HPoint.create(0.002, 0.0)
        );

        RWG rwgX = new RWG(Axis.X, 1.0, tri1, tri2);
        RWG rwgY = new RWG(Axis.Y, 1.0, tri1, tri2);

        Domain dom = Domain.ofBounds(-0.01, 0.01, -0.01, 0.01);

        // Test across a matrix of frequencies and phases
        double[] as = {0.0, 100.0, 500.0, 1500.0};
        double[] cs = {0.0, 200.0, 800.0, 2000.0};
        double[] bs = {0.0, Math.PI / 4, Math.PI / 2};
        double[] ds = {0.0, Math.PI / 3, Math.PI / 2};

        int count = 0;
        double maxDiff = 0;
        System.out.println("rwgY.tr1 = " + rwgY.tr1 + ", p1=" + rwgY.tr1.p1() + ", area=" + rwgY.tr1.area());
        System.out.println("rwgY.tr2 = " + rwgY.tr2 + ", p1=" + rwgY.tr2.p1() + ", area=" + rwgY.tr2.area());
        System.out.println("rwgY.edgeLength = " + rwgY.edgeLength);
        for (double a : as) {
            for (double c : cs) {
                for (double b : bs) {
                    for (double d : ds) {
                        CosXCosY cos = new CosXCosY(2.5, a, b, c, d, dom);

                        // Test on tr1
                        double anaTr1X = CosCosVsRWGScalarProduct.integrateTriangle(rwgX.tr1, cos, rwgX, true);
                        double quadTr1X = ScalarProductHelper.integrateOverTriangleUsingGaussianQuadrature(rwgX.tr1, cos, rwgX);
                        double diffTr1X = Math.abs(anaTr1X - quadTr1X);
                        maxDiff = Math.max(maxDiff, diffTr1X);
                        Assertions.assertEquals(quadTr1X, anaTr1X, 1e-6,
                                String.format("tr1 X mismatch for a=%.1f, c=%.1f, b=%.2f, d=%.2f", a, c, b, d));

                        // Test on tr2
                        double anaTr2Y = CosCosVsRWGScalarProduct.integrateTriangle(rwgY.tr2, cos, rwgY, false);
                        double quadTr2Y = ScalarProductHelper.integrateOverTriangleUsingGaussianQuadrature(rwgY.tr2, cos, rwgY);
                        double diffTr2Y = Math.abs(anaTr2Y - quadTr2Y);
                        maxDiff = Math.max(maxDiff, diffTr2Y);
                        Assertions.assertEquals(quadTr2Y, anaTr2Y, 1e-6,
                                String.format("tr2 Y mismatch for a=%.1f, c=%.1f, b=%.2f, d=%.2f", a, c, b, d));

                        count += 2;
                    }
                }
            }
        }

        System.out.printf("PASS: Verified %d analytical scalar products. Max difference from quadrature: %.2e%n", count, maxDiff);
    }
}
