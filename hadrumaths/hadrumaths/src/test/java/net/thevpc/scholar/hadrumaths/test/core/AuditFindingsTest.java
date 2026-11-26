package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.nuts.Nuts;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.thevpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.thevpc.scholar.hadrumaths.transform.ExpressionTransformer;
import net.thevpc.scholar.hadrumaths.transform.DoubleMultiplierTransform;
import net.thevpc.scholar.hadrumaths.util.ExprElementParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Regression tests for the verified findings of CORRECTNESS-FIXES.md.
 * Each test names the finding it pins.
 */
public class AuditFindingsTest {

    @BeforeAll
    static void init() {
        Nuts.require();
    }

    // ------------------------------------------------------------------ C1
    @Test
    public void c1_domainXYZIntersectUsesZNotY() {
        Domain a = Domain.ofBounds(0, 1, 0, 1, 2, 3);
        Domain b = Domain.ofBounds(0, 1, 0, 1, 0, 5);
        Domain i = a.intersect(b);
        Assertions.assertEquals(2.0, i.zmin(), 1e-12, "zmin must come from z, not y");
        Assertions.assertEquals(3.0, i.zmax(), 1e-12, "zmax must come from z, not y");
        Assertions.assertEquals(0.0, i.xmin(), 1e-12);
        Assertions.assertEquals(1.0, i.xmax(), 1e-12);

        // overlapping z -> real z interval, taken from z and not from y
        Domain c = Domain.ofBounds(0, 1, 0, 1, 1.5, 2.5);
        Assertions.assertEquals(2.0, a.intersect(c).zmin(), 1e-12);
        Assertions.assertEquals(2.5, a.intersect(c).zmax(), 1e-12);

        // z disjoint -> empty
        Domain d = Domain.ofBounds(0, 1, 0, 1, 10, 20);
        Assertions.assertTrue(a.intersect(d).isEmpty(), "disjoint z must intersect to empty");
    }

    // ------------------------------------------------------------------ C2
    @Test
    public void c2_reminderIsUndefinedOutsideDivisorSupport() {
        Domain dA = Domain.ofBounds(-1, 1, 0, 1);
        Domain dB = Domain.ofBounds(0, 1, 0, 1);
        Expr r = Maths.X.mul(dA).rem(Maths.expr(2.0, dB));

        // support is the intersection of both operands
        Assertions.assertEquals(0.0, r.getDomain().xmin(), 1e-12, "domain = intersect");
        Assertions.assertEquals(1.0, r.getDomain().xmax(), 1e-12, "domain = intersect");

        BooleanRef inside = BooleanMarker.ref();
        double v = r.toDD().evalDouble(0.5, 0.5, inside);
        Assertions.assertTrue(inside.get(), "both operands defined");
        Assertions.assertEquals(0.5, v, 1e-12);

        // x < 0: dividend defined, divisor not -> undefined, no NaN leak
        BooleanRef outside = BooleanMarker.ref();
        double v2 = r.toDD().evalDouble(-0.5, 0.5, outside);
        Assertions.assertFalse(outside.get(), "divisor undefined -> whole reminder undefined");
        Assertions.assertFalse(Double.isNaN(v2), "must not report NaN as a defined sample");
    }

    @Test
    public void c2_binaryTemplateUsesIndependentMarkers() {
        // Div has the same template; the divisor must not inherit the dividend's definedness.
        Domain dA = Domain.ofBounds(0, 2, 0, 1);
        Domain dB = Domain.ofBounds(1, 2, 0, 1);
        Expr q = Maths.expr(4.0, dA).div(Maths.expr(2.0, dB));

        BooleanRef outside = BooleanMarker.ref();
        double v = q.toDD().evalDouble(0.5, 0.5, outside);
        Assertions.assertFalse(outside.get(), "divisor undefined outside [1,2)");
        Assertions.assertFalse(Double.isInfinite(v), "must not report a division by a phantom zero");
    }

    // ------------------------------------------------------------------ C3
    @Test
    public void c3_parserMapsYAndZToTheirAxes() {
        NOptional<Expr> ey = ExprElementParser.parse(NElement.ofName("y"));
        NOptional<Expr> ez = ExprElementParser.parse(NElement.ofName("z"));
        Assertions.assertTrue(ey.isPresent(), "y parses");
        Assertions.assertTrue(ez.isPresent(), "z parses");
        Assertions.assertEquals(Maths.Y, ey.get(), "y must not become x");
        Assertions.assertEquals(Maths.Z, ez.get(), "z must not become x");
        Assertions.assertEquals(3.0, ey.get().toDD().evalDouble(0.0, 3.0), 1e-12, "y(3)=3");
    }

