package net.thevpc.scholar.hadruwaves.mom.mpie;

/**
 * Mixed-potential Green's functions of a horizontal electric dipole lying on top of a grounded
 * dielectric slab (thickness h, relative permittivity epsR(1-j tanD)), observer on the same plane,
 * air half-space above. Time convention exp(+j w t).
 *
 * <p>Spectral forms (Mosig, "formulation C", source and observer at z = z' = 0+):</p>
 * <pre>
 *   u0 = sqrt(krho^2 - k0^2),  u1 = sqrt(krho^2 - epsR k0^2)            (Re u &gt;= 0)
 *   D_TE = u0 + u1 coth(u1 h),  D_TM = epsR u0 + u1 tanh(u1 h)
 *   ~G_A = mu0 / D_TE
 *   ~G_V = (u0 + u1 tanh(u1 h)) / (eps0 D_TE D_TM)
 *   G(rho) = 1/(2 pi) Int_0^inf J0(krho rho) krho ~G(krho) dkrho
 * </pre>
 *
 * <p>The quasi-static singular parts mu0/(4 pi rho) and 1/(4 pi eps0 epsAvg rho), epsAvg = (epsR+1)/2,
 * are subtracted in the spectral domain; this class returns the smooth remainders in "g units",
 * i.e. normalized so that</p>
 * <pre>
 *   G_A(rho) = mu0/(4 pi)               * (1/rho + remA(rho))
 *   G_V(rho) = 1/(4 pi eps0 epsAvg)     * (1/rho + remV(rho))
 * </pre>
 * <p>which is the form the MPIE triangle integrals use (1/R analytically via Wilton, the rest by
 * quadrature).</p>
 *
 * <p>Integration: semi-ellipse krho = a(1-cos t) + j b sin t, t in [0, pi], 2a = (sqrt(epsR)+1) k0,
 * which passes above the surface-wave poles and the branch points (correct for exp(+jwt) with
 * losses); then the real axis from 2a to K1 = 80/h by Gauss-Legendre panels; then the analytic tail
 * of the leading 1/krho^2 asymptote.</p>
 */
public final class GroundedSlabGreen {

    public static final double C0 = 299792458.0;
    public static final double MU0 = 4e-7 * Math.PI;
    public static final double EPS0 = 1.0 / (MU0 * C0 * C0);

    // ------------------------------------------------------------------ minimal complex type
    /** Immutable complex number; kept local so the kernel does not depend on hadrumaths' Complex. */
    public static final class Cx {
        public final double re;
        public final double im;

        public Cx(double re, double im) {
            this.re = re;
            this.im = im;
        }

        public static Cx of(double re) {
            return new Cx(re, 0);
        }

        public Cx add(Cx o) {
            return new Cx(re + o.re, im + o.im);
        }

        public Cx sub(Cx o) {
            return new Cx(re - o.re, im - o.im);
        }

        public Cx mul(Cx o) {
            return new Cx(re * o.re - im * o.im, re * o.im + im * o.re);
        }

        public Cx mul(double d) {
            return new Cx(re * d, im * d);
        }

        public Cx div(Cx o) {
            // Smith's algorithm, avoids overflow
            if (Math.abs(o.re) >= Math.abs(o.im)) {
                double r = o.im / o.re;
                double d = o.re + o.im * r;
                return new Cx((re + im * r) / d, (im - re * r) / d);
            } else {
                double r = o.re / o.im;
                double d = o.re * r + o.im;
                return new Cx((re * r + im) / d, (im * r - re) / d);
            }
        }

        public Cx inv() {
            return of(1).div(this);
        }

        public double abs() {
            return Math.hypot(re, im);
        }

        public double arg() {
            return Math.atan2(im, re);
        }

        /** principal square root, Re &gt;= 0. */
        public Cx sqrt() {
            double r = abs();
            if (r == 0) {
                return new Cx(0, 0);
            }
            double sr = Math.sqrt((r + Math.abs(re)) / 2);
            if (re >= 0) {
                return new Cx(sr, im / (2 * sr));
            }
            return new Cx(Math.abs(im) / (2 * sr), Math.copySign(sr, im));
        }

        public Cx exp() {
            double e = Math.exp(re);
            return new Cx(e * Math.cos(im), e * Math.sin(im));
        }

