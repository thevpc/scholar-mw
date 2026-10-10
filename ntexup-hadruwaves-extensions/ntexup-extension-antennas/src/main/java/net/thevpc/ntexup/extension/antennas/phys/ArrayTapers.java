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
     * Standard window from the inverse DFT of a sampled Chebyshev polynomial.
     */
    public static double[] chebyshev(int n, double sllDb) {
        int nn = Math.max(1, n);
        if (nn <= 2) {
            return uniform(nn);
        }
        double r = Math.pow(10.0, -Math.abs(sllDb) / 20.0); // sidelobe amplitude
        double x0 = Math.cosh(acosh(r) / (nn - 1));
        double[] w = new double[nn];
        for (int k = 0; k < nn; k++) {
            double x = x0 * Math.cos(Math.PI * k / nn);
            double t = chebyshevPoly(nn - 1, x);
            w[k] = (k & 1) == 1 ? -t : t;
        }
        double[] win = new double[nn];
        for (int j = 0; j < nn; j++) {
            double s = 0;
            for (int k = 0; k < nn; k++) {
                s += w[k] * Math.cos(2.0 * Math.PI * j * k / nn);
            }
            win[j] = s / nn;
        }
        return normalize(win);
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