package net.thevpc.ntexup.extension.antennas.phys;

import java.util.LinkedHashMap;
import java.util.Map;

import net.thevpc.nuts.math.NDoubleComplex;

/**
 * Coupled thin-dipole model of a Yagi-Uda array (all elements parallel, centred on a common boom).
 * <p>
 * Method: induced-EMF with sinusoidal currents I(z) = Im sin(k(h - |z|)) on every element.
 * Self and mutual impedances (referred to the feed currents) come from the closed-form field of a sinusoidal
 * dipole integrated numerically along the other element; the parasitic elements are short-circuited at their
 * centre, the driven one gets V = 1, so [Z] I = [0 .. 1 .. 0]. The far field is then
 * E(psi) = sum_i I_i g_i(theta) exp(j k x_i cos psi).
 * <p>
 * Validated against textbook values: Z11 = 73.1 + j42.2 (half-wave, thin), Z12(d = 0.5 lambda) = -12.5 - j29.9,
 * Z12(0.25) = 40.8 - j28.3. All lengths and distances are in wavelengths.
 */
public final class YagiSolver {
    private YagiSolver() {
    }

    public static final class Result {
        public int n, drv;
        public double[] len, pos, g90;
        public NDoubleComplex[] cur;                // feed currents, normalised so that cur[drv] = 1
        public NDoubleComplex zin;                  // input impedance of the driven element (ohm)
        public double directivity;     // linear
        public double frontToBackDb;   // H plane, psi = 0 versus psi = pi
        public double hpbwDeg;         // H plane
        public double fieldPeak;       // max over psi of |E| in the H plane (normalised currents), for the wave map
        public double[] patH, patE;    // |E| in the H / E plane versus psi (0..2pi, 361 samples), 1.0 = 3-D maximum

        /**
         * Normalised pattern value at the angle psi (counter-clockwise from the boom direction).
         */
        public double pat(double psi, boolean ePlane) {
            double[] a = ePlane ? patE : patH;
            double x = ((psi % (2 * Math.PI)) + 2 * Math.PI) % (2 * Math.PI) / (2 * Math.PI) * (a.length - 1);
            int i = (int) Math.floor(x);
            if (i >= a.length - 1) return a[a.length - 1];
            double t = x - i;
            return a[i] * (1 - t) + a[i + 1] * t;
        }
    }

