package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.antennas.phys.ArrayTapers;
import net.thevpc.ntexup.extension.antennas.phys.LinearArrayModel;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code radiation-pattern} — polar far-field radiation pattern of a linear array
 * (port of {@code RadiationPatternPanel}).
 *
 * The pattern is the element-pattern-weighted array factor
 * {@code F(theta) = |AF(theta)| * f_el(theta)}, drawn in a polar plot whose radial axis is
 * either linear (0..1) or a logarithmic dB scale ({@code db-scale}) clamped at
 * {@code db-floor}. The steering direction is shown as a dashed line, the -3 dB
 * half-power beamwidth as a radial arc, and the exact null directions as markers.
 *
 * Properties:
 * <ul>
 *   <li>array: {@code n}, {@code d-lambda}, {@code lambda}, {@code frequency},
 *       {@code steering} / {@code beta}, {@code taper}, {@code chebyshev-sll},
 *       {@code amplitude}, {@code phase}, {@code element-pattern} (see
 *       {@link ArrayConfig});</li>
 *   <li>display: {@code db-scale} (flag, default false), {@code db-floor} (default -30),
 *       {@code samples} (default 720), {@code show-steering}, {@code show-hpbw},
 *       {@code show-nulls}, {@code show-header} (all default true), {@code title};</li>
 *   <li>{@code theme} (dark|light) and one color property per visual element.</li>
 * </ul>
 */
public class RadiationPatternBuilder implements NTxNodeBuilder {

    // translucent overlay alpha levels (documented, not scattered literals)
    private static final int PATTERN_FILL_ALPHA = 80;
    private static final int GRID_ALPHA = 60;
    private static final int GRID_STRONG_ALPHA = 120;
    private static final int RING_LBL_ALPHA = 150;
    private static final int SPOKE_ALPHA = 55;
    private static final int SPOKE_STRONG_ALPHA = 110;
    private static final int HDR_FILL_ALPHA = 210;
    private static final int HDR_BORDER_ALPHA = 180;

    // --- default palettes (dark = original simulator colors, light adaptations) ---
    private static final Color D_BG = new Color(0x0F121C);
    private static final Color L_BG = new Color(0xFFFFFF);
    private static final Color D_GRID = new Color(0xFFFFFF);
    private static final Color L_GRID = new Color(0x37474F);
    private static final Color D_GRID_LBL = new Color(0xAABED2);
    private static final Color L_GRID_LBL = new Color(0x546E7A);
    private static final Color D_PATTERN = new Color(0x00DCFF);
    private static final Color L_PATTERN = new Color(0x007FB8);
    private static final Color D_FILL = new Color(0x00B4FF);
    private static final Color L_FILL = new Color(0x0E7FBE);
    private static final Color D_STEERING = new Color(0xFFD700);
    private static final Color L_STEERING = new Color(0xB8860B);
    private static final Color D_HPBW = new Color(0xFF64FF);
    private static final Color L_HPBW = new Color(0xB13DB1);
    private static final Color D_NULL = new Color(0xFF4B4B);
    private static final Color L_NULL = new Color(0xD32F2F);
    private static final Color D_TITLE = new Color(0x64DCFF);
    private static final Color L_TITLE = new Color(0x0B5E83);
    private static final Color D_METRIC = new Color(0xDCE6F5);
    private static final Color L_METRIC = new Color(0x2B3B4D);

    @Override
    public void build(NTxNodeBuilderContext c) {
        c.id("radiation-pattern")
                .parseParam()
                .matchesNamedPair(
                        "n", "d-lambda", "lambda", "frequency", "steering", "beta",
                        "taper", "chebyshev-sll", "phase", "amplitude", "element-pattern",
                        "title", "samples", "db-floor", "theme",
                        // colors (all theme-aware properties, see PanelTheme)
                        "bg", "grid-color", "grid-label-color", "pattern-color", "pattern-fill",
                        "steering-color", "hpbw-color", "null-color", "title-color", "metric-color",
                        "header-fill", "header-border")
                .asFlags()
                .matchesNamedPair("db-scale", "show-steering", "show-hpbw", "show-nulls", "show-header")
                .end()
                .renderComponent(this::render);
    }

