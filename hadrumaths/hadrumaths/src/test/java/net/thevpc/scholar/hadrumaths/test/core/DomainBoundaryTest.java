package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.Range;
import net.thevpc.scholar.hadrumaths.integration.IntegrationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Boundary special cases for domains, sampling and evaluation.
 * <p>
 * Convention under test: a domain is <b>half-open</b>, [min, max). A boundary point belongs
 * to exactly one of two adjacent domains, which is what keeps piecewise sums from being
 * counted twice. Everything here is a case that a "closed domain" change would break.
 */
public class DomainBoundaryTest {

    // ------------------------------------------------------------------ set algebra
    @Test
    public void adjacentDomainsIntersectToEmpty() {
        // [0,1) and [1,2) share no point, so their intersection is empty
        Domain a = Domain.ofBounds(0, 1, 0, 1);
        Domain b = Domain.ofBounds(1, 2, 0, 1);
        Domain i = a.intersect(b);
        Assertions.assertTrue(i.isEmpty(), "adjacent half-open boxes must intersect to empty, got " + i);
        Assertions.assertFalse(i.contains(1.0, 0.5), "the shared coordinate is in neither intersection");
    }

    @Test
    public void overlappingDomainsIntersectAtTheSharedPart() {
        Domain a = Domain.ofBounds(0, 1, 0, 1);
        Domain b = Domain.ofBounds(0.5, 2, 0, 1);
        Domain i = a.intersect(b);
        Assertions.assertFalse(i.isEmpty());
        Assertions.assertEquals(0.5, i.xmin(), 1e-12);
        Assertions.assertEquals(1.0, i.xmax(), 1e-12);
        // x=1 is max of the intersection, hence outside by the half-open rule
        Assertions.assertFalse(i.contains(1.0, 0.5));
        Assertions.assertTrue(i.contains(0.999999, 0.5));
    }