    private static final Map<String, Result> CACHE = new LinkedHashMap<String, Result>(32, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Result> e) {
            return size() > 48;
        }
    };

    public static synchronized Result solve(double[] len, double[] pos, int drv, double radius) {
        StringBuilder key = new StringBuilder();
        key.append(drv).append('|').append(radius);
        for (double v : len) key.append('|').append(v);
        key.append('#');
        for (double v : pos) key.append('|').append(v);
        Result cached = CACHE.get(key.toString());
        if (cached != null) return cached;
        Result r = compute(len, pos, drv, radius);
        CACHE.put(key.toString(), r);
        return r;
    }

    // ------------------------------------------------------------------ solver

    private static Result compute(double[] len, double[] pos, int drv, double a) {
        int n = len.length;
        Result r = new Result();
        r.n = n;
        r.drv = drv;
        r.len = len.clone();
        r.pos = pos.clone();
        double[] h = new double[n];
        for (int i = 0; i < n; i++) h[i] = len[i] / 2;

        NDoubleComplex[][] z = new NDoubleComplex[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                z[i][j] = zmut(h[i], h[j], i == j ? a : Math.abs(pos[i] - pos[j]));
        NDoubleComplex[] v = new NDoubleComplex[n];
        for (int i = 0; i < n; i++) v[i] = NDoubleComplex.of(i == drv ? 1 : 0, 0);
        NDoubleComplex[] cur = solveLinear(z, v);
        r.zin = NDoubleComplex.of(1, 0).divideDoubleComplex(cur[drv]);
        NDoubleComplex ref = cur[drv];
        for (int i = 0; i < n; i++) cur[i] = cur[i].divideDoubleComplex(ref);
        r.cur = cur;

        r.g90 = new double[n];
        for (int i = 0; i < n; i++) r.g90[i] = elemFactor(h[i], 0.0);

        // 3-D directivity on a (theta, phi) grid
        int nt = 90, np = 72;
        double umax = 0, integral = 0;
        for (int it = 0; it <= nt; it++) {
            double th = it * Math.PI / nt, sth = Math.sin(th), cth = Math.cos(th);
            double rowSum = 0;
            for (int ip = 0; ip < np; ip++) {
                double ph = 2 * Math.PI * ip / np;
                NDoubleComplex e = NDoubleComplex.of(0, 0);
                for (int i = 0; i < n; i++) {
                    double g = elemFactor(h[i], cth);
                    e = e.addDoubleComplex(cur[i].multiplyDoubleComplex(NDoubleComplex.ofPolar(1, 2 * Math.PI * pos[i] * sth * Math.cos(ph))).multiplyDouble(g));
                }
                double u = e.realValue() * e.realValue() + e.imagValue() * e.imagValue();
                umax = Math.max(umax, u);
                rowSum += u;
            }
            double wgt = (it == 0 || it == nt) ? 0.5 : 1.0;
            integral += wgt * rowSum * sth * (Math.PI / nt) * (2 * Math.PI / np);
        }
        r.directivity = integral > 0 ? 4 * Math.PI * umax / integral : 1;

        // pattern cuts
        int ns = 360;
        double[] hArr = new double[ns + 1], eArr = new double[ns + 1];
        double peak = 0;
        for (int s = 0; s <= ns; s++) {
            double psi = 2 * Math.PI * s / ns;
            hArr[s] = cut(cur, pos, h, psi, false).absDouble();
            eArr[s] = cut(cur, pos, h, psi, true).absDouble();
            peak = Math.max(peak, hArr[s]);
        }
        r.fieldPeak = peak;
        double norm = Math.max(Math.sqrt(umax), 1e-12);
        r.patH = new double[ns + 1];
        r.patE = new double[ns + 1];
        for (int s = 0; s <= ns; s++) {
            r.patH[s] = hArr[s] / norm;
            r.patE[s] = eArr[s] / norm;
        }
        r.frontToBackDb = 20 * Math.log10(Math.max(hArr[0], 1e-9) / Math.max(hArr[ns / 2], 1e-9));

        // half-power beamwidth of the H-plane main lobe
        int m = 0;
        for (int s = 1; s < ns; s++) if (hArr[s] > hArr[m]) m = s;
        int lo = 0, hi = 0;
        while (lo < ns && hArr[Math.floorMod(m - lo, ns)] >= peak / Math.sqrt(2)) lo++;
        while (hi < ns && hArr[Math.floorMod(m + hi, ns)] >= peak / Math.sqrt(2)) hi++;
        r.hpbwDeg = (lo + hi - 1) * 360.0 / ns;
        return r;
    }

    /**
     * Field in the H plane (theta = 90 deg) or in the E plane (plane containing boom and element axis).
     */
    private static NDoubleComplex cut(NDoubleComplex[] cur, double[] pos, double[] h, double psi, boolean ePlane) {
        double cosTheta = ePlane ? Math.sin(psi) : 0.0;
        NDoubleComplex e = NDoubleComplex.of(0, 0);
        for (int i = 0; i < cur.length; i++) {
            double g = elemFactor(h[i], cosTheta);
            e = e.addDoubleComplex(cur[i].multiplyDoubleComplex(NDoubleComplex.ofPolar(1, 2 * Math.PI * pos[i] * Math.cos(psi))).multiplyDouble(g));
        }
        return e;
    }

    /**
     * Element pattern referred to the feed current: [cos(kh cos t) - cos(kh)] / [sin(kh) sin t].
     */
    public static double elemFactor(double h, double cosTheta) {
        double sinT = Math.sqrt(Math.max(0, 1 - cosTheta * cosTheta));
        if (sinT < 1e-9) return 0;
        double kh = 2 * Math.PI * h;
        return (Math.cos(kh * cosTheta) - Math.cos(kh)) / (Math.sin(kh) * sinT);
    }

    // ------------------------------------------------------------------ impedances

    /**
     * Z_ij referred to the feed currents: parallel side-by-side thin dipoles, half-lengths hi/hj, spacing d (wavelengths).
     */
    public static NDoubleComplex zmut(double hi, double hj, double d) {
        int n = 400;
        double k = 2 * Math.PI, dz = 2 * hj / n, sr = 0, si = 0, ch = Math.cos(k * hi);
        for (int i = 0; i <= n; i++) {
            double zz = -hj + i * dz;
            double r1 = Math.sqrt(d * d + (zz - hi) * (zz - hi));
            double r2 = Math.sqrt(d * d + (zz + hi) * (zz + hi));
            double r0 = Math.sqrt(d * d + zz * zz);
            double br = Math.cos(k * r1) / r1 + Math.cos(k * r2) / r2 - 2 * ch * Math.cos(k * r0) / r0;
            double bi = -(Math.sin(k * r1) / r1 + Math.sin(k * r2) / r2 - 2 * ch * Math.sin(k * r0) / r0);
            double er = 30 * bi, ei = -30 * br;                       // e = -j 30 [br + j bi]
            double sn = Math.sin(k * (hj - Math.abs(zz)));
            double wgt = (i == 0 || i == n) ? 1 : (i % 2 == 1 ? 4 : 2);
            sr += wgt * er * sn;
            si += wgt * ei * sn;
        }
        double scale = -dz / 3 / (Math.sin(k * hi) * Math.sin(k * hj));
        return NDoubleComplex.of(sr * scale, si * scale);
    }

    private static NDoubleComplex[] solveLinear(NDoubleComplex[][] a, NDoubleComplex[] b) {
        int n = b.length;
        NDoubleComplex[][] m = new NDoubleComplex[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, m[i], 0, n);
            m[i][n] = b[i];
        }
        for (int c = 0; c < n; c++) {
            int piv = c;
            for (int r = c + 1; r < n; r++) if (m[r][c].absDouble() > m[piv][c].absDouble()) piv = r;
            NDoubleComplex[] tmp = m[c];
            m[c] = m[piv];
            m[piv] = tmp;
            for (int r = c + 1; r < n; r++) {
                NDoubleComplex f = m[r][c].divideDoubleComplex(m[c][c]);
                for (int k = c; k <= n; k++) m[r][k] = m[r][k].subtractDoubleComplex(f.multiplyDoubleComplex(m[c][k]));
            }
        }
        NDoubleComplex[] x = new NDoubleComplex[n];
        for (int i = n - 1; i >= 0; i--) {
            NDoubleComplex s = m[i][n];
            for (int k = i + 1; k < n; k++) s = s.subtractDoubleComplex(m[i][k].multiplyDoubleComplex(x[k]));
            x[i] = s.divideDoubleComplex(m[i][i]);
        }
        return x;
    }
}