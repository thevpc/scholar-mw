package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.antennas.phys.YagiSolver;
import net.thevpc.nuts.math.NDoubleComplex;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import static net.thevpc.ntexup.extension.antennas.phys.AntennaKit.*;

/**
 * Yagi-Uda array: reflector + driven element + N directors, solved with a coupled-dipole model
 * (see YagiSolver: mutual impedances, parasitic currents, pattern, directivity, input impedance).
 *
 * view:"front" (default): the usual drawing - elements (vertical) along the boom, each with its own
 *   instantaneous current: amplitude and phase come from the solver, so the audience sees that the reflector
 *   current is large and in opposition-ish phase and the directors lag, which is what steers the beam.
 * view:"top": azimuth plane seen from above (elements are dots on the boom), the field of all elements is summed:
 *   waves add up (constructive) in front of the directors and cancel behind the reflector (destructive).
 *
 * Aspects: current, arrows, values, waves, rays, pattern, labels.
 * Pattern plane: plane:"H" (default, perpendicular to the elements) or "E" (contains elements and boom).
 *
 * Geometry (all in wavelengths): directors (count, default 3), or lengths:"0.495,0.473,0.44" and
 * positions:"-0.2,0,0.2" (reflector first, then the driven element, then directors; "driven" = its index, default 1),
 * radius (wire radius, default 0.003). Defaults: reflector 0.495 at -0.2, driven 0.473 at 0, directors 0.44, 0.43, 0.43, ...
 *
 *   yagi-uda()                                       // 5 elements
 *   yagi-uda(directors:1 show:"all")                 // 3 elements
 *   yagi-uda(view:"top" show:"waves,rays,pattern,labels" directors:5)
 *   yagi-uda(lengths:"0.49,0.47,0.44" positions:"-0.15,0,0.15")
 */
public class YagiUdaBuilder implements NTxNodeBuilder {

    private static final String ALL = "current,arrows,values,waves,rays,pattern,labels";
    private static final String DEFAULT_SHOW = "current,arrows,values,pattern,labels";

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext
                .id("yagi-uda")
                .parseParam()
                .matchesNamedPair(params("directors", "lengths", "positions", "driven", "radius", "view", "plane",
                        "z0", "reflector-color", "driven-color", "director-color", "min-wire"))
                .end()
                .renderComponent(this::render);
    }

    private void render(NTxRendererContext rc) {
        NTxNode node = rc.node();
        NTxBounds2D b = rc.selfBounds2D();
        double w = b.widthX(), h = b.widthY();
        Mode m = mode(rc, DEFAULT_SHOW, ALL);
        Style st = style(rc, h);
        boolean top = "top".equalsIgnoreCase(str(node, "view", "front"));
        boolean ePlane = "e".equalsIgnoreCase(str(node, "plane", "H"));
        boolean pattern = m.show.contains("pattern");

        // ---- geometry and solution
        int nd = (int) clamp(dbl(node, "directors", 3), 0, 12);
        double[] len = list(node, "lengths", defaultLengths(nd));
        double[] pos = list(node, "positions", defaultPositions(nd));
        int n = Math.min(len.length, pos.length);
        len = java.util.Arrays.copyOf(len, n);
        pos = java.util.Arrays.copyOf(pos, n);
        for (int i = 0; i < n; i++) len[i] = clamp(len[i], 0.05, 0.95);
        int drv = (int) clamp(dbl(node, "driven", 1), 0, n - 1);
        YagiSolver.Result r = YagiSolver.solve(len, pos, drv, Math.max(1e-4, dbl(node, "radius", 0.003)));

        Paint cRefl = paint(node, "reflector-color", new Color(0xC62828));
        Paint cDrv = paint(node, "driven-color", new Color(0x1565C0));
        Paint cDir = paint(node, "director-color", new Color(0x2E7D32));

        // ---- layout
        double patW = pattern ? w * clamp(dbl(node, "pattern-width", 0.38), 0.15, 0.7) : 0;
        double leftW = w - patW;
        int labelLines = m.show.contains("labels") ? 4 : 0;
        double hEff = h - (labelLines > 0 ? labelLines * st.fontSize * 1.3 + 10 : 0);
        double cx = b.minX() + leftW / 2.0, cy = b.minY() + hEff / 2.0;
        double xMin = pos[0], xMax = pos[0], lMax = len[0];
        for (int i = 0; i < n; i++) {
            xMin = Math.min(xMin, pos[i]);
            xMax = Math.max(xMax, pos[i]);
            lMax = Math.max(lMax, len[i]);
        }
        double xMid = (xMin + xMax) / 2, span = xMax - xMin;
        double waveRMax = Math.min(leftW / 2.0, hEff / 2.0) - 2;
        double waveCount = Math.max(0.5, dbl(node, "wave-count", 2.5));
        double lambdaPx = top
                ? waveRMax / waveCount
                : Math.min(hEff * 0.72 / lMax, leftW * 0.9 / (span + 0.4));
        double cosT = Math.cos(m.wt);
        double[] px = new double[n];
        for (int i = 0; i < n; i++) px[i] = cx + (pos[i] - xMid) * lambdaPx;
        Paint[] col = new Paint[n];
        String[] names = new String[n];
        for (int i = 0; i < n; i++) {
            col[i] = i < drv ? cRefl : (i == drv ? cDrv : cDir);
            names[i] = i < drv ? (drv > 1 ? "R" + (i + 1) : "R") : (i == drv ? "D" : "d" + (i - drv));
        }
        NTxGraphics g = rc.graphics();
        java.util.function.DoubleUnaryOperator patFn = psi -> r.pat(psi, ePlane);

        // ---- top view: summed field of all elements in the azimuth plane
        if (top && m.show.contains("waves")) {
            double r0 = Math.max(span / 2 * lambdaPx * 1.1, 0.35 * lambdaPx);
            double norm = r.fieldPeak / (r0 / lambdaPx);
            Field f = (x, y) -> {
                double sr = 0, si = 0;
                for (int i = 0; i < n; i++) {
                    double dx = x - px[i], dy = y - cy;
                    double dist = Math.hypot(dx, dy) / lambdaPx;      // in wavelengths
                    if (dist < 1e-3) continue;
                    NDoubleComplex c = r.cur[i].multiplyDouble(r.g90[i] / dist).multiplyDoubleComplex(NDoubleComplex.ofPolar(1,m.wt - 2 * Math.PI * dist));
                    sr += c.realValue();
                    si += c.imagValue();
                }
                return sr / norm;
            };
            if ("rings".equalsIgnoreCase(str(node, "wave-style", "field"))) {
                rings(g, cx, cy, waveRMax, r0, lambdaPx, m.wt, st.wavePos, st.waveNeg, st.lineW,
                        (int) dbl(node, "wave-rings", 40), psi -> patFn.applyAsDouble(psi));
            } else {
                fieldMap(g, cx, cy, waveRMax, r0, Math.max(3, dbl(node, "wave-cell", 5)), f, st.wavePos, st.waveNeg);
            }
        }

        // ---- boom
        stroke(g, 1.5f);
        g.setPaint(alpha(st.wire, 150));
        GeneralPath boom = new GeneralPath();
        boom.moveTo(px[0] - 0.08 * lambdaPx, cy);
        boom.lineTo(px[n - 1] + 0.08 * lambdaPx, cy);
        g.draw(boom);

        if (!top) {
            drawFront(g, m, st, node, r, px, cy, lambdaPx, col, names, cosT, leftW, b.minX());
        } else {
            for (int i = 0; i < n; i++) {                       // elements seen end-on
                g.setPaint(col[i]);
                stroke(g, 2.5f);
                g.draw(new Ellipse2D.Double(px[i] - 4, cy - 4, 8, 8));
                if (m.show.contains("current")) {
                    double a = Math.abs(r.cur[i].absDouble() * Math.cos(m.wt + r.cur[i].argDouble()));
                    g.setPaint(alpha(col[i], (int) (80 + 175 * Math.min(1, a))));
                    stroke(g, 5f);
                    GeneralPath dot = new GeneralPath();
                    dot.moveTo(px[i] - 0.5, cy);
                    dot.lineTo(px[i] + 0.5, cy);
                    g.draw(dot);
                }
                text(g, names[i], px[i] - 5, cy + 22, Math.max(9, st.fontSize - 4), col[i], true);
            }
        }

        // ---- beam direction arrows and pattern
        if (m.show.contains("rays")) {
            double rb = top ? waveRMax * 0.55 : Math.min(leftW, hEff) * 0.3;
            rays(g, cx, cy, rb, waveRMax * 0.4, Math.max(4, (int) dbl(node, "rays", 12)), st.pattern, st.lineW, patFn);
        }
        if (pattern) {
            double R = Math.min(patW / 2.0, hEff / 2.0) * 0.82;
            polar(g, b.minX() + leftW + patW / 2.0, cy, R, patFn, str(node, "pattern-scale", "field"),
                    st.pattern, st.grid, st.lineW);
            text(g, ePlane ? "E-plane" : "H-plane", b.minX() + leftW + patW / 2.0 - 30, b.minY() + Math.max(9, st.fontSize - 4) + 2,
                    Math.max(9, st.fontSize - 4), st.grid, false);
        }

        // ---- labels
        if (m.show.contains("labels")) {
            double z0 = dbl(node, "z0", 50);
            NDoubleComplex gam = r.zin.subtractDoubleComplex(NDoubleComplex.of(z0,0)).divideDoubleComplex(r.zin.addDoubleComplex(NDoubleComplex.of(z0,0)));
            double swr = (1 + gam.absDouble()) / Math.max(1e-6, 1 - gam.absDouble());
            int nDir = n - drv - 1, nRef = drv;
            List<Object[]> L = new ArrayList<>();
            L.add(line("Yagi-Uda  " + n + tr(st.lang, " elements", " \u00e9l\u00e9ments") + "  (" + nRef + " "
                    + tr(st.lang, "reflector", "r\u00e9flecteur") + (nRef > 1 ? "s" : "") + " + 1 "
                    + tr(st.lang, "driven", "rayonnant") + " + " + nDir + " "
                    + tr(st.lang, "director", "directeur") + (nDir > 1 ? "s" : "") + ")", st.text, true));
            L.add(line("D = " + fmt(r.directivity, 1) + " (" + fmt(10 * Math.log10(r.directivity), 1) + " dBi)   F/B = "
                    + fmt(r.frontToBackDb, 1) + " dB   HPBW(H) = " + fmt(r.hpbwDeg, 0) + "\u00b0", st.text, false));
            L.add(line("Zin = " + r.zin + " \u03a9   SWR(" + fmt(z0, 0) + " \u03a9) = " + fmt(swr, 1), st.text, false));
            L.add(line(tr(st.lang, "reflector longer, directors shorter than \u03bb/2: currents lag \u2192 beam toward the directors",
                            "r\u00e9flecteur plus long, directeurs plus courts que \u03bb/2 : courants en retard \u2192 faisceau vers les directeurs"),
                    st.grid, false));
            textBlock(g, b.minX() + 6, b.minY() + hEff + 4, st.fontSize, L);
        }

        rc.drawContour();
        if (m.anim) {
            rc.requestAnimationFrame(true);
        }
    }

    // ------------------------------------------------------------------ front view

    private void drawFront(NTxGraphics g, Mode m, Style st, NTxNode node, YagiSolver.Result r, double[] px, double cy,
                           double lambdaPx, Paint[] col, String[] names, double cosT, double leftW, double leftX) {
        int n = r.n;
        double bulgePx = Math.min(0.2 * lambdaPx, leftW * 0.06) * dbl(node, "bulge", 1.0);
        // normalisation: largest feed-current amplitude among the elements (profile peaks are close to 1)
        double aMax = 0;
        for (int i = 0; i < n; i++) aMax = Math.max(aMax, r.cur[i].absDouble());
        int nArrows = (int) dbl(node, "arrows", 5);
        int small = Math.max(9, st.fontSize - 4);
        for (int i = 0; i < n; i++) {
            double half = r.len[i] * lambdaPx / 2;
            double x = px[i];
            g.setPaint(col[i]);
            stroke(g, st.wireW);
            GeneralPath wire = new GeneralPath();
            if (i == r.drv) {
                wire.moveTo(x, cy - half);
                wire.lineTo(x, cy - 2.5);
                wire.moveTo(x, cy + 2.5);
                wire.lineTo(x, cy + half);
            } else {
                wire.moveTo(x, cy - half);
                wire.lineTo(x, cy + half);
            }
            g.draw(wire);
            if (i == r.drv) {
                stroke(g, 1.5f);
                g.draw(new Ellipse2D.Double(x - 4, cy - 4, 8, 8));
            }
            double s = r.cur[i].absDouble() / aMax * Math.cos(m.wt + r.cur[i].argDouble());   // signed amplitude at the feed
            double sinKh = Math.sin(Math.PI * r.len[i]);
            if (m.show.contains("current")) {
                GeneralPath curve = new GeneralPath();
                int samples = 80;
                for (int k = 0; k <= samples; k++) {
                    double u = -1 + 2.0 * k / samples;
                    double prof = Math.sin(Math.PI * r.len[i] * (1 - Math.abs(u))) / sinKh;
                    double xx = x + s * prof * bulgePx, yy = cy - u * half;
                    if (k == 0) curve.moveTo(xx, yy);
                    else curve.lineTo(xx, yy);
                }
                stroke(g, st.lineW);
                g.draw(curve);
            }
            if (m.show.contains("arrows") && nArrows > 0) {
                GeneralPath arrows = new GeneralPath();
                double cell = 2 * half / nArrows;
                for (int k = 0; k < nArrows; k++) {
                    double u = -1 + (k + 0.5) * 2.0 / nArrows;
                    double prof = Math.sin(Math.PI * r.len[i] * (1 - Math.abs(u))) / sinKh;
                    double len = s * prof * cell * 0.9;
                    if (Math.abs(len) < 2.5) continue;
                    double yMid = cy - u * half;
                    arrow(arrows, x - 8, yMid + len / 2, x - 8, yMid - len / 2, Math.min(5, Math.abs(len) * 0.5));
                }
                stroke(g, Math.max(1.5f, st.lineW * 0.8f));
                g.draw(arrows);
            }
            text(g, names[i], x - 5, cy - half - 6, small + 2, col[i], true);
            if (m.show.contains("values")) {
                String v = fmt(r.cur[i].absDouble(), 2) + "\u2220" + fmt(Math.toDegrees(r.cur[i].argDouble()), 0) + "\u00b0";
                text(g, v, x - textWidth(v, small) / 2, cy + half + small + 4 + (i % 2) * (small + 3), small, col[i], false);  // staggered
            }
        }
        // beam direction
        double xEnd = px[n - 1] + 0.12 * lambdaPx;
        GeneralPath beam = new GeneralPath();
        arrow(beam, xEnd, cy, Math.min(xEnd + 0.3 * lambdaPx, leftX + leftW - 4), cy, 9);
        stroke(g, 3f);
        g.setPaint(st.pattern);
        g.draw(beam);
    }

    // ------------------------------------------------------------------ default designs

    /** Reflector, driven element, then directors (lengths in wavelengths). */
    private static double[] defaultLengths(int directors) {
        double[] dirs = {0.44, 0.43, 0.43, 0.425};
        double[] out = new double[2 + directors];
        out[0] = 0.495;
        out[1] = 0.473;
        for (int i = 0; i < directors; i++) out[2 + i] = dirs[Math.min(i, dirs.length - 1)];
        return out;
    }

    private static double[] defaultPositions(int directors) {
        double[] out = new double[2 + directors];
        out[0] = -0.2;
        out[1] = 0;
        for (int i = 0; i < directors; i++) {
            out[2 + i] = (directors == 1) ? 0.2 : 0.15 + 0.25 * i;
        }
        return out;
    }
}