        /** tanh without overflow: (1-e)/(1+e), e = exp(-2z) for Re z &gt;= 0; odd otherwise. */
        public Cx tanh() {
            if (re < 0) {
                return new Cx(-re, -im).tanh().mul(-1);
            }
            Cx e = new Cx(-2 * re, -2 * im).exp();
            return of(1).sub(e).div(of(1).add(e));
        }

        @Override
        public String toString() {
            return re + (im >= 0 ? "+" : "") + im + "j";
        }
    }

    // ------------------------------------------------------------------ problem data
    private final double freq;
    private final double h;
    private final double k0;
    private final Cx epsR;      // complex relative permittivity epsR(1 - j tanD)
    private final Cx k0sq;
    private final Cx k1sq;
    private final Cx epsAvg;    // (epsR + 1)/2
    private final double ellipseA;
    private final double k1Max; // end of the numerical real-axis tail
    // leading asymptotes krho*(~G - ~Gqs) ~ c/krho^2, in g units
    private final Cx cA;
    private final Cx cV;

    public GroundedSlabGreen(double freq, double epsR, double tanD, double h) {
        if (!(freq > 0) || !(h > 0) || !(epsR >= 1)) {
            throw new IllegalArgumentException("invalid slab: f=" + freq + " h=" + h + " epsR=" + epsR);
        }
        this.freq = freq;
        this.h = h;
        this.k0 = 2 * Math.PI * freq / C0;
        this.epsR = new Cx(epsR, -epsR * tanD);
        this.k0sq = Cx.of(k0 * k0);
        this.k1sq = this.epsR.mul(k0 * k0);
        this.epsAvg = this.epsR.add(Cx.of(1)).mul(0.5);
        this.ellipseA = 0.5 * (Math.sqrt(epsR) + 1) * k0 * 1.05;
        // 80/h: the next asymptotic term c4/(3K^3) is then ~1e-8 relative (measured, see tests)
        this.k1Max = Math.max(80.0 / h, 4 * ellipseA);
        // A: krho(~GA - mu0/(2krho)) ~ mu0 (k0^2+k1^2)/(8 krho^2)  -> g units: * 4pi/mu0 * 1/(2pi) = *2/mu0
        //    so contribution to remA = (1/2pi)(4pi/mu0) Int J0 krho (..)  = (2/mu0) Int J0 (..)
        this.cA = k0sq.add(k1sq).mul(1.0 / 8.0); // already divided by mu0
        // V: krho(~GV - qs) ~ epsR k0^2 / (eps0 (epsR+1)^2 krho^2)
        Cx ep1 = this.epsR.add(Cx.of(1));
        this.cV = this.epsR.mul(k0 * k0).div(ep1.mul(ep1)); // already divided by 1/eps0
    }

    public double getFrequency() {
        return freq;
    }

    public double getH() {
        return h;
    }

    public double getK0() {
        return k0;
    }

    public Cx getEpsR() {
        return epsR;
    }

    /** (epsR+1)/2: the permittivity that multiplies the singular 1/R scalar-potential term. */
    public Cx getEpsAvg() {
        return epsAvg;
    }

    // ------------------------------------------------------------------ spectral functions
    /** ~G_A(krho) / mu0. */
    public Cx spectralA(Cx kr) {
        Cx kr2 = kr.mul(kr);
        Cx u0 = kr2.sub(k0sq).sqrt();
        Cx u1 = kr2.sub(k1sq).sqrt();
        Cx th = u1.mul(h).tanh();
        Cx dte = u0.add(u1.div(th));
        return dte.inv();
    }

    /** ~G_V(krho) * eps0. */
    public Cx spectralV(Cx kr) {
        Cx kr2 = kr.mul(kr);
        Cx u0 = kr2.sub(k0sq).sqrt();
        Cx u1 = kr2.sub(k1sq).sqrt();
        Cx th = u1.mul(h).tanh();
        Cx u1th = u1.mul(th);
        Cx dte = u0.add(u1.div(th));
        Cx dtm = epsR.mul(u0).add(u1th);
        return u0.add(u1th).div(dte.mul(dtm));
    }

    /** D_TM(krho); its zero on the proper sheet is the TM0 surface-wave pole. */
    public Cx dtm(Cx kr) {
        Cx kr2 = kr.mul(kr);
        Cx u0 = kr2.sub(k0sq).sqrt();
        Cx u1 = kr2.sub(k1sq).sqrt();
        return epsR.mul(u0).add(u1.mul(u1.mul(h).tanh()));
    }