    private void render(NTxRendererContext ctx) {
        NTxBounds2D b = ctx.selfBounds2D();
        if (b.widthX() <= 12 || b.widthY() <= 12) {
            return;
        }
        ctx.withinClip(b, this::renderBody);
        ctx.drawContour();
    }

    private void renderBody(NTxRendererContext ctx) {
        NTxBounds2D b = ctx.selfBounds2D();
        NTxGraphics g = ctx.graphics();
        double x0 = b.minX(), y0 = b.minY(), w = b.widthX(), h = b.widthY();
        if (w <= 12 || h <= 12) {
            return;
        }
        LinearArrayModel model = ArrayConfig.model(ctx);

        boolean dbScale = Props.bool(ctx, "db-scale", false);
        double dbFloor = Math.min(-5.0, Props.dbl(ctx, "db-floor", -30.0));
        int samples = Math.max(120, Math.min(3600, Props.integer(ctx, "samples", 720)));
        boolean showSteering = Props.bool(ctx, "show-steering", true);
        boolean showHpbw = Props.bool(ctx, "show-hpbw", true);
        boolean showNulls = Props.bool(ctx, "show-nulls", true);
        boolean showHeader = Props.bool(ctx, "show-header", true);
        String title = Props.string(ctx, "title", "Diagramme de rayonnement");

        Color panelBg = Props.color(ctx, "bg", D_BG, L_BG);
        g.setPaint(panelBg);
        g.fillRect(x0, y0, w, h);

        Color gridColor = PanelTheme.translucent(Props.color(ctx, "grid-color", D_GRID, L_GRID), GRID_ALPHA);
        Color gridStrong = PanelTheme.translucent(Props.color(ctx, "grid-color", D_GRID, L_GRID), GRID_STRONG_ALPHA);
        Color gridLbl = PanelTheme.translucent(Props.color(ctx, "grid-label-color", D_GRID_LBL, L_GRID_LBL), RING_LBL_ALPHA);
        Color patternColor = Props.color(ctx, "pattern-color", D_PATTERN, L_PATTERN);
        Color patternFill = PanelTheme.translucent(Props.color(ctx, "pattern-fill", D_FILL, L_FILL), PATTERN_FILL_ALPHA);
        Color steeringColor = Props.color(ctx, "steering-color", D_STEERING, L_STEERING);
        Color hpbwColor = Props.color(ctx, "hpbw-color", D_HPBW, L_HPBW);
        Color nullColor = Props.color(ctx, "null-color", D_NULL, L_NULL);
        Color titleColor = Props.color(ctx, "title-color", D_TITLE, L_TITLE);
        Color metricColor = Props.color(ctx, "metric-color", D_METRIC, L_METRIC);
        Color hdrFill = PanelTheme.translucent(Props.color(ctx, "header-fill", D_BG, L_BG), HDR_FILL_ALPHA);
        Color hdrBorder = PanelTheme.translucent(Props.color(ctx, "header-border", D_STEERING, L_STEERING), HDR_BORDER_ALPHA);

        // --- layout: title band on top, metric band at the bottom, polar plot in between ---
        boolean hasTitle = showHeader && title != null && !title.trim().isEmpty();
        double titleH = hasTitle ? h * 0.07 : 0.0;
        double metricH = showHeader ? h * 0.06 : 0.0;
        double plotTop = y0 + titleH + h * 0.015;
        double plotBot = y0 + h - metricH - h * 0.015;
        double plotH = plotBot - plotTop;
        double cx = x0 + w / 2.0;
        double cy = plotTop + plotH / 2.0;
        double margin = Math.min(w, Math.max(20.0, h * 0.04));
        double R = Math.max(10.0, Math.min(w, plotH) / 2.0 - margin);

        // angle convention: theta=0 (broadside) points up; +theta towards +x

        // --- grid rings ---
        List<Double> rings = new ArrayList<>();
        rings.add(0.0);
        if (dbScale) {
            rings.add(-3.0);
            for (double lvl = -10.0; lvl >= dbFloor; lvl -= 10.0) {
                rings.add(lvl);
            }
        } else {
            rings.add(0.25);
            rings.add(0.5);
            rings.add(0.707);
        }
        g.setStroke(new BasicStroke(1.0f));
        for (double ring : rings) {
            double frac = dbScale ? Math.max(0.0, Math.min(1.0, (ring - dbFloor) / (-dbFloor))) : ring;
            if (frac <= 0.02) {
                continue;
            }
            double rr = R * frac;
            g.setPaint(ring == 0.0 ? gridStrong : gridColor);
            g.drawOval(cx - rr, cy - rr, 2 * rr, 2 * rr);
            g.setPaint(gridLbl);
            String lbl = dbScale ? String.format("%.0f dB", ring) : String.format("%.3g", ring);
            g.setFont(font(SANS_SERIF, PLAIN, Math.max(9.0, h * 0.018)));
            double lw = g.getFontMetrics().stringWidth(lbl);
            g.drawString(lbl, cx - rr - lw - 5, cy + 4);
        }

        // --- spokes (theta = -90..+90 every 30 deg) ---
        g.setStroke(new BasicStroke(1.0f));
        for (double t = -90.0; t <= 90.0 + 1e-9; t += 30.0) {
            double sx = cx + R * Math.sin(Math.toRadians(t));
            double sy = cy - R * Math.cos(Math.toRadians(t));
            boolean major = ((int) Math.round(Math.abs(t))) % 45 == 0;
            g.setPaint(major ? gridStrong : gridColor);
            g.drawLine(cx, cy, sx, sy);
            if (major) {
                g.setPaint(gridLbl);
                String lbl = String.format("%.0f°", t);
                double lx = cx + (R + 14) * Math.sin(Math.toRadians(t));
                double ly = cy - (R + 14) * Math.cos(Math.toRadians(t)) + 4;
                double lw = g.getFontMetrics().stringWidth(lbl);
                g.drawString(lbl, lx - lw / 2, ly);
            }
        }

        // --- pattern path ---
        Path2D.Double curve = new Path2D.Double();
        boolean first = true;
        for (int i = 0; i <= samples; i++) {
            double theta = -90.0 + (i / (double) samples) * 180.0;
            double af = model.getNormalizedArrayFactor(Math.toRadians(theta));
            double pat = model.getElementPatternFactor(Math.toRadians(theta));
            double frac = patternRadius(af, pat, dbScale, dbFloor);
            double px = cx + frac * R * Math.sin(Math.toRadians(theta));
            double py = cy - frac * R * Math.cos(Math.toRadians(theta));
            if (first) {
                curve.moveTo(px, py);
                first = false;
            } else {
                curve.lineTo(px, py);
            }
        }
        Path2D.Double fill = (Path2D.Double) curve.clone();
        fill.lineTo(cx, cy);
        fill.closePath();
        g.setPaint(patternFill);
        g.fill(fill);
        g.setPaint(patternColor);
        g.setStroke(new BasicStroke(2.4f));
        g.draw(curve);

        // --- beam metrics ---
        double steeringDeg = model.getSteeringAngleDeg();
        double hpbwDeg = Math.toDegrees(model.getApproxHpbwRad());
        List<Double> nullsRad = model.getNullAnglesRad();

        // steering direction (dashed line from center to outer radius)
        if (showSteering) {
            g.setPaint(steeringColor);
            g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{6, 4}, 0f));
            double sx = cx + R * Math.sin(Math.toRadians(steeringDeg));
            double sy = cy - R * Math.cos(Math.toRadians(steeringDeg));
            g.drawLine(cx, cy, sx, sy);
            g.setStroke(new BasicStroke(1.8f));
            g.fillOval(sx - 4, sy - 4, 8, 8);
            g.drawOval(sx - 4, sy - 4, 8, 8);
        }

        // HPBW -3 dB arc around the steering direction
        if (showHpbw && hpbwDeg > 0.05 && hpbwDeg < 180.0) {
            double rFrac = dbScale
                    ? Math.max(0.0, Math.min(1.0, (-3.0 - dbFloor) / (-dbFloor)))
                    : 0.7071067811865476;
            double rr = R * rFrac;
            double a1 = steeringDeg - hpbwDeg / 2.0;
            double a2 = steeringDeg + hpbwDeg / 2.0;
            Path2D.Double arc = new Path2D.Double();
            int arcSteps = 24;
            for (int i = 0; i <= arcSteps; i++) {
                double t = a1 + (i / (double) arcSteps) * (a2 - a1);
                double ax = cx + rr * Math.sin(Math.toRadians(t));
                double ay = cy - rr * Math.cos(Math.toRadians(t));
                if (i == 0) {
                    arc.moveTo(ax, ay);
                } else {
                    arc.lineTo(ax, ay);
                }
            }
            g.setPaint(hpbwColor);
            g.setStroke(new BasicStroke(1.6f));
            g.draw(arc);
        }

        // null markers on the outer ring
        if (showNulls) {
            g.setPaint(nullColor);
            g.setStroke(new BasicStroke(1.6f));
            for (double nr : nullsRad) {
                double t = Math.toDegrees(nr);
                double sx = cx + (R + 2) * Math.sin(nr);
                double sy = cy - (R + 2) * Math.cos(nr);
                g.drawLine(sx - 4, sy - 4, sx + 4, sy + 4);
                g.drawLine(sx - 4, sy + 4, sx + 4, sy - 4);
            }
        }

        // --- title (top band) ---
        if (hasTitle) {
            g.setFont(font(SANS_SERIF, BOLD, Math.max(11.0, h * 0.032)));
            g.setPaint(titleColor);
            double tw = g.getFontMetrics().stringWidth(title);
            g.drawString(title, cx - tw / 2, y0 + titleH * 0.68);
        }

        // --- metric footer (bottom band) ---
        if (showHeader) {
            String taper = Props.string(ctx, "taper", ArrayTapers.UNIFORM);
            double[] amps = model.getAmplitudes();
            double maxA = 0;
            for (double a : amps) {
                maxA = Math.max(maxA, a);
            }
            boolean custom = false;
            double[] explicit = Props.dblArray(ctx, "amplitude");
            custom = explicit != null && explicit.length > 0;
            String taperTxt = custom ? "personnalisées" : taper;
            if (!custom && ArrayTapers.CHEBYSHEV.equalsIgnoreCase(taper)) {
                taperTxt += String.format("(%.0f dB)", Props.dbl(ctx, "chebyshev-sll", -30.0));
            }
            String metrics = String.format("N = %d │ d/λ = %.2f │ θ0 = %.1f° │ HPBW = %.1f° │ Taper = %s │ Élément = %s",
                    model.getNumElements(), model.getDOverLambda(), steeringDeg, hpbwDeg,
                    taperTxt, model.getElementPattern().label());
            g.setFont(font(MONOSPACED, PLAIN, Math.max(9.0, h * 0.02)));
            g.setPaint(metricColor);
            double tw = g.getFontMetrics().stringWidth(metrics);
            g.drawString(metrics, cx - tw / 2, y0 + h - metricH * 0.55);
        }
    }

    /**
     * Radial fraction (0..1) of the combined AF * element-pattern value in the chosen scale.
     */
    private static double patternRadius(double af, double pat, boolean dbScale, double dbFloor) {
        if (!dbScale) {
            return Math.max(0.0, Math.min(1.0, af * pat));
        }
        double v = af * pat;
        double db = v < 1e-5 ? dbFloor : Math.max(dbFloor, 20.0 * Math.log10(v));
        return Math.max(0.0, Math.min(1.0, (db - dbFloor) / (-dbFloor)));
    }

    private static Font font(String family, int style, double sizePx) {
        return new Font(family, style, Math.max(7, (int) Math.round(sizePx)));
    }

    private static final int PLAIN = Font.PLAIN;
    private static final int BOLD = Font.BOLD;
    private static final String SANS_SERIF = "SansSerif";
    private static final String MONOSPACED = "Monospaced";
}