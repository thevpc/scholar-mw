package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.antennas.phys.ColorRamp;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * {@code field-colorbar} — standalone two-color gradient color bar with an optional
 * midpoint, tick marks and end labels. Used to explain the color mapping of the
 * {@link WaveFieldBuilder} wave-field component.
 *
 * Properties:
 * <ul>
 *   <li>{@code low-color}, {@code mid-color} (optional), {@code high-color},
 *       {@code gamma} (default 1.0);</li>
 *   <li>{@code orientation} (vertical|horizontal, default vertical), {@code title},
 *       {@code min-label} (default 0), {@code max-label} (default 1),
 *       {@code tick-count} (default 5);</li>
 *   <li>flags {@code show-ticks}, {@code show-frame}, {@code show-title};</li>
 *   <li>{@code theme} (dark|light) and color properties for labels, ticks and frame.</li>
 * </ul>
 */
public class FieldColorBarBuilder implements NTxNodeBuilder {

    private static final Color D_BG = new Color(0x0F121C);
    private static final Color L_BG = new Color(0xFFFFFF);
    private static final Color D_LABEL = new Color(0xB4C8DC);
    private static final Color L_LABEL = new Color(0x3A4F64);
    private static final Color D_TICK = new Color(0xDCE6F5);
    private static final Color L_TICK = new Color(0x33475C);
    private static final Color D_FRAME = new Color(0x4682BE);
    private static final Color L_FRAME = new Color(0x3B6C99);
    private static final Color D_TITLE = new Color(0x64DCFF);
    private static final Color L_TITLE = new Color(0x0B5E83);

    @Override
    public void build(NTxNodeBuilderContext c) {
        c.id("field-colorbar")
                .parseParam()
                .matchesNamedPair(
                        "low-color", "mid-color", "high-color", "gamma",
                        "orientation", "title", "min-label", "max-label", "tick-count",
                        "theme",
                        // colors
                        "bg", "label-color", "tick-color", "frame-color", "title-color")
                .asFlags()
                .matchesNamedPair("show-ticks", "show-frame", "show-title")
                .end()
                .renderComponent(this::render);
    }

    private void render(NTxRendererContext ctx) {
        NTxBounds2D b = ctx.selfBounds2D();
        if (b.widthX() <= 8 || b.widthY() <= 8) {
            return;
        }
        ctx.withinClip(b, this::renderBody);
        ctx.drawContour();
    }

