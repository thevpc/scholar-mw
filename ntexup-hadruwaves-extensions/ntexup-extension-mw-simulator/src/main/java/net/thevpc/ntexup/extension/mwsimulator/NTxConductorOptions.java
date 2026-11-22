package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.eval.NTxResolutionContext;
import net.thevpc.ntexup.api.util.NTxNumberUtils;
import net.thevpc.ntexup.api.util.NTxUtils;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NPairElement;

import java.nio.charset.StandardCharsets;

/**
 * The shared ntx option {@code conductor:{rho:2.44e-8, thickness:35um, roughness:1um}} of the
 * solvers built on {@link MicrostripCircuitModel}. {@code conductivity:5.8e7} (S/m) is accepted in
 * place of rho. Every field is 0 until set, in which case the model keeps its own default.
 */
public class NTxConductorOptions {
    /** ohm m */
    public double rho;
    /** m */
    public double thickness;
    /** m */
    public double roughness;

    /**
     * @param solver  the solver name for the error messages
     * @param allowed the options this solver maps, among rho (and conductivity), thickness and roughness
     */
    public static NTxConductorOptions parse(String solver, NElement value, NTxResolutionContext context, String... allowed) {
        if (!value.isAnyObject()) {
            throw new IllegalArgumentException(solver + ": 'conductor' expects an object, as in conductor:{rho:1.72e-8}");
        }
        java.util.Set<String> ok = new java.util.HashSet<>(java.util.Arrays.asList(allowed));
        NTxConductorOptions o = new NTxConductorOptions();
        for (NElement c : value.asObject().get().children()) {
            if (!c.isNamedPair()) {
                continue;
            }
            NPairElement p = c.asNamedPair().get();
            String key = NTxUtils.uid(p.key().asStringValue().orElse(""));
            NElement v = context.evalExpression(p.value()).orElse(p.value());
            String option = "conductivity".equals(key) ? "rho" : key;
            if (!ok.contains(option)) {
                throw new IllegalArgumentException(solver + ": unknown conductor option '" + key + "' (expected "
                        + String.join(", ", allowed).replace("rho", "rho, conductivity") + ")");
            }
            switch (key) {
                case "rho": {
                    o.rho = v.asDoubleValue().orElse(0.0);
                    if (!(o.rho > 0)) {
                        throw new IllegalArgumentException(solver + ": conductor option 'rho' expects a positive resistivity in ohm m, as in rho:1.72e-8");
                    }
                    break;
                }
                case "conductivity": {
                    double sigma = v.asDoubleValue().orElse(0.0);
                    if (!(sigma > 0)) {
                        throw new IllegalArgumentException(solver + ": conductor option 'conductivity' expects a positive value in S/m, as in conductivity:5.8e7");
                    }
                    o.rho = 1.0 / sigma;
                    break;
                }
                case "thickness":
                case "roughness": {
                    double m = NTxNumberUtils.toMeter(v).orElse(0.0);
                    if (!(m > 0)) {
                        throw new IllegalArgumentException(solver + ": conductor option '" + key + "' expects a positive length, as in " + key + ":35um");
                    }
                    if ("thickness".equals(key)) {
                        o.thickness = m;
                    } else {
                        o.roughness = m;
                    }
                    break;
                }
            }
        }
        return o;
    }

    /** Adds the options that are set, so a deck without them keeps its hash. */
    public static void addToDigest(Object digest, NTxConductorOptions o) {
        if (o == null) {
            return;
        }
        if (o.rho > 0) {
            NTxMwSimulationUtils.addDigestSource(digest, ("conductor-rho:" + o.rho).getBytes(StandardCharsets.UTF_8));
        }
        if (o.thickness > 0) {
            NTxMwSimulationUtils.addDigestSource(digest, ("conductor-thickness:" + o.thickness).getBytes(StandardCharsets.UTF_8));
        }
        if (o.roughness > 0) {
            NTxMwSimulationUtils.addDigestSource(digest, ("conductor-roughness:" + o.roughness).getBytes(StandardCharsets.UTF_8));
        }
    }
}
