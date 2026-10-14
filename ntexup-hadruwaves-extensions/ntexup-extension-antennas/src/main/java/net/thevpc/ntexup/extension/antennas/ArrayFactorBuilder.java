package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.antennas.phys.ArrayFactorMath;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

/**
 * {@code array-factor} — the "Facteur de réseau graphique rapide" nomogram
 * (port of {@code FastGraphicalArrayFactorPanel}).
 *
 * Top: universal array function Fr(2γ) = |sin(Nγ)/(N·sinγ)| with its N−1 equidistant zeros
 * at 2γ = 360·m/N. Bottom: semi-circle of radius βd centered at 2γ = α, visibility zone
 * [α−βd, α+βd], the vertical projections linking curve points to the circle, and the polar
 * pattern r(ψ) = Fr·βd traced inside it. An animated scanning probe sweeps ψ when the
 * renderer supports animation.
 *
 * Properties:
 * <ul>
 *   <li>{@code n} (2..12, default 5), {@code beta-d} (°, default 216), {@code alpha}
 *       (°, default 124), {@code psi} (°, default 55), {@code psi-speed} (°/s, default 26.7);</li>
 *   <li>{@code animate} (flag, default true), {@code show-projections} (default true),
 *       {@code show-rays} (default true), {@code show-visibility} (default true),
 *       {@code show-probe} (default true), {@code show-header} (default true);</li>
 *   <li>{@code theme} (dark|light) and the whole palette of colors, one property per
 *       visual element (see {@link PanelTheme}).</li>
 * </ul>
 */
public class ArrayFactorBuilder implements NTxNodeBuilder {

    // alpha overlays shared across the drawing helpers (documented, not scattered literals)
    private static final int FILL_ALPHA = 25;        // fill under Fr(2γ) and visibility band
    private static final int GRID_ALPHA_SOFT = 25;   // minor amplitude grid lines
    private static final int GRID_ALPHA_STRONG = 50; // major (1.0) amplitude grid line
    private static final int GRID_LBL_ALPHA = 150;
    private static final int VIS_FILL_ALPHA = 45;
    private static final int VIS_LINE_ALPHA = 140;
    private static final int ZONE_LBL_ALPHA = 180;
    private static final int PROJ_ALPHA = 110;
    private static final int RAY_ALPHA = 80;
    private static final int SEMI_ALPHA = 120;
    private static final int PROBE_LINE_ALPHA = 200;
    private static final int PROBE_ARC_ALPHA = 180;
    private static final int POLAR_FILL_ALPHA = 90;
    private static final int HDR_FILL_ALPHA = 210;
    private static final int HDR_BORDER_ALPHA = 180;
    private static final int TICK_ALPHA_MAJOR = 160;
    private static final int TICK_ALPHA_MINOR = 70;

