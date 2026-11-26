package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.NormStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * Regression tests for the Complex/Matrix/base-type fixes:
 * <ul>
 *   <li>Smith division (no overflow for huge/tiny divisors, identical for ordinary values)</li>
 *   <li>algebraic principal square root</li>
 *   <li>abssqr = |z|^2 (was |z|)</li>
 *   <li>-0.0 normalisation so that value equality/hashing is stable</li>
 *   <li>covariant sub/add(Complex) (they used to widen to Expr through sub(Expr))</li>
 *   <li>DoubleToDouble.apply(x,y,z) must not drop z</li>
 *   <li>Expr.log10() is base 10, not natural log</li>
 * </ul>
 */
public class ComplexMathTest {

    private static double ref(double v) {
        return Math.max(1.0, Math.abs(v));
    }

    // ------------------------------------------------------------------ division
    @Test
    public void divisionMatchesReferenceFormula() {
        Random rnd = new Random(1234);
        for (int i = 0; i < 5000; i++) {
            double a = (rnd.nextDouble() - 0.5) * 1e6;
            double b = (rnd.nextDouble() - 0.5) * 1e6;
            double c = (rnd.nextDouble() - 0.5) * 1e6;
            double d = (rnd.nextDouble() - 0.5) * 1e6;
            if (c == 0 && d == 0) {
                continue;
            }
            Complex q = Complex.of(a, b).div(Complex.of(c, d));
            double den = c * c + d * d;
            double re = (a * c + b * d) / den;
            double im = (b * c - a * d) / den;
            Assertions.assertEquals(re, q.getReal(), 1e-12 * ref(re));
            Assertions.assertEquals(im, q.getImag(), 1e-12 * ref(im));
        }
    }

    @Test
    public void divisionDoesNotOverflowForHugeDivisor() {
        Complex z = Complex.of(1, 1);
        Complex q = z.div(Complex.of(1e300, 1e300));
        Assertions.assertFalse(Double.isNaN(q.getReal()));
        Assertions.assertFalse(Double.isInfinite(q.getReal()));
        Assertions.assertTrue(q.absdbl() < 1e-299, "|1/(1e300+1e300i)| ~ 5e-301, got " + q.absdbl());
    }

    @Test
    public void divisionDoesNotUnderflowForTinyDivisor() {
        Complex z = Complex.of(1, 1);
        Complex q = z.div(Complex.of(1e-300, 1e-300));
        Assertions.assertFalse(Double.isNaN(q.getReal()));
        Assertions.assertFalse(Double.isInfinite(q.getReal()));
        Assertions.assertTrue(q.absdbl() > 1e299, "|1/(1e-300+1e-300i)| ~ 7e299, got " + q.absdbl());
    }

    @Test
    public void mutableDivisionMatchesImmutable() {
        Random rnd = new Random(99);
        for (int i = 0; i < 2000; i++) {
            double a = (rnd.nextDouble() - 0.5) * 1e4;
            double b = (rnd.nextDouble() - 0.5) * 1e4;
            double c = (rnd.nextDouble() - 0.5) * 1e4;
            double d = (rnd.nextDouble() - 0.5) * 1e4;
            if (c == 0 && d == 0) {
                continue;
            }
            Complex expected = Complex.of(a, b).div(Complex.of(c, d));
            net.thevpc.scholar.hadrumaths.MutableComplex m =
                    new net.thevpc.scholar.hadrumaths.MutableComplex(a, b);
            m.div(c, d);
            Assertions.assertEquals(expected.getReal(), m.getReal(), 1e-12 * ref(expected.getReal()));
            Assertions.assertEquals(expected.getImag(), m.getImag(), 1e-12 * ref(expected.getImag()));
        }
    }

    // ------------------------------------------------------------------ sqrt
    @Test
    public void sqrtSquaresBackToOriginal() {
        Random rnd = new Random(7);
        for (int i = 0; i < 5000; i++) {
            double re = (rnd.nextDouble() - 0.5) * 200;
            double im = (rnd.nextDouble() - 0.5) * 200;
            Complex z = Complex.of(re, im);
            Complex s = z.sqrt();
            Complex back = s.mul(s);
            Assertions.assertEquals(re, back.getReal(), 1e-9 * ref(re));
            Assertions.assertEquals(im, back.getImag(), 1e-9 * ref(im));
            Assertions.assertTrue(s.getReal() >= 0, "principal branch must have Re >= 0: " + s);
        }
    }

