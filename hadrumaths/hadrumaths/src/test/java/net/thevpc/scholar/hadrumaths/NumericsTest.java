package net.thevpc.scholar.hadrumaths;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Checks the numerical primitives the EM solvers rely on (complex arithmetic and branch cuts,
 * transcendental functions, Bessel functions, dense complex linear algebra) against values that
 * are computed independently here (java.lang.Math on real/imag parts) or tabulated
 * (Abramowitz &amp; Stegun).
 */
public class NumericsTest {

    private static final double TOL = 1e-12;

    private static void near(double expRe, double expIm, Complex got, double relTol, String what) {
        double scale = Math.max(1.0, Math.hypot(expRe, expIm));
        double err = Math.hypot(got.getReal() - expRe, got.getImag() - expIm);
        assertTrue(err <= relTol * scale,
                what + ": expected " + expRe + (expIm >= 0 ? "+" : "") + expIm + "i, got " + got + " (err " + err + ")");
    }

    private static final double[][] SAMPLES = {
            {1.3, 0.7}, {-1.3, 0.7}, {-1.3, -0.7}, {1.3, -0.7}, {0.0, 2.0}, {0.0, -2.0}, {-4.0, 0.0},
            {4.0, 0.0}, {1e-3, 5.0}, {12.5, -0.01}, {-0.2, 30.0}
    };

    // ---------------------------------------------------------------- arithmetic
    @Test
    public void mulDivInv() {
        for (double[] a : SAMPLES) {
            for (double[] b : SAMPLES) {
                Complex x = Complex.of(a[0], a[1]);
                Complex y = Complex.of(b[0], b[1]);
                near(a[0] * b[0] - a[1] * b[1], a[0] * b[1] + a[1] * b[0], x.mul(y), TOL, "mul");
                double d = b[0] * b[0] + b[1] * b[1];
                near((a[0] * b[0] + a[1] * b[1]) / d, (a[1] * b[0] - a[0] * b[1]) / d, x.div(y), TOL, "div " + x + "/" + y);
            }
            Complex x = Complex.of(a[0], a[1]);
            double d = a[0] * a[0] + a[1] * a[1];
            near(a[0] / d, -a[1] / d, x.inv(), TOL, "inv " + x);
        }
    }

    // ---------------------------------------------------------------- branch cuts
    @Test
    public void sqrtIsPrincipalBranch() {
        for (double[] a : SAMPLES) {
            Complex z = Complex.of(a[0], a[1]);
            Complex s = z.sqrt();
            near(a[0], a[1], s.mul(s), 1e-12, "sqrt^2 of " + z);
            assertTrue(s.getReal() >= 0, "principal sqrt must have Re>=0: sqrt(" + z + ")=" + s);
            double r = Math.hypot(a[0], a[1]);
            double expRe = Math.sqrt((r + a[0]) / 2);
            double expIm = Math.copySign(Math.sqrt((r - a[0]) / 2), a[1]);
            near(expRe, expIm, s, 1e-12, "sqrt " + z);
        }
    }

    @Test
    public void expLog() {
        for (double[] a : SAMPLES) {
            Complex z = Complex.of(a[0], a[1]);
            double e = Math.exp(a[0]);
            near(e * Math.cos(a[1]), e * Math.sin(a[1]), z.exp(), 1e-13, "exp " + z);
            near(Math.log(Math.hypot(a[0], a[1])), Math.atan2(a[1], a[0]), z.log(), 1e-13, "log " + z);
            near(a[0], a[1], z.log().exp(), 1e-12, "exp(log z) " + z);
        }
    }

    /** Regression: ComplexR (pure real values) used real-only formulas that returned NaN or 0. */
    @Test
    public void negativeRealsUsePrincipalBranch() {
        Complex m4 = Complex.of(-4.0);
        assertEquals(Math.PI, m4.arg().getReal(), 0, "arg(-4)");
        near(Math.log(4), Math.PI, m4.log(), 1e-15, "log(-4)");
        near(2, Math.PI / Math.log(10), Complex.of(-100.0).log10(), 1e-15, "log10(-100)");
        near(0, 2, m4.pow(0.5), 1e-15, "(-4)^0.5");
        near(-64, 0, m4.pow(3.0), 1e-15, "(-4)^3");
        near(0, 2, m4.sqrt(), 0, "sqrt(-4)");
        // asin(2) = pi/2 - i*ln(2+sqrt3) on the principal branch (Im<=0 for x>1 approached from above)
        Complex as = Complex.of(2.0).asin();
        near(2, 0, as.sin(), 1e-12, "sin(asin 2)");
        Complex ac = Complex.of(-3.0).acos();
        near(-3, 0, ac.cos(), 1e-12, "cos(acos -3)");
    }

