package net.thevpc.ntexup.extension.antennas.phys;

/**
 * Two-color gradient lookup table (ARGB int entries) with a power gamma.
 *
 * Reproduces the instantaneous / intensity colormaps of the original simulator when fed the
 * same colors and exponents, but as fully parametrizable properties. Pure Java
 * (no ntexup dependency).
 */
public class ColorRamp {

    private final int size;
    private final int[] lut;
    private final double gamma;

    public ColorRamp(int rLow, int gLow, int bLow, int rHigh, int gHigh, int bHigh, double gamma) {
        this(256, rLow, gLow, bLow, rHigh, gHigh, bHigh, gamma);
    }

    public ColorRamp(int size, int rLow, int gLow, int bLow, int rHigh, int gHigh, int bHigh, double gamma) {
        this.size = Math.max(2, size);
        this.gamma = gamma > 0 ? gamma : 1.0;
        this.lut = new int[this.size];
        for (int i = 0; i < this.size; i++) {
            double t = i / (double) (this.size - 1);
            double p = Math.pow(t, this.gamma);
            int r = (int) Math.round(rLow + (rHigh - rLow) * p);
            int g = (int) Math.round(gLow + (gHigh - gLow) * p);
            int b = (int) Math.round(bLow + (bHigh - bLow) * p);
            lut[i] = (255 << 24) | ((r & 255) << 16) | ((g & 255) << 8) | (b & 255);
        }
    }

    public int size() {
        return size;
    }

    public double gamma() {
        return gamma;
    }

    /**
     * Color entry for normalized value {@code t} in [0, 1] (clamped).
     */
    public int color(double t) {
        double c = Math.max(0.0, Math.min(1.0, t));
        int idx = (int) Math.round(c * (size - 1));
        return lut[idx];
    }

    /**
     * Direct LUT access (index 0..size-1, clamped).
     */
    public int colorAt(int index) {
        int idx = Math.max(0, Math.min(size - 1, index));
        return lut[idx];
    }
}