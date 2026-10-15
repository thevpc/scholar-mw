package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import static net.thevpc.ntexup.extension.antennas.phys.AntennaKit.*;

/**
 * Folded dipole (dipole replie).
 *
 * Aspects: current, envelope, arrows (total current per wire), modes (decomposition into the radiating
 * "antenna mode" = same direction in all wires, and the non-radiating "transmission-line mode" = opposite
 * directions), waves, rays, pattern, labels.
 *
 * Input impedance: Zin = N^2 * R_dipole (N wires of equal radius): 4 x 73 = 292 ohm for N = 2.
 *
 *   folded-dipole()                                   // N = 2, half-wave
 *   folded-dipole(wires:3 show:"all" size:(520,320))   // 3-wire folded dipole, Zin = 9 x 73
 *   folded-dipole(length:0.5 show:"current,modes,labels" lang:"fr")
 */
public class FoldedDipoleBuilder implements NTxNodeBuilder {

    private static final String ALL = "current,envelope,arrows,modes,waves,rays,pattern,labels";
    private static final String DEFAULT_SHOW = "current,arrows,modes,pattern,labels";

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext
                .id("folded-dipole")
                .parseParam()
                .matchesNamedPair(params("length", "wires", "spacing", "z-dipole", "line-mode", "min-wire"))
                .end()
                .renderComponent(this::render);
    }

    private void render(NTxRendererContext rc) {
        NTxNode node = rc.node();
        NTxBounds2D b = rc.selfBounds2D();
        double w = b.widthX(), h = b.widthY();
        Mode m = mode(rc, DEFAULT_SHOW, ALL);
        Style st = style(rc, h);
        boolean pattern = m.show.contains("pattern");

        double length = Math.max(0.05, dbl(node, "length", 0.5));
        int wires = (int) clamp(dbl(node, "wires", 2), 2, 4);
        double zDip = dbl(node, "z-dipole", 73.1);
        double lineMode = dbl(node, "line-mode", 0.18); // |I_line| / |I_antenna|, drawn exaggerated

        double patW = pattern ? w * clamp(dbl(node, "pattern-width", 0.42), 0.15, 0.7) : 0;
        double leftW = w - patW;
        // reserve room at the bottom for the labels; everything is laid out in the remaining height
        int labelLines = m.show.contains("labels") ? (m.show.contains("modes") ? 5 : 3) : 0;
        double hEff = h - (labelLines > 0 ? labelLines * st.fontSize * 1.3 + 10 : 0);
        double cx = b.minX() + leftW / 2.0, cy = b.minY() + hEff / 2.0;

        double waveRMax = Math.min(leftW / 2.0, hEff / 2.0) - 2;
        double waveCount = Math.max(0.5, dbl(node, "wave-count", 3.0));
        boolean waves = m.show.contains("waves") || m.show.contains("rays");
        double lambdaPx = waves ? waveRMax / waveCount : hEff * 0.8 / Math.max(length, 1.0);
        double wirePx = Math.max(length * lambdaPx, dbl(node, "min-wire", waves ? 0.22 : 0.7) * hEff);
        double halfPx = wirePx / 2.0;
        double spPx = clamp(dbl(node, "spacing", 0.3), 0.05, 0.8) * wirePx * 0.5;
        double bulgePx = Math.min(spPx * 0.9, leftW * 0.12) * dbl(node, "bulge", 1.0);

        double iMax = 0;
        for (int i = 0; i <= 200; i++) iMax = Math.max(iMax, Math.abs(cur(-1 + i / 100.0, length)));
        if (iMax < 1e-9) iMax = 1;
        double cosWt = Math.cos(m.wt);

        double[] pat = sampleAxi(th -> dipoleField(th, Math.PI * length));
        double[] xs = new double[wires];
        for (int i = 0; i < wires; i++) xs[i] = cx + (i - (wires - 1) / 2.0) * spPx;

        NTxGraphics g = rc.graphics();

        // ---- radiated field
        if (m.show.contains("waves")) {
            double r0 = Math.max(Math.hypot(halfPx, spPx * wires / 2) * 1.05, 0.25 * lambdaPx);
            if ("rings".equalsIgnoreCase(str(node, "wave-style", "field"))) {
                rings(g, cx, cy, waveRMax, r0, lambdaPx, m.wt, st.wavePos, st.waveNeg, st.lineW,
                        (int) dbl(node, "wave-rings", 40), psi -> axiAt(pat, Math.PI / 2 - psi));
            } else {
                fieldMap(g, cx, cy, waveRMax, r0, Math.max(3, dbl(node, "wave-cell", 5)),
                        axisField(cx, cy, r0, lambdaPx, m.wt, pat), st.wavePos, st.waveNeg);
            }
        }

        // ---- wires: N vertical conductors, joined at both ends; the feed gap is in the first one
        double gap = 2.5;
        GeneralPath wire = new GeneralPath();
        for (int i = 0; i < wires; i++) {
            if (i == 0) {
                wire.moveTo(xs[i], cy - halfPx);
                wire.lineTo(xs[i], cy - gap);
                wire.moveTo(xs[i], cy + gap);
                wire.lineTo(xs[i], cy + halfPx);
            } else {
                wire.moveTo(xs[i], cy - halfPx);
                wire.lineTo(xs[i], cy + halfPx);
            }
        }
        wire.moveTo(xs[0], cy - halfPx);
        wire.lineTo(xs[wires - 1], cy - halfPx);
        wire.moveTo(xs[0], cy + halfPx);
        wire.lineTo(xs[wires - 1], cy + halfPx);
        stroke(g, st.wireW);
        g.setPaint(st.wire);
        g.draw(wire);
        stroke(g, 1.5f);
        g.draw(new Ellipse2D.Double(xs[0] - 4, cy - 4, 8, 8));

        // ---- current profile (antenna mode: same sign in every wire) and its envelope
        int samples = 120;
        if (m.show.contains("envelope")) {
            stroke(g, 1.2f);
            g.setPaint(alpha(st.current, 90));
            for (int k = 0; k < wires; k++) {
                GeneralPath ep = new GeneralPath(), en = new GeneralPath();
                for (int i = 0; i <= samples; i++) {
                    double u = -1 + 2.0 * i / samples, y = cy - u * halfPx;
                    double off = cur(u, length) / iMax * bulgePx;
                    if (i == 0) { ep.moveTo(xs[k] + off, y); en.moveTo(xs[k] - off, y); }
                    else { ep.lineTo(xs[k] + off, y); en.lineTo(xs[k] - off, y); }
                }
                g.draw(ep);
                g.draw(en);
            }
        }
        if (m.show.contains("current")) {
            stroke(g, st.lineW);
            g.setPaint(st.current);
            for (int k = 0; k < wires; k++) {
                GeneralPath curve = new GeneralPath();
                for (int i = 0; i <= samples; i++) {
                    double u = -1 + 2.0 * i / samples;
                    double x = xs[k] + cur(u, length) / iMax * cosWt * bulgePx;
                    double y = cy - u * halfPx;
                    if (i == 0) curve.moveTo(x, y);
                    else curve.lineTo(x, y);
                }
                g.draw(curve);
            }
        }

        // ---- arrows: total current per wire = antenna mode +/- line mode (wire 0 is the fed one)
        int nArrows = (int) dbl(node, "arrows", 7);
        double cell = wirePx / Math.max(1, nArrows), maxLen = cell * 0.9;
        if (m.show.contains("arrows") && nArrows > 0) {
            GeneralPath arrows = new GeneralPath();
            for (int k = 0; k < wires; k++) {
                double sgn = (k == 0) ? 1 : -1 / (double) (wires - 1);
                for (int i = 0; i < nArrows; i++) {
                    double u = -1 + (i + 0.5) * 2.0 / nArrows;
                    double v = (cur(u, length) / iMax + sgn * lineMode * 0.5) * cosWt;
                    addVerticalArrow(arrows, xs[k], cy - u * halfPx, v * maxLen);
                }
            }
            stroke(g, Math.max(1.5f, st.lineW * 0.8f));
            g.setPaint(st.current);
            g.draw(arrows);
        }

        // ---- decomposition: antenna mode (same direction, radiates) vs line mode (opposite, does not radiate)
        if (m.show.contains("modes")) {
            GeneralPath ant = new GeneralPath(), lin = new GeneralPath();
            double off = 9;
            for (int k = 0; k < wires; k++) {
                for (int i = 0; i < nArrows; i++) {
                    double u = -1 + (i + 0.5) * 2.0 / nArrows, y = cy - u * halfPx;
                    addVerticalArrow(ant, xs[k] - off, y, cur(u, length) / iMax * cosWt * maxLen);
                    double s = (k % 2 == 0) ? 1 : -1;
                    addVerticalArrow(lin, xs[k] + off, y, s * lineMode * cosWt * maxLen * 1.8);
                }
            }
            stroke(g, Math.max(1.5f, st.lineW * 0.8f));
            g.setPaint(st.pos);
            g.draw(ant);
            g.setPaint(st.wavePos);
            g.draw(lin);
        }

        // ---- propagation arrows and pattern
        if (m.show.contains("rays")) {
            rays(g, cx, cy, waveRMax * 0.55, waveRMax * 0.4, Math.max(4, (int) dbl(node, "rays", 12)),
                    st.pattern, st.lineW, psi -> axiAt(pat, Math.PI / 2 - psi));
        }
        if (pattern) {
            double R = Math.min(patW / 2.0, hEff / 2.0) * 0.82;
            polar(g, b.minX() + leftW + patW / 2.0, cy, R, psi -> axiAt(pat, Math.PI / 2 - psi),
                    str(node, "pattern-scale", "field"), st.pattern, st.grid, st.lineW);
        }

        // ---- labels (top-down, under the drawing)
        if (m.show.contains("labels")) {
            double dirD = axiDirectivity(pat);
            java.util.List<Object[]> L = new java.util.ArrayList<>();
            L.add(line(tr(st.lang, "Folded dipole   L = " + trim(length) + " \u03bb   N = " + wires + " wires",
                    "Dip\u00f4le repli\u00e9   L = " + trim(length) + " \u03bb   N = " + wires + " brins"), st.text, true));
            L.add(line("Zin = N\u00b2 \u00b7 R_dip = " + wires + "\u00b2 \u00d7 " + fmt(zDip, 0) + " = "
                    + fmt(wires * wires * zDip, 0) + " \u03a9", st.text, false));
            L.add(line("D = " + fmt(dirD, 2) + " (" + fmt(10 * Math.log10(dirD), 2) + " dBi)   HPBW = "
                    + fmt(axiHpbwDeg(pat), 0) + "\u00b0", st.text, false));
            if (m.show.contains("modes")) {
                L.add(line(tr(st.lang, "antenna mode: same direction \u2192 radiates",
                        "mode antenne : m\u00eame sens \u2192 rayonne"), st.pos, false));
                L.add(line(tr(st.lang, "line mode: opposite directions \u2192 no radiation",
                        "mode ligne : sens oppos\u00e9s \u2192 ne rayonne pas"), st.wavePos, false));
            }
            textBlock(g, b.minX() + 6, b.minY() + hEff + 4, st.fontSize, L);
        }

        rc.drawContour();
        if (m.anim) {
            rc.requestAnimationFrame(true);
        }
    }

    // ------------------------------------------------------------------ helpers

    private static void addVerticalArrow(GeneralPath p, double x, double yMid, double lenUp) {
        if (Math.abs(lenUp) < 2.5) return;
        double y1 = yMid + lenUp / 2, y2 = yMid - lenUp / 2;
        arrow(p, x, y1, x, y2, Math.min(5, Math.abs(lenUp) * 0.5));
    }

    /** I(u) = sin(pi L (1 - |u|)), u = 2z/L in [-1,1]. */
    private static double cur(double u, double lengthWl) {
        return Math.sin(Math.PI * lengthWl * (1 - Math.abs(u)));
    }

    private static double dipoleField(double theta, double halfKL) {
        double s = Math.sin(theta);
        if (Math.abs(s) < 1e-9) return 0;
        return Math.abs((Math.cos(halfKL * Math.cos(theta)) - Math.cos(halfKL)) / s);
    }

    private static String trim(double v) {
        return (v == Math.rint(v)) ? String.valueOf((long) v) : String.valueOf(v);
    }
}