    // ---------------------------------------------------------------- trig / hyperbolic
    @Test
    public void trigAndHyperbolic() {
        for (double[] a : SAMPLES) {
            if (Math.abs(a[1]) > 20 || Math.abs(a[0]) > 20) {
                continue; // tested separately for overflow
            }
            Complex z = Complex.of(a[0], a[1]);
            double x = a[0], y = a[1];
            near(Math.sin(x) * Math.cosh(y), Math.cos(x) * Math.sinh(y), z.sin(), 1e-13, "sin " + z);
            near(Math.cos(x) * Math.cosh(y), -Math.sin(x) * Math.sinh(y), z.cos(), 1e-13, "cos " + z);
            near(Math.sinh(x) * Math.cos(y), Math.cosh(x) * Math.sin(y), z.sinh(), 1e-12, "sinh " + z);
            near(Math.cosh(x) * Math.cos(y), Math.sinh(x) * Math.sin(y), z.cosh(), 1e-12, "cosh " + z);
            // tanh(x+iy) = (sinh 2x + i sin 2y)/(cosh 2x + cos 2y)
            double den = Math.cosh(2 * x) + Math.cos(2 * y);
            near(Math.sinh(2 * x) / den, Math.sin(2 * y) / den, z.tanh(), 1e-12, "tanh " + z);
            Complex t = z.tanh();
            Complex ct = z.cotanh();
            near(1, 0, t.mul(ct), 1e-11, "tanh*cotanh " + z);
            near(1, 0, z.tan().mul(z.cotan()), 1e-11, "tan*cotan " + z);
        }
    }

    /**
     * The slab admittance in the box solver uses coth(gamma*h) of a large evanescent argument.
     * tanh/coth of a complex argument with a large real part must saturate to +-1, not overflow.
     */
    @Test
    public void tanhCothLargeArgumentsDoNotOverflow() {
        double[] reals = {50, 400, 800, -800};
        for (double x : reals) {
            for (double y : new double[]{0.3, -2.0}) {
                Complex z = Complex.of(x, y);
                double sgn = Math.signum(x);
                near(sgn, 0, z.tanh(), 1e-12, "tanh " + z);
                near(sgn, 0, z.cotanh(), 1e-12, "coth " + z);
            }
        }
    }

    @Test
    public void tanCotLargeImaginaryDoNotOverflow() {
        // tan(x+iy) -> i*sign(y) as |y| grows; used e.g. for lossy line formulas
        for (double y : new double[]{50, 400, -800}) {
            Complex z = Complex.of(0.4, y);
            near(0, Math.signum(y), z.tan(), 1e-12, "tan " + z);
        }
    }

    @Test
    public void powAndInverseTrig() {
        for (double[] a : SAMPLES) {
            if (Math.abs(a[1]) > 20 || Math.abs(a[0]) > 20) {
                continue;
            }
            Complex z = Complex.of(a[0], a[1]);
            // principal power: exp(p log z)
            for (double p : new double[]{0.5, 1.5, -0.5, 3.0, -2.0}) {
                Complex expd = Complex.of(p).mul(z.log()).exp();
                near(expd.getReal(), expd.getImag(), z.pow(p), 1e-11, "pow(" + z + "," + p + ")");
            }
            near(a[0], a[1], z.atan().tan(), 1e-10, "tan(atan z) " + z);
            near(a[0], a[1], z.asin().sin(), 1e-10, "sin(asin z) " + z);
            near(a[0], a[1], z.acos().cos(), 1e-10, "cos(acos z) " + z);
        }
    }

    // ---------------------------------------------------------------- Bessel (A&S tables, 10 digits)
    /**
     * Bessel.java uses the Numerical Recipes rational approximations: about 1e-8 absolute accuracy,
     * not machine precision. BES_TOL documents that limit; callers needing more must not use them.
     */
    private static final double BES_TOL = 5e-8;

