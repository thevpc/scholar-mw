package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.antennas.phys.ArrayTapers;
import net.thevpc.ntexup.extension.antennas.phys.LinearArrayModel;

import java.awt.*;

/**
 * {@code antenna-array} — schematic top view of the linear array: the array axis, one
 * marker per element whose size encodes the excitation amplitude, per-element index and
 * amplitude labels, and optional phase-offset arrows.
 *
 * This is a *configuration* view (no radiation math) built from the same array properties
 * as the other components (see {@link ArrayConfig}).
 *
 * Properties:
 * <ul>
 *   <li>array: {@code n}, {@code d-lambda}, {@code lambda}, {@code steering} /
 *       {@code beta}, {@code taper}, {@code chebyshev-sll}, {@code amplitude},
 *       {@code phase} (see {@link ArrayConfig});</li>
 *   <li>markers: {@code min-marker} / {@code max-marker} (pixel radii, defaults 3 / 12),
 *       flags {@code show-axis}, {@code show-index}, {@code show-amplitude},
 *       {@code show-phase}, {@code show-title};</li>
 *   <li>{@code theme} (dark|light) and one color property per visual element.</li>
 * </ul>
 */
public class AntennaArrayBuilder implements NTxNodeBuilder {

    private static final Color D_BG = new Color(0x0F121C);
    private static final Color L_BG = new Color(0xFFFFFF);
    private static final Color D_AXIS = new Color(0xDCE6F5);
    private static final Color L_AXIS = new Color(0x33475C);
    private static final Color D_ELEMENT = new Color(0xFFD700);
    private static final Color L_ELEMENT = new Color(0xB8860B);
    private static final Color D_ELEMENT_STROKE = new Color(0xFFFFFF);
    private static final Color L_ELEMENT_STROKE = new Color(0x263238);
    private static final Color D_LABEL = new Color(0xB4C8DC);
    private static final Color L_LABEL = new Color(0x3A4F64);
    private static final Color D_PHASE = new Color(0xFF64FF);
    private static final Color L_PHASE = new Color(0xB13DB1);
    private static final Color D_TITLE = new Color(0x64DCFF);
    private static final Color L_TITLE = new Color(0x0B5E83);

