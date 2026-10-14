package net.thevpc.ntexup.extension.antennas.phys;

/**
 * Excitation amplitude taper generators for linear arrays.
 *
 * Pure Java (no ntexup dependency). All tapers return the per-element amplitude array
 * (length {@code n}), peak-normalized to 1.0.
 */
public final class ArrayTapers {

    public static final String UNIFORM = "uniform";
    public static final String BINOMIAL = "binomial";
    public static final String CHEBYSHEV = "chebyshev";
    public static final String TRIANGULAR = "triangular";
    public static final String HANN = "hann";
    public static final String HAMMING = "hamming";
    public static final String BLACKMAN = "blackman";

    private ArrayTapers() {
    }

    /** True if the given name is a known generator. */
    public static boolean isKnown(String name) {
        return UNIFORM.equalsIgnoreCase(name)
                || BINOMIAL.equalsIgnoreCase(name)
                || CHEBYSHEV.equalsIgnoreCase(name)
                || TRIANGULAR.equalsIgnoreCase(name)
                || HANN.equalsIgnoreCase(name)
                || HAMMING.equalsIgnoreCase(name)
                || BLACKMAN.equalsIgnoreCase(name);
    }

    /**
     * Generates a peak-normalized amplitude taper.
     *
     * @param name    generator id (case-insensitive), see constants
     * @param n       number of elements (&gt;= 1)
     * @param sllDb   desired Chebyshev sidelobe level in dB (only used for {@link #CHEBYSHEV})
     * @return amplitude array of length {@code n}, peak 1.0; unknown names yield uniform
     */
    public static double[] generate(String name, int n, double sllDb) {
        if (name == null) {
            return uniform(n);
        }
        switch (name.trim().toLowerCase()) {
            case BINOMIAL:
                return binomial(n);
            case CHEBYSHEV:
                return chebyshev(n, sllDb);
            case TRIANGULAR:
                return triangular(n);
            case HANN:
                return hann(n);
            case HAMMING:
                return hamming(n);
            case BLACKMAN:
                return blackman(n);
            case UNIFORM:
            default:
                return uniform(n);
        }
    }

    public static double[] uniform(int n) {
        double[] a = new double[Math.max(1, n)];
        java.util.Arrays.fill(a, 1.0);
        return a;
    }

    /**
     * Binomial taper: coefficients of (1+x)^(n-1), C(n-1, k).
     * Produces a pattern with no sidelobes (nulls only at the grating-lobe angles).
     */
    public static double[] binomial(int n) {
        int nn = Math.max(1, n);
        double[] a = new double[nn];
        long c = 1;
        a[0] = c;
        for (int k = 1; k < nn; k++) {
            c = c * (nn - k) / k;
            a[k] = c;
        }
        return normalize(a);
    }

    /**
     * Dolph-Chebyshev taper with equal-ripple sidelobes of {@code sllDb} dB.
     *
     * <p>
     * Designed for isotropic elements at {@code d = lambda/2}: the array factor is
     * made proportional to {@code T_{n-1}(x0 cos(phi))} with
     * {@code x0 = cosh(acosh(R)/(n-1))} and {@code R = 10^(|sllDb|/20)} the voltage
     * sidelobe ratio. The per-element amplitudes are the expansion coefficients of
     * that polynomial in the {@code cos} basis of the array factor (solved exactly
     * for the few unknowns an {@code n}-element array has), then peak-normalized.
     * For spacings other than {@code lambda/2} the taper is still applied and simply
     * yields a lower (not exactly equal-ripple) sidelobe level.
     */
    public static double[] chebyshev(int n, double sllDb) {
        int nn = Math.max(1, n);
        if (nn <= 2) {
            return uniform(nn);
        }
        double r = Math.pow(10.0, Math.abs(sllDb) / 20.0); // voltage sidelobe ratio (>= 1)
        double x0 = Math.cosh(acosh(r) / (nn - 1));
        boolean even = (nn % 2 == 0);
        int k = even ? nn / 2 : nn / 2 + 1; // independent amplitudes: pairs, plus center if odd
        // Array factor target for symmetric excitation (d = lambda/2):
        //   even n = 2k     : AF(phi) = sum_{i=0..k-1} c_i cos((2i+1) phi)
        //   odd  n = 2k+1   : AF(phi) = sum_{i=0..k-1} c_i cos(2i phi),  c_0 = center
        // with phi = (pi d / lambda) sin(theta) = (pi/2) sin(theta). Solve the small
        // square system that makes AF equal T_{n-1}(x0 cos phi) / T_{n-1}(x0).
        double[][] m = new double[k][k];
        double[] rhs = new double[k];
        double tDen = chebyshevPoly(nn - 1, x0);
        for (int l = 0; l < k; l++) {
            double phi = Math.PI * (l + 0.5) / (2.0 * k);
            for (int i = 0; i < k; i++) {
                m[l][i] = even ? Math.cos((2 * i + 1) * phi) : Math.cos(2 * i * phi);
            }
            rhs[l] = chebyshevPoly(nn - 1, x0 * Math.cos(phi)) / tDen;
        }
        double[] coeff = solveLinear(m, rhs);
        double[] full = new double[nn];
        if (even) {
            for (int i = 0; i < k; i++) {
                double amp = coeff[i] / 2.0; // array factor sums symmetric pairs
                full[k - 1 - i] = amp;
                full[k + i] = amp;
            }
        } else {
            full[k - 1] = coeff[0]; // center element is not doubled
            for (int i = 1; i < k; i++) {
                double amp = coeff[i] / 2.0;
                full[k - 1 - i] = amp;
                full[k - 1 + i] = amp;
            }
        }
        return normalize(full);
    }

