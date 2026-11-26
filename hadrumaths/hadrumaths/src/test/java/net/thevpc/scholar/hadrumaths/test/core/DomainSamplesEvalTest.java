package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Domain / sampling / evaluation invariants.
 * <p>
 * The important convention here is that a domain is <b>half-open</b> ([min, max)):
 * a boundary point belongs to exactly one side, so a piecewise function built as the sum
 * of pieces with adjacent supports is not doubled at the seam. These tests pin that.
 */
public class DomainSamplesEvalTest {

    // ------------------------------------------------------------------ domain
    @Test
    public void domainBasics() {
        Domain d = Domain.ofBounds(0, 10, -5, 5);
        Assertions.assertEquals(2, d.getDimension());
        Assertions.assertEquals(10.0, d.xwidth(), 1e-12);
        Assertions.assertEquals(10.0, d.ywidth(), 1e-12);
        Assertions.assertEquals(0.0, d.xmin(), 1e-12);
        Assertions.assertEquals(10.0, d.xmax(), 1e-12);
        Assertions.assertTrue(d.contains(0.0, 0.0), "inside");
        Assertions.assertTrue(d.contains(5.0, 4.99), "inside");
        Assertions.assertFalse(d.contains(11.0, 0.0), "outside x");
        Assertions.assertFalse(d.contains(5.0, 5.01), "outside y");
    }

