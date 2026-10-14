package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.antennas.phys.ColorRamp;
import net.thevpc.ntexup.extension.antennas.phys.LinearArrayModel;
import net.thevpc.ntexup.extension.antennas.phys.WaveFieldPhasorGrid;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

/**
 * {@code wave-field} — animated 2D wave field radiated by the array
 * (port of {@code WaveFieldPanel}).
 *
 * The field {@code E(x,y,t)} is rendered as a raster whose per-pixel phasor sums A, B are
 * frame-invariant and cached per node (see {@link WaveFieldPhasorGrid}); each animation
 * frame only re-evaluates {@code A*cos(wt) - B*sin(wt)} (instant mode) or the static
 * intensity {@code A^2 + B^2}. Two bicolor color ramps map the instantaneous field, a
 * powered two-color ramp maps the intensity. Wavefront rings and steering rays are drawn
 * as vector overlays.
 *
 * Properties:
 * <ul>
 *   <li>array: {@code n}, {@code d-lambda}, {@code lambda}, {@code frequency},
 *       {@code steering} / {@code beta}, {@code taper}, {@code chebyshev-sll},
 *       {@code amplitude}, {@code phase}, {@code element-pattern} (see {@link ArrayConfig});</li>
 *   <li>field: {@code mode} (instant|intensity), {@code resolution} (default 160),
 *       {@code x-range}, {@code y-range} (physical bounds in wavelengths),
 *       {@code time} (°, print mode), {@code speed} (°/s, live mode), {@code animate}
 *       (flag, default true), {@code instant-scale} (1.4), {@code intensity-scale} (1.5),
 *       {@code distance-eps} (0.35), {@code instant-gamma} / {@code intensity-gamma}
 *       (1.0);</li>
 *   <li>overlays: {@code show-wavefronts}, {@code show-rays}, {@code show-axis},
 *       {@code show-elements}, {@code show-colorbar}, {@code show-header}, {@code title};</li>
 *   <li>{@code theme} (dark|light) and one color property per visual element.</li>
 * </ul>
 */
public class WaveFieldBuilder implements NTxNodeBuilder {

    private static final String GRID_CACHE = "ntexup-antennas.wave-field.grid";

    private static final int RING_ALPHA = 70;
    private static final int RAY_ALPHA = 80;
    private static final int AXIS_ALPHA = 130;
    private static final int HDR_FILL_ALPHA = 195;
    private static final int HDR_BORDER_ALPHA = 170;

    // --- default palettes ---
    private static final Color D_BG = new Color(0x0F121C);
    private static final Color L_BG = new Color(0xFFFFFF);
    private static final Color D_RING = new Color(0x64A0DC);
    private static final Color L_RING = new Color(0x1F5C8E);
    private static final Color D_RAY = new Color(0x64DCFF);
    private static final Color L_RAY = new Color(0x0E7FBE);
    private static final Color D_AXIS = new Color(0xDCE6F5);
    private static final Color L_AXIS = new Color(0x33475C);
    private static final Color D_ELEMENT = new Color(0xFFD700);
    private static final Color L_ELEMENT = new Color(0xB8860B);
    private static final Color D_ELEMENT_STROKE = new Color(0xFFFFFF);
    private static final Color L_ELEMENT_STROKE = new Color(0x263238);
    private static final Color D_LABEL = new Color(0xDCE6F5);
    private static final Color L_LABEL = new Color(0x2B3B4D);
    private static final Color D_HDR_FILL = new Color(0x0F1420);
    private static final Color L_HDR_FILL = new Color(0xF1F5F9);
    private static final Color D_HDR_BORDER = new Color(0x4682BE);
    private static final Color L_HDR_BORDER = new Color(0x3B6C99);

    // field color ramps (theme aware)
    private static final Color D_CREST_LOW = new Color(0x001F4A);
    private static final Color D_CREST_HIGH = new Color(0x00DCFF);
    private static final Color L_CREST_LOW = new Color(0xDBEEFF);
    private static final Color L_CREST_HIGH = new Color(0x0B7EB2);
    private static final Color D_TROUGH_LOW = new Color(0x2B0A05);
    private static final Color D_TROUGH_HIGH = new Color(0xFF6B4B);
    private static final Color L_TROUGH_LOW = new Color(0xFFE9E2);
    private static final Color L_TROUGH_HIGH = new Color(0xC23B1D);
    private static final Color D_POWER_LOW = new Color(0x0A1226);
    private static final Color D_POWER_HIGH = new Color(0xFFE94E);
    private static final Color L_POWER_LOW = new Color(0xEAF0FA);
    private static final Color L_POWER_HIGH = new Color(0xC48A00);

