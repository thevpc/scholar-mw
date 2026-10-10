package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.eval.NTxValue;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;

import java.awt.*;

/**
 * Light / dark theme handling for the antenna components.
 *
 * Each component ships two built-in default palettes: the original simulator colors
 * (dark theme) and an adapted light-theme palette for white-background documents. The
 * {@code theme} property ({@code dark} | {@code light}, default {@code dark}) selects the
 * defaults; any individual color property overrides its default. No color is hard-coded
 * in rendering logic — every color is a property with a theme-dependent default.
 */
enum PanelTheme {

    DARK,
    LIGHT;

    /**
     * Reads the {@code theme} property (unknown / missing = {@link #DARK}).
     */
    static PanelTheme of(NTxRendererContext ctx) {
        String s = NTxValue.of(ctx.computePropertyValue("theme").orNull()).asStringOrName().orElse("dark");
        if ("light".equalsIgnoreCase(s.trim())) {
            return LIGHT;
        }
        return DARK;
    }

    /**
     * Resolves a color property: explicit property wins, otherwise the theme default.
     */
    static Color color(NTxRendererContext ctx, String propName, Color dark, Color light) {
        if (propName != null && !propName.isEmpty()) {
            java.awt.Paint p = ctx.getColorProperty(propName).orElse(null);
            if (p instanceof Color) {
                return (Color) p;
            }
        }
        return of(ctx) == LIGHT ? light : dark;
    }

    /**
     * A color with the given alpha overlay over the component background — used for
     * translucent grid lines and zone highlights. The base color stays a property.
     */
    static Color translucent(Color base, int alpha) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(), Math.max(0, Math.min(255, alpha)));
    }
}