    /** TM0 surface-wave pole by Newton iteration on D_TM, started between k0 and sqrt(epsR) k0. */
    public Cx tm0Pole() {
        Cx k = Cx.of(k0 * (1 + 0.5 * (Math.sqrt(epsR.re) - 1)));
        for (int it = 0; it < 100; it++) {
            Cx f = dtm(k);
            double d = 1e-7 * k0;
            Cx fp = dtm(k.add(Cx.of(d))).sub(dtm(k.sub(Cx.of(d)))).mul(1 / (2 * d));
            Cx step = f.div(fp);
            k = k.sub(step);
            if (step.abs() < 1e-13 * k0) {
                break;
            }
        }
        return k;
    }

    // ------------------------------------------------------------------ space-domain remainders
    /** smooth remainder of G_A in g units (see class doc). */
    public Cx remA(double rho) {
        return remainder(rho, true);
    }

    /** smooth remainder of G_V in g units (see class doc). */
    public Cx remV(double rho) {
        return remainder(rho, false);
    }

    /** G_A(rho) in H/m (for tests and diagnostics). */
    public Cx fullA(double rho) {
        return remA(rho).add(Cx.of(1 / rho)).mul(MU0 / (4 * Math.PI));
    }

    /** G_V(rho) in 1/(F/m) (for tests and diagnostics). */
    public Cx fullV(double rho) {
        return remV(rho).add(Cx.of(1 / rho)).div(epsAvg.mul(4 * Math.PI * EPS0));
    }

    /** integrand krho (~G - ~Gqs) in the normalized units (A: /mu0, V: *eps0). */
    private Cx reducedIntegrand(Cx kr, boolean a) {
        if (a) {
            // ~GA/mu0 - 1/(2 krho)
            return kr.mul(spectralA(kr)).sub(Cx.of(0.5));
        } else {
            // eps0 ~GV - 1/((epsR+1) krho)
            return kr.mul(spectralV(kr)).sub(epsR.add(Cx.of(1)).inv());
        }
    }

    private Cx remainder(double rho, boolean a) {
        return remainder(rho, a, ellipseA, ellipseB(rho), k1Max);
    }

    private double ellipseB(double rho) {
        // keep |Im krho| * rho <= 1 so J0 does not grow, but stay well clear of the real-axis poles
        double b = 0.25 * ellipseA;
        if (rho > 0) {
            b = Math.min(b, 1.0 / rho);
        }
        return Math.max(b, 0.02 * ellipseA);
    }

    /**
     * Exposed for tests: the same remainder with an explicit path (ellipse semi-axes a, b) and an
     * explicit end of the numerical tail.
     */
    Cx remainder(double rho, boolean a, double ea, double eb, double kEnd) {
        // 1) semi-ellipse, composite Gauss-Legendre in t
        int panels = (int) Math.ceil(Math.max(48, 6 * Math.PI * ea / eb));
        Cx sum = Cx.of(0);
        double dt = Math.PI / panels;
        for (int p = 0; p < panels; p++) {
            double t0 = p * dt;
            for (int g = 0; g < GL_X.length; g++) {
                double t = t0 + 0.5 * dt * (GL_X[g] + 1);
                double w = 0.5 * dt * GL_W[g];
                Cx kr = new Cx(ea * (1 - Math.cos(t)), eb * Math.sin(t));
                Cx dk = new Cx(ea * Math.sin(t), eb * Math.cos(t));
                Cx j0 = besselJ0(kr.mul(rho));
                sum = sum.add(j0.mul(reducedIntegrand(kr, a)).mul(dk).mul(w));
            }
        }
        // 2) real axis from 2a to kEnd
        double start = 2 * ea;
        double width = 0.5 / h;
        if (rho > 0) {
            width = Math.min(width, Math.PI / (2 * rho));
        }
        int n = (int) Math.ceil((kEnd - start) / width);
        width = (kEnd - start) / n;
        for (int p = 0; p < n; p++) {
            double x0 = start + p * width;
            for (int g = 0; g < GL_X.length; g++) {
                double x = x0 + 0.5 * width * (GL_X[g] + 1);
                double w = 0.5 * width * GL_W[g];
                sum = sum.add(reducedIntegrand(Cx.of(x), a).mul(besselJ0(x * rho) * w));
            }
        }
        // 3) analytic tail of c/krho^2: Int_K^inf J0(x rho)/x^2 dx = rho * S(K rho)  (or 1/K at rho=0)
        double tailInt = rho == 0 ? 1.0 / kEnd : rho * tailS(kEnd * rho);
        sum = sum.add((a ? cA : cV).mul(tailInt));
        // g units: remA = (1/2pi)(4pi/mu0) Int J0 krho(~GA-qs) = 2 * Int J0 [krho(~GA/mu0 - ..)]
        //          remV = (1/2pi)(4pi eps0 epsAvg) Int J0 krho(~GV-qs) = 2 epsAvg * Int J0 [eps0 ...]
        return a ? sum.mul(2) : sum.mul(epsAvg).mul(2);
    }