    @Override
    public void build(NTxNodeBuilderContext c) {
        c.id("antenna-array")
                .parseParam()
                .matchesNamedPair(
                        "n", "d-lambda", "lambda", "steering", "beta",
                        "taper", "chebyshev-sll", "phase", "amplitude",
                        "title", "min-marker", "max-marker", "theme",
                        // colors
                        "bg", "axis-color", "element-color", "element-stroke",
                        "label-color", "phase-color", "title-color")
                .asFlags()
                .matchesNamedPair("show-axis", "show-index", "show-amplitude", "show-phase", "show-title")
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
        int n = model.getNumElements();
        double[] amps = model.getAmplitudes();
        double maxA = 1.0;
        for (double a : amps) {
            maxA = Math.max(maxA, a);
        }

        boolean showAxis = Props.bool(ctx, "show-axis", true);
        boolean showIndex = Props.bool(ctx, "show-index", true);
        boolean showAmplitude = Props.bool(ctx, "show-amplitude", true);
        boolean showPhase = Props.bool(ctx, "show-phase", true);
        boolean showTitle = Props.bool(ctx, "show-title", true);
        String title = Props.string(ctx, "title", "Réseau linéaire");
        double minMarker = Math.max(1.0, Props.dbl(ctx, "min-marker", 3.0));
        double maxMarker = Math.max(minMarker, Props.dbl(ctx, "max-marker", 12.0));

        Color panelBg = Props.color(ctx, "bg", D_BG, L_BG);
        g.setPaint(panelBg);
        g.fillRect(x0, y0, w, h);

        Color axisColor = Props.color(ctx, "axis-color", D_AXIS, L_AXIS);
        Color elementColor = Props.color(ctx, "element-color", D_ELEMENT, L_ELEMENT);
        Color elementStroke = Props.color(ctx, "element-stroke", D_ELEMENT_STROKE, L_ELEMENT_STROKE);
        Color labelColor = Props.color(ctx, "label-color", D_LABEL, L_LABEL);
        Color phaseColor = Props.color(ctx, "phase-color", D_PHASE, L_PHASE);
        Color titleColor = Props.color(ctx, "title-color", D_TITLE, L_TITLE);

        // layout: title band on top, schematic in the middle, footer at the bottom
        boolean hasTitle = showTitle && title != null && !title.trim().isEmpty();
        double titleH = hasTitle ? h * 0.12 : 0.0;
        double footH = h * 0.10;
        double midTop = y0 + titleH;
        double midBot = y0 + h - footH;
        double cy = (midTop + midBot) / 2.0;
        double labelPad = w * 0.06; // reserve space for index labels
        double halfW = w / 2.0 - labelPad;

        // half extent of the array in wavelengths (>= 0.5 to keep a minimum schematic)
        double halfExtent = Math.max(0.5, (n - 1) / 2.0 * model.getDOverLambda());

        if (showAxis) {
            g.setPaint(axisColor);
            g.setStroke(new BasicStroke(1.6f));
            g.drawLine((int) Math.round(x0 + labelPad), (int) Math.round(cy),
                    (int) Math.round(x0 + w - labelPad), (int) Math.round(cy));
            // small end ticks
            g.drawLine((int) Math.round(x0 + labelPad), (int) Math.round(cy - 4),
                    (int) Math.round(x0 + labelPad), (int) Math.round(cy + 4));
            g.drawLine((int) Math.round(x0 + w - labelPad), (int) Math.round(cy - 4),
                    (int) Math.round(x0 + w - labelPad), (int) Math.round(cy + 4));
        }

        for (int i = 0; i < n; i++) {
            double xi = model.getElementX(i); // wavelength units, centered at 0
            double frac = xi / halfExtent;
            double sx = x0 + w / 2.0 + frac * halfW;
            double a = amps[i];
            double r = minMarker + (maxMarker - minMarker) * (a / maxA);

            // element marker
            g.setPaint(elementColor);
            g.fillOval(sx - r, cy - r, 2 * r, 2 * r);
            g.setPaint(elementStroke);
            g.setStroke(new BasicStroke(1.2f));
            g.drawOval(sx - r, cy - r, 2 * r, 2 * r);

            // phase-offset arrow (drawn from the marker edge)
            if (showPhase) {
                double phi = model.getPhaseOffset(i);
                double len = r + Math.max(6.0, h * 0.04);
                double ax = sx + len * Math.sin(phi);
                double ay = cy - len * Math.cos(phi);
                g.setPaint(phaseColor);
                float arrowStroke = (float) Math.max(2.0, Math.min(3.0, h * 0.0045));
                g.setStroke(new BasicStroke(arrowStroke));
                g.drawLine((int) Math.round(sx), (int) Math.round(cy),
                        (int) Math.round(ax), (int) Math.round(ay));
                // arrow head - bigger
                double ang = Math.atan2(-(ay - cy), ax - sx);
                double arrowSize = Math.max(8.0, h * 0.018);
                double hx1 = ax + arrowSize * Math.cos(ang + Math.toRadians(140));
                double hy1 = ay - arrowSize * Math.sin(ang + Math.toRadians(140));
                double hx2 = ax + arrowSize * Math.cos(ang - Math.toRadians(140));
                double hy2 = ay - arrowSize * Math.sin(ang - Math.toRadians(140));
                g.drawLine((int) Math.round(ax), (int) Math.round(ay),
                        (int) Math.round(hx1), (int) Math.round(hy1));
                g.drawLine((int) Math.round(ax), (int) Math.round(ay),
                        (int) Math.round(hx2), (int) Math.round(hy2));
            }

            // labels
            g.setFont(font(SANS_SERIF, PLAIN, Math.max(8.0, h * 0.022)));
            g.setPaint(labelColor);
            if (showIndex) {
                String idx = Integer.toString(i + 1);
                double iw = g.getFontMetrics().stringWidth(idx);
                g.drawString(idx, sx - iw / 2, cy + r + Math.max(10.0, h * 0.03));
            }
            if (showAmplitude) {
                String av = String.format("%.2f", a);
                double aw = g.getFontMetrics().stringWidth(av);
                g.drawString(av, sx - aw / 2, cy - r - Math.max(4.0, h * 0.02));
            }
        }

        // title
        if (hasTitle) {
            g.setFont(font(SANS_SERIF, BOLD, Math.max(11.0, h * 0.05)));
            g.setPaint(titleColor);
            double tw = g.getFontMetrics().stringWidth(title);
            g.drawString(title, x0 + w / 2 - tw / 2, y0 + titleH * 0.62);
        }

        // footer: configuration summary
        String taper = Props.string(ctx, "taper", ArrayTapers.UNIFORM);
        double[] explicit = Props.dblArray(ctx, "amplitude");
        boolean custom = explicit != null && explicit.length > 0;
        String taperTxt = custom ? "explicite" : taper;
        String footer = String.format("N = %d │ d/λ = %.2f │ θ0 = %.1f° │ β = %.1f° │ Taper = %s",
                n, model.getDOverLambda(), model.getSteeringAngleDeg(), model.getBetaDeg(), taperTxt);
        g.setFont(font(MONOSPACED, PLAIN, Math.max(8.0, h * 0.028)));
        g.setPaint(labelColor);
        double fw = g.getFontMetrics().stringWidth(footer);
        g.drawString(footer, x0 + w / 2 - fw / 2, y0 + h - footH * 0.55);
    }

    private static Font font(String family, int style, double sizePx) {
        return new Font(family, style, Math.max(7, (int) Math.round(sizePx)));
    }

    private static final int PLAIN = Font.PLAIN;
    private static final int BOLD = Font.BOLD;
    private static final String SANS_SERIF = "SansSerif";
    private static final String MONOSPACED = "Monospaced";
}