    @Test
    public void translateKeepsHalfOpenSemantics() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Domain m = d.translate(1, 0);
        Assertions.assertTrue(m.contains(1.0, 0.5), "new min is included");
        Assertions.assertFalse(m.contains(2.0, 0.5), "new max is excluded");
    }

    @Test
    public void halfOpenOnEveryAxis() {
        Domain d = Domain.ofBounds(0, 1, 0, 1, 0, 1);
        Assertions.assertTrue(d.contains(0.5, 0.5, 0.5), "inside");
        Assertions.assertFalse(d.contains(1.0, 0.5, 0.5), "x max excluded");
        Assertions.assertFalse(d.contains(0.5, 1.0, 0.5), "y max excluded");
        Assertions.assertFalse(d.contains(0.5, 0.5, 1.0), "z max excluded");
    }

    // ------------------------------------------------------------------ seam evaluation
    @Test
    public void threePiecesSeamCountedOnce() {
        Domain d1 = Domain.ofBounds(0, 1, 0, 1);
        Domain d2 = Domain.ofBounds(1, 2, 0, 1);
        Domain d3 = Domain.ofBounds(2, 3, 0, 1);
        Expr f = Maths.expr(1.0, d1).plus(Maths.expr(10.0, d2)).plus(Maths.expr(100.0, d3));
        Assertions.assertEquals(1.0, f.toDD().evalDouble(0.5, 0.5), 1e-12, "piece 1");
        Assertions.assertEquals(10.0, f.toDD().evalDouble(1.0, 0.5), 1e-12, "seam 1|2 -> piece 2 only");
        Assertions.assertEquals(10.0, f.toDD().evalDouble(1.5, 0.5), 1e-12, "piece 2");
        Assertions.assertEquals(100.0, f.toDD().evalDouble(2.0, 0.5), 1e-12, "seam 2|3 -> piece 3 only");
        Assertions.assertEquals(100.0, f.toDD().evalDouble(2.5, 0.5), 1e-12, "piece 3");
    }

    @Test
    public void seamIsCountedOnceOnTheOtherAxisToo() {
        Domain d1 = Domain.ofBounds(0, 1, 0, 1);
        Domain d2 = Domain.ofBounds(0, 1, 1, 2);
        Expr f = Maths.expr(3.0, d1).plus(Maths.expr(7.0, d2));
        Assertions.assertEquals(3.0, f.toDD().evalDouble(0.5, 0.5), 1e-12, "lower");
        Assertions.assertEquals(7.0, f.toDD().evalDouble(0.5, 1.0), 1e-12, "seam -> upper only");
        Assertions.assertEquals(7.0, f.toDD().evalDouble(0.5, 1.5), 1e-12, "upper");
    }

    @Test
    public void piecewiseDifferenceIsAlsoCountedOnce() {
        Domain d1 = Domain.ofBounds(0, 1, 0, 1);
        Domain d2 = Domain.ofBounds(1, 2, 0, 1);
        Expr f = Maths.expr(4.0, d1).sub(Maths.expr(1.5, d2));
        Assertions.assertEquals(4.0, f.toDD().evalDouble(0.5, 0.5), 1e-12, "left");
        Assertions.assertEquals(-1.5, f.toDD().evalDouble(1.0, 0.5), 1e-12, "seam -> right only");
    }

    // ------------------------------------------------------------------ range search edges
    @Test
    public void rangeCoEdgeCases() {
        Assertions.assertNull(Maths.rangeCO(new double[]{}, 0, 1), "empty array");
        Assertions.assertNull(Maths.rangeCO(new double[]{0, 1}, 5, 6), "window before all values");
        Assertions.assertNull(Maths.rangeCO(new double[]{0, 1}, -5, -1), "window after all values");
        Assertions.assertNull(Maths.rangeCO(new double[]{1.0}, 0, 1), "only value sits on the excluded max");
        // only value sits on min -> included
        int[] one = Maths.rangeCO(new double[]{0.5}, 0.5, 1.0);
        Assertions.assertNotNull(one);
        Assertions.assertEquals(0, one[0]);
        Assertions.assertEquals(0, one[1]);
        // degenerate (empty) window
        Assertions.assertNull(Maths.rangeCO(new double[]{0.5}, 0.5, 0.5), "min == max -> empty");
        // full window
        int[] all = Maths.rangeCO(new double[]{0, 1, 2}, 0, 2);
        Assertions.assertEquals(0, all[0]);
        Assertions.assertEquals(1, all[1], "value equal to max excluded");
    }

    @Test
    public void gridEvaluationDropsExplicitBoundaryPoints() {
        // documented consequence: an explicitly closed sample set loses its max point
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr f = Maths.expr(1.0, d);
        double[] vals = f.toDD().evalDouble(new double[]{0.0, 0.5, 1.0});
        Assertions.assertEquals(1.0, vals[0], 1e-12, "min included");
        Assertions.assertEquals(1.0, vals[1], 1e-12, "inside");
        Assertions.assertEquals(0.0, vals[2], 1e-12, "max excluded -> outside value");
    }

    @Test
    public void explicitClosedRelativeSamplesRespectTheDomain() {
        // a user-supplied closed grid maps 1.0 onto max and is therefore outside;
        // the default (int) sampler avoids this by using cell centres
        Domain d = Domain.ofBounds(0, 10, 0, 1);
        double[] rel = {0.0, 0.5, 1.0};
        double[] xs = d.toAbsolute(Samples.relative(rel)).getX();
        Assertions.assertEquals(0.0, xs[0], 1e-12);
        Assertions.assertEquals(10.0, xs[2], 1e-12);
        double[] vals = Maths.expr(1.0, d).toDD().evalDouble(xs);
        Assertions.assertEquals(1.0, vals[0], 1e-12);
        Assertions.assertEquals(1.0, vals[1], 1e-12);
        Assertions.assertEquals(0.0, vals[2], 1e-12, "the closed-grid endpoint is outside");
    }

    // ------------------------------------------------------------------ integral over a partition
    @Test
    public void integralOverAdjacentSupportsEqualsSumOfIntegrals() {
        // f = x on [0,1) then x on [1,2): the analytic integral is 2.
        Domain d1 = Domain.ofBounds(0, 1, 0, 1);
        Domain d2 = Domain.ofBounds(1, 2, 0, 1);
        Expr f1 = Maths.X.mul(d1);
        Expr f2 = Maths.X.mul(d2);
        Expr whole = f1.plus(f2);

        IntegrationOperator io = IntegrationOperatorFactory.numeric();
        Complex wholeIntegral = io.eval(Domain.ofBounds(0, 2, 0, 1), whole);
        Complex sumOfParts = io.eval(d1, f1).add(io.eval(d2, f2));
        Assertions.assertEquals(2.0, wholeIntegral.getReal(), 1e-6, "integral of the sum");
        Assertions.assertEquals(2.0, sumOfParts.getReal(), 1e-6, "sum of the integrals");
        // the two must agree: no seam term is counted twice
        Assertions.assertEquals(wholeIntegral.getReal(), sumOfParts.getReal(), 1e-6,
                "partition sum equals the integral of the sum");
    }

    @Test
    public void scalarProductOfPartitionMatchesSum() {
        Domain d1 = Domain.ofBounds(0, 1, 0, 1);
        Domain d2 = Domain.ofBounds(1, 2, 0, 1);
        Expr f = Maths.expr(1.0, d1).plus(Maths.expr(2.0, d2));
        Expr g = Maths.expr(1.0, Domain.ofBounds(0, 2, 0, 1));
        Complex whole = Maths.NUMERIC_SP.eval(Domain.ofBounds(0, 2, 0, 1), f, g).toComplex();
        Complex parts = Maths.NUMERIC_SP.eval(d1, Maths.expr(1.0, d1), g)
                .toComplex()
                .add(Maths.NUMERIC_SP.eval(d2, Maths.expr(2.0, d2), g).toComplex());
        Assertions.assertEquals(3.0, whole.getReal(), 1e-6, "<f,g> over the whole support");
        Assertions.assertEquals(3.0, parts.getReal(), 1e-6, "<f1,g>+<f2,g>");
        Assertions.assertEquals(whole.getReal(), parts.getReal(), 1e-6, "no seam double count");
    }
}