    @Override
    public void build(NTxNodeBuilderContext c) {
        c.id("wave-field")
                .parseParam()
                .matchesNamedPair(
                        "n", "d-lambda", "lambda", "frequency", "steering", "beta",
                        "taper", "chebyshev-sll", "phase", "amplitude", "element-pattern",
                        "mode", "resolution", "time", "speed", "x-range", "y-range",
                        "instant-scale", "intensity-scale", "distance-eps",
                        "instant-gamma", "intensity-gamma", "title", "theme",
                        // colors
                        "bg", "ring-color", "ray-color", "axis-color",
                        "element-color", "element-stroke", "label-color",
                        "crest-low", "crest-high", "trough-low", "trough-high",
                        "power-low", "power-high",
                        "header-fill", "header-border")
                .asFlags()
                .matchesNamedPair("animate", "show-wavefronts", "show-rays", "show-axis",
                        "show-elements", "show-colorbar", "show-header")
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
        LinearArrayModel model = ArrayConfig.model(ctx);

        boolean intensity = isIntensityMode(ctx);
        int res = Math.max(24, Math.min(480, Props.integer(ctx, "resolution", 160)));
        double[] xr = Props.dblArray(ctx, "x-range");
        double[] yr = Props.dblArray(ctx, "y-range");
        double x0v = rangeMin(xr, -3.0);
        double x1v = rangeMax(xr, 3.0);
        double y0v = rangeMin(yr, -1.0);
        double y1v = rangeMax(yr, 5.0);
        if (x1v - x0v < 1e-3) {
            x0v = -3.0;
            x1v = 3.0;
        }
        if (y1v - y0v < 1e-3) {
            y0v = -1.0;
            y1v = 5.0;
        }
        final double xMin = x0v;
        final double xMax = x1v;
        final double yMin = y0v;
        final double yMax = y1v;

        // phase wt: static `time` in print mode, advancing by `speed` against the page clock in live mode
        boolean animate = Props.bool(ctx, "animate", true);
        double timeDeg = Props.dbl(ctx, "time", 0.0);
        double speed = Props.dbl(ctx, "speed", 90.0);
        double phaseT;
        if (animate && ctx.isAnimate() && ctx.isPageShowing()) {
            phaseT = Math.toRadians(((timeDeg + speed * ctx.pageElapsedSeconds()) % 360.0 + 360.0) % 360.0);
        } else {
            phaseT = Math.toRadians(timeDeg);
        }
        ctx.requestAnimationFrame(animate);

        double instantScale = Props.dbl(ctx, "instant-scale", WaveFieldPhasorGrid.DEFAULT_INSTANT_SCALE);
        double intensityScale = Props.dbl(ctx, "intensity-scale", WaveFieldPhasorGrid.DEFAULT_INTENSITY_SCALE);
        double distanceEps = Props.dbl(ctx, "distance-eps", WaveFieldPhasorGrid.DEFAULT_DISTANCE_EPS);

        Color panelBg = Props.color(ctx, "bg", D_BG, L_BG);
        g.setPaint(panelBg);
        g.fillRect(x0, y0, w, h);

        Color ringColor = PanelTheme.translucent(Props.color(ctx, "ring-color", D_RING, L_RING), RING_ALPHA);
        Color rayColor = PanelTheme.translucent(Props.color(ctx, "ray-color", D_RAY, L_RAY), RAY_ALPHA);
        Color axisColor = PanelTheme.translucent(Props.color(ctx, "axis-color", D_AXIS, L_AXIS), AXIS_ALPHA);
        Color elementColor = Props.color(ctx, "element-color", D_ELEMENT, L_ELEMENT);
        Color elementStroke = Props.color(ctx, "element-stroke", D_ELEMENT_STROKE, L_ELEMENT_STROKE);
        Color labelColor = Props.color(ctx, "label-color", D_LABEL, L_LABEL);

        double instantGamma = Math.max(0.2, Props.dbl(ctx, "instant-gamma", 1.0));
        double intensityGamma = Math.max(0.2, Props.dbl(ctx, "intensity-gamma", 1.0));
        ColorRamp crestRamp = rampOf(Props.color(ctx, "crest-low", D_CREST_LOW, L_CREST_LOW),
                Props.color(ctx, "crest-high", D_CREST_HIGH, L_CREST_HIGH), instantGamma);
        ColorRamp troughRamp = rampOf(Props.color(ctx, "trough-low", D_TROUGH_LOW, L_TROUGH_LOW),
                Props.color(ctx, "trough-high", D_TROUGH_HIGH, L_TROUGH_HIGH), instantGamma);
        ColorRamp powerRamp = rampOf(Props.color(ctx, "power-low", D_POWER_LOW, L_POWER_LOW),
                Props.color(ctx, "power-high", D_POWER_HIGH, L_POWER_HIGH), intensityGamma);

        // --- cached, frame-invariant phasor grid ---
        WaveFieldPhasorGrid grid = cacheGrid(ctx, model, res,
                xMin, xMax, yMin, yMax, distanceEps, instantScale, intensityScale);
        if (grid == null) {
            return;
        }

        // --- build raster frame ---
        BufferedImage img = new BufferedImage(res, res, BufferedImage.TYPE_INT_ARGB);
        int[] px = new int[res * res];
        for (int py = 0; py < res; py++) {
            for (int i = 0; i < res; i++) {
                int idx = grid.index(i, py);
                if (intensity) {
                    px[idx] = powerRamp.color(grid.intensity(idx));
                } else {
                    double e = grid.instantaneousNormalized(idx, phaseT);
                    px[idx] = e >= 0 ? crestRamp.color(e) : troughRamp.color(-e);
                }
            }
        }
        img.setRGB(0, 0, res, res, px, 0, res);
        g.graphics2D().drawImage(img, (int) Math.round(x0), (int) Math.round(y0),
                (int) Math.round(w), (int) Math.round(h), null);

        // --- vector overlays (phys -> screen mapping) ---
        double xSpan = xMax - xMin;
        double ySpan = yMax - yMin;
        java.util.function.DoubleUnaryOperator sx = p -> x0 + (p - xMin) / xSpan * w;
        java.util.function.DoubleUnaryOperator sy = p -> y0 + (yMax - p) / ySpan * h;

        boolean showRings = Props.bool(ctx, "show-wavefronts", true);
        boolean showRays = Props.bool(ctx, "show-rays", true);
        boolean showAxis = Props.bool(ctx, "show-axis", true);
        boolean showElements = Props.bool(ctx, "show-elements", true);
        boolean showColorbar = Props.bool(ctx, "show-colorbar", true);
        boolean showHeader = Props.bool(ctx, "show-header", true);
        String title = Props.string(ctx, "title", "Champ rayonné E(x, y, t)");

        int n = model.getNumElements();

        if (showRings) {
            g.setStroke(new BasicStroke(1.1f));
            double maxDist = Math.hypot(Math.max(Math.abs(xMin), Math.abs(xMax)),
                    Math.max(Math.abs(yMin), Math.abs(yMax)));
            int maxRing = Math.max(1, (int) Math.ceil(maxDist / model.getWavelength()));
            for (int i = 0; i < n; i++) {
                double ex = model.getElementX(i);
                for (int m = 1; m <= maxRing; m++) {
                    double rPhys = m * model.getWavelength();
                    Path2D.Double ring = new Path2D.Double();
                    for (int k = 0; k <= 64; k++) {
                        double a = k / 64.0 * 2.0 * Math.PI;
                        double pxh = ex + rPhys * Math.cos(a);
                        double pyh = rPhys * Math.sin(a);
                        double pxs = sx.applyAsDouble(pxh);
                        double pys = sy.applyAsDouble(pyh);
                        if (k == 0) {
                            ring.moveTo(pxs, pys);
                        } else {
                            ring.lineTo(pxs, pys);
                        }
                    }
                    ring.closePath();
                    g.setPaint(ringColor);
                    g.draw(ring);
                }
            }
        }

        if (showRays) {
            g.setStroke(new BasicStroke(1.3f));
            double steer = Math.toRadians(model.getSteeringAngleDeg());
            double dirX = Math.sin(steer);
            double dirY = Math.cos(steer);
            for (int i = 0; i < n; i++) {
                double ex = model.getElementX(i);
                double tEnd = rayEndT(ex, dirX, dirY, xMin, xMax, yMin, yMax);
                if (Double.isNaN(tEnd) || tEnd <= 1e-6) {
                    continue;
                }
                double ex2 = ex + dirX * tEnd;
                double ey2 = dirY * tEnd;
                g.setPaint(rayColor);
                g.drawLine(sx.applyAsDouble(ex), sy.applyAsDouble(0.0),
                        sx.applyAsDouble(ex2), sy.applyAsDouble(ey2));
            }
        }

        if (showAxis) {
            g.setPaint(axisColor);
            g.setStroke(new BasicStroke(1.2f));
            g.drawLine((int) Math.round(sx.applyAsDouble(xMin)), (int) Math.round(sy.applyAsDouble(0.0)),
                    (int) Math.round(sx.applyAsDouble(xMax)), (int) Math.round(sy.applyAsDouble(0.0)));
        }

        if (showElements) {
            double er = Math.max(2.5, Math.min(w, h) * 0.009);
            for (int i = 0; i < n; i++) {
                double esx = sx.applyAsDouble(model.getElementX(i));
                double esy = sy.applyAsDouble(0.0);
                g.setPaint(elementColor);
                g.fillOval(esx - er, esy - er, 2 * er, 2 * er);
                g.setPaint(elementStroke);
                g.drawOval(esx - er, esy - er, 2 * er, 2 * er);
            }
        }

        // --- colorbar (right edge) ---
        if (showColorbar) {
            double cbW = Math.max(12.0, Math.min(26.0, w * 0.04));
            double cbX = x0 + w - cbW - 6;
            double cbY = y0 + h * 0.08;
            double cbH = h * 0.84;
            int cbRes = 96;
            BufferedImage cb = new BufferedImage((int) Math.max(4, cbW), cbRes, BufferedImage.TYPE_INT_ARGB);
            for (int row = 0; row < cbRes; row++) {
                double t = (cbRes - 1 - row) / (double) (cbRes - 1); // top = 1
                int col;
                if (intensity) {
                    col = powerRamp.color(t);
                } else {
                    col = t >= 0.5 ? crestRamp.color(2 * t - 1) : troughRamp.color(1 - 2 * t);
                }
                for (int ccol = 0; ccol < cb.getWidth(); ccol++) {
                    cb.setRGB(ccol, row, col);
                }
            }
            g.graphics2D().drawImage(cb, (int) Math.round(cbX), (int) Math.round(cbY),
                    (int) Math.round(cbW), (int) Math.round(cbH), null);
            g.setPaint(labelColor);
            g.setFont(font(SANS_SERIF, PLAIN, Math.max(8.0, h * 0.017)));
            String hi = intensity ? "1.0" : "+1";
            String lo = intensity ? "0.0" : "-1";
            double htw = g.getFontMetrics().stringWidth(hi);
            g.drawString(hi, cbX - htw / 2, cbY + 8);
            double ltw = g.getFontMetrics().stringWidth(lo);
            g.drawString(lo, cbX - ltw / 2, cbY + cbH + Math.max(10.0, h * 0.02));
        }

        // --- header overlay ---
        if (showHeader) {
            String modeTxt = intensity ? "Intensité I(x,y)" : "Champ instantané E(x,y,t)";
            String cfg = String.format("%s │ N = %d │ d/λ = %.2f │ θ0 = %.1f° │ %s",
                    modeTxt, n, model.getDOverLambda(), model.getSteeringAngleDeg(),
                    model.getElementPattern().label());
            Color hdrFill = PanelTheme.translucent(Props.color(ctx, "header-fill", D_HDR_FILL, L_HDR_FILL), HDR_FILL_ALPHA);
            Color hdrBorder = PanelTheme.translucent(Props.color(ctx, "header-border", D_HDR_BORDER, L_HDR_BORDER), HDR_BORDER_ALPHA);
            double hx = x0 + 10;
            double hy = y0 + 8;
            double hw = Math.min(w - 20, Math.max(200.0, w * 0.72));
            double hh = Math.max(42.0, h * 0.09);
            g.setPaint(hdrFill);
            g.fillRoundRect(hx, hy, hw, hh, 8, 8);
            g.setPaint(hdrBorder);
            g.setStroke(new BasicStroke(1.1f));
            g.drawRoundRect(hx, hy, hw, hh, 8, 8);
            g.setFont(font(SANS_SERIF, BOLD, Math.max(9.0, h * 0.022)));
            g.setPaint(labelColor);
            g.drawString(title, hx + 10, hy + hh * 0.34);
            g.setFont(font(MONOSPACED, PLAIN, Math.max(8.0, h * 0.018)));
            double sw = g.getFontMetrics().stringWidth(cfg);
            String cfg2 = sw > hw - 22 ? cfg.replaceAll(" │ ", " · ") : cfg;
            g.drawString(cfg2, hx + 10, hy + hh * 0.75);
        }
    }

