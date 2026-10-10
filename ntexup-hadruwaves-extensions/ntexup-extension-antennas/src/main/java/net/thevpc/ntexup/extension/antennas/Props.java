package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.eval.NTxValue;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;

import java.awt.*;

/**
 * Small typed property readers shared by all antenna components.
 *
 * Every component property is read through the renderer context so that values set on
 * parent styles / templates are honored. Missing properties fall back to the default
 * passed by the caller.
 */
final class Props {

    private Props() {
    }

    static double dbl(NTxRendererContext ctx, String name, double def) {
        return NTxValue.of(ctx.computePropertyValue(name).orNull()).asDouble().orElse(def);
    }

    static int integer(NTxRendererContext ctx, String name, int def) {
        return NTxValue.of(ctx.computePropertyValue(name).orNull()).asInt().orElse(def);
    }

    static boolean bool(NTxRendererContext ctx, String name, boolean def) {
        return NTxValue.of(ctx.computePropertyValue(name).orNull()).asBoolean().orElse(def);
    }

    static String string(NTxRendererContext ctx, String name, String def) {
        return NTxValue.of(ctx.computePropertyValue(name).orNull()).asStringOrName().orElse(def);
    }

    /**
     * Reads a list-valued property (explicit amplitude / phase lists, axis ranges, ...).
     */
    static double[] dblArray(NTxRendererContext ctx, String name) {
        return NTxValue.of(ctx.computePropertyValue(name).orNull()).asDoubleArray().orNull();
    }

    /**
     * Reads a color property with a theme-dependent fallback (see {@link PanelTheme}).
     */
    static Color color(NTxRendererContext ctx, String name, Color dark, Color light) {
        return PanelTheme.color(ctx, name, dark, light);
    }
}