    @Test
    public void sqrtMatchesPolarDefinition() {
        Random rnd = new Random(8);
        for (int i = 0; i < 2000; i++) {
            double re = (rnd.nextDouble() - 0.5) * 50;
            double im = (rnd.nextDouble() - 0.5) * 50;
            Complex z = Complex.of(re, im);
            double r = Math.sqrt(z.absdbl());
            double theta = Math.atan2(im, re) / 2;
            Complex expected = Complex.of(r * Math.cos(theta), r * Math.sin(theta));
            Complex actual = z.sqrt();
            Assertions.assertEquals(expected.getReal(), actual.getReal(), 1e-10 * ref(expected.getReal()));
            Assertions.assertEquals(expected.getImag(), actual.getImag(), 1e-10 * ref(expected.getImag()));
        }
    }

    @Test
    public void sqrtOnNegativeRealsStaysImaginary() {
        Assertions.assertEquals(Complex.of(0, 2), Complex.of(-4).sqrt());
        Assertions.assertEquals(Complex.of(2), Complex.of(4).sqrt());
        Assertions.assertEquals(Complex.of(0, 0), Complex.of(0).sqrt());
    }

    // ------------------------------------------------------------------ abssqr / -0.0
    @Test
    public void abssqrIsSquaredModulus() {
        Assertions.assertEquals(25.0, Complex.of(3, 4).abssqr().getReal(), 0.0);
        Assertions.assertEquals(25.0, Complex.of(0, 5).abssqr().getReal(), 0.0);
        Assertions.assertEquals(0.0, Complex.of(0, 0).abssqr().getReal(), 0.0);
    }

    @Test
    public void arithmeticNeverLeaksNegativeZero() {
        // -1 + 1 must be exactly Complex.ZERO (value *and* hash), not a -0.0 variant
        Complex a = Complex.of(-1, 0).add(Complex.of(1, 0));
        Assertions.assertEquals(Complex.of(0), a);
        Assertions.assertEquals(Complex.of(0).hashCode(), a.hashCode());

        Complex b = Complex.of(-4, 0).mul(Complex.of(1, 0));
        Assertions.assertEquals(Complex.of(-4), b);
        Assertions.assertEquals(Complex.of(-4).hashCode(), b.hashCode());

        Complex c = Complex.of(2, -3).sub(Complex.of(2, -3));
        Assertions.assertEquals(Complex.ZERO, c);
        Assertions.assertEquals(Complex.ZERO.hashCode(), c.hashCode());
    }

    // ------------------------------------------------------------------ covariant API
    @Test
    public void subAndAddStayComplex() {
        // these lines only compile if sub/add(Complex) return Complex (not Expr)
        Complex r = Complex.of(1, 2).sub(Complex.of(1, 1)).add(Complex.I);
        Assertions.assertEquals(Complex.of(0, 2), r);
    }

    // ------------------------------------------------------------------ tanh / exp
    @Test
    public void tanhMatchesExpDefinition() {
        Random rnd = new Random(11);
        for (int i = 0; i < 2000; i++) {
            double re = (rnd.nextDouble() - 0.5) * 40;
            double im = (rnd.nextDouble() - 0.5) * 40;
            Complex z = Complex.of(re, im);
            // tanh z = (e^z - e^-z)/(e^z + e^-z)
            Complex expected = z.exp().minus(z.neg().exp()).div(z.exp().plus(z.neg().exp()));
            Complex actual = z.tanh();
            Assertions.assertEquals(expected.getReal(), actual.getReal(), 1e-9 * ref(expected.getReal()));
            Assertions.assertEquals(expected.getImag(), actual.getImag(), 1e-9 * ref(expected.getImag()));
        }
    }