    /** Gaussian elimination with partial pivoting for a small square system. */
    private static double[] solveLinear(double[][] a, double[] b) {
        int n = b.length;
        double[][] m = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, m[i], 0, n);
            m[i][n] = b[i];
        }
        for (int col = 0; col < n; col++) {
            int piv = col;
            for (int row = col + 1; row < n; row++) {
                if (Math.abs(m[row][col]) > Math.abs(m[piv][col])) {
                    piv = row;
                }
            }
            double[] swap = m[col];
            m[col] = m[piv];
            m[piv] = swap;
            for (int row = 0; row < n; row++) {
                if (row == col) {
                    continue;
                }
                double f = m[row][col] / m[col][col];
                for (int c = col; c <= n; c++) {
                    m[row][c] -= f * m[col][c];
                }
            }
        }
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = m[i][n] / m[i][i];
        }
        return x;
    }

    private static double chebyshevPoly(int m, double x) {
        // T_0=1, T_1=x, T_m = 2x*T_{m-1} - T_{m-2}
        if (m == 0) return 1;
        if (m == 1) return x;
        double t0 = 1, t1 = x;
        for (int i = 2; i <= m; i++) {
            double t2 = 2 * x * t1 - t0;
            t0 = t1;
            t1 = t2;
        }
        return t1;
    }

    private static double acosh(double x) {
        // java.lang.Math has no acosh; acosh(x) = ln(x + sqrt(x^2 - 1)) for x >= 1
        return Math.log(x + Math.sqrt(x * x - 1.0));
    }

    /** Bartlett (triangular) window. */
    public static double[] triangular(int n) {
        int nn = Math.max(1, n);
        double[] a = new double[nn];
        double den = Math.max(1, nn - 1);
        for (int k = 0; k < nn; k++) {
            a[k] = 1.0 - Math.abs(2.0 * k - (nn - 1)) / den;
        }
        return normalize(a);
    }

    public static double[] hann(int n) {
        int nn = Math.max(1, n);
        double[] a = new double[nn];
        double den = Math.max(1, nn - 1);
        for (int k = 0; k < nn; k++) {
            a[k] = 0.5 * (1.0 - Math.cos(2.0 * Math.PI * k / den));
        }
        return normalize(a);
    }

    public static double[] hamming(int n) {
        int nn = Math.max(1, n);
        double[] a = new double[nn];
        double den = Math.max(1, nn - 1);
        for (int k = 0; k < nn; k++) {
            a[k] = 0.54 - 0.46 * Math.cos(2.0 * Math.PI * k / den);
        }
        return normalize(a);
    }

    public static double[] blackman(int n) {
        int nn = Math.max(1, n);
        double[] a = new double[nn];
        double den = Math.max(1, nn - 1);
        for (int k = 0; k < nn; k++) {
            a[k] = 0.42 - 0.5 * Math.cos(2.0 * Math.PI * k / den)
                    + 0.08 * Math.cos(4.0 * Math.PI * k / den);
        }
        return normalize(a);
    }

    private static double[] normalize(double[] a) {
        double max = 0;
        for (double v : a) {
            max = Math.max(max, Math.abs(v));
        }
        if (max < 1e-12) {
            java.util.Arrays.fill(a, 1.0);
            return a;
        }
        for (int i = 0; i < a.length; i++) {
            a[i] = a[i] / max;
        }
        return a;
    }
}