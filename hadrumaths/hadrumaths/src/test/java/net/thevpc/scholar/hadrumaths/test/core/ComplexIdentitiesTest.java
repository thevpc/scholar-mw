package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Maths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * Algebraic identity checks for Complex. A failure here is a calculation error, not a
 * styling issue: every assertion is either a definition or an identity that must hold.
 * Random points avoid the axes so branch conventions are not the thing under test.
 */
public class ComplexIdentitiesTest {

    static final double PI = Math.PI;

    static double tol(double v) {
        return 1e-9 * Math.max(1.0, Math.abs(v));
    }

    static void eq(double expected, double actual, String what) {
        Assertions.assertEquals(expected, actual, tol(expected), what);
    }

    static void eq(Complex expected, Complex actual, String what) {
        eq(expected.getReal(), actual.getReal(), what + " (re)");
        eq(expected.getImag(), actual.getImag(), what + " (im)");
    }

    static Complex rnd(Random r, double scale) {
        double re = (r.nextDouble() * 2 - 1) * scale;
        double im = (r.nextDouble() * 2 - 1) * scale;
        if (Math.abs(im) < 0.05) im = 0.37;   // stay off the real axis
        return Complex.of(re, im);
    }

    // ------------------------------------------------------------------ field ops
    @Test
    public void divisionInvertsMultiplication() {
        Random r = new Random(1);
        for (int i = 0; i < 3000; i++) {
            Complex z = rnd(r, 50), w = rnd(r, 50);
            eq(z, z.div(w).mul(w), "z/w*w");
            eq(z, w.mul(z.div(w)), "w*(z/w)");
        }
    }

    @Test
    public void conjugateAndModulus() {
        Random r = new Random(2);
        for (int i = 0; i < 3000; i++) {
            Complex z = rnd(r, 100);
            eq(z, z.conj().conj(), "conj(conj(z))");
            eq(z.mul(z.conj()).getImag(), 0.0, "Im(z*conj z)");
            eq(z.absdblsqr(), z.mul(z.conj()).getReal(), "|z|^2 == Re(z conj z)");
            eq(z.absdbl(), Math.hypot(z.getReal(), z.getImag()), "|z|");
        }
    }

    @Test
    public void polarFormIsConsistent() {
        Random r = new Random(3);
        for (int i = 0; i < 3000; i++) {
            Complex z = rnd(r, 100);
            double m = z.absdbl();
            double a = z.arg().toDouble();
            eq(z, Complex.polar(m, a), "|z| e^{i arg z}");
        }
    }

    // ------------------------------------------------------------------ exponential / log
    @Test
    public void expAddsArguments() {
        Random r = new Random(4);
        for (int i = 0; i < 3000; i++) {
            Complex a = rnd(r, 3), b = rnd(r, 3);
            eq(a.add(b).exp(), a.exp().mul(b.exp()), "exp(a+b) == exp a exp b");
        }
    }

    @Test
    public void logIsInverseOfExp() {
        Random r = new Random(5);
        for (int i = 0; i < 3000; i++) {
            // keep Im in (-pi, pi) so the principal branch is the inverse
            double re = (r.nextDouble() * 2 - 1) * 5;
            double im = (r.nextDouble() * 2 - 1) * (PI - 0.2);
            Complex z = Complex.of(re, im);
            eq(z, z.exp().log(), "log(exp z)");
        }
    }

    @Test
    public void log10IsLogOverLn10() {
        Random r = new Random(6);
        for (int i = 0; i < 2000; i++) {
            Complex z = rnd(r, 20);
            eq(z.log().div(Math.log(10)), z.log10(), "log10 == log/ln10");
        }
    }

    @Test
    public void powAgreesWithExpLog() {
        Random r = new Random(7);
        for (int i = 0; i < 2000; i++) {
            Complex z = rnd(r, 5);
            double p = (r.nextDouble() * 2 - 1) * 4;
            eq(z.log().mul(p).exp(), z.pow(p), "z^p == exp(p log z)");
        }
    }

    // ------------------------------------------------------------------ trigonometric
    @Test
    public void pythagoreanIdentity() {
        Random r = new Random(8);
        for (int i = 0; i < 3000; i++) {
            // |Im| is kept small: sin^2+cos^2 is exact but ill-conditioned for large |Im|
            Complex z = Complex.of((r.nextDouble() * 2 - 1) * 20, (r.nextDouble() * 2 - 1) * 3);
            eq(Complex.ONE, z.sin().sqr().add(z.cos().sqr()), "sin^2+cos^2");
        }
        for (int i = 0; i < 3000; i++) {
            // cosh^2 - sinh^2 suffers cancellation proportional to e^{2|Re z|};
            // scale the tolerance accordingly instead of pretending it is exact
            Complex z = Complex.of((r.nextDouble() * 2 - 1) * 3, (r.nextDouble() * 2 - 1) * 3);
            Complex d = z.cosh().sqr().minus(z.sinh().sqr());
            double scale = z.cosh().absdbl() * z.cosh().absdbl() + z.sinh().absdbl() * z.sinh().absdbl();
            Assertions.assertEquals(1.0, d.getReal(), 1e-13 * Math.max(1.0, scale), "cosh^2-sinh^2 (re)");
            Assertions.assertEquals(0.0, d.getImag(), 1e-13 * Math.max(1.0, scale), "cosh^2-sinh^2 (im)");
        }
    }