    // --- default palettes (original simulator colors, plus light-theme adaptations) ---
    private static final Color D_BG = new Color(0x0F121C);
    private static final Color L_BG = new Color(0xFFFFFF);
    private static final Color D_FILL = new Color(0x00B4FF);
    private static final Color L_FILL = new Color(0x0E7FBE);
    private static final Color D_CURVE = new Color(0x00DCFF);
    private static final Color L_CURVE = new Color(0x007FB8);
    private static final Color D_ZERO = new Color(0xFF4B4B);
    private static final Color L_ZERO = new Color(0xD32F2F);
    private static final Color D_ZERO_LBL = new Color(0xFF8282);
    private static final Color L_ZERO_LBL = new Color(0xB71C1C);
    private static final Color D_MAX = new Color(0xFFD700);
    private static final Color L_MAX = new Color(0xB8860B);
    private static final Color D_MAX_LBL = new Color(0xFFE146);
    private static final Color L_MAX_LBL = new Color(0x9A6F00);
    private static final Color D_GRID = new Color(0xFFFFFF);
    private static final Color L_GRID = new Color(0x37474F);
    private static final Color D_GRID_LBL = new Color(0xAABED2);
    private static final Color L_GRID_LBL = new Color(0x546E7A);
    private static final Color D_AXIS = new Color(0xDCE6F5);
    private static final Color L_AXIS = new Color(0x33475C);
    private static final Color D_AXIS_LBL = new Color(0xB4C8DC);
    private static final Color L_AXIS_LBL = new Color(0x3A4F64);
    private static final Color D_SEMI = new Color(0x64A0DC);
    private static final Color L_SEMI = new Color(0x1F5C8E);
    private static final Color D_SEMI_LBL = new Color(0xB4CDE6);
    private static final Color L_SEMI_LBL = new Color(0x2E5578);
    private static final Color D_RADIUS_LBL = new Color(0x82B4F0);
    private static final Color L_RADIUS_LBL = new Color(0x1D5B9E);
    private static final Color D_VIS_FILL = new Color(0x285A3C);
    private static final Color L_VIS_FILL = new Color(0x4FA37A);
    private static final Color D_VIS_LINE = new Color(0x3CB46E);
    private static final Color L_VIS_LINE = new Color(0x2E7D56);
    private static final Color D_ZONE_LBL = new Color(0x50DC8C);
    private static final Color L_ZONE_LBL = new Color(0x1B6B45);
    private static final Color D_PROJ = new Color(0x96B4DC);
    private static final Color L_PROJ = new Color(0x26435F);
    private static final Color D_RAY = new Color(0x6496DC);
    private static final Color L_RAY = new Color(0x2A4A75);
    private static final Color D_DOT = new Color(0xFFFFFF);
    private static final Color L_DOT = new Color(0x263238);
    private static final Color D_PROBE_LINE = new Color(0xFF64FF);
    private static final Color L_PROBE_LINE = new Color(0xB13DB1);
    private static final Color D_PROBE_ARC = new Color(0xFFB400);
    private static final Color L_PROBE_ARC = new Color(0xB3780A);
    private static final Color D_PROBE_PT = new Color(0xFF50FF);
    private static final Color L_PROBE_PT = new Color(0xAD33AD);
    private static final Color D_PROBE_CIRC = new Color(0x64DCFF);
    private static final Color L_PROBE_CIRC = new Color(0x0E8FBB);
    private static final Color D_PROBE_PAT = new Color(0xFFE632);
    private static final Color L_PROBE_PAT = new Color(0x916E08);
    private static final Color D_PROBE_RO = new Color(0xFFF096);
    private static final Color L_PROBE_RO = new Color(0x6B5410);
    private static final Color D_HDR_FILL = new Color(0x0F1420);
    private static final Color L_HDR_FILL = new Color(0xF1F5F9);
    private static final Color D_HDR_BORDER = new Color(0x4682BE);
    private static final Color L_HDR_BORDER = new Color(0x3B6C99);
    private static final Color D_HDR_TITLE = new Color(0x64DCFF);
    private static final Color L_HDR_TITLE = new Color(0x0B5E83);
    private static final Color D_HDR_TEXT = new Color(0xDCE6F5);
    private static final Color L_HDR_TEXT = new Color(0x2B3B4D);

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext
                .id("array-factor")
                .parseParam()
                .matchesNamedPair("n", "beta-d", "alpha", "psi", "psi-speed")
                .matchesNamedPair(
                        "bg", "fill-color", "curve-color", "zero-color", "zero-label-color",
                        "max-color", "max-label-color", "grid-color", "grid-label-color",
                        "axis-color", "axis-label-color", "semicircle-color", "semicircle-label-color",
                        "radius-label-color", "visibility-fill", "visibility-line", "zone-label-color",
                        "projection-color", "ray-color", "circumference-dot-color",
                        "probe-line-color", "probe-arc-color", "probe-point-color",
                        "probe-circle-color", "probe-pattern-color", "probe-readout-color",
                        "header-fill", "header-border", "header-title-color", "header-text-color")
                .matchesNamedPair("theme", "title")
                .asFlags()
                .matchesNamedPair("animate", "show-projections", "show-rays", "show-visibility",
                        "show-probe", "show-header")
                .end()
                .renderComponent(this::render);
    }

    private void render(NTxRendererContext ctx) {
        NTxBounds2D b = ctx.selfBounds2D();
        if (b.widthX() <= 2 || b.widthY() <= 2) {
            return;
        }
        ctx.withinClip(b, this::renderBody);
        ctx.drawContour();
    }

    private void renderBody(NTxRendererContext ctx) {
        NTxBounds2D b = ctx.selfBounds2D();
        NTxGraphics g = ctx.graphics();
        double x0 = b.minX(), y0 = b.minY(), w = b.widthX(), h = b.widthY();
        if (w <= 2 || h <= 2) {
            return;
        }
        PanelTheme theme = PanelTheme.of(ctx);

        // panel background (explicit `bg` color property or theme default)
        Color panelBg = PanelTheme.color(ctx, "bg", D_BG, L_BG);
        g.setPaint(panelBg);
        g.fillRect(x0, y0, w, h);

        // --- geometry parameters ---
        int n = Math.max(2, Math.min(12, Props.integer(ctx, "n", 5)));
        double betaD = Math.max(5, Props.dbl(ctx, "beta-d", 216.0));
        double alpha = Props.dbl(ctx, "alpha", 124.0);

        // scanning angle psi (static in print mode; advanced by wall clock in live mode)
        boolean animate = Props.bool(ctx, "animate", true);
        double psi = Props.dbl(ctx, "psi", 55.0);
        if (animate && ctx.isAnimate() && ctx.isPageShowing()) {
            double psiSpeed = Props.dbl(ctx, "psi-speed", 26.7);
            psi = psi + psiSpeed * ctx.pageElapsedSeconds();
            psi = ((psi % 180.0) + 180.0) % 180.0;
        }
        ctx.requestAnimationFrame(animate);

        boolean showVisibility = Props.bool(ctx, "show-visibility", true);
        boolean showProjections = Props.bool(ctx, "show-projections", true);
        boolean showRays = Props.bool(ctx, "show-rays", true);
        boolean showProbe = Props.bool(ctx, "show-probe", true);
        boolean showHeader = Props.bool(ctx, "show-header", true);
        String title = Props.string(ctx, "title", "Facteur de Réseau Graphique Rapide");

        // --- colors ---
        Color fillColor = alpha(PanelTheme.color(ctx, "fill-color", D_FILL, L_FILL), FILL_ALPHA);
        Color curveColor = PanelTheme.color(ctx, "curve-color", D_CURVE, L_CURVE);
        Color zeroColor = PanelTheme.color(ctx, "zero-color", D_ZERO, L_ZERO);
        Color zeroLbl = PanelTheme.color(ctx, "zero-label-color", D_ZERO_LBL, L_ZERO_LBL);
        Color maxColor = PanelTheme.color(ctx, "max-color", D_MAX, L_MAX);
        Color maxLbl = PanelTheme.color(ctx, "max-label-color", D_MAX_LBL, L_MAX_LBL);
        Color gridColor = PanelTheme.color(ctx, "grid-color", D_GRID, L_GRID);
        Color gridLbl = PanelTheme.color(ctx, "grid-label-color", D_GRID_LBL, L_GRID_LBL);
        Color axisColor = PanelTheme.color(ctx, "axis-color", D_AXIS, L_AXIS);
        Color axisLbl = PanelTheme.color(ctx, "axis-label-color", D_AXIS_LBL, L_AXIS_LBL);
        Color semiColor = PanelTheme.color(ctx, "semicircle-color", D_SEMI, L_SEMI);
        Color semiLbl = PanelTheme.color(ctx, "semicircle-label-color", D_SEMI_LBL, L_SEMI_LBL);
        Color radiusLbl = PanelTheme.color(ctx, "radius-label-color", D_RADIUS_LBL, L_RADIUS_LBL);
        Color visFill = alpha(PanelTheme.color(ctx, "visibility-fill", D_VIS_FILL, L_VIS_FILL), VIS_FILL_ALPHA);
        Color visLine = alpha(PanelTheme.color(ctx, "visibility-line", D_VIS_LINE, L_VIS_LINE), VIS_LINE_ALPHA);
        Color zoneLbl = alpha(PanelTheme.color(ctx, "zone-label-color", D_ZONE_LBL, L_ZONE_LBL), ZONE_LBL_ALPHA);
        Color projColor = alpha(PanelTheme.color(ctx, "projection-color", D_PROJ, L_PROJ), PROJ_ALPHA);
        Color rayColor = alpha(PanelTheme.color(ctx, "ray-color", D_RAY, L_RAY), RAY_ALPHA);
        Color dotColor = PanelTheme.color(ctx, "circumference-dot-color", D_DOT, L_DOT);
        Color probeLine = alpha(PanelTheme.color(ctx, "probe-line-color", D_PROBE_LINE, L_PROBE_LINE), PROBE_LINE_ALPHA);
        Color probeArc = alpha(PanelTheme.color(ctx, "probe-arc-color", D_PROBE_ARC, L_PROBE_ARC), PROBE_ARC_ALPHA);
        Color probePt = PanelTheme.color(ctx, "probe-point-color", D_PROBE_PT, L_PROBE_PT);
        Color probeCirc = PanelTheme.color(ctx, "probe-circle-color", D_PROBE_CIRC, L_PROBE_CIRC);
        Color probePat = PanelTheme.color(ctx, "probe-pattern-color", D_PROBE_PAT, L_PROBE_PAT);
        Color probeRo = PanelTheme.color(ctx, "probe-readout-color", D_PROBE_RO, L_PROBE_RO);
        Color hdrFill = alpha(PanelTheme.color(ctx, "header-fill", D_HDR_FILL, L_HDR_FILL), HDR_FILL_ALPHA);
        Color hdrBorder = alpha(PanelTheme.color(ctx, "header-border", D_HDR_BORDER, L_HDR_BORDER), HDR_BORDER_ALPHA);
        Color hdrTitle = PanelTheme.color(ctx, "header-title-color", D_HDR_TITLE, L_HDR_TITLE);
        Color hdrText = PanelTheme.color(ctx, "header-text-color", D_HDR_TEXT, L_HDR_TEXT);

        // --- layout geometry (port of the original panel) ---
        double axisY = y0 + h * 0.45;
        double topPlotHeight = Math.max(30, axisY - y0 - Math.max(24, h * 0.09));
        double minVis = alpha - betaD;
        double maxVis = alpha + betaD;
        double plotMin2G = Math.min(-90.0, Math.floor((minVis - 40.0) / 45.0) * 45.0);
        double plotMax2G = Math.max(450.0, Math.ceil((maxVis + 40.0) / 45.0) * 45.0);
        double marginX = Math.max(28, w * 0.09);
        double plotW = w - 2 * marginX;

        java.util.function.DoubleUnaryOperator toX = twoG ->
                marginX + x0 + ((twoG - plotMin2G) / (plotMax2G - plotMin2G)) * plotW;

        // 1. visibility zone background + boundaries
        if (showVisibility) {
            double vx1 = toX.applyAsDouble(minVis);
            double vx2 = toX.applyAsDouble(maxVis);
            g.setPaint(visFill);
            g.fillRect(vx1, y0 + h * 0.06, vx2 - vx1, y0 + h - (y0 + h * 0.06));
            g.setPaint(visLine);
            g.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{4, 4}, 0f));
            g.drawLine(vx1, y0 + h * 0.06, vx1, axisY);
            g.drawLine(vx2, y0 + h * 0.06, vx2, axisY);
            String zoneLblText = String.format("Zone de visibilité : [%.0f°, %.0f°]", minVis, maxVis);
            g.setFont(font(SANS_SERIF, BOLD, h * 0.026));
            double sw = g.getFontMetrics().stringWidth(zoneLblText);
            g.setPaint(zoneLbl);
            g.drawString(zoneLblText, (vx1 + vx2) / 2 - sw / 2, y0 + h * 0.09);
        }

        // 2. top Cartesian plot of Fr(2gamma)
        g.setFont(font(MONOSPACED, PLAIN, h * 0.024));
        double[] amps = {0.25, 0.5, 0.75, 1.0};
        for (double a : amps) {
            double ay = axisY - a * topPlotHeight;
            g.setPaint(alpha(gridColor, a == 1.0 ? GRID_ALPHA_STRONG : GRID_ALPHA_SOFT));
            g.drawLine(marginX + x0 - 10, ay, marginX + x0 + plotW + 10, ay);
            g.setPaint(alpha(gridLbl, GRID_LBL_ALPHA));
            g.drawString(String.format("%.2f", a), marginX + x0 - 40, ay + 4);
        }

        Path2D.Double curvePath = new Path2D.Double();
        int steps = 1000;
        boolean first = true;
        for (int i = 0; i <= steps; i++) {
            double twoG = plotMin2G + (i / (double) steps) * (plotMax2G - plotMin2G);
            double fr = ArrayFactorMath.fr(n, twoG);
            double sx = toX.applyAsDouble(twoG);
            double sy = axisY - fr * topPlotHeight;
            if (first) {
                curvePath.moveTo(sx, sy);
                first = false;
            } else {
                curvePath.lineTo(sx, sy);
            }
        }
        Path2D.Double fillPath = (Path2D.Double) curvePath.clone();
        fillPath.lineTo(toX.applyAsDouble(plotMax2G), axisY);
        fillPath.lineTo(toX.applyAsDouble(plotMin2G), axisY);
        fillPath.closePath();
        g.setPaint(fillColor);
        g.fill(fillPath);
        g.setPaint(curveColor);
        g.setStroke(new BasicStroke(2.2f));
        g.draw(curvePath);

        // zeros in [0, 360]
        List<Double> zeros = ArrayFactorMath.zerosDeg(n);
        g.setFont(font(SANS_SERIF, BOLD, h * 0.024));
        for (double z : zeros) {
            double zx = toX.applyAsDouble(z);
            g.setPaint(zeroColor);
            g.fillOval(zx - 4, axisY - 4, 8, 8);
            g.setPaint(dotColor);
            g.drawOval(zx - 4, axisY - 4, 8, 8);
            g.setPaint(zeroLbl);
            String zLbl = String.format("%.0f°", z);
            double sw = g.getFontMetrics().stringWidth(zLbl);
            g.drawString(zLbl, zx - sw / 2, axisY - 8);
        }
        // main maxima at 0 and 360
        for (double mDeg : new double[]{0.0, 360.0}) {
            if (mDeg >= plotMin2G && mDeg <= plotMax2G) {
                double mx = toX.applyAsDouble(mDeg);
                double my = axisY - topPlotHeight;
                g.setPaint(maxColor);
                g.fillOval(mx - 5, my - 5, 10, 10);
                g.setPaint(dotColor);
                g.drawOval(mx - 5, my - 5, 10, 10);
                g.setFont(font(SANS_SERIF, BOLD, h * 0.025));
                g.setPaint(maxLbl);
                String mLbl = String.format("Max (%.0f°)", mDeg);
                double sw = g.getFontMetrics().stringWidth(mLbl);
                g.drawString(mLbl, mx - sw / 2, my - 8);
            }
        }

        // 3. shared 2gamma axis
        g.setPaint(axisColor);
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(marginX + x0 - 15, axisY, marginX + x0 + plotW + 25, axisY);
        double endX = marginX + x0 + plotW + 25;
        g.drawLine(endX, axisY, endX - 8, axisY - 5);
        g.drawLine(endX, axisY, endX - 8, axisY + 5);
        g.setFont(font(SANS_SERIF, BOLD, h * 0.03));
        g.setPaint(axisLbl);
        g.drawString("2γ", endX + 8, axisY + 6);
        g.setFont(font(MONOSPACED, PLAIN, h * 0.024));
        for (double deg = Math.ceil(plotMin2G / 45.0) * 45.0; deg <= plotMax2G + 1e-9; deg += 45.0) {
            double tx = toX.applyAsDouble(deg);
            boolean isMajor = Math.abs((Math.round(deg) % 90)) < 1e-9;
            g.setPaint(alpha(gridColor, isMajor ? TICK_ALPHA_MAJOR : TICK_ALPHA_MINOR));
            g.drawLine(tx, axisY - (isMajor ? 6 : 3), tx, axisY + (isMajor ? 6 : 3));
            if (isMajor) {
                g.setPaint(axisLbl);
                String tickStr = String.format("%.0f°", deg);
                double sw = g.getFontMetrics().stringWidth(tickStr);
                g.drawString(tickStr, tx - sw / 2, axisY + 18);
            }
        }

        // 4. semi-circle and polar pattern
        double cx = toX.applyAsDouble(alpha);
        double radiusPx = toX.applyAsDouble(alpha + betaD) - cx;
        if (radiusPx > 5) {
            // center mark (alpha)
            g.setPaint(PanelTheme.color(ctx, "semicircle-color", D_SEMI, L_SEMI));
            g.fillOval(cx - 5, axisY - 5, 10, 10);
            g.setPaint(dotColor);
            g.drawOval(cx - 5, axisY - 5, 10, 10);
            g.setFont(font(SANS_SERIF, BOLD, h * 0.026));
            g.setPaint(semiLbl);
            String alphaLbl = String.format("Centre α = %.0f°", alpha);
            double sw = g.getFontMetrics().stringWidth(alphaLbl);
            g.drawString(alphaLbl, cx - 25, axisY + 34);

            // semi-circle below the axis
            g.setPaint(alpha(semiColor, SEMI_ALPHA));
            g.setStroke(new BasicStroke(1.5f));
            g.drawArc(cx - radiusPx, axisY - radiusPx, 2 * radiusPx, 2 * radiusPx, 180, 180);

            g.setFont(font(SANS_SERIF, PLAIN, h * 0.024));
            g.setPaint(semiLbl);
            g.drawString("ψ = 180°", cx - radiusPx - 45, axisY + 16);
            g.drawString("ψ = 0°", cx + radiusPx + 8, axisY + 16);
            g.setPaint(radiusLbl);
            g.drawString(String.format("Rayon βd = %.0f°", betaD), cx + radiusPx / 2 - 30, axisY - 8);

            // strategic projections
            if (showProjections) {
                drawProjections(g, n, alpha, betaD, toX, axisY, cx, radiusPx,
                        showRays, projColor, rayColor, dotColor, zeroColor, maxColor, h);
            }

            // continuous polar pattern inside the semi-circle
            Path2D.Double polarPath = new Path2D.Double();
            int numSamples = 360;
            first = true;
            for (int i = 0; i <= numSamples; i++) {
                double psiDeg = (i / (double) numSamples) * 180.0;
                double psiRad = Math.toRadians(psiDeg);
                double twoGamma = alpha + betaD * Math.cos(psiRad);
                double fr = ArrayFactorMath.fr(n, twoGamma);
                double rPx = fr * radiusPx;
                double px = cx + rPx * Math.cos(psiRad);
                double py = axisY + rPx * Math.sin(psiRad);
                if (first) {
                    polarPath.moveTo(px, py);
                    first = false;
                } else {
                    polarPath.lineTo(px, py);
                }
            }
            polarPath.lineTo(cx, axisY);
            polarPath.closePath();
            g.setPaint(alpha(maxColor, POLAR_FILL_ALPHA));
            g.fill(polarPath);
            g.setPaint(maxColor);
            g.setStroke(new BasicStroke(2.2f));
            g.draw(polarPath);

            // 5. animated scanning probe
            if (showProbe) {
                drawProbe(g, n, alpha, betaD, psi, toX, axisY, cx, radiusPx, topPlotHeight, w,
                        probeLine, probeArc, probePt, probeCirc, probePat, probeRo, dotColor, h);
            }
        }

        // 6. header badge + formulas
        if (showHeader) {
            drawHeader(g, ctx, n, alpha, betaD, title, x0, y0, w, h, hdrFill, hdrBorder, hdrTitle, hdrText);
        }
    }

    private void drawProjections(NTxGraphics g, int n, double alpha, double betaD,
                                 java.util.function.DoubleUnaryOperator toX,
                                 double axisY, double cx, double radiusPx, boolean showRays,
                                 Color projColor, Color rayColor, Color dotColor, Color zeroColor,
                                 Color maxColor, double h) {
        List<Double> points = new java.util.ArrayList<>();
        points.add(0.0);
        points.add(360.0);
        points.addAll(ArrayFactorMath.zerosDeg(n));
        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{3, 3}, 0f));
        g.setFont(font(SANS_SERIF, PLAIN, h * 0.022));
        for (double pt2G : points) {
            if (pt2G >= alpha - betaD && pt2G <= alpha + betaD) {
                double px = toX.applyAsDouble(pt2G);
                double cosPsi = Math.max(-1.0, Math.min(1.0, (pt2G - alpha) / betaD));
                double psiRad = Math.acos(cosPsi);
                double py = axisY + radiusPx * Math.sin(psiRad);
                g.setPaint(projColor);
                g.drawLine(px, axisY, px, py);
                if (showRays) {
                    g.setPaint(rayColor);
                    g.drawLine(cx, axisY, px, py);
                }
                g.setPaint(dotColor);
                g.fillOval(px - 3, py - 3, 6, 6);
                double fr = ArrayFactorMath.fr(n, pt2G);
                double rPx = cx + fr * radiusPx * Math.cos(psiRad);
                double rPy = axisY + fr * radiusPx * Math.sin(psiRad);
                if (fr < 0.05) {
                    g.setPaint(zeroColor);
                    g.fillOval(rPx - 4, rPy - 4, 8, 8);
                } else {
                    g.setPaint(maxColor);
                    g.fillOval(rPx - 5, rPy - 5, 10, 10);
                }
            }
        }
    }

    private void drawProbe(NTxGraphics g, int n, double alpha, double betaD,
                           double currentPsi, java.util.function.DoubleUnaryOperator toX, double axisY,
                           double cx, double radiusPx, double topPlotHeight, double w,
                           Color probeLine, Color probeArc, Color probePt, Color probeCirc, Color probePat,
                           Color probeRo, Color dotColor, double h) {
        double psiRad = Math.toRadians(currentPsi);
        double twoG = alpha + betaD * Math.cos(psiRad);
        double fr = ArrayFactorMath.fr(n, twoG);
        double sx = toX.applyAsDouble(twoG);
        double topY = axisY - fr * topPlotHeight;
        double circX = cx + radiusPx * Math.cos(psiRad);
        double circY = axisY + radiusPx * Math.sin(psiRad);
        double patX = cx + fr * radiusPx * Math.cos(psiRad);
        double patY = axisY + fr * radiusPx * Math.sin(psiRad);

        g.setPaint(probeLine);
        g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, new float[]{4, 3}, 0f));
        g.drawLine(sx, topY, sx, circY);
        g.setPaint(probeArc);
        g.setStroke(new BasicStroke(1.8f));
        g.drawLine(cx, axisY, circX, circY);
        g.setPaint(probePt);
        g.fillOval(sx - 5, topY - 5, 10, 10);
        g.setPaint(dotColor);
        g.drawOval(sx - 5, topY - 5, 10, 10);
        g.setPaint(probeCirc);
        g.fillOval(circX - 4, circY - 4, 8, 8);
        g.setPaint(probePat);
        g.fillOval(patX - 6, patY - 6, 12, 12);
        g.setPaint(dotColor);
        g.drawOval(patX - 6, patY - 6, 12, 12);

        g.setFont(font(MONOSPACED, BOLD, h * 0.024));
        g.setPaint(probeRo);
        String readStr = String.format("ψ = %.1f°  │  2γ = %.1f°  │  Fr = %.3f", currentPsi, twoG, fr);
        g.drawString(readStr, Math.min(patX + 12, w - 300 * (h / 500.0) - 20), patY + 16);
    }

    private void drawHeader(NTxGraphics g, NTxRendererContext ctx, int n, double alpha, double betaD,
                            String title, double x0, double y0, double w, double h,
                            Color hdrFill, Color hdrBorder, Color hdrTitle, Color hdrText) {
        double hx = x0 + 14;
        double hy = y0 + 10;
        double hw = Math.min(w - 28, w * 0.62);
        double hh = Math.max(60, h * 0.17);
        g.setPaint(hdrFill);
        g.fillRoundRect(hx, hy, hw, hh, 10, 10);
        g.setPaint(hdrBorder);
        g.setStroke(new BasicStroke(1.2f));
        g.drawRoundRect(hx, hy, hw, hh, 10, 10);

        g.setFont(font(SANS_SERIF, BOLD, h * 0.03));
        g.setPaint(hdrTitle);
        g.drawString(title, hx + 10, hy + hh * 0.22);

        g.setFont(font(MONOSPACED, PLAIN, h * 0.024));
        g.setPaint(hdrText);
        g.drawString("• Fr(2γ) = |sin(Nγ) / (N sin γ)|  —  N−1 zéros équidistants à 2π/N",
                hx + 10, hy + hh * 0.46);
        double psiPeak = -1.0;
        if (Math.abs(alpha) <= betaD) {
            double ratio = alpha / betaD;
            if (ratio >= -1.0 && ratio <= 1.0) {
                psiPeak = Math.toDegrees(Math.acos(ratio));
            }
        }
        String peakStr = psiPeak >= 0 ? String.format("%.1f°", psiPeak) : "Hors visibilité";
        g.drawString(String.format("• Centre: α = %.0f° │ Rayon: βd = %.0f° │ Pic arccos(α/βd) = %s",
                alpha, betaD, peakStr), hx + 10, hy + hh * 0.68);
        g.drawString("• ψ balaye 0→180° (animé) ; figé via la propriété ψ en mode print",
                hx + 10, hy + hh * 0.9);
    }

    private static Font font(String family, int style, double sizePx) {
        return new Font(family, style, Math.max(6, (int) Math.round(sizePx)));
    }

    private static final int PLAIN = Font.PLAIN;
    private static final int BOLD = Font.BOLD;
    private static final String SANS_SERIF = "SansSerif";
    private static final String MONOSPACED = "Monospaced";

    private static Color alpha(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, Math.min(255, a)));
    }
}