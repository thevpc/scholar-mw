package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.GeometryFactory;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.geom.HPolygon;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.BooleanMarker;
import net.thevpc.scholar.hadrumaths.BooleanRef;

/**
 * Minimal test to diagnose NaN in evalX/evalY for a specific degenerate RWG.
 * tf[263]: rwg(T1=(-0.00375,0.00127)(-0.00315,8.5e-4)(-0.00435,0.0017),
 *              T2=(-0.00315,0.0017)(-0.00315,8.5e-4)(-0.00435,0.0017))
 */
public class TestRWGArea {
    public static void main(String[] args) {
        // T1: area ~ 0 via Heron's formula
        HPolygon tri1 = GeometryFactory.createPolygon(
                HPoint.create(-0.00375, 0.0012749999999999999),
                HPoint.create(-0.00315, 8.5e-4),
                HPoint.create(-0.00435, 0.0017));
        // T2: normal area
        HPolygon tri2 = GeometryFactory.createPolygon(
                HPoint.create(-0.00315, 0.0017),
                HPoint.create(-0.00315, 8.5e-4),
                HPoint.create(-0.00435, 0.0017));

        HTriangle t1 = tri1.toTriangle();
        HTriangle t2 = tri2.toTriangle();
        System.out.printf("T1 area = %.6e%n", t1.area());
        System.out.printf("T2 area = %.6e%n", t2.area());

        RWG rwgX = new RWG(Axis.X, 1.0, tri1, tri2);
        System.out.println("RWG created OK, max=" + rwgX.max);

        // Evaluate at centroid of T1
        double cx = (-0.00375 - 0.00315 - 0.00435) / 3;
        double cy = (0.0012749999999999999 + 8.5e-4 + 0.0017) / 3;
        BooleanRef defined = BooleanMarker.ref();
        double vx = rwgX.evalX(cx, cy, defined);
        System.out.println("evalX at T1 centroid: " + vx + " (defined=" + defined.get() + ")");
        System.out.println("evalX isNaN: " + Double.isNaN(vx));

        double vy = rwgX.evalY(cx, cy, defined);
        System.out.println("evalY at T1 centroid: " + vy + " (defined=" + defined.get() + ")");
        System.out.println("evalY isNaN: " + Double.isNaN(vy));

        // Test scalar product path via RWGVsAnyScalarProduct — use integrateOverTriangle directly
        System.out.println("RWG tr1 area = " + rwgX.tr1.area());
        System.out.println("RWG tr2 area = " + rwgX.tr2.area());
    }
}
