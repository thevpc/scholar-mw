package net.thevpc.scholar.hadruwaves.mom.mpie;

import net.thevpc.scholar.hadruwaves.mom.mpie.GroundedSlabGreen.Cx;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Every check compares against something computed independently of GroundedSlabGreen's own
 * integration: closed forms, series, image expansions or a separately solved dispersion equation.
 */
public class GroundedSlabGreenTest {

    private static void near(Cx exp, Cx got, double tol, String what) {
        double err = got.sub(exp).abs();
        assertTrue(err <= tol, what + ": expected " + exp + " got " + got + " |err|=" + err + " tol=" + tol);
    }

    // ------------------------------------------------------------------ special functions
    private static Cx j0Series(Cx z) {
        // sum_k (-z^2/4)^k / (k!)^2, fine for |z| <= 12 in double
        Cx q = z.mul(z).mul(-0.25);
        Cx term = Cx.of(1);
        Cx sum = Cx.of(1);
        for (int k = 1; k < 80; k++) {
            term = term.mul(q).mul(1.0 / ((double) k * k));
            sum = sum.add(term);
        }
        return sum;
    }

    @Test
    public void besselJ0J1Real() {
        // A&S 9.1 / high-precision references
        assertEquals(0.76519768655796655, GroundedSlabGreen.besselJ0(1.0), 1e-14);
        assertEquals(-0.24593576445134834, GroundedSlabGreen.besselJ0(10.0), 1e-14);
        assertEquals(0.019985850304223122, GroundedSlabGreen.besselJ0(100.0), 1e-14);
        assertEquals(0.0, GroundedSlabGreen.besselJ0(2.404825557695773), 1e-14);
        assertEquals(0.44005058574493352, GroundedSlabGreen.besselJ1(1.0), 1e-14);
        assertEquals(0.043472746168861438, GroundedSlabGreen.besselJ1(10.0), 1e-14);
        assertEquals(-0.077145352014112158, GroundedSlabGreen.besselJ1(100.0), 1e-14);
        assertEquals(-0.44005058574493352, GroundedSlabGreen.besselJ1(-1.0), 1e-14);
        // continuity across the trapezoid / asymptotic switch at 40 (40 itself is on the trapezoid side)
        assertEquals(GroundedSlabGreen.besselJ0(40), GroundedSlabGreen.besselJ0(40 + 1e-12), 5e-13);
        assertEquals(GroundedSlabGreen.besselJ1(40), GroundedSlabGreen.besselJ1(40 + 1e-12), 5e-13);
        assertEquals(0.0073668905842374, GroundedSlabGreen.besselJ0(40), 1e-15);
    }

    @Test
    public void besselJ0Complex() {
        // J0(i x) = I0(x)
        near(Cx.of(1.2660658777520082), GroundedSlabGreen.besselJ0(new Cx(0, 1)), 1e-14, "I0(1)");
        near(Cx.of(27.239871823604442), GroundedSlabGreen.besselJ0(new Cx(0, 5)), 1e-12, "I0(5)");
        for (double[] z : new double[][]{{3, 2}, {0.5, -0.7}, {8, 0.4}, {11, -1.5}, {1e-3, 1e-3}}) {
            Cx zz = new Cx(z[0], z[1]);
            near(j0Series(zz), GroundedSlabGreen.besselJ0(zz), 1e-11, "J0(" + zz + ")");
        }
        // large argument, small imaginary part eps: second-order Taylor around the real axis,
        // J0(x+i eps) = J0 - i eps J1 - eps^2/2 J0'',  J0'' = -J0 + J1/x
        double x = 300, e = 1e-3;
        double j0 = GroundedSlabGreen.besselJ0(x), j1 = GroundedSlabGreen.besselJ1(x);
        Cx z = new Cx(x, e);
        Cx expd = new Cx(j0 + 0.5 * e * e * (j0 - j1 / x), -e * j1);
        near(expd, GroundedSlabGreen.besselJ0(z), 1e-11, "J0(300+0.001j)");
    }

    @Test
    public void integralJ0AndTailS() {
        for (double s : new double[]{0.5, 3, 9}) {
            // series Int_0^s J0 = sum (-1)^k s^(2k+1) / ((2k+1) (k!)^2 4^k)
            double sum = 0, term = s;
            for (int k = 0; k < 60; k++) {
                sum += term / (2 * k + 1);
                term *= -s * s / (4.0 * (k + 1) * (k + 1));
            }
            assertEquals(sum, GroundedSlabGreen.integralJ0(s), 1e-13, "Int J0 to " + s);
        }
        // S(s) = Int_s^inf J0/t^2 by brute force: trapezoid to T (|rest| < sqrt(2/(pi T))/T^2 ~ 2e-13)
        // with Richardson extrapolation (trapezoid error ~ dt^2)
        for (double s : new double[]{0.5, 3, 30, 250}) {
            double T = 4000;
            double t1 = trapezoidJ0OverT2(s, T, 2_000_000), t2 = trapezoidJ0OverT2(s, T, 4_000_000);
            double acc = (4 * t2 - t1) / 3;
            assertEquals(acc, GroundedSlabGreen.tailS(s), 1e-10 + 1e-10 * Math.abs(acc), "S(" + s + ")");
        }
    }