    /**
     * C99 branch cuts. These used to be wrong: asin/acos were conjugated for real |x|&gt;1,
     * acosh was wrong for the whole left half-plane, asinh on the imaginary axis |y|&gt;1.
     */
    @Test
    public void inverseFunctionsMatchC99BranchCuts() {
        final double L = Math.log(2 + Math.sqrt(3));      // acosh(2)
        final double P = Math.PI;

        eq(Complex.of(P / 2, L), Complex.of(2).asin(), "asin(2)");
        eq(Complex.of(-P / 2, L), Complex.of(-2).asin(), "asin(-2)");
        eq(Complex.of(0, -L), Complex.of(2).acos(), "acos(2)");
        eq(Complex.of(P, -L), Complex.of(-2).acos(), "acos(-2)");
        eq(Complex.of(L, P), Complex.of(-2).acosh(), "acosh(-2)");
        eq(Complex.of(0, Math.acos(0.5)), Complex.of(0.5).acosh(), "acosh(0.5)");
        eq(Complex.of(L, -P / 2), Complex.of(0, -2).asinh(), "asinh(-2i)");
        eq(Complex.of(L, P / 2), Complex.of(0, 2).asinh(), "asinh(2i)");
        eq(Complex.of(0, Math.log(2 + Math.sqrt(5))), Complex.of(0, 2).asin(), "asin(2i)");
        eq(Complex.of(Math.log(2 + Math.sqrt(5)), 0), Complex.of(2).asinh(), "asinh(2)");
        // interior points, from C99 reference values
        eq(Complex.of(1.063440023577752, 1.4693517443681854), Complex.of(2, 1).asin(), "asin(2+i)");
        eq(Complex.of(0.5073563032171445, -1.4693517443681854), Complex.of(2, 1).acos(), "acos(2+i)");
        eq(Complex.of(1.4693517443681854, 0.5073563032171445), Complex.of(2, 1).acosh(), "acosh(2+i)");
    }

    @Test
    public void inverseFunctionsRoundTripOnTheirPrincipalDomain() {
        Random r = new Random(17);
        for (int i = 0; i < 2000; i++) {
            // asinh: |Im| < pi/2
            Complex z = Complex.of((r.nextDouble() * 2 - 1) * 2, (r.nextDouble() * 2 - 1) * 1.4);
            eq(z, z.sinh().asinh(), "asinh(sinh z)");
            // acosh: Re > 0, 0 < Im < pi
            Complex w = Complex.of(r.nextDouble() * 2 + 0.1, r.nextDouble() * 2.8 + 0.1);
            eq(w, w.cosh().acosh(), "acosh(cosh z)");
        }
    }

    @Test
    public void circularAndHyperbolicMatchTheirDefinitions() {
        Random r = new Random(9);
        for (int i = 0; i < 3000; i++) {
            Complex z = rnd(r, 10);
            // sin z = (e^{iz} - e^{-iz}) / 2i
            Complex iz = z.mul(Complex.I);
            eq(iz.exp().sub(iz.neg().exp()).div(Complex.of(0, 2)), z.sin(), "sin definition");
            // cos z = (e^{iz} + e^{-iz}) / 2
            eq(iz.exp().add(iz.neg().exp()).div(2), z.cos(), "cos definition");
            // sinh z = (e^z - e^-z)/2, cosh z = (e^z + e^-z)/2
            eq(z.exp().sub(z.neg().exp()).div(2), z.sinh(), "sinh definition");
            eq(z.exp().add(z.neg().exp()).div(2), z.cosh(), "cosh definition");
        }
    }

    @Test
    public void tanAndCotanAreRatios() {
        Random r = new Random(10);
        for (int i = 0; i < 3000; i++) {
            Complex z = rnd(r, 8);
            Complex s = z.sin(), c = z.cos();
            if (s.absdbl() < 1e-6 || c.absdbl() < 1e-6) continue;
            eq(s.div(c), z.tan(), "tan == sin/cos");
            eq(c.div(s), z.cotan(), "cotan == cos/sin");
        }
    }

    @Test
    public void tanhAndCotanhAreRatios() {
        Random r = new Random(11);
        for (int i = 0; i < 3000; i++) {
            Complex z = rnd(r, 8);
            Complex sh = z.sinh(), ch = z.cosh();
            if (sh.absdbl() < 1e-6 || ch.absdbl() < 1e-6) continue;
            eq(sh.div(ch), z.tanh(), "tanh == sinh/cosh");
            eq(ch.div(sh), z.cotanh(), "cotanh == cosh/sinh");
        }
    }

    @Test
    public void sqrtSquaresBack() {
        Random r = new Random(12);
        for (int i = 0; i < 3000; i++) {
            Complex z = rnd(r, 100);
            eq(z, z.sqrt().sqr(), "sqrt(z)^2");
        }
    }

