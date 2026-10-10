package net.thevpc.ntexup.extension.antennas.phys;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared math of the "Facteur de réseau graphique rapide" nomogram
 * (port of {@code FastGraphicalArrayFactorPanel}).
 *
 * Fr(2gamma) = |sin(N * gamma) / (N * sin(gamma))|  with N-1 equidistant zeros at
 * 2*gamma = 2*m*PI/N (m = 1..N-1). Pure Java (no ntexup dependency).
 */
public final class ArrayFactorMath {

    private ArrayFactorMath() {
    }

    /**
     * Evaluates Fr(2gamma) for the given element count.
     *
     * @param twoGammaDeg abscissa in degrees (2*gamma)
     * @return |sin(N*gamma)/(N*sin(gamma))| with the removable singularity handled
     */
    public static double fr(int n, double twoGammaDeg) {
        if (n <= 1) {
            return 1.0;
        }
        double gammaRad = Math.toRadians(twoGammaDeg / 2.0);
        double sinGamma = Math.sin(gammaRad);
        if (Math.abs(sinGamma) < 1e-6) {
            return 1.0;
        }
        double val = Math.sin(n * gammaRad) / (n * sinGamma);
        return Math.abs(val);
    }

    /**
     * The N-1 zeros of Fr in [0, 360 deg]: 2*gamma = 360*m/N, m = 1..N-1.
     */
    public static List<Double> zerosDeg(int n) {
        List<Double> zeros = new ArrayList<>();
        for (int m = 1; m < n; m++) {
            zeros.add(360.0 * m / n);
        }
        return zeros;
    }
}