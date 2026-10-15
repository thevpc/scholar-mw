package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import static net.thevpc.ntexup.extension.antennas.phys.AntennaKit.*;

/**
 * Loop antenna / magnetic dipole (dipole magnetique, antenne boucle).
 *
 * Model: circular loop of circumference C (in wavelengths) carrying a uniform current I cos(wt).
 * Far field E_phi ~ J1(ka sin(theta)), theta from the loop axis, with ka = C/lambda.
 * For small loops J1(x) ~ x/2, hence F(theta) = sin(theta) (the dual of the short electric dipole)
 * and Rr = 20 pi^2 (C/lambda)^4 N^2.  The uniform-current model is only accurate for C < ~0.5 lambda.
 *
 * view:"side" (default): loop seen edge-on (current flows into / out of the screen at its two ends),
 *   H-field lines of the loop (Biot-Savart), radiated field, doughnut pattern.
 * view:"top": loop seen along its axis: circulating current, H out of / into the page at the centre,
 *   E_phi wavefronts (omnidirectional in the loop plane).
 *
 * Aspects: current, hfield, waves, rays, pattern, labels.
 *
 *   loop-antenna()                                         // small loop, side view
 *   loop-antenna(circumference:0.05 turns:10 show:"all")
 *   loop-antenna(view:"top" show:"current,waves,labels")
 *   loop-antenna(preset:"large" show:"waves,pattern,labels")   // C = 1 lambda: pattern changes shape
 */
public class LoopAntennaBuilder implements NTxNodeBuilder {

