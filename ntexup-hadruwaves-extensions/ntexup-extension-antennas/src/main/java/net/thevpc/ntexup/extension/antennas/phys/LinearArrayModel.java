package net.thevpc.ntexup.extension.antennas.phys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Physical and mathematical model of a linear antenna array.
 *
 * Pure Java (no ntexup dependency) so it can be unit-tested standalone and shared by all
 * antenna components.
 *
 * Geometry:
 * <ul>
 * <li>Elements are placed along the X-axis, centered at the origin:
 *     {@code x_n = (n - (N - 1) / 2.0) * d}.</li>
 * <li>Broadside direction is theta = 0 (along the +Y axis); theta is measured from
 *     broadside towards +X, in [-90 deg, +90 deg].</li>
 * </ul>
 *
 * Array factor:
 * <ul>
 * <li>{@code k = 2*PI/lambda}, {@code psi(theta) = k*d*sin(theta) + beta} is the total phase
 *     difference between adjacent elements.</li>
 * <li>Element n carries excitation amplitude {@code a[n]} and an extra phase offset
 *     {@code phase[n]} (rad), in addition to the progressive phase {@code n*beta}.</li>
 * <li>{@code AF(theta) = |sum_n a[n]*exp(j*(n*psi + phase[n]))| / sum|a[n]|}
 *     (reduces to the classic uniform formula {@code |sin(N*psi/2)/(N*sin(psi/2))|}).</li>
 * </ul>
 *
 * Instantaneous wave field (used by the wave-field component):
 * {@code E_n(x,y,t) = a[n] * (1/sqrt(r_n+eps)) * cos(omega*t - k*r_n + n*beta + phase[n])}.
 */
public class LinearArrayModel {

    private int numElements = 4;
    private double dOverLambda = 0.50;   // element spacing relative to wavelength d/lambda
    private double wavelength = 1.0;     // arbitrary spatial units (e.g. meters)
    private double frequency = 1.0;      // Hz, omega = 2*PI*frequency
    private double beta = 0.0;           // progressive phase shift in radians
    private double steeringAngleDeg = 0.0; // steering angle in degrees [-90, +90]

    private double[] amplitudes = new double[]{1.0};  // per-element excitation magnitude a[n]
    private double[] phaseOffsets = new double[]{0.0}; // per-element extra phase (rad)
    private ElementPattern elementPattern = ElementPattern.ISOTROPIC;

    // visualization preferences that other classes may query in generic fashion
    private boolean showWavefrontRings = true;
    private boolean showBeamRays = true;
    private boolean showDbScale = false;
    private boolean intensityMode = false;

    public LinearArrayModel() {
        updateBetaFromSteeringAngle();
    }

    // --- Physics calculations ---

    public double getWavenumberK() {
        return 2.0 * Math.PI / wavelength;
    }

    public double getElementSpacingD() {
        return dOverLambda * wavelength;
    }

    public double getElementX(int index) {
        double d = getElementSpacingD();
        return (index - (numElements - 1) / 2.0) * d;
    }

    public double getOmega() {
        return 2.0 * Math.PI * frequency;
    }

    /**
     * Total phase difference between adjacent elements in direction theta (rad).
     */
    public double getPsi(double thetaRad) {
        double k = getWavenumberK();
        double d = getElementSpacingD();
        return k * d * Math.sin(thetaRad) + beta;
    }

    /** Per-element excitation magnitude (clamped index). */
    public double getAmplitude(int n) {
        int nn = Math.max(0, Math.min(numElements - 1, n));
        if (amplitudes == null || amplitudes.length == 0) {
            return 1.0;
        }
        return nn < amplitudes.length ? amplitudes[nn] : 1.0;
    }

    /** Per-element extra phase offset in radians (clamped index). */
    public double getPhaseOffset(int n) {
        int nn = Math.max(0, Math.min(numElements - 1, n));
        if (phaseOffsets == null || phaseOffsets.length == 0) {
            return 0.0;
        }
        return nn < phaseOffsets.length ? phaseOffsets[nn] : 0.0;
    }