    @Test
    public void domainIsHalfOpenAtTheUpperBound() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Assertions.assertTrue(d.contains(0.0, 0.0), "min is included");
        Assertions.assertTrue(d.contains(0.999999, 0.5), "just below max is included");
        Assertions.assertFalse(d.contains(1.0, 0.5), "xmax is excluded");
        Assertions.assertFalse(d.contains(0.5, 1.0), "ymax is excluded");
    }

    @Test
    public void intersectionIsCorrectAndDisjointIsEmpty() {
        Domain a = Domain.ofBounds(0, 10, 0, 10);
        Domain b = Domain.ofBounds(5, 15, -5, 5);
        Domain i = a.intersect(b);
        Assertions.assertEquals(5.0, i.xmin(), 1e-12);
        Assertions.assertEquals(10.0, i.xmax(), 1e-12);
        Assertions.assertEquals(0.0, i.ymin(), 1e-12);
        Assertions.assertEquals(5.0, i.ymax(), 1e-12);

        Domain disjoint = a.intersect(Domain.ofBounds(20, 30, 20, 30));
        Assertions.assertTrue(disjoint.isEmpty(), "disjoint boxes must be empty, got " + disjoint);

        Assertions.assertEquals(a, a.intersect(a));
        Domain sup = Domain.ofBounds(-1, 11, -1, 11);
        Domain j = a.intersect(sup);
        Assertions.assertEquals(a.xmin(), j.xmin(), 1e-12);
        Assertions.assertEquals(a.xmax(), j.xmax(), 1e-12);
    }

    @Test
    public void expandAndTranslateAreInverses() {
        Domain a = Domain.ofBounds(1, 4, -2, 2);
        Domain m = a.translate(3, -1);
        Assertions.assertEquals(4.0, m.xmin(), 1e-12);
        Assertions.assertEquals(7.0, m.xmax(), 1e-12);
        Assertions.assertEquals(-3.0, m.ymin(), 1e-12);
        Assertions.assertEquals(1.0, m.ymax(), 1e-12);
        Domain back = m.translate(-3, 1);
        Assertions.assertEquals(a.xmin(), back.xmin(), 1e-12);
        Assertions.assertEquals(a.xmax(), back.xmax(), 1e-12);
        Assertions.assertEquals(a.ymin(), back.ymin(), 1e-12);
        Assertions.assertEquals(a.ymax(), back.ymax(), 1e-12);
    }

    @Test
    public void rangeIsHalfOpenAndRangeCCIsClosed() {
        double[] xs = {0.0, 1.0, 2.0, 3.0};
        Domain d = Domain.ofBounds(0, 2, 0, 1);
        Range r = d.range(xs);
        Assertions.assertEquals(0, r.xmin, "first index");
        Assertions.assertEquals(1, r.xmax, "x=2 is excluded by the half-open convention");
        int[] cc = Maths.rangeCC(xs, 0, 2);
        Assertions.assertEquals(0, cc[0], "rangeCC first");
        Assertions.assertEquals(2, cc[1], "rangeCC includes x=2");
    }

    // ------------------------------------------------------------------ samples
    @Test
    public void relativeSamplesAreCellCentres() {
        for (int n : new int[]{2, 5, 17}) {
            double[] rel = ((RelativeSamples) Samples.relative(n)).getX();
            Assertions.assertEquals(n, rel.length, "sample count");
            Assertions.assertEquals(0.5 / n, rel[0], 1e-12, "first centre");
            Assertions.assertEquals(1.0 - 0.5 / n, rel[n - 1], 1e-12, "last centre");
            for (int i = 0; i < n; i++) {
                Assertions.assertTrue(rel[i] > 0.0 && rel[i] < 1.0, "strictly inside (0,1)");
                if (i > 0) {
                    Assertions.assertEquals(1.0 / n, rel[i] - rel[i - 1], 1e-12, "uniform");
                }
            }
        }
    }

    @Test
    public void absoluteSamplesLandStrictlyInside() {
        Domain d = Domain.ofBounds(10, 20, -2, 2);
        int n = 9;
        double[] x = d.toAbsolute(Samples.relative(n)).getX();
        double step = d.xwidth() / n;
        Assertions.assertEquals(d.xmin() + step / 2, x[0], 1e-12, "first");
        Assertions.assertEquals(d.xmax() - step / 2, x[x.length - 1], 1e-12, "last");
        for (double v : x) {
            Assertions.assertTrue(v >= d.xmin() && v < d.xmax(), "inside half-open domain: " + v);
        }
    }

    @Test
    public void stepsAreBoundedAndUniform() {
        double min = 0, max = 10, step = 0.5;
        double[] v = Maths.dsteps(min, max, step);
        Assertions.assertTrue(v.length >= 2, "non trivial");
        Assertions.assertEquals(min, v[0], 1e-12, "starts at min");
        Assertions.assertTrue(v[v.length - 1] <= max + 1e-12, "ends within max");
        for (int i = 1; i < v.length; i++) {
            Assertions.assertEquals(step, v[i] - v[i - 1], 1e-9, "uniform step");
        }
    }

    // ------------------------------------------------------------------ evaluation
    @Test
    public void expressionOnGridEqualsPointwise() {
        Expr f = Maths.X.mul(2).plus(Maths.Y.sqr()).plus(3);
        double[] xs = {-1, 0, 0.5, 2};
        double[] ys = {0, 1, 3};
        double[][] grid = f.toDD().evalDouble(xs, ys, (Domain) null, null);
        for (int j = 0; j < ys.length; j++) {
            for (int i = 0; i < xs.length; i++) {
                double point = f.toDD().evalDouble(xs[i], ys[j]);
                Assertions.assertEquals(point, grid[j][i], 1e-12,
                        "grid vs point at (" + xs[i] + "," + ys[j] + ")");
            }
        }
    }

    @Test
    public void generatedSamplesAreAllEvaluated() {
        // every sample produced by the default sampler is inside, so none evaluates to the
        // "outside" value 0 (this is what the old closed grid got wrong)
        Domain d = Domain.ofBounds(0, 10, -1, 1);
        double[] xs = d.toAbsolute(Samples.relative(5)).getX();
        double[] vals = Maths.expr(1.0, d).toDD().evalDouble(xs);
        for (int i = 0; i < xs.length; i++) {
            Assertions.assertEquals(1.0, vals[i], 1e-12, "sample at x=" + xs[i]);
        }
    }

    /**
     * The case that rules out closed domains: a piecewise function built as the sum of
     * pieces with adjacent supports must not count the shared border twice.
     */
    @Test
    public void piecewiseSeamIsCountedOnce() {
        Domain d1 = Domain.ofBounds(0, 1, 0, 1);
        Domain d2 = Domain.ofBounds(1, 2, 0, 1);
        Expr f = Maths.expr(1.0, d1).plus(Maths.expr(5.0, d2));
        Assertions.assertEquals(1.0, f.toDD().evalDouble(0.5, 0.5), 1e-12, "left piece");
        Assertions.assertEquals(5.0, f.toDD().evalDouble(1.0, 0.5), 1e-12,
                "seam belongs to the right piece only (1+5 would be the bug)");
        Assertions.assertEquals(5.0, f.toDD().evalDouble(1.5, 0.5), 1e-12, "right piece");
    }

    @Test
    public void continuousPiecewiseSumHasNoSeamSpike() {
        // f(x) = x, split in two supports; the seam value must be 1, not 2
        Domain d1 = Domain.ofBounds(0, 1, 0, 1);
        Domain d2 = Domain.ofBounds(1, 2, 0, 1);
        Expr f = Maths.X.mul(d1).plus(Maths.X.mul(d2));
        Assertions.assertEquals(1.0, f.toDD().evalDouble(1.0, 0.5), 1e-12, "seam value");

        Complex i = IntegrationOperatorFactory.numeric().eval(Domain.ofBounds(0, 2, 0, 1), f);
        Assertions.assertEquals(2.0, i.getReal(), 1e-6, "integral of x over [0,2]x[0,1]");
        Assertions.assertEquals(0.0, i.getImag(), 1e-12);
    }

    @Test
    public void arrayEvalEqualsScalarEval() {
        Expr f = Maths.X.sin().plus(Maths.X.sqr());
        double[] xs = {-2, -0.5, 0, 0.5, 2};
        double[] vals = f.toDD().evalDouble(xs);
        for (int i = 0; i < xs.length; i++) {
            Assertions.assertEquals(f.toDD().evalDouble(xs[i]), vals[i], 1e-12, "array vs scalar");
        }
    }

    @Test
    public void sqrIsSquareNotSquareRoot() {
        Assertions.assertEquals(9.0, Maths.X.sqr().toDD().evalDouble(3.0), 1e-12, "X.sqr() at 3");
        Assertions.assertEquals(9.0, Maths.X.pow(Maths.expr(2.0)).toDD().evalDouble(3.0), 1e-12, "X^2 at 3");
        Assertions.assertEquals(3.0, Maths.X.sqrt().toDC().evalComplex(9.0).getReal(), 1e-12, "sqrt at 9");
        // sqrt is deliberately complex-typed (it is a complex function), see Sqrt.of
    }

    @Test
    public void restrictionToDomainIsUndefinedOutside() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr g = Maths.X.mul(d);
        BooleanRef inside = BooleanMarker.ref();
        double vIn = g.toDD().evalDouble(0.5, 0.5, inside);
        Assertions.assertTrue(inside.get(), "inside is defined");
        Assertions.assertEquals(0.5, vIn, 1e-12);

        BooleanRef outside = BooleanMarker.ref();
        g.toDD().evalDouble(2.0, 0.5, outside);
        Assertions.assertFalse(outside.get(), "outside must be undefined");
    }

    @Test
    public void sumOfRestrictionsDoesNotLeakAcrossSupports() {
        Domain d1 = Domain.ofBounds(0, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Domain d2 = Domain.ofBounds(1, 2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Expr f = Maths.expr(1.0, d1).plus(Maths.expr(2.0, d2));
        Assertions.assertEquals(1.0, f.toDD().evalDouble(0.5, 0.0), 1e-12);
        Assertions.assertEquals(2.0, f.toDD().evalDouble(1.5, 0.0), 1e-12);
        BooleanRef none = BooleanMarker.ref();
        f.toDD().evalDouble(3.0, 0.0, none);
        Assertions.assertFalse(none.get(), "outside both supports");
    }
}