    private static final String ALL = "current,hfield,waves,rays,pattern,labels";
    private static final String DEFAULT_SHOW = "current,hfield,pattern,labels";
    private static final double ETA = 376.73;

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext
                .id("loop-antenna")
                .parseParam()
                .matchesNamedPair(params("preset", "circumference", "turns", "view", "h-lines", "loop-size"))
                .end()
                .renderComponent(this::render);
    }

    private void render(NTxRendererContext rc) {
        NTxNode node = rc.node();
        NTxBounds2D b = rc.selfBounds2D();
        double w = b.widthX(), h = b.widthY();
        Mode m = mode(rc, DEFAULT_SHOW, ALL);
        Style st = style(rc, h);
        boolean top = "top".equalsIgnoreCase(str(node, "view", "side"));
        boolean pattern = m.show.contains("pattern");

        double cParam = dbl(node, "circumference", Double.NaN);
        double circ = !Double.isNaN(cParam) ? cParam : presetCirc(str(node, "preset", "small"));
        circ = clamp(circ, 0.005, 3.0);          // circumference in wavelengths = ka
        int turns = (int) clamp(dbl(node, "turns", 1), 1, 1000);
        double ka = circ;

        double patW = pattern ? w * clamp(dbl(node, "pattern-width", 0.42), 0.15, 0.7) : 0;
        double leftW = w - patW;
        int labelLines = m.show.contains("labels") ? 4 : 0;
        double hEff = h - (labelLines > 0 ? labelLines * st.fontSize * 1.3 + 10 : 0);
        double cx = b.minX() + leftW / 2.0, cy = b.minY() + hEff / 2.0;

        double waveRMax = Math.min(leftW / 2.0, hEff / 2.0) - 2;
        double waveCount = Math.max(0.5, dbl(node, "wave-count", 3.0));
        boolean waves = m.show.contains("waves") || m.show.contains("rays");
        double lambdaPx = waves ? waveRMax / waveCount : waveRMax / 2.0;
        double loopSize = clamp(dbl(node, "loop-size", 0.25), 0.05, 0.6);
        double rPx = loopSize * waveRMax;                       // drawn loop radius (exaggerated when small)
        if (waves) rPx = Math.min(rPx, Math.max(circ / (2 * Math.PI) * lambdaPx, 0.1 * waveRMax));
        double cosWt = Math.cos(m.wt);

        double[] pat = sampleAxi(th -> j1(ka * Math.sin(th)));
        NTxGraphics g = rc.graphics();

        // ---- radiated field
        if (m.show.contains("waves")) {
            double r0 = Math.max(rPx * 1.3, 0.25 * lambdaPx);
            boolean rings = "rings".equalsIgnoreCase(str(node, "wave-style", "field"));
            if (top) {
                // seen along the axis the loop is omnidirectional: E_phi circles expand uniformly
                rings(g, cx, cy, waveRMax, r0, lambdaPx, m.wt, st.wavePos, st.waveNeg, st.lineW,
                        (int) dbl(node, "wave-rings", 40), psi -> 1.0);
            } else if (rings) {
                rings(g, cx, cy, waveRMax, r0, lambdaPx, m.wt, st.wavePos, st.waveNeg, st.lineW,
                        (int) dbl(node, "wave-rings", 40), psi -> axiAt(pat, Math.PI / 2 - psi));
            } else {
                fieldMap(g, cx, cy, waveRMax, r0, Math.max(3, dbl(node, "wave-cell", 5)),
                        axisField(cx, cy, r0, lambdaPx, m.wt, pat), st.wavePos, st.waveNeg);
            }
        }

        if (!top) {
            drawSide(g, m, st, node, cx, cy, rPx, waveRMax, cosWt);
        } else {
            drawTop(g, m, st, cx, cy, rPx, cosWt);
        }

        // ---- propagation arrows and pattern
        if (m.show.contains("rays")) {
            rays(g, cx, cy, waveRMax * 0.62, waveRMax * 0.36, Math.max(4, (int) dbl(node, "rays", 12)),
                    st.pattern, st.lineW, top ? psi -> 1.0 : psi -> axiAt(pat, Math.PI / 2 - psi));
        }
        if (pattern) {
            double R = Math.min(patW / 2.0, hEff / 2.0) * 0.82;
            polar(g, b.minX() + leftW + patW / 2.0, cy, R, top ? psi -> 1.0 : psi -> axiAt(pat, Math.PI / 2 - psi),
                    str(node, "pattern-scale", "field"), st.pattern, st.grid, st.lineW);
        }

        // ---- labels
        if (m.show.contains("labels")) {
            double dirD = axiDirectivity(pat);
            double rr = radiationResistance(ka) * turns * turns;
            List<Object[]> L = new ArrayList<>();
            String kind = circ <= 0.25
                    ? tr(st.lang, "Small loop (magnetic dipole)", "Boucle \u00e9lectriquement petite (dip\u00f4le magn\u00e9tique)")
                    : tr(st.lang, "Loop antenna", "Antenne boucle");
            L.add(line(kind + "   C = " + fmt(circ, circ < 0.1 ? 3 : 2) + " \u03bb   N = " + turns, st.text, true));
            L.add(line("Rr = " + (rr < 1 ? fmt(rr * 1000, 1) + " m\u03a9" : fmt(rr, 1) + " \u03a9")
                    + "   D = " + fmt(dirD, 2) + " (" + fmt(10 * Math.log10(dirD), 2) + " dBi)   HPBW = "
                    + fmt(axiHpbwDeg(pat), 0) + "\u00b0", st.text, false));
            L.add(line(circ <= 0.25
                            ? "E\u03c6 \u221d sin\u03b8 \u00b7 cos \u03c9t    " + tr(st.lang, "(dual of the short dipole: E \u2194 H)", "(dual du dip\u00f4le court : E \u2194 H)")
                            : "E\u03c6 \u221d J\u2081(ka sin\u03b8)    " + tr(st.lang, "(uniform current assumed)", "(courant uniforme suppos\u00e9)"),
                    st.text, false));
            L.add(line(top ? tr(st.lang, "top view: H out of / into the page, E\u03c6 circles around the axis",
                            "vue de dessus : H sortant / entrant, E\u03c6 en cercles autour de l'axe")
                            : tr(st.lang, "side view: current out of (\u2299) / into (\u2297) the page, H lines through the loop",
                            "vue de c\u00f4t\u00e9 : courant sortant (\u2299) / entrant (\u2297), lignes de H \u00e0 travers la boucle"),
                    st.neg, false));
            textBlock(g, b.minX() + 6, b.minY() + hEff + 4, st.fontSize, L);
        }

        rc.drawContour();
        if (m.anim) {
            rc.requestAnimationFrame(true);
        }
    }

    // ------------------------------------------------------------------ side view

    private void drawSide(NTxGraphics g, Mode m, Style st, NTxNode node, double cx, double cy, double rPx,
                          double bound, double cosWt) {
        double amp = Math.abs(cosWt);
        // H field lines (quasi-static Biot-Savart field of the loop; same shape at all times, direction follows cos wt)
        if (m.show.contains("hfield")) {
            int nLines = (int) clamp(dbl(node, "h-lines", 7), 1, 9);
            double[] seeds = {0, 0.4, 0.75, 1.3, 1.8, 2.5, 3.4, 4.5, 6.0};
            int alpha = (int) (70 + 160 * amp);
            double limit = bound / rPx;
            GeneralPath arrows = new GeneralPath();
            stroke(g, Math.max(1.2f, st.lineW * 0.7f));
            for (int s = 0; s < nLines; s++) {
                List<double[]> pts = traceLine(seeds[s], 0, limit);
                // the field is symmetric about the loop axis: draw the mirror image too (except the axis line itself)
                for (int sgn = 1; sgn >= (s == 0 ? 1 : -1); sgn -= 2) {
                    GeneralPath p = new GeneralPath();
                    for (int i = 0; i < pts.size(); i++) {
                        double x = cx + sgn * pts.get(i)[0] * rPx, y = cy - pts.get(i)[1] * rPx;
                        if (i == 0) p.moveTo(x, y);
                        else p.lineTo(x, y);
                    }
                    g.setPaint(alpha(st.neg, alpha));
                    g.draw(p);
                    // two arrow heads per line, pointing along H (reversed when the current is negative)
                    for (double frac : new double[]{0.28, 0.72}) {
                        int i = (int) (frac * (pts.size() - 1));
                        if (i < 1 || i >= pts.size() - 1) continue;
                        double[] a0 = pts.get(i - 1), c0 = pts.get(i + 1), mid = pts.get(i);
                        double dx = sgn * (c0[0] - a0[0]) * rPx, dy = -(c0[1] - a0[1]) * rPx, len = Math.hypot(dx, dy);
                        if (len < 1e-6 || amp < 0.15) continue;
                        double sg = cosWt >= 0 ? 1 : -1;
                        dx = dx / len * sg;
                        dy = dy / len * sg;
                        double x = cx + sgn * mid[0] * rPx, y = cy - mid[1] * rPx;
                        arrow(arrows, x - dx * 4, y - dy * 4, x + dx * 4, y + dy * 4, 7);
                    }
                }
            }
            stroke(g, 1.6f);
            g.setPaint(alpha(st.neg, alpha));
            g.draw(arrows);
        }
        // the loop itself, edge-on, with the current direction at its two ends
        stroke(g, st.wireW);
        g.setPaint(st.wire);
        GeneralPath wire = new GeneralPath();
        wire.moveTo(cx - rPx, cy);
        wire.lineTo(cx + rPx, cy);
        g.draw(wire);
        if (m.show.contains("current")) {
            int a = (int) (110 + 145 * amp);
            double rr = 8;
            for (int side = -1; side <= 1; side += 2) {
                boolean out = (side > 0) == (cosWt >= 0);       // CCW seen from +z: out of the page at x > 0
                double x = cx + side * rPx;
                g.setPaint(alpha(st.current, 255));
                stroke(g, 2f);
                g.draw(new Ellipse2D.Double(x - rr, cy - rr, 2 * rr, 2 * rr));
                g.setPaint(alpha(st.pos, a));
                if (out) {
                    stroke(g, 5f);
                    GeneralPath dot = new GeneralPath();
                    dot.moveTo(x - 0.5, cy);
                    dot.lineTo(x + 0.5, cy);
                    g.draw(dot);
                } else {
                    stroke(g, 2f);
                    GeneralPath cross = new GeneralPath();
                    cross.moveTo(x - rr * 0.6, cy - rr * 0.6);
                    cross.lineTo(x + rr * 0.6, cy + rr * 0.6);
                    cross.moveTo(x - rr * 0.6, cy + rr * 0.6);
                    cross.lineTo(x + rr * 0.6, cy - rr * 0.6);
                    g.draw(cross);
                }
            }
        }
    }

    // ------------------------------------------------------------------ top view

    private void drawTop(NTxGraphics g, Mode m, Style st, double cx, double cy, double rPx, double cosWt) {
        stroke(g, st.wireW);
        g.setPaint(st.wire);
        GeneralPath circle = new GeneralPath();
        for (int s = 0; s <= 90; s++) {
            double a = 2 * Math.PI * s / 90;
            if (s == 0) circle.moveTo(cx + rPx * Math.cos(a), cy - rPx * Math.sin(a));
            else circle.lineTo(cx + rPx * Math.cos(a), cy - rPx * Math.sin(a));
        }
        g.draw(circle);
        if (!m.show.contains("current")) return;
        double amp = Math.abs(cosWt);
        double dir = cosWt >= 0 ? 1 : -1;                        // +1: counter-clockwise
        int n = 8;
        GeneralPath arrows = new GeneralPath();
        for (int k = 0; k < n; k++) {
            double a = 2 * Math.PI * (k + 0.5) / n;
            double half = (Math.PI / n) * 0.75 * Math.max(0.25, amp);   // half angular length of the arrow
            double a1 = a - dir * half, a2 = a + dir * half;
            arrow(arrows, cx + rPx * Math.cos(a1), cy - rPx * Math.sin(a1),
                    cx + rPx * Math.cos(a2), cy - rPx * Math.sin(a2), 8);
        }
        stroke(g, Math.max(2f, st.lineW));
        g.setPaint(st.current);
        g.draw(arrows);
        // H through the loop: out of the page for CCW current
        double rr = 6 + 10 * amp;
        g.setPaint(alpha(st.neg, (int) (110 + 145 * amp)));
        stroke(g, 2f);
        g.draw(new Ellipse2D.Double(cx - rr, cy - rr, 2 * rr, 2 * rr));
        if (cosWt >= 0) {
            stroke(g, Math.max(5f, (float) rr * 0.5f));
            GeneralPath dot = new GeneralPath();
            dot.moveTo(cx - 0.5, cy);
            dot.lineTo(cx + 0.5, cy);
            g.draw(dot);
        } else {
            GeneralPath cross = new GeneralPath();
            cross.moveTo(cx - rr * 0.6, cy - rr * 0.6);
            cross.lineTo(cx + rr * 0.6, cy + rr * 0.6);
            cross.moveTo(cx - rr * 0.6, cy + rr * 0.6);
            cross.lineTo(cx + rr * 0.6, cy - rr * 0.6);
            g.draw(cross);
        }
    }

    // ------------------------------------------------------------------ physics

    private static double presetCirc(String preset) {
        switch (norm(preset)) {
            case "tiny": return 0.03;
            case "small": return 0.1;
            case "medium": return 0.3;
            case "large": case "resonant": case "full": return 1.0;
            default: return 0.1;
        }
    }

    /** Rr (ohm) of one turn, uniform current: (pi eta / 2) (ka)^2 * integral J1^2(ka sin t) sin t dt. */
    static double radiationResistance(double ka) {
        int n = 120;
        double h = Math.PI / n, s = 0;
        for (int i = 0; i <= n; i++) {
            double t = i * h, v = j1(ka * Math.sin(t));
            v = v * v * Math.sin(t);
            s += (i == 0 || i == n) ? v : (i % 2 == 1 ? 4 * v : 2 * v);
        }
        return Math.PI * ETA / 2 * ka * ka * s * h / 3;
    }

    /** B field (Biot-Savart) of a unit loop (radius 1, in the plane z=0, CCW current) at (x, 0, z); returns {Bx, Bz}. */
    private static double[] bField(double x, double z) {
        int n = 48;
        double bx = 0, bz = 0, dpsi = 2 * Math.PI / n;
        for (int k = 0; k < n; k++) {
            double psi = dpsi * (k + 0.5), sx = Math.cos(psi), sy = Math.sin(psi);
            double dlx = -sy * dpsi, dly = sx * dpsi;
            double rx = x - sx, ry = -sy, rz = z;
            double r2 = rx * rx + ry * ry + rz * rz, r3 = r2 * Math.sqrt(r2) + 1e-12;
            bx += dly * rz / r3;
            bz += (dlx * ry - dly * rx) / r3;
        }
        return new double[]{bx, bz};
    }

    /** Field line through (sx, sz): points ordered along H (for a CCW current), clipped at |p| = limit. */
    private static List<double[]> traceLine(double sx, double sz, double limit) {
        List<double[]> fwd = traceDir(sx, sz, 1, limit), bwd = traceDir(sx, sz, -1, limit);
        List<double[]> out = new ArrayList<>();
        for (int i = bwd.size() - 1; i >= 0; i--) out.add(bwd.get(i));
        out.add(new double[]{sx, sz});
        out.addAll(fwd);
        return out;
    }

    private static List<double[]> traceDir(double sx, double sz, int dir, double limit) {
        List<double[]> pts = new ArrayList<>();
        double ds = 0.05, x = sx, z = sz;
        for (int step = 0; step < 1400; step++) {
            double[] b1 = bField(x, z);
            double n1 = Math.hypot(b1[0], b1[1]);
            if (n1 < 1e-12) break;
            double mx = x + dir * ds / 2 * b1[0] / n1, mz = z + dir * ds / 2 * b1[1] / n1;
            double[] b2 = bField(mx, mz);
            double n2 = Math.hypot(b2[0], b2[1]);
            if (n2 < 1e-12) break;
            x += dir * ds * b2[0] / n2;
            z += dir * ds * b2[1] / n2;
            if (Math.hypot(x, z) > limit) break;
            pts.add(new double[]{x, z});
            if (step > 40 && Math.hypot(x - sx, z - sz) < ds * 1.2) {   // closed line
                pts.add(new double[]{sx, sz});
                break;
            }
        }
        return pts;
    }
}