    /** Sum of excitation magnitudes; used to normalize the array factor. */
    public double getAmplitudeSum() {
        double s = 0;
        for (int n = 0; n < numElements; n++) {
            s += getAmplitude(n);
        }
        return Math.max(s, 1e-9);
    }

    /** Sum of squared excitation magnitudes; used to normalize wave-field intensity. */
    public double getAmplitudeSquareSum() {
        double s = 0;
        for (int n = 0; n < numElements; n++) {
            double a = getAmplitude(n);
            s += a * a;
        }
        return Math.max(s, 1e-9);
    }

    /**
     * Normalized array factor in direction theta (rad). Range [0, 1].
     */
    public double getNormalizedArrayFactor(double thetaRad) {
        double psi = getPsi(thetaRad);
        double sumCos = 0.0;
        double sumSin = 0.0;
        for (int n = 0; n < numElements; n++) {
            double a = getAmplitude(n);
            double phi = n * psi + getPhaseOffset(n);
            sumCos += a * Math.cos(phi);
            sumSin += a * Math.sin(phi);
        }
        return Math.hypot(sumCos, sumSin) / getAmplitudeSum();
    }

    /** Array factor in dB, clamped to {@code minDb} (e.g. -30 dB). */
    public double getArrayFactorDb(double thetaRad, double minDb) {
        double af = getNormalizedArrayFactor(thetaRad);
        if (af < 1e-5) {
            return minDb;
        }
        return Math.max(20.0 * Math.log10(af), minDb);
    }

    /** Array factor multiplied by the selected element pattern (far-field amplitude). */
    public double getElementPatternFactor(double thetaRad) {
        return elementPattern.factor(Math.toDegrees(thetaRad));
    }

    /** Confirms whether excitation is uniform (all amplitudes equal to max). */
    public boolean isUniformExcitation() {
        double a0 = getAmplitude(0);
        for (int n = 1; n < numElements; n++) {
            if (Math.abs(getAmplitude(n) - a0) > 1e-9) {
                return false;
            }
        }
        return true;
    }

    /**
     * Theoretical null angles (rad) in the forward half-space [-pi/2, +pi/2].
     * For uniform excitation the analytic formula 2*m*pi/N is used; for tapered
     * excitation the nulls are located numerically (dip of |AF| below a tolerance).
     */
    public List<Double> getNullAnglesRad() {
        List<Double> nulls = new ArrayList<>();
        if (numElements <= 1) {
            return nulls;
        }
        double k = getWavenumberK();
        double d = getElementSpacingD();
        double kd = k * d;
        if (kd <= 1e-6) {
            return nulls;
        }
        if (isUniformExcitation()) {
            // Nulls: N*psi/2 = m*PI (m != 0 mod N) => sin(theta) = (2*m*PI/N - beta)/kd
            int maxM = (int) Math.ceil((kd + Math.abs(beta)) * numElements / (2.0 * Math.PI)) + 2;
            for (int m = -maxM; m <= maxM; m++) {
                if (m % numElements == 0) {
                    continue; // peak / grating lobe, not a null
                }
                double targetPsi = (2.0 * m * Math.PI) / numElements;
                double sinTheta = (targetPsi - beta) / kd;
                if (sinTheta >= -1.0 && sinTheta <= 1.0) {
                    nulls.add(Math.asin(sinTheta));
                }
            }
            return nulls;
        }
        // Numeric null detection for tapered arrays: local minima of |AF|^2 below tol.
        int steps = 1800; // 0.1 deg
        double tol = 1e-4;
        boolean inRun = false;
        double runStart = 0;
        for (int i = 0; i <= steps; i++) {
            double theta = -Math.PI / 2.0 + (i / (double) steps) * Math.PI;
            double f = getNormalizedArrayFactor(theta);
            if (f < tol) {
                if (!inRun) {
                    inRun = true;
                    runStart = theta;
                }
            } else {
                if (inRun) {
                    double mid = (runStart + theta) / 2.0;
                    // refine with a small golden-section search around the bracket
                    nulls.add(refineNull(mid, 0.02));
                    inRun = false;
                }
            }
        }
        if (inRun) {
            double mid = (runStart + Math.PI / 2.0) / 2.0;
            nulls.add(refineNull(mid, 0.02));
        }
        // dedupe and sort
        java.util.Collections.sort(nulls);
        List<Double> dedup = new ArrayList<>();
        for (double v : nulls) {
            if (dedup.isEmpty() || Math.abs(dedup.get(dedup.size() - 1) - v) > 1e-3) {
                dedup.add(v);
            }
        }
        return dedup;
    }