    @Test
    public void mutableTanhMatchesImmutable() {
        Complex z = Complex.of(0.3, -1.7);
        net.thevpc.scholar.hadrumaths.MutableComplex m = new net.thevpc.scholar.hadrumaths.MutableComplex(0.3, -1.7);
        m.tanh();
        Complex expected = z.tanh();
        Assertions.assertEquals(expected.getReal(), m.getReal(), 1e-15);
        Assertions.assertEquals(expected.getImag(), m.getImag(), 1e-15);
    }

    // ------------------------------------------------------------------ matrix
    @Test
    public void complexMatrixProductMatchesNaive() {
        Random rnd = new Random(5);
        int n = 6, m = 4, p = 5;
        Complex[][] a = new Complex[n][m];
        Complex[][] b = new Complex[m][p];
        for (int i = 0; i < n; i++) for (int j = 0; j < m; j++) a[i][j] = Complex.of(rnd.nextGaussian(), rnd.nextGaussian());
        for (int i = 0; i < m; i++) for (int j = 0; j < p; j++) b[i][j] = Complex.of(rnd.nextGaussian(), rnd.nextGaussian());

        ComplexMatrix ma = Maths.matrix(a);
        ComplexMatrix mb = Maths.matrix(b);
        ComplexMatrix r = ma.mul(mb);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                Complex expected = Complex.ZERO;
                for (int k = 0; k < m; k++) {
                    expected = expected.add(a[i][k].mul(b[k][j]));
                }
                Complex actual = r.get(i, j);
                Assertions.assertEquals(expected.getReal(), actual.getReal(), 1e-12 * ref(expected.getReal()));
                Assertions.assertEquals(expected.getImag(), actual.getImag(), 1e-12 * ref(expected.getImag()));
            }
        }
    }

    // ------------------------------------------------------------------ base-type bugs
    @Test
    public void norm2IsSpectralAndNormFIsFrobenius() {
        // diagonal: singular values 3 and 5
        ComplexMatrix d = Maths.matrix(new Complex[][]{
                {Complex.of(3), Complex.of(0)},
                {Complex.of(0), Complex.of(5)}});
        Assertions.assertEquals(5.0, d.norm2(), 1e-9);
        Assertions.assertEquals(5.0, d.norm(NormStrategy.NORM2), 1e-9);
        Assertions.assertEquals(5.0, d.norm(NormStrategy.DEFAULT), 1e-9);
        Assertions.assertEquals(Math.sqrt(34), d.normF(), 1e-9);
        Assertions.assertEquals(Math.sqrt(34), d.norm(NormStrategy.NORM_F), 1e-9);

        // [[1,2],[3,4]] : sigma_max = sqrt(15 + sqrt(221)), Frobenius = sqrt(30)
        ComplexMatrix a = Maths.matrix(new Complex[][]{
                {Complex.of(1), Complex.of(2)},
                {Complex.of(3), Complex.of(4)}});
        Assertions.assertEquals(Math.sqrt(15 + Math.sqrt(221)), a.norm2(), 1e-9);
        Assertions.assertEquals(Math.sqrt(30), a.normF(), 1e-9);
    }

    @Test
    public void abssqrOfComplexConstantIsSquaredModulus() {
        Expr sq = Maths.EXPR_VECTOR_SPACE.abssqr(Complex.of(3, 4));
        Assertions.assertEquals(25.0, sq.toDD().evalDouble(0.0), 0.0);
        Expr sq2 = Maths.EXPR_VECTOR_SPACE.abssqr(Maths.expr(3.0));
        Assertions.assertEquals(9.0, sq2.toDD().evalDouble(0.0), 0.0);
    }

    @Test
    public void applyHonoursZ() throws Exception {
        // f(x,y,z) = z ; apply(x,y,z) used to return evalDouble(x,y) and silently drop z
        Expr z = Maths.Z;
        double v = z.toDD().apply(0.0, 0.0, 7.0);
        Assertions.assertEquals(7.0, v, 0.0);
    }

    @Test
    public void log10IsBaseTen() {
        // log10(100) = 2 must not be ln(100) = 4.605...
        double v = Maths.expr(100.0).log10().toDD().evalDouble(0.0);
        Assertions.assertEquals(2.0, v, 1e-12);
    }
}