    private static double trapezoidJ0OverT2(double s, double T, int n) {
        double dt = (T - s) / n, acc = 0;
        for (int i = 0; i <= n; i++) {
            double t = s + i * dt;
            double w = (i == 0 || i == n) ? 0.5 : 1;
            acc += w * GroundedSlabGreen.besselJ0(t) / (t * t);
        }
        return acc * dt;
    }

    @Test
    public void complexSqrtAndTanh() {
        Cx[] zs = {new Cx(1, 2), new Cx(-1, 2), new Cx(-1, -2), new Cx(-4, 0), new Cx(-4, -0.0), new Cx(3e-9, -5)};
        for (Cx z : zs) {
            Cx s = z.sqrt();
            assertTrue(s.re >= 0, "Re sqrt >= 0 for " + z);
            near(z, s.mul(s), 1e-12 * Math.max(1, z.abs()), "sqrt^2 " + z);
        }
        near(Cx.of(1), new Cx(800, 0.3).tanh(), 1e-15, "tanh large");
        near(Cx.of(-1), new Cx(-800, 0.3).tanh(), 1e-15, "tanh large neg");
        Cx z = new Cx(0.4, 1.1);
        double den = Math.cosh(0.8) + Math.cos(2.2);
        near(new Cx(Math.sinh(0.8) / den, Math.sin(2.2) / den), z.tanh(), 1e-15, "tanh closed form");
    }

    // ------------------------------------------------------------------ physics checks
    /** epsR = 1: the slab is air, so G = free space + negative image at depth 2h, in closed form. */
    @Test
    public void airSlabEqualsFreeSpacePlusImage() {
        double f = 7e9;
        for (double h : new double[]{1.6e-3, 3.2e-3}) {
            GroundedSlabGreen g = new GroundedSlabGreen(f, 1.0, 0.0, h);
            double k = g.getK0();
            for (double rho : new double[]{0, 1e-4, 1e-3, 5e-3, 2e-2, 8e-2}) {
                double ri = Math.sqrt(rho * rho + 4 * h * h);
                Cx img = new Cx(Math.cos(k * ri), -Math.sin(k * ri)).mul(1 / ri);
                Cx direct = rho == 0 ? new Cx(0, -k)
                        : new Cx(Math.cos(k * rho) - 1, -Math.sin(k * rho)).mul(1 / rho);
                Cx expd = direct.sub(img);
                double tol = 1e-6 * Math.max(k, expd.abs());
                near(expd, g.remA(rho), tol, "remA air h=" + h + " rho=" + rho);
                // epsAvg = 1 here, so remV has the same closed form
                near(expd, g.remV(rho), tol, "remV air h=" + h + " rho=" + rho);
            }
        }
    }

    /** f -&gt; 0: G_V is the electrostatic image series of a charge on a grounded slab; G_A the PEC image. */
    @Test
    public void staticLimitEqualsImageSeries() {
        double er = 4.4, h = 1.6e-3;
        GroundedSlabGreen g = new GroundedSlabGreen(1e5, er, 0.0, h);
        double eta = (er - 1) / (er + 1);
        for (double rho : new double[]{1e-4, 1e-3, 4e-3, 2e-2}) {
            // remV = sum_n (-eta)^n (1/R_n - 1/R_{n+1}) - 1/rho, R_n = sqrt(rho^2 + (2nh)^2)
            double s = 0;
            for (int n = 0; n < 400; n++) {
                double rn = Math.hypot(rho, 2 * n * h), rn1 = Math.hypot(rho, 2 * (n + 1) * h);
                s += Math.pow(-eta, n) * (1 / rn - 1 / rn1);
            }
            double expV = s - 1 / rho;
            assertEquals(expV, g.remV(rho).re, 1e-6 * Math.max(1 / h, Math.abs(expV)), "static remV rho=" + rho);
            assertEquals(0, g.remV(rho).im, 1e-3, "static remV imag");
            double expA = -1 / Math.hypot(rho, 2 * h);
            assertEquals(expA, g.remA(rho).re, 1e-6 / h, "static remA rho=" + rho);
        }
    }