    // ------------------------------------------------------------------ tabulation
    /** Tabulated remainders on a uniform grid [0, rhoMax], cubic (Catmull-Rom) interpolation. */
    public Table tabulate(double rhoMax) {
        double lambdaMin = 2 * Math.PI / (k0 * Math.sqrt(epsR.re));
        double step = Math.min(0.25 * h, lambdaMin / 40);
        int n = Math.max(8, (int) Math.ceil(rhoMax / step) + 3);
        final double dr = rhoMax / (n - 3);
        Cx[] ta = new Cx[n];
        Cx[] tv = new Cx[n];
        java.util.stream.IntStream.range(0, n).parallel().forEach(i -> {
            double r = i * dr;
            ta[i] = remA(r);
            tv[i] = remV(r);
        });
        return new Table(dr, ta, tv, epsAvg);
    }

    public static final class Table {
        private final double step;
        private final double[] ar, ai, vr, vi;
        private final Cx epsAvg;

        Table(double step, Cx[] a, Cx[] v, Cx epsAvg) {
            this.step = step;
            this.epsAvg = epsAvg;
            ar = new double[a.length];
            ai = new double[a.length];
            vr = new double[a.length];
            vi = new double[a.length];
            for (int i = 0; i < a.length; i++) {
                ar[i] = a[i].re;
                ai[i] = a[i].im;
                vr[i] = v[i].re;
                vi[i] = v[i].im;
            }
        }

        public Cx getEpsAvg() {
            return epsAvg;
        }

        public double maxRho() {
            return step * (ar.length - 3);
        }

        public Cx remA(double rho) {
            return new Cx(interp(ar, rho), interp(ai, rho));
        }

        public Cx remV(double rho) {
            return new Cx(interp(vr, rho), interp(vi, rho));
        }

        private double interp(double[] y, double rho) {
            double s = rho / step;
            int i = (int) Math.floor(s);
            if (i < 0) {
                i = 0;
            }
            if (i > y.length - 3) {
                throw new IllegalArgumentException("rho=" + rho + " beyond the tabulated range " + maxRho());
            }
            double t = s - i;
            double y0 = y[i], y1 = y[i + 1], y2 = y[i + 2];
            // left end: the remainders are functions of |rho| with a nonzero slope at 0
            // (free space: -jk - k^2 rho/2 ...), so extrapolate quadratically instead of mirroring
            double ym = i == 0 ? 3 * y[0] - 3 * y[1] + y[2] : y[i - 1];
            // Catmull-Rom cubic
            return y0 + 0.5 * t * (y1 - ym + t * (2 * ym - 5 * y0 + 4 * y1 - y2 + t * (3 * (y0 - y1) + y2 - ym)));
        }
    }

    // ------------------------------------------------------------------ special functions
    /**
     * J0 of a complex argument from J0(z) = (1/pi) Int_0^pi cos(z cos th) d th, trapezoidal rule
     * (spectrally accurate for this periodic analytic integrand once N &gt; |z| + 20).
     */
    public static Cx besselJ0(Cx z) {
        if (z.im == 0) {
            return Cx.of(besselJ0(z.re));
        }
        int n = (int) Math.ceil(z.abs() + 4 * Math.abs(z.im)) + 24;
        double sr = 0, si = 0;
        for (int k = 0; k <= n; k++) {
            double c = Math.cos(Math.PI * k / n);
            double x = z.re * c, y = z.im * c;
            // cos(x + j y) = cos x cosh y - j sin x sinh y
            double wr = Math.cos(x) * Math.cosh(y);
            double wi = -Math.sin(x) * Math.sinh(y);
            double w = (k == 0 || k == n) ? 0.5 : 1.0;
            sr += w * wr;
            si += w * wi;
        }
        return new Cx(sr / n, si / n);
    }