    private static ColorRamp rampOf(Color low, Color high, double gamma) {
        return new ColorRamp(low.getRed(), low.getGreen(), low.getBlue(),
                high.getRed(), high.getGreen(), high.getBlue(), gamma);
    }

    private static boolean isIntensityMode(NTxRendererContext ctx) {
        String m = Props.string(ctx, "mode", "instant");
        return "intensity".equalsIgnoreCase(m) || "power".equalsIgnoreCase(m) || "i".equalsIgnoreCase(m);
    }

    private static double rangeMin(double[] r, double def) {
        if (r == null || r.length < 2) {
            return def;
        }
        return Math.min(r[0], r[1]);
    }

    private static double rangeMax(double[] r, double def) {
        if (r == null || r.length < 2) {
            return def;
        }
        return Math.max(r[0], r[1]);
    }

    /**
     * Ray parameter t where the ray from (ex, 0) along (dirX, dirY) leaves the physical box.
     */
    private static double rayEndT(double ex, double dirX, double dirY,
                                  double xMin, double xMax, double yMin, double yMax) {
        double t = Double.POSITIVE_INFINITY;
        if (Math.abs(dirX) > 1e-9) {
            double tX = dirX > 0 ? (xMax - ex) / dirX : (xMin - ex) / dirX;
            if (tX > 0) {
                t = Math.min(t, tX);
            }
        }
        if (Math.abs(dirY) > 1e-9) {
            double tY = dirY > 0 ? yMax / dirY : yMin / dirY;
            if (tY > 0) {
                t = Math.min(t, tY);
            }
        }
        return t == Double.POSITIVE_INFINITY ? Double.NaN : t;
    }