    /** The TM0 pole of D_TM must satisfy the textbook dispersion relation eps alpha0 = k1z tan(k1z h). */
    @Test
    public void tm0PoleMatchesDispersionRelation() {
        double[][] cases = {{4.4, 3.2e-3, 7.7e9}, {4.4, 1.6e-3, 6.9e9}, {2.2, 0.8e-3, 10e9}};
        for (double[] c : cases) {
            double er = c[0], h = c[1], f = c[2];
            GroundedSlabGreen g = new GroundedSlabGreen(f, er, 0.0, h);
            double k0 = g.getK0();
            // independent bisection on F(beta) = er*sqrt(beta^2-k0^2) - k1z tan(k1z h)
            double lo = k0 * (1 + 1e-12), hi = Math.min(Math.sqrt(er) * k0 * (1 - 1e-12), k0 * Math.sqrt(er));
            for (int it = 0; it < 200; it++) {
                double m = 0.5 * (lo + hi);
                double k1z = Math.sqrt(er * k0 * k0 - m * m);
                double F = er * Math.sqrt(m * m - k0 * k0) - k1z * Math.tan(k1z * h);
                double k1zl = Math.sqrt(er * k0 * k0 - lo * lo);
                double Fl = er * Math.sqrt(lo * lo - k0 * k0) - k1zl * Math.tan(k1zl * h);
                if (Fl * F <= 0) {
                    hi = m;
                } else {
                    lo = m;
                }
            }
            double beta = 0.5 * (lo + hi);
            Cx p = g.tm0Pole();
            assertEquals(beta, p.re, 1e-9 * k0, "TM0 pole er=" + er + " h=" + h);
            assertEquals(0, p.im, 1e-9 * k0);
            assertTrue(g.dtm(p).abs() < 1e-8 * k0, "D_TM(pole) ~ 0");
        }
        // the value used in the box-mode analysis of the article deck
        GroundedSlabGreen g = new GroundedSlabGreen(7.7e9, 4.4, 0, 3.2e-3);
        assertEquals(1.1275, g.tm0Pole().re / g.getK0(), 1e-4);
    }

    /** Cauchy: the result must not depend on the deformed path or on where the numerical tail stops. */
    @Test
    public void independentOfPathAndTailCut() {
        double h = 1.6e-3;
        GroundedSlabGreen g = new GroundedSlabGreen(6.9e9, 4.4, 0.02, h);
        double a = 0.5 * (Math.sqrt(4.4) + 1) * g.getK0() * 1.05;
        for (double rho : new double[]{0, 2e-3, 3e-2}) {
            for (boolean isA : new boolean[]{true, false}) {
                double b = rho > 0 ? Math.min(0.25 * a, 1 / rho) : 0.25 * a;
                Cx ref = g.remainder(rho, isA, a, b, 80 / h);
                Cx p1 = g.remainder(rho, isA, 1.3 * a, 0.5 * b, 80 / h);
                Cx p2 = g.remainder(rho, isA, a, 0.6 * b, 160 / h);
                double tol = 1e-7 * Math.max(ref.abs(), g.getK0());
                near(ref, p1, tol, (isA ? "A" : "V") + " other ellipse rho=" + rho);
                near(ref, p2, tol, (isA ? "A" : "V") + " longer tail rho=" + rho);
            }
        }
    }

    /** Far away on the interface the TM0 surface wave dominates G_V: phase advances at beta, |G| ~ rho^-1/2. */
    @Test
    public void farFieldIsTheSurfaceWave() {
        GroundedSlabGreen g = new GroundedSlabGreen(7.7e9, 4.4, 0.0, 3.2e-3);
        double beta = g.tm0Pole().re;
        double lam = 2 * Math.PI / beta;
        double r1 = 25 * lam, r2 = r1 + lam / 8;
        Cx v1 = g.fullV(r1), v2 = g.fullV(r2);
        double dphi = v2.arg() - v1.arg();
        dphi = Math.atan2(Math.sin(dphi), Math.cos(dphi));
        assertEquals(-beta * (r2 - r1), dphi, 0.02 * beta * (r2 - r1), "phase slope = -beta");
        Cx v3 = g.fullV(4 * r1);
        assertEquals(0.5, v3.abs() / v1.abs(), 0.05, "|G_V| ~ rho^-1/2");
    }

    /** The tabulated kernel used by the MPIE must reproduce the direct integral between grid points. */
    @Test
    public void tableInterpolation() {
        GroundedSlabGreen g = new GroundedSlabGreen(6.9e9, 4.4, 0.02, 1.6e-3);
        GroundedSlabGreen.Table t = g.tabulate(0.06);
        Random r = new Random(3);
        double scale = g.getK0() * Math.sqrt(4.4);
        for (int i = 0; i < 25; i++) {
            double rho = (i < 5 ? 1e-3 * r.nextDouble() : 0.06 * r.nextDouble());
            near(g.remA(rho), t.remA(rho), 2e-4 * scale, "table A at " + rho);
            near(g.remV(rho), t.remV(rho), 2e-4 * scale, "table V at " + rho);
        }
        assertThrows(IllegalArgumentException.class, () -> t.remA(0.07));
    }
}