    @Test
    public void besselTabulated() {
        assertEquals(0.7651976866, Bessel.j0(1.0), BES_TOL);
        assertEquals(-0.2459357645, Bessel.j0(10.0), BES_TOL);
        assertEquals(0.0, Bessel.j0(2.404825557695773), BES_TOL);
        assertEquals(0.4400505857, Bessel.j1(1.0), BES_TOL);
        assertEquals(0.0434727462, Bessel.j1(10.0), BES_TOL);
        assertEquals(0.1149034849, Bessel.jn(2, 1.0), BES_TOL);
        assertEquals(-0.2340615282, Bessel.jn(5, 10.0), BES_TOL);
        assertEquals(0.0882569642, Bessel.y0(1.0), BES_TOL);
        assertEquals(0.0556711673, Bessel.y0(10.0), BES_TOL);
        assertEquals(-0.7812128213, Bessel.y1(1.0), BES_TOL);
        assertEquals(0.2490154242, Bessel.y1(10.0), BES_TOL);
        assertEquals(0.4210244382, Bessel.k0(1.0), BES_TOL);
        assertEquals(1.266065878, Bessel.i0(1.0), BES_TOL);
    }

    @Test
    public void besselLargeArgumentAsymptotics() {
        // J0(x) ~ sqrt(2/(pi x)) cos(x - pi/4) - ..., check against the 2-term asymptotic form at x=200
        for (double x : new double[]{200.0, 1234.5}) {
            double p = 1 - 9.0 / (128 * x * x);
            double q = -1.0 / (8 * x);
            double chi = x - Math.PI / 4;
            double j0 = Math.sqrt(2 / (Math.PI * x)) * (p * Math.cos(chi) - q * Math.sin(chi));
            assertEquals(j0, Bessel.j0(x), BES_TOL, "J0(" + x + ")");
        }
        // Wronskian J1 Y0 - J0 Y1 = 2/(pi x)
        for (double x : new double[]{0.3, 2.0, 7.7, 25.0, 140.0}) {
            double w = Bessel.j1(x) * Bessel.y0(x) - Bessel.j0(x) * Bessel.y1(x);
            assertEquals(2 / (Math.PI * x), w, 1e-6 * (2 / (Math.PI * x)), "Wronskian at " + x);
        }
    }

    // ---------------------------------------------------------------- dense complex linear algebra
    private static ComplexMatrix randomMatrix(Random r, int n, int m, boolean diagDominant) {
        Complex[][] a = new Complex[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                a[i][j] = Complex.of(r.nextGaussian(), r.nextGaussian());
                if (diagDominant && i == j) {
                    a[i][j] = a[i][j].plus(Complex.of(0, 0));
                }
            }
        }
        return Maths.matrix(a);
    }

    private static double maxAbs(ComplexMatrix m) {
        double e = 0;
        for (int i = 0; i < m.getRowCount(); i++) {
            for (int j = 0; j < m.getColumnCount(); j++) {
                e = Math.max(e, m.get(i, j).absdbl());
            }
        }
        return e;
    }

    @Test
    public void solveAndInverseResiduals() {
        Random r = new Random(42);
        for (int n : new int[]{1, 3, 20, 64}) {
            ComplexMatrix a = randomMatrix(r, n, n, false);
            ComplexMatrix b = randomMatrix(r, n, 2, false);
            ComplexMatrix x = a.solve(b);
            double res = maxAbs(a.mul(x).sub(b));
            assertTrue(res < 1e-9 * Math.max(1, maxAbs(x)), "solve residual n=" + n + ": " + res);
            ComplexMatrix ai = a.inv();
            double resI = maxAbs(a.mul(ai).sub(Maths.identityMatrix(n)));
            assertTrue(resI < 1e-9 * Math.max(1, maxAbs(ai)), "inverse residual n=" + n + ": " + resI);
        }
    }

    @Test
    public void transposeHermitianAndProduct() {
        Random r = new Random(7);
        ComplexMatrix a = randomMatrix(r, 4, 3, false);
        ComplexMatrix ah = a.transposeHermitian();
        assertEquals(3, ah.getRowCount());
        assertEquals(4, ah.getColumnCount());
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                Complex v = a.get(i, j);
                near(v.getReal(), -v.getImag(), ah.get(j, i), 0, "conj transpose");
            }
        }
        // (A^H A) must be Hermitian with real non-negative diagonal
        ComplexMatrix g = ah.mul(a);
        for (int i = 0; i < 3; i++) {
            assertEquals(0, g.get(i, i).getImag(), 1e-12);
            assertTrue(g.get(i, i).getReal() > 0);
            for (int j = 0; j < 3; j++) {
                Complex gij = g.get(i, j);
                near(gij.getReal(), -gij.getImag(), g.get(j, i), 1e-12, "hermitian");
            }
        }
    }
}
