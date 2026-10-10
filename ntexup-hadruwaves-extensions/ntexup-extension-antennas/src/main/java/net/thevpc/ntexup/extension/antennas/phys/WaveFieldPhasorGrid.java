package net.thevpc.ntexup.extension.antennas.phys;

/**
 * Precomputed per-pixel phasor sums of the 2D wave field.
 *
 * The field of element n at a pixel is
 * {@code a_n * f_el(theta) / sqrt(r_n + eps) * cos(omega*t - k*r_n + n*beta + phase_n)}.
 * Splitting time out:
 * <pre>
 *   A_i = sum_n a_n*f_el/sqrt(r) * cos(-k*r_n + n*beta + phase_n)
 *   B_i = sum_n a_n*f_el/sqrt(r) * sin(-k*r_n + n*beta + phase_n)
 *   E_i(t) = A_i*cos(omega*t) - B_i*sin(omega*t)
 *   I_i     = (A_i^2 + B_i^2) / (sum a^2 * intensityScale)
 * </pre>
 * A and B are frame-invariant (they depend only on geometry, frequency, excitation and the
 * element pattern), so animating the field only needs {@code cos/sin} per pixel, not a
 * per-element loop. Pure Java (no ntexup dependency).
 */
public class WaveFieldPhasorGrid {

    /** Geometric 2D cylindrical-wave decay regularizer (1/sqrt(r + eps)). */
    public static final double DEFAULT_DISTANCE_EPS = 0.35;
    /** Default instantaneous scale factor (matches the original simulator's visual contrast). */
    public static final double DEFAULT_INSTANT_SCALE = 1.4;
    /** Default intensity scale factor. */
    public static final double DEFAULT_INTENSITY_SCALE = 1.5;

    private final int width;
    private final int height;
    private final double[] sumCos; // A per pixel
    private final double[] sumSin; // B per pixel
    private final double instantNorm;   // sqrt(sum a^2) * instantScale
    private final double intensityNorm; // sum a^2 * intensityScale

    public WaveFieldPhasorGrid(double[] elementX, double[] amplitudes, double[] phaseOffsets,
                               double beta, double k, ElementPattern pattern,
                               double physXMin, double physXMax, double physYMin, double physYMax,
                               int bufferWidth, int bufferHeight,
                               double distanceEps, double instantScale, double intensityScale) {
        int n = elementX.length;
        this.width = Math.max(2, bufferWidth);
        this.height = Math.max(2, bufferHeight);
        this.sumCos = new double[this.width * this.height];
        this.sumSin = new double[this.width * this.height];

        double dx = (physXMax - physXMin) / this.width;
        double dy = (physYMax - physYMin) / this.height;

        double sumA2 = 0;
        for (double a : amplitudes) {
            sumA2 += a * a;
        }
        this.instantNorm = Math.sqrt(Math.max(sumA2, 1e-12)) * (instantScale > 0 ? instantScale : DEFAULT_INSTANT_SCALE);
        this.intensityNorm = Math.max(sumA2, 1e-12) * (intensityScale > 0 ? intensityScale : DEFAULT_INTENSITY_SCALE);

        double eps = distanceEps > 0 ? distanceEps : DEFAULT_DISTANCE_EPS;
        int idx = 0;
        for (int py = 0; py < this.height; py++) {
            double y = physYMax - py * dy; // py = 0 is physYMax (top of screen)
            double y2 = y * y;
            for (int px = 0; px < this.width; px++) {
                double x = physXMin + px * dx;
                // direction of the observation point relative to the array center (0, 0)
                double thetaDeg = Math.toDegrees(Math.atan2(x, y));
                double pat = pattern == null ? 1.0 : pattern.factor(thetaDeg);
                double a = 0;
                double b = 0;
                for (int i = 0; i < n; i++) {
                    double deltaX = x - elementX[i];
                    double r = Math.sqrt(deltaX * deltaX + y2);
                    double amp = amplitudes[i] * pat / Math.sqrt(r + eps);
                    double spatialPhase = -k * r + i * beta + phaseOffset(phaseOffsets, i);
                    a += amp * Math.cos(spatialPhase);
                    b += amp * Math.sin(spatialPhase);
                }
                this.sumCos[idx] = a;
                this.sumSin[idx] = b;
                idx++;
            }
        }
    }

    private static double phaseOffset(double[] phases, int i) {
        if (phases == null || phases.length == 0) {
            return 0.0;
        }
        return i < phases.length ? phases[i] : 0.0;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int pixelCount() {
        return sumCos.length;
    }

    public int index(int px, int py) {
        return py * width + px;
    }

    /**
     * Instantaneous field E(t) at pixel index (unnormalized).
     */
    public double instantaneous(int index, double phaseT) {
        return sumCos[index] * Math.cos(phaseT) - sumSin[index] * Math.sin(phaseT);
    }

    /**
     * Instantaneous field normalized to [-1, 1] (clamped), suitable for a bicolor LUT.
     */
    public double instantaneousNormalized(int index, double phaseT) {
        double e = instantaneous(index, phaseT) / instantNorm;
        return Math.max(-1.0, Math.min(1.0, e));
    }

    /**
     * RMS intensity in [0, 1] (clamped), suitable for a monochrome LUT.
     */
    public double intensity(int index) {
        double a = sumCos[index];
        double b = sumSin[index];
        return Math.max(0.0, Math.min(1.0, (a * a + b * b) / intensityNorm));
    }
}