    private void renderBody(NTxRendererContext ctx) {
        NTxBounds2D b = ctx.selfBounds2D();
        NTxGraphics g = ctx.graphics();
        double x0 = b.minX(), y0 = b.minY(), w = b.widthX(), h = b.widthY();
        if (w <= 8 || h <= 8) {
            return;
        }
        boolean vertical = !"horizontal".equalsIgnoreCase(Props.string(ctx, "orientation", "vertical"));
        double gamma = Math.max(0.2, Props.dbl(ctx, "gamma", 1.0));
        boolean showTicks = Props.bool(ctx, "show-ticks", true);
        boolean showFrame = Props.bool(ctx, "show-frame", true);
        boolean showTitle = Props.bool(ctx, "show-title", true);
        int tickCount = Math.max(1, Math.min(20, Props.integer(ctx, "tick-count", 5)));
        String title = Props.string(ctx, "title", "");

        Color low = Props.color(ctx, "low-color", new Color(0x0A1226), new Color(0xEAF0FA));
        Color high = Props.color(ctx, "high-color", new Color(0xFFE94E), new Color(0xC48A00));
        String midProp = null;
        Paint midP = ctx.getColorProperty("mid-color").orElse(null);
        boolean hasMid = midP instanceof Color;
        Color mid = hasMid ? (Color) midP : null;

        Color panelBg = Props.color(ctx, "bg", D_BG, L_BG);
        g.setPaint(panelBg);
        g.fillRect(x0, y0, w, h);

        Color labelColor = Props.color(ctx, "label-color", D_LABEL, L_LABEL);
        Color tickColor = Props.color(ctx, "tick-color", D_TICK, L_TICK);
        Color frameColor = Props.color(ctx, "frame-color", D_FRAME, L_FRAME);
        Color titleColor = Props.color(ctx, "title-color", D_TITLE, L_TITLE);

        boolean hasTitleTxt = showTitle && title != null && !title.trim().isEmpty();
        double titleH = hasTitleTxt ? h * 0.16 : 0.0;

        double pad = Math.max(4.0, Math.min(w, h) * 0.03);
        double bx, by, bw, bh;
        if (vertical) {
            bw = Math.max(10.0, w - 2 * pad);
            bh = h - titleH - 2 * pad - Math.max(6.0, h * 0.04);
            bx = x0 + (w - bw) / 2.0;
            by = y0 + titleH + pad;
        } else {
            bh = Math.max(10.0, h - 2 * pad);
            bw = w - 2 * pad - Math.max(6.0, w * 0.04);
            bx = x0 + pad;
            by = y0 + (h - bh) / 2.0;
        }

        // gradient image sampling the (optionally two-stage) ramp
        int gres = 128;
        BufferedImage img = new BufferedImage(vertical ? 8 : gres, vertical ? gres : 8, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < gres; i++) {
            double t = i / (double) (gres - 1);
            int col = rampColor(low, mid, high, hasMid, t, gamma);
            if (vertical) {
                for (int c = 0; c < 8; c++) {
                    img.setRGB(c, i, col); // row 0 = bottom (t=0 low)
                }
            } else {
                for (int r = 0; r < 8; r++) {
                    img.setRGB(i, r, col);
                }
            }
        }
        g.graphics2D().drawImage(img, (int) Math.round(bx), (int) Math.round(by),
                (int) Math.round(bw), (int) Math.round(bh), null);

        if (showFrame) {
            g.setPaint(frameColor);
            g.setStroke(new BasicStroke(1.2f));
            g.drawRect(bx, by, bw, bh);
        }

        if (showTicks) {
            g.setPaint(tickColor);
            g.setStroke(new BasicStroke(1.1f));
            for (int i = 0; i <= tickCount; i++) {
                double t = i / (double) tickCount;
                double tp;
                if (vertical) {
                    tp = by + bh * t; // low (0) at bottom
                    g.drawLine((int) Math.round(bx + bw), (int) Math.round(tp),
                            (int) Math.round(bx + bw + 5), (int) Math.round(tp));
                } else {
                    tp = bx + bw * t; // low (0) at left
                    g.drawLine((int) Math.round(tp), (int) Math.round(by + bh),
                            (int) Math.round(tp), (int) Math.round(by + bh + 5));
                }
            }
        }

        // end labels
        String minLbl = Props.string(ctx, "min-label", "0");
        String maxLbl = Props.string(ctx, "max-label", "1");
        g.setFont(font(SANS_SERIF, PLAIN, Math.max(8.0, Math.min(w, h) * 0.045)));
        g.setPaint(labelColor);
        if (vertical) {
            double mw2 = g.getFontMetrics().stringWidth(minLbl);
            g.drawString(minLbl, bx + bw / 2 - mw2 / 2, by + bh + Math.max(9.0, h * 0.04));
            double mw1 = g.getFontMetrics().stringWidth(maxLbl);
            g.drawString(maxLbl, bx + bw / 2 - mw1 / 2, by - 4);
        } else {
            double mw1 = g.getFontMetrics().stringWidth(minLbl);
            g.drawString(minLbl, bx, by + bh + Math.max(9.0, h * 0.04));
            double mw2 = g.getFontMetrics().stringWidth(maxLbl);
            g.drawString(maxLbl, bx + bw - mw2, by + bh + Math.max(9.0, h * 0.04));
        }

        // title (top, vertical only; horizontal uses the left)
        if (hasTitleTxt) {
            g.setFont(font(SANS_SERIF, BOLD, Math.max(9.0, h * 0.05)));
            g.setPaint(titleColor);
            double tw = g.getFontMetrics().stringWidth(title);
            g.drawString(title, x0 + w / 2 - tw / 2, y0 + titleH * 0.62);
        }
    }

    private static int rampColor(Color low, Color mid, Color high, boolean hasMid, double t, double gamma) {
        if (hasMid) {
            if (t < 0.5) {
                ColorRamp r = new ColorRamp(low.getRed(), low.getGreen(), low.getBlue(),
                        mid.getRed(), mid.getGreen(), mid.getBlue(), gamma);
                return r.color(2 * t);
            }
            ColorRamp r = new ColorRamp(mid.getRed(), mid.getGreen(), mid.getBlue(),
                    high.getRed(), high.getGreen(), high.getBlue(), gamma);
            return r.color(2 * (t - 0.5));
        }
        ColorRamp r = new ColorRamp(low.getRed(), low.getGreen(), low.getBlue(),
                high.getRed(), high.getGreen(), high.getBlue(), gamma);
        return r.color(t);
    }

    private static Font font(String family, int style, double sizePx) {
        return new Font(family, style, Math.max(7, (int) Math.round(sizePx)));
    }

    private static final int PLAIN = Font.PLAIN;
    private static final int BOLD = Font.BOLD;
    private static final String SANS_SERIF = "SansSerif";
    private static final String MONOSPACED = "Monospaced";
}