    /** J0 of a real argument: trapezoidal integral up to |x| = 40, Hankel asymptotic beyond. */
    public static double besselJ0(double x) {
        double ax = Math.abs(x);
        if (ax <= 40) {
            int n = (int) Math.ceil(ax) + 24;
            double s = 0;
            for (int k = 0; k <= n; k++) {
                double w = (k == 0 || k == n) ? 0.5 : 1.0;
                s += w * Math.cos(ax * Math.cos(Math.PI * k / n));
            }
            return s / n;
        }
        return hankelAsym(ax, 0);
    }

    /** J1 of a real argument, same scheme: J1(x) = (1/pi) Int_0^pi cos(th - x sin th) d th. */
    public static double besselJ1(double x) {
        double ax = Math.abs(x);
        double sign = Math.signum(x);
        if (ax <= 40) {
            int n = 2 * ((int) Math.ceil(ax) + 24);
            // integrand is not even-periodic on [0,pi]; use the 2pi-periodic form over [0, 2pi]
            double s = 0;
            for (int k = 0; k < n; k++) {
                double th = 2 * Math.PI * k / n;
                s += Math.cos(th - ax * Math.sin(th));
            }
            return sign * s / n;
        }
        return sign * hankelAsym(ax, 1);
    }

    /** Hankel asymptotic expansion of J_nu, nu in {0,1}, accurate to ~1e-15 for x &gt; 40. */
    private static double hankelAsym(double x, int nu) {
        double mu = 4.0 * nu * nu;
        double p = 1, q = 0;
        double term = 1;
        double z8 = 8 * x;
        // P = sum (-1)^k a_{2k}/x^{2k}, Q = sum (-1)^k a_{2k+1}/x^{2k+1}, a_k = prod (mu-(2j-1)^2)/(k! 8^k)
        double[] a = new double[16];
        a[0] = 1;
        for (int k = 1; k < 16; k++) {
            a[k] = a[k - 1] * (mu - (2.0 * k - 1) * (2.0 * k - 1)) / (k * z8);
        }
        p = 0;
        q = 0;
        for (int k = 0; k < 16; k++) {
            if (k % 2 == 0) {
                p += ((k / 2) % 2 == 0 ? 1 : -1) * a[k];
            } else {
                q += (((k - 1) / 2) % 2 == 0 ? 1 : -1) * a[k];
            }
        }
        double chi = x - (0.5 * nu + 0.25) * Math.PI;
        return Math.sqrt(2 / (Math.PI * x)) * (p * Math.cos(chi) - q * Math.sin(chi));
    }

    /** Int_0^s J0(t) dt, composite Gauss-Legendre (panels of width &lt;= 1). */
    public static double integralJ0(double s) {
        if (s == 0) {
            return 0;
        }
        int n = (int) Math.ceil(s);
        double w = s / n;
        double sum = 0;
        for (int p = 0; p < n; p++) {
            double x0 = p * w;
            for (int g = 0; g < GL_X.length; g++) {
                sum += 0.5 * w * GL_W[g] * besselJ0(x0 + 0.5 * w * (GL_X[g] + 1));
            }
        }
        return sum;
    }

    /**
     * S(s) = Int_s^inf J0(t)/t^2 dt = J0(s)/s - 1 + Int_0^s J0(t) dt - J1(s)
     * (parts, then J1/t = J0 - J1' and Int_0^inf J1/t = 1).
     */
    public static double tailS(double s) {
        return besselJ0(s) / s - 1 + integralJ0(s) - besselJ1(s);
    }

    // 8-point Gauss-Legendre on [-1,1]
    static final double[] GL_X = {
            -0.9602898564975363, -0.7966664774136267, -0.5255324099163290, -0.1834346424956498,
            0.1834346424956498, 0.5255324099163290, 0.7966664774136267, 0.9602898564975363};
    static final double[] GL_W = {
            0.1012285362903763, 0.2223810344533745, 0.3137066458778873, 0.3626837833783620,
            0.3626837833783620, 0.3137066458778873, 0.2223810344533745, 0.1012285362903763};
}