    // C5 (Mul's transform returned a sum) is fixed in Mul.java but cannot be exercised until
    // C4 is resolved: the handler recurses through ExpressionTransformFactory.transform, and no
    // transformer is registered for leaf nodes, so any Mul with scalar children throws first.


    // ------------------------------------------------------------------ C6 + C7
    @Test
    public void c6_degenerate3DBoxIsCanonicalEmpty() {
        Assertions.assertSame(Domain.EMPTYXYZ, Domain.ofBounds(0, 0, 0, 0, 0, 0),
                "all-degenerate 3-D box must be EMPTYXYZ");
        Assertions.assertSame(Domain.EMPTYXY, Domain.ofBounds(0, 0, 0, 0),
                "all-degenerate 2-D box must be EMPTYXY");
    }

    @Test
    public void c7_disjointIntersectionIsCanonicalEmpty() {
        Domain a = Domain.ofBounds(0, 1, 0, 1);
        Domain b = Domain.ofBounds(2, 3, 0, 1);
        Domain i = a.intersect(b);
        Assertions.assertSame(Domain.EMPTYXY, i, "disjoint XY intersection must be EMPTYXY");
        Assertions.assertTrue(i.isEmpty());

        Domain a3 = Domain.ofBounds(0, 1, 0, 1, 0, 1);
        Domain b3 = Domain.ofBounds(0, 1, 5, 6, 0, 1);
        Assertions.assertSame(Domain.EMPTYXYZ, a3.intersect(b3), "disjoint 3-D intersection");
    }

    // ------------------------------------------------------------------ C8
    @Test
    public void c8_expandUsesTheYMargin() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Domain e = d.expand(0.1, 0.2, 0.3, 0.4);
        Assertions.assertEquals(-0.1, e.xmin(), 1e-12);
        Assertions.assertEquals(1.2, e.xmax(), 1e-12);
        Assertions.assertEquals(-0.3, e.ymin(), 1e-12);
        Assertions.assertEquals(1.4, e.ymax(), 1e-12, "upper y margin must be ymax (0.4), not xmax (0.2)");
    }

    // ------------------------------------------------------------------ C10
    @Test
    public void c10_expandPercentOnHalfUnboundedUsesTheFiniteEnd() {
        Domain d = Domain.ofBounds(1, Double.POSITIVE_INFINITY);
        Domain e = d.expandPercent(0.5f);
        Assertions.assertEquals(0.5, e.xmin(), 1e-12, "1 - |1|*0.5");
        Assertions.assertEquals(Double.POSITIVE_INFINITY, e.xmax(), 0.0);
    }

    // ------------------------------------------------------------------ C11
    @Test
    public void c11_emptyDomainIsZeroAndConvertsToZero() {
        Assertions.assertTrue(Domain.EMPTYX.isZero(), "empty domain is the zero indicator");
        Assertions.assertEquals(0.0, Domain.EMPTYX.toDouble(), 0.0);
        Assertions.assertFalse(Domain.FULLX.isZero(), "full domain is not zero");
        Assertions.assertEquals(1.0, Domain.FULLX.toDouble(), 0.0);
        Assertions.assertFalse(Domain.ofBounds(0, 1, 0, 1).isZero(), "non-empty is not zero");
        Assertions.assertEquals(1.0, Domain.ofBounds(0, 1, 0, 1).toDouble(), 0.0);
    }

    // ------------------------------------------------------------------ extra (found while checking C1-C11)
    @Test
    public void extra_replaceYminKeepsXmax() {
        Domain d = Domain.ofBounds(2, 5, 0, 1);
        Domain r = d.replaceYmin(0.5);
        Assertions.assertEquals(2.0, r.xmin(), 1e-12);
        Assertions.assertEquals(5.0, r.xmax(), 1e-12, "xmax must not become xwidth");
        Assertions.assertEquals(0.5, r.ymin(), 1e-12);
        Assertions.assertEquals(1.0, r.ymax(), 1e-12);
        // siblings
        Assertions.assertEquals(1.0, d.replaceXmin(1).xmin(), 1e-12);
        Assertions.assertEquals(5.0, d.replaceXwidth(3).xmax(), 1e-12, "xmax = xmin + width");
        Assertions.assertEquals(0.5, d.replaceYwidth(0.5).ymax(), 1e-12);
    }

    // ------------------------------------------------------------------ C9 (names)
    @Test
    public void c9_nodeNamesAreNotCopiedFromOtherNodes() {
        String mul = Mul.of(Maths.expr(2.0), Maths.X).toElement().toString();
        Assertions.assertTrue(mul.contains("Mul"), "Mul serialises as Mul: " + mul);
        Assertions.assertFalse(mul.contains("Rooftop"), "Mul must not serialise as Rooftop");
        String db = Maths.X.db().toElement().toString();
        Assertions.assertTrue(db.contains("Db"), "Db serialises as Db: " + db);
    }
}