    @Test
    public void inverseTrigRoundTrip() {
        Random r = new Random(13);
        for (int i = 0; i < 2000; i++) {
            // domain where the principal branch is the inverse
            Complex z = Complex.of((r.nextDouble() * 2 - 1) * 0.8, (r.nextDouble() * 2 - 1) * 0.8);
            eq(z, z.sin().asin(), "asin(sin z)");
            eq(z, z.tan().atan(), "atan(tan z)");
        }
    }

    @Test
    public void sincardAndAcotanDefinitions() {
        eq(1.0, Complex.ZERO.sincard().getReal(), "sincard(0)");
        Random r = new Random(14);
        for (int i = 0; i < 1000; i++) {
            double x = (r.nextDouble() * 2 - 1) * 8;
            if (Math.abs(x) < 1e-3) continue;
            eq(Math.sin(x) / x, Complex.of(x).sincard().getReal(), "sincard(x)");
            eq(Math.atan(1 / x), Complex.of(x).acotan().getReal(), "acotan(x)");
        }
        eq(PI / 2, Complex.ZERO.acotan().getReal(), "acotan(0)");
    }

    /**
     * Same identities, but on the real and imaginary axes so the ComplexR / ComplexI
     * subclasses are exercised too (Complex.of(a,b) with both parts non-zero only ever
     * produces ComplexRI).
     */
    @Test
    public void identitiesHoldOnRealAndImaginaryAxes() {
        double[] vals = {-2.5, -1, -0.5, 0.5, 1, 2.5};
        for (double v : vals) {
            for (Complex z : new Complex[]{Complex.of(v), Complex.of(0, v)}) {
                String tag = "z=" + z;
                eq(Complex.ONE, z.sin().sqr().add(z.cos().sqr()), "sin^2+cos^2 " + tag);
                eq(z, z.sqrt().sqr(), "sqrt^2 " + tag);
                Complex w = Complex.of(0.3, 0.7);
                eq(z, z.div(w).mul(w), "z/w*w " + tag);
                eq(z, z.exp().log(), "log(exp z) " + tag);
                eq(Complex.ONE, z.cosh().sqr().minus(z.sinh().sqr()), "hyperbolic " + tag);
            }
        }
        // real-only subclass arithmetic
        eq(Complex.of(2.5), Complex.of(-2.5).neg(), "ComplexR neg");
        eq(Complex.of(0, -1.25), Complex.of(0, 1.25).neg(), "ComplexI neg");
        eq(Complex.of(1), Complex.of(-1).mul(Complex.of(-1)), "ComplexR mul");
        eq(Complex.of(0, 1), Complex.of(0, 2).div(Complex.of(2)), "ComplexI div real");
        eq(Complex.of(4), Complex.of(-2).sqr(), "ComplexR sqr");
    }

    // ------------------------------------------------------------------ special values
    @Test
    public void zeroAndUnitEdgeCases() {
        eq(0.0, Complex.ZERO.absdbl(), "|0|");
        eq(1.0, Complex.ONE.absdbl(), "|1|");
        eq(1.0, Complex.I.absdbl(), "|i|");
        eq(PI / 2, Complex.I.arg().toDouble(), "arg(i)");
        eq(-PI / 2, Complex.MINUS_I.arg().toDouble(), "arg(-i)");
        eq(PI, Complex.of(-1).arg().toDouble(), "arg(-1)");
        eq(0.0, Complex.of(1).arg().toDouble(), "arg(1)");
        Assertions.assertTrue(Complex.of(Double.NaN).isNaN(), "NaN");
        Assertions.assertTrue(Complex.of(Double.POSITIVE_INFINITY).isInfinite(), "inf");
    }

    @Test
    public void equalsAndHashAreValueBased() {
        Random r = new Random(15);
        for (int i = 0; i < 500; i++) {
            double re = (r.nextDouble() * 2 - 1) * 1e3;
            double im = (r.nextDouble() * 2 - 1) * 1e3;
            Complex a = Complex.of(re, im);
            // round-trip through arithmetic that yields a fresh instance
            Complex b = a.add(Complex.ZERO).mul(Complex.ONE);
            Assertions.assertEquals(a, b, "value equality");
            Assertions.assertEquals(a.hashCode(), b.hashCode(), "hash equality");
        }
        // imaginary / real representations of the same value
        Assertions.assertEquals(Complex.of(0, 2), Complex.of(0, 1).add(Complex.I));
        Assertions.assertEquals(Complex.of(2, 0), Complex.of(1, 0).add(Complex.of(1)));
    }

    @Test
    public void doubleOpsMatchTheirComplexForm() {
        Random r = new Random(16);
        for (int i = 0; i < 2000; i++) {
            Complex z = rnd(r, 10);
            double d = (r.nextDouble() * 2 - 1) * 10;
            eq(z.add(Complex.of(d)), z.plus(d), "plus(double)");
            eq(z.sub(Complex.of(d)), z.minus(Complex.of(d)), "minus(Complex)");
            eq(z.mul(Complex.of(d)), z.mul(d), "mul(double)");
            eq(z.div(Complex.of(d)), z.div(d), "div(double)");
        }
    }
}