    private double refineNull(double center, double halfSpan) {
        double lo = center - halfSpan;
        double hi = center + halfSpan;
        double fLo = f2(lo);
        for (int i = 0; i < 60; i++) {
            double m1 = lo + (hi - lo) / 3.0;
            double m2 = hi - (hi - lo) / 3.0;
            if (f2(m1) < f2(m2)) {
                hi = m2;
            } else {
                lo = m1;
            }
        }
        return (lo + hi) / 2.0;
    }

    private double f2(double theta) {
        double f = getNormalizedArrayFactor(theta);
        return f * f;
    }

    /**
     * Approximate Half-Power Beamwidth (HPBW) in radians.
     * Analytic 0.886*lambda/(N*d)/cos(theta0) for uniform excitation (guarded when
     * cos(theta0) is small), numeric -3 dB search for tapered excitation.
     */
    public double getApproxHpbwRad() {
        if (numElements <= 1) {
            return Math.PI;
        }
        if (isUniformExcitation()) {
            double d = getElementSpacingD();
            double totalLength = (numElements - 1) * d;
            if (totalLength < 1e-4) {
                return Math.PI;
            }
            double broadsideHpbw = 0.886 * wavelength / (numElements * d);
            double cosSteer = Math.cos(Math.toRadians(steeringAngleDeg));
            if (cosSteer > 0.1) {
                return broadsideHpbw / cosSteer;
            }
            return broadsideHpbw;
        }
        // numeric: find -3 dB crossing around the steering direction
        double theta0 = Math.toRadians(steeringAngleDeg);
        double peak = getNormalizedArrayFactor(theta0);
        if (peak <= 0) {
            return Math.PI;
        }
        double target = peak * 0.7071067811865476;
        double left = findCrossing(theta0, -1, target);
        double right = findCrossing(theta0, 1, target);
        if (Double.isNaN(left) || Double.isNaN(right)) {
            return Math.PI;
        }
        double hpbw = Math.abs(right - left);
        return Math.min(hpbw, Math.PI);
    }

    private double findCrossing(double theta0, double dir, double target) {
        double step = Math.toRadians(0.2);
        double t = theta0;
        double prev = t;
        double prevF = normalizedAt(t);
        for (int i = 0; i < 500; i++) {
            t = t + dir * step;
            if (t < -Math.PI / 2.0 || t > Math.PI / 2.0) {
                return Double.NaN;
            }
            double f = normalizedAt(t);
            if ((f - target) * (prevF - target) < 0) {
                // bisect between prev and t
                double lo = prev, hi = t;
                for (int j = 0; j < 50; j++) {
                    double mid = (lo + hi) / 2.0;
                    if ((normalizedAt(mid) - target) * (normalizedAt(lo) - target) < 0) {
                        hi = mid;
                    } else {
                        lo = mid;
                    }
                }
                return (lo + hi) / 2.0;
            }
            prev = t;
            prevF = f;
        }
        return Double.NaN;
    }

    private double normalizedAt(double theta) {
        return getNormalizedArrayFactor(theta);
    }

    // --- Steering & Phase sync ---

    public void setSteeringAngleDeg(double angleDeg) {
        this.steeringAngleDeg = Math.max(-90.0, Math.min(90.0, angleDeg));
        updateBetaFromSteeringAngle();
    }

    public void setBetaRad(double betaRad) {
        this.beta = betaRad;
        while (this.beta > Math.PI) this.beta -= 2.0 * Math.PI;
        while (this.beta < -Math.PI) this.beta += 2.0 * Math.PI;
        updateSteeringAngleFromBeta();
    }

    private void updateBetaFromSteeringAngle() {
        double thetaRad = Math.toRadians(steeringAngleDeg);
        double k = getWavenumberK();
        double d = getElementSpacingD();
        this.beta = -k * d * Math.sin(thetaRad);
    }

    private void updateSteeringAngleFromBeta() {
        double k = getWavenumberK();
        double d = getElementSpacingD();
        double kd = k * d;
        if (kd > 1e-6) {
            double sinTheta = -beta / kd;
            if (sinTheta >= -1.0 && sinTheta <= 1.0) {
                this.steeringAngleDeg = Math.toDegrees(Math.asin(sinTheta));
            }
        }
    }

    // --- Getters and Setters (same clamps as the original simulator) ---

    public int getNumElements() {
        return numElements;
    }

    public void setNumElements(int numElements) {
        this.numElements = Math.max(1, Math.min(16, numElements));
        updateBetaFromSteeringAngle();
    }

    public double getDOverLambda() {
        return dOverLambda;
    }

    public void setDOverLambda(double dOverLambda) {
        this.dOverLambda = Math.max(0.1, Math.min(2.0, dOverLambda));
        updateBetaFromSteeringAngle();
    }

    public double getWavelength() {
        return wavelength;
    }

    public void setWavelength(double wavelength) {
        this.wavelength = Math.max(0.1, wavelength);
        updateBetaFromSteeringAngle();
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = Math.max(0.1, frequency);
    }

    public double getBeta() {
        return beta;
    }

    public double getBetaDeg() {
        return Math.toDegrees(beta);
    }

    public double getSteeringAngleDeg() {
        return steeringAngleDeg;
    }

    /** Sets the whole per-element amplitude array. */
    public void setAmplitudes(double[] amplitudes) {
        this.amplitudes = amplitudes == null ? null : amplitudes.clone();
    }

    /** Sets the per-element extra phase offset array (rad). */
    public void setPhaseOffsets(double[] phaseOffsets) {
        this.phaseOffsets = phaseOffsets == null ? null : phaseOffsets.clone();
    }

    public double[] getAmplitudes() {
        double[] a = new double[numElements];
        for (int n = 0; n < numElements; n++) {
            a[n] = getAmplitude(n);
        }
        return a;
    }

    public ElementPattern getElementPattern() {
        return elementPattern;
    }

    public void setElementPattern(ElementPattern elementPattern) {
        this.elementPattern = elementPattern == null ? ElementPattern.ISOTROPIC : elementPattern;
    }

    // --- visualization preferences (kept for parity with the simulator) ---

    public boolean isShowWavefrontRings() {
        return showWavefrontRings;
    }

    public void setShowWavefrontRings(boolean showWavefrontRings) {
        this.showWavefrontRings = showWavefrontRings;
    }

    public boolean isShowBeamRays() {
        return showBeamRays;
    }

    public void setShowBeamRays(boolean showBeamRays) {
        this.showBeamRays = showBeamRays;
    }

    public boolean isShowDbScale() {
        return showDbScale;
    }

    public void setShowDbScale(boolean showDbScale) {
        this.showDbScale = showDbScale;
    }

    public boolean isIntensityMode() {
        return intensityMode;
    }

    public void setIntensityMode(boolean intensityMode) {
        this.intensityMode = intensityMode;
    }

    @Override
    public String toString() {
        return "LinearArrayModel{N=" + numElements
                + ", d/lambda=" + dOverLambda
                + ", steering=" + steeringAngleDeg + "deg"
                + ", beta=" + beta
                + ", amplitudes=" + Arrays.toString(getAmplitudes())
                + ", pattern=" + elementPattern
                + '}';
    }
}