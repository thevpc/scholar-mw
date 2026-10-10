package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.antennas.phys.ArrayTapers;
import net.thevpc.ntexup.extension.antennas.phys.ElementPattern;
import net.thevpc.ntexup.extension.antennas.phys.LinearArrayModel;

/**
 * Builds a configured {@link LinearArrayModel} from component properties, shared by all
 * array components.
 *
 * Array properties:
 * <ul>
 *   <li>{@code n} — element count (clamped to [1..16]);</li>
 *   <li>{@code d-lambda} — spacing in wavelengths (clamped to [0.1..2.0]);</li>
 *   <li>{@code lambda}, {@code frequency} — physical wavelength / frequency;</li>
 *   <li>{@code steering} — beam steering angle in degrees (default 0); {@code beta} —
 *       progressive phase in degrees, overrides {@code steering} when present;</li>
 *   <li>{@code taper} — {@code uniform | binomial | chebyshev | triangular | hann |
 *       hamming | blackman} plus {@code chebyshev-sll} (dB); the explicit
 *       {@code amplitude} list overrides the taper generator;</li>
 *   <li>{@code phase} — per-element extra phase offsets in degrees;</li>
 *   <li>{@code element-pattern} — {@code isotropic | dipole | cardioid}.</li>
 * </ul>
 */
final class ArrayConfig {

    private ArrayConfig() {
    }

    static LinearArrayModel model(NTxRendererContext ctx) {
        LinearArrayModel m = new LinearArrayModel();
        int n = Props.integer(ctx, "n", 4);
        m.setNumElements(n);
        m.setDOverLambda(Props.dbl(ctx, "d-lambda", 0.5));
        m.setWavelength(Props.dbl(ctx, "lambda", 1.0));
        m.setFrequency(Props.dbl(ctx, "frequency", 1.0));

        if (ctx.computePropertyValue("beta").isPresent()) {
            m.setBetaRad(Math.toRadians(Props.dbl(ctx, "beta", 0.0)));
        } else {
            m.setSteeringAngleDeg(Props.dbl(ctx, "steering", 0.0));
        }

        double[] explicitAmp = Props.dblArray(ctx, "amplitude");
        if (explicitAmp != null && explicitAmp.length > 0) {
            m.setAmplitudes(explicitAmp);
        } else {
            String taper = Props.string(ctx, "taper", ArrayTapers.UNIFORM);
            double sll = Props.dbl(ctx, "chebyshev-sll", -30.0);
            m.setAmplitudes(ArrayTapers.generate(taper, n, sll));
        }

        double[] phases = Props.dblArray(ctx, "phase");
        if (phases != null && phases.length > 0) {
            double[] ph = new double[phases.length];
            for (int i = 0; i < ph.length; i++) {
                ph[i] = Math.toRadians(phases[i]);
            }
            m.setPhaseOffsets(ph);
        }

        m.setElementPattern(ElementPattern.parse(Props.string(ctx, "element-pattern", "isotropic")));
        return m;
    }
}