    /**
     * Returns the cached phasor grid for the current model geometry, rebuilding it when any
     * frame-invariant input changes.
     */
    private static WaveFieldPhasorGrid cacheGrid(NTxRendererContext ctx, LinearArrayModel model,
                                                 int res, double xMin, double xMax,
                                                 double yMin, double yMax,
                                                 double distanceEps, double instantScale, double intensityScale) {
        NTxNode node = ctx.node();
        String sig = signature(model, res, xMin, xMax, yMin, yMax, distanceEps, instantScale, intensityScale);
        Object cached = node.getRenderCache(GRID_CACHE).orNull();
        if (cached instanceof GridCache) {
            GridCache gc = (GridCache) cached;
            if (gc.sig.equals(sig)) {
                return gc.grid;
            }
        }
        int n = model.getNumElements();
        double[] ex = new double[n];
        double[] amps = new double[n];
        double[] phases = new double[n];
        for (int i = 0; i < n; i++) {
            ex[i] = model.getElementX(i);
            amps[i] = model.getAmplitude(i);
            phases[i] = model.getPhaseOffset(i);
        }
        WaveFieldPhasorGrid grid = new WaveFieldPhasorGrid(ex, amps, phases,
                model.getBeta(), model.getWavenumberK(), model.getElementPattern(),
                xMin, xMax, yMin, yMax, res, res, distanceEps, instantScale, intensityScale);
        node.setRenderCache(GRID_CACHE, new GridCache(sig, grid));
        return grid;
    }

    private static String signature(LinearArrayModel m, int res,
                                    double xMin, double xMax, double yMin, double yMax,
                                    double eps, double iscale, double pscale) {
        return "n=" + m.getNumElements()
                + ",d=" + m.getDOverLambda()
                + ",l=" + m.getWavelength()
                + ",f=" + m.getFrequency()
                + ",b=" + m.getBeta()
                + ",a=" + Arrays.toString(m.getAmplitudes())
                + ",p=" + Arrays.toString(phaseArray(m))
                + ",pat=" + m.getElementPattern()
                + ",r=" + res
                + ",x=" + xMin + ".." + xMax
                + ",y=" + yMin + ".." + yMax
                + ",eps=" + eps
                + ",is=" + iscale
                + ",ps=" + pscale;
    }

    private static double[] phaseArray(LinearArrayModel m) {
        double[] p = new double[m.getNumElements()];
        for (int i = 0; i < p.length; i++) {
            p[i] = m.getPhaseOffset(i);
        }
        return p;
    }

    private static Font font(String family, int style, double sizePx) {
        return new Font(family, style, Math.max(7, (int) Math.round(sizePx)));
    }

    private static final int PLAIN = Font.PLAIN;
    private static final int BOLD = Font.BOLD;
    private static final String SANS_SERIF = "SansSerif";
    private static final String MONOSPACED = "Monospaced";

    /**
     * Render-cache holder pairing the phasor grid with the signature it was built from.
     */
    private static final class GridCache {

        final String sig;
        final WaveFieldPhasorGrid grid;

        GridCache(String sig, WaveFieldPhasorGrid grid) {
            this.sig = sig;
            this.grid = grid;
        }
    }
}