package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.eval.NTxValue;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Center-fed linear dipole visualization for the ntexup-antennas extension.
 *
 * Aspects (toggle with show:"..."):
 *   current  : instantaneous current I(z,t) = I(z) cos(wt) drawn as a bulge around the wire
 *   envelope : standing-wave envelope +-I(z) (the "box" the current breathes in)
 *   charge   : +/- charge density q(z,t), 90 degrees out of phase with the current
 *   arrows   : current direction/magnitude arrows along the wire
 *   waves    : radiated wavefronts (crests/troughs), weighted by the radiation pattern
 *   rays     : outward propagation arrows (Poynting direction), length ~ radiation pattern
 *   pattern  : polar radiation pattern (elevation cut) on the right
 *   labels   : name, formula, directivity and half-power beamwidth
 *
 * Presets (preset:"..."): hertzian, short, quarter-wave, half-wave, three-quarter,
 * full-wave, three-half-wave, two-wave.  An explicit length (in wavelengths) overrides the preset.
 *
 * Usage:
 *   dipole-current(preset:"half-wave" show:"all" size:(400,260))
 *   dipole-current(length:0.1 size:(50,50))
 *   dipole-current(preset:"full-wave" show:"current,envelope,pattern" phase:90)   // static snapshot (print mode)
 *
 * Per-mode content: "show" is the base list, "show-animate" / "show-print" override it (token "base" = the base list):
 *   dipole-current(preset:"half-wave" show-animate:"current,charge,waves,pattern" show-print:"base,envelope,rays,labels" phase-print:90)
 */
public class DipoleCurrentBuilder implements NTxNodeBuilder {

    private static final String[] PARAMS = {
            // physics
            "preset", "length", "phase",
            // animation
            "animate-period",
            // what to show
            "show", "show-print", "show-animate", "phase-print", "pattern-scale", "pattern-width",
            // geometry / density
            "wavelength", "bulge", "arrows", "charges", "charge-offset", "min-wire",
            "wave-count", "wave-style", "wave-cell", "wave-rings", "rays",
            // style
            "wire-color", "charge-pos-color", "charge-neg-color", "wave-color", "wave-neg-color",
            "pattern-color", "grid-color", "text-color", "line-width", "wire-width", "font-size"
    };

    private static final String ALL = "current,envelope,charge,arrows,waves,rays,pattern,labels";
    private static final String DEFAULT_SHOW = "current,charge,arrows,pattern,labels";

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext
                .id("dipole-current")
                .parseParam()
                .matchesNamedPair(PARAMS)   // if your API only takes 2 names, chain one call per pair
                .end()
                .renderComponent(this::render);
    }

    // ------------------------------------------------------------------ rendering

    private void render(NTxRendererContext rc) {
        NTxBounds2D b = rc.selfBounds2D();
        if (b.widthX() <= 4 || b.widthY() <= 4) {
            return;
        }
        rc.withinClip(b, this::renderBody);
        rc.drawContour();
    }

    private void renderBody(NTxRendererContext rc) {
        NTxNode node = rc.node();

        // ---- physics parameters
        double lenParam = dbl(node, "length", Double.NaN);
        double length = !Double.isNaN(lenParam) ? lenParam : presetLength(str(node, "preset", "half-wave"));
        length = Math.max(0.001, length); // in wavelengths

        // ---- time
        double periodMs = dbl(node, "animate-period", 2000.0);
        if (periodMs <= 0) periodMs = 2000.0;
        // Mode: print (export) -> static; animate (live presentation) -> moving; otherwise static (editor preview)
        boolean print = rc.isPrint();
        boolean animMode = rc.isAnimate() && !print;
        boolean live = animMode && rc.isPageShowing();
        double phaseDeg = dbl(node, "phase", 0);                 // static snapshot phase
        if (print) phaseDeg = dbl(node, "phase-print", phaseDeg); // optional print-specific snapshot
        double wt = Math.toRadians(phaseDeg);
        if (live) {
            double elapsedMs = rc.pageElapsedSeconds() * 1000.0;
            wt += 2 * Math.PI * ((elapsedMs % periodMs) / periodMs);
        }
        double cosWt = Math.cos(wt);
        double sinWt = Math.sin(wt);

        // ---- what to show
        // "show" is the base; "show-print" / "show-animate" override it per mode.
        // Inside an override, the token "base" expands to the base list: show-print:"base,envelope,-arrows"
        String baseSpec = str(node, "show", DEFAULT_SHOW);
        String showSpec = baseSpec;
        if (print) {
            showSpec = str(node, "show-print", baseSpec).replaceAll("\\bbase\\b", baseSpec);
        } else if (animMode) {
            showSpec = str(node, "show-animate", baseSpec).replaceAll("\\bbase\\b", baseSpec);
        }
        Set<String> show = parseShow(showSpec);
        boolean pattern = show.contains("pattern");

        // ---- style
        Paint currentPaint = rc.getLineColor(true);
        Paint wirePaint = paint(node, "wire-color", Color.DARK_GRAY);
        Paint posPaint = paint(node, "charge-pos-color", new Color(0xD32F2F));
        Paint negPaint = paint(node, "charge-neg-color", new Color(0x1976D2));
        Paint wavePos = paint(node, "wave-color", new Color(0xE65100));
        Paint waveNeg = paint(node, "wave-neg-color", new Color(0x0277BD));
        Paint patPaint = paint(node, "pattern-color", new Color(0x2E7D32));
        Paint gridPaint = paint(node, "grid-color", new Color(128, 128, 128));
        Paint textPaint = paint(node, "text-color", Color.DARK_GRAY);
        float lineW = (float) dbl(node, "line-width", 2.0);
        float wireW = (float) dbl(node, "wire-width", 3.0);

        // ---- layout
        NTxBounds2D b = rc.selfBounds2D();
        double w = b.widthX();
        double h = b.widthY();
        double patW = pattern ? w * clamp(dbl(node, "pattern-width", 0.42), 0.15, 0.7) : 0;
        double leftW = w - patW;
        double cx = b.minX() + leftW / 2.0;
        double cy = b.minY() + h / 2.0;

        // Wavelength in pixels (drives the wavefront spacing). The wire itself is drawn at least
        // "min-wire" (fraction of height) long so very short dipoles stay visible.
        // Radius available for the radiated field, and how many wavelengths we want to see in it.
        double waveRMax = Math.min(leftW / 2.0, h / 2.0) - 2;
        double waveCount = Math.max(0.5, dbl(node, "wave-count", 3.0));
        double lambdaPx;
        if (dbl(node, "wavelength", Double.NaN) > 0) {
            lambdaPx = dbl(node, "wavelength", 0.5) * h;
        } else if (show.contains("waves") || show.contains("rays")) {
            lambdaPx = waveRMax / waveCount;   // several wavelengths visible => propagation is readable
        } else {
            lambdaPx = h * 0.8 / Math.max(length, 1.0);
        }
        double minWire = dbl(node, "min-wire", 0.14) * h;
        double wirePx = Math.max(length * lambdaPx, minWire);
        double halfPx = wirePx / 2.0;
        double bulgePx = Math.min(leftW * 0.3, h * 0.2) * dbl(node, "bulge", 1.0);

        // normalization of I(u) so the largest current always uses the full bulge
        double iMax = 0;
        for (int i = 0; i <= 200; i++) iMax = Math.max(iMax, Math.abs(cur(-1 + i / 100.0, length)));
        if (iMax < 1e-9) iMax = 1;

        double[] pat = samplePattern(length);

        NTxGraphics g = rc.graphics();

        // ---- 1. radiated field (background)
        //   E(r,theta,t) ~ F(theta)/r * sin(wt - kr)   -> moves outward at the phase velocity.
        //   Strong colour = constructive interference (contributions from the whole wire add up),
        //   faded colour = destructive interference (they cancel, e.g. along the dipole axis).
        if (show.contains("waves")) {
            double rMax = waveRMax;
            double r0 = Math.max(halfPx * 1.1, 0.25 * lambdaPx);
            double frac = wt / (2 * Math.PI);
            String style = str(node, "wave-style", "field");
            if (style.equalsIgnoreCase("rings")) {
                int maxRings = (int) dbl(node, "wave-rings", 40);
                // crests at r = lambda (frac - 1/4 + j/2), alternating sign
                int j = (int) Math.ceil((r0 / lambdaPx - frac + 0.25) * 2);
                stroke(g, lineW);
                for (int drawn = 0; drawn < maxRings; j++, drawn++) {
                    double r = lambdaPx * (frac - 0.25 + 0.5 * j);
                    if (r > rMax) break;
                    if (r < r0) continue;
                    Paint base = (Math.floorMod(j, 2) == 0) ? wavePos : waveNeg;
                    double fade = Math.min(1.0, r0 / r);
                    int seg = 90;
                    for (int s = 0; s < seg; s++) {
                        double a1 = 2 * Math.PI * s / seg, a2 = 2 * Math.PI * (s + 1) / seg;
                        int alpha = (int) (235 * at(pat, (a1 + a2) / 2) * fade);
                        if (alpha < 8) continue;
                        GeneralPath piece = new GeneralPath();
                        piece.moveTo(cx + r * Math.sin(a1), cy - r * Math.cos(a1));
                        piece.lineTo(cx + r * Math.sin(a2), cy - r * Math.cos(a2));
                        g.setPaint(alpha(base, alpha));
                        g.draw(piece);
                    }
                }
            } else {
                // "field": colour map of the instantaneous E field (crest / trough), drawn as short thick strokes
                double cell = Math.max(3, dbl(node, "wave-cell", 5));
                strokeButt(g, (float) cell);
                GeneralPath dash = new GeneralPath();
                for (double py = cy - rMax; py <= cy + rMax; py += cell) {
                    for (double px = cx - rMax; px <= cx + rMax; px += cell) {
                        double dx = px - cx, dy = cy - py;          // dy > 0 is +z (up)
                        double r = Math.hypot(dx, dy);
                        if (r < r0 || r > rMax) continue;
                        double theta = Math.atan2(dx, dy);          // angle from the dipole axis
                        double amp = at(pat, theta) * Math.min(1.0, r0 / r);
                        double e = amp * Math.sin(wt - 2 * Math.PI * r / lambdaPx);
                        int alpha = (int) (240 * Math.abs(e));
                        if (alpha < 6) continue;
                        dash.reset();
                        dash.moveTo(px - cell / 2, py);
                        dash.lineTo(px + cell / 2, py);
                        g.setPaint(alpha(e > 0 ? wavePos : waveNeg, alpha));
                        g.draw(dash);
                    }
                }
            }
        }

        // ---- 2. wire (with feed gap)
        double gap = 2.5;
        GeneralPath wire = new GeneralPath();
        wire.moveTo(cx, cy - halfPx);
        wire.lineTo(cx, cy - gap);
        wire.moveTo(cx, cy + gap);
        wire.lineTo(cx, cy + halfPx);
        stroke(g, wireW);
        g.setPaint(wirePaint);
        g.draw(wire);
        stroke(g, 1.5f);
        g.draw(new Ellipse2D.Double(cx - 4, cy - 4, 8, 8)); // feed point

        // ---- 3. current envelope (ghost) and instantaneous current
        int samples = 120;
        if (show.contains("envelope")) {
            GeneralPath envPos = new GeneralPath();
            GeneralPath envNeg = new GeneralPath();
            for (int i = 0; i <= samples; i++) {
                double u = -1 + 2.0 * i / samples;
                double y = cy - u * halfPx;
                double off = cur(u, length) / iMax * bulgePx;
                if (i == 0) { envPos.moveTo(cx + off, y); envNeg.moveTo(cx - off, y); }
                else { envPos.lineTo(cx + off, y); envNeg.lineTo(cx - off, y); }
            }
            stroke(g, 1.2f);
            g.setPaint(alpha(currentPaint, 90));
            g.draw(envPos);
            g.draw(envNeg);
        }
        if (show.contains("current")) {
            GeneralPath curve = new GeneralPath();
            for (int i = 0; i <= samples; i++) {
                double u = -1 + 2.0 * i / samples;
                double y = cy - u * halfPx;
                double x = cx + cur(u, length) / iMax * cosWt * bulgePx;
                if (i == 0) curve.moveTo(x, y); else curve.lineTo(x, y);
            }
            stroke(g, lineW);
            g.setPaint(currentPaint);
            g.draw(curve);
        }

        // ---- 4. current arrows (direction + magnitude, +z is up)
        int nArrows = (int) dbl(node, "arrows", 9);
        if (show.contains("arrows") && nArrows > 0) {
            GeneralPath arrows = new GeneralPath();
            double cell = wirePx / nArrows;
            double maxLen = cell * 0.9;
            for (int i = 0; i < nArrows; i++) {
                double u = -1 + (i + 0.5) * 2.0 / nArrows;
                double v = cur(u, length) / iMax * cosWt;
                double len = v * maxLen;
                if (Math.abs(len) < 2.5) continue;
                double yMid = cy - u * halfPx;
                double y1 = yMid + len / 2, y2 = yMid - len / 2; // tail -> head, up if v > 0
                double head = Math.min(5, Math.abs(len) * 0.5);
                double dir = Math.signum(y2 - y1);
                arrows.moveTo(cx, y1);
                arrows.lineTo(cx, y2);
                arrows.moveTo(cx - head, y2 - dir * head);
                arrows.lineTo(cx, y2);
                arrows.lineTo(cx + head, y2 - dir * head);
            }
            stroke(g, Math.max(1.5f, lineW * 0.8f));
            g.setPaint(currentPaint);
            g.draw(arrows);
        }

        // ---- 5. charge density (+ / -), 90 degrees ahead of the current
        int nCharges = (int) dbl(node, "charges", 11);
        if (show.contains("charge") && nCharges > 0) {
            double xOff = dbl(node, "charge-offset", 14);
            stroke(g, 1.6f);
            for (int i = 0; i < nCharges; i++) {
                double u = -1 + (i + 0.5) * 2.0 / nCharges;
                double q = chg(u, length) * sinWt;
                double m = Math.abs(q);
                if (m < 0.08) continue;
                double x = cx - xOff, y = cy - u * halfPx;
                double r = 2 + 4 * m;
                int a = (int) (70 + 185 * m);
                GeneralPath sign = new GeneralPath();
                sign.moveTo(x - r, y);
                sign.lineTo(x + r, y);
                if (q > 0) { sign.moveTo(x, y - r); sign.lineTo(x, y + r); }
                g.setPaint(alpha(q > 0 ? posPaint : negPaint, a));
                g.draw(sign);
            }
        }

        // ---- 6. radiation pattern (polar, elevation cut; the dipole axis is vertical)
        double dirD = directivity(pat);
        double hpbw = hpbwDeg(pat);
        if (pattern) {
            double pcx = b.minX() + leftW + patW / 2.0;
            double pcy = cy;
            double R = Math.min(patW / 2.0, h / 2.0) * 0.82;
            boolean power = "power".equalsIgnoreCase(str(node, "pattern-scale", "field"));

            stroke(g, 1.0f);
            g.setPaint(alpha(gridPaint, 110));
            for (double rr : new double[]{0.5, 1.0}) {
                GeneralPath circle = new GeneralPath();
                for (int s = 0; s <= 72; s++) {
                    double a = 2 * Math.PI * s / 72;
                    double x = pcx + R * rr * Math.sin(a), y = pcy - R * rr * Math.cos(a);
                    if (s == 0) circle.moveTo(x, y); else circle.lineTo(x, y);
                }
                g.draw(circle);
            }
            GeneralPath axes = new GeneralPath();
            axes.moveTo(pcx, pcy - R * 1.08); axes.lineTo(pcx, pcy + R * 1.08);
            axes.moveTo(pcx - R * 1.08, pcy); axes.lineTo(pcx + R * 1.08, pcy);
            g.draw(axes);

            GeneralPath lobe = new GeneralPath();
            int ps = 180;
            for (int s = 0; s <= ps; s++) {
                double a = 2 * Math.PI * s / ps;
                double f = at(pat, a);
                if (power) f = f * f;
                double x = pcx + R * f * Math.sin(a), y = pcy - R * f * Math.cos(a);
                if (s == 0) lobe.moveTo(x, y); else lobe.lineTo(x, y);
            }
            stroke(g, lineW);
            g.setPaint(patPaint);
            g.draw(lobe);
        }

        // ---- 7. labels
        if (show.contains("labels")) {
            int fs = (int) dbl(node, "font-size", Math.max(10, h * 0.05));
            g.setFont(new Font("SansSerif", Font.PLAIN, fs));
            g.setPaint(textPaint);
            double x = b.minX() + 6;
            double y = b.minY() + b.widthY() - 6;
            g.drawString("I(z,t) = I\u2080 sin[k(L/2 \u2212 |z|)] cos \u03c9t", (float) x, (float) y);
            y -= fs * 1.3;
            g.drawString(String.format("D = %.2f (%.2f dBi)   HPBW = %.0f\u00b0", dirD, 10 * Math.log10(dirD), hpbw),
                    (float) x, (float) y);
            y -= fs * 1.3;
            g.drawString(nameFor(length) + " dipole   L = " + trim(length) + " \u03bb", (float) x, (float) y);
        }

        // ---- 6b. propagation arrows (Poynting direction), length follows the radiation pattern
        if (show.contains("rays")) {
            int nr = Math.max(4, (int) dbl(node, "rays", 12));
            double rb = waveRMax * 0.55, maxLen = waveRMax * 0.4;
            stroke(g, Math.max(1.5f, lineW * 0.8f));
            g.setPaint(patPaint);
            for (int k = 0; k < nr; k++) {
                double a = 2 * Math.PI * (k + 0.5) / nr;
                double f = at(pat, a);
                if (f < 0.06) continue;
                double len = maxLen * f;
                double dx = Math.sin(a), dy = -Math.cos(a);
                double x2 = cx + dx * (rb + len), y2 = cy + dy * (rb + len);
                double head = Math.min(6, len * 0.4), nx = -dy, ny = dx;
                GeneralPath ray = new GeneralPath();
                ray.moveTo(cx + dx * rb, cy + dy * rb);
                ray.lineTo(x2, y2);
                ray.moveTo(x2 - dx * head + nx * head * 0.6, y2 - dy * head + ny * head * 0.6);
                ray.lineTo(x2, y2);
                ray.lineTo(x2 - dx * head - nx * head * 0.6, y2 - dy * head - ny * head * 0.6);
                g.draw(ray);
            }
        }

        // ---- 6c. constructive / destructive markers
        if (show.contains("labels") && (show.contains("waves") || show.contains("rays"))) {
            int fs = (int) dbl(node, "font-size", Math.max(10, h * 0.05));
            g.setFont(new Font("SansSerif", Font.BOLD, fs));
            double am = mainLobe(pat);
            double rr = waveRMax * 0.95;
            g.setPaint(patPaint);
            g.drawString("constructive", (float) (cx + rr * Math.sin(am) * 0.8 - fs * 3), (float) (cy - rr * Math.cos(am) * 0.8 - 4));
            g.setPaint(negPaint);
            g.drawString("destructive", (float) (cx + 6), (float) (cy - rr + fs));
        }

        // Ask the hosting page for the next FPS tick; speed comes from the clock, not a timer.
        if (animMode) {
            rc.requestAnimationFrame(true);
        }
    }

    // ------------------------------------------------------------------ physics

    /** Normalized current at u in [-1,1] (u = 2z/L): I(z) = sin(k(L/2 - |z|)) = sin(pi L (1 - |u|)). */
    private static double cur(double u, double lengthWl) {
        return Math.sin(Math.PI * lengthWl * (1 - Math.abs(u)));
    }

    /** Charge density shape: q ~ -dI/dz, odd in z, 90 degrees out of phase with I. */
    private static double chg(double u, double lengthWl) {
        return Math.signum(u) * Math.cos(Math.PI * lengthWl * (1 - Math.abs(u)));
    }

    /** Far-field pattern |F(theta)|, theta from the dipole axis. */
    private static double field(double theta, double halfKL) {
        double s = Math.sin(theta);
        if (Math.abs(s) < 1e-9) return 0;
        return Math.abs((Math.cos(halfKL * Math.cos(theta)) - Math.cos(halfKL)) / s);
    }

    /** 361 samples of the normalized pattern over theta in [0, pi]. */
    private static double[] samplePattern(double lengthWl) {
        int n = 360;
        double hk = Math.PI * lengthWl;
        double[] f = new double[n + 1];
        double max = 0;
        for (int i = 0; i <= n; i++) {
            f[i] = field(i * Math.PI / n, hk);
            max = Math.max(max, f[i]);
        }
        if (max > 0) for (int i = 0; i <= n; i++) f[i] /= max;
        return f;
    }

    /** Pattern value at any angle (folded by symmetry), linear interpolation. */
    private static double at(double[] f, double a) {
        a = Math.abs(a % (2 * Math.PI));
        if (a > Math.PI) a = 2 * Math.PI - a;
        double x = a / Math.PI * (f.length - 1);
        int i = (int) Math.floor(x);
        if (i >= f.length - 1) return f[f.length - 1];
        double t = x - i;
        return f[i] * (1 - t) + f[i + 1] * t;
    }

    /** D = 2 / integral(F^2 sin(theta) dtheta), F normalized to its maximum (lossless antenna). */
    private static double directivity(double[] f) {
        int n = f.length - 1;
        double d = Math.PI / n, sum = 0;
        for (int i = 0; i < n; i++) {
            double a = f[i] * f[i] * Math.sin(i * d);
            double c = f[i + 1] * f[i + 1] * Math.sin((i + 1) * d);
            sum += 0.5 * (a + c) * d;
        }
        return sum > 0 ? 2.0 / sum : 1.0;
    }

    /** Half-power beamwidth of the strongest lobe, in degrees (0.5 degree resolution). */
    private static double hpbwDeg(double[] f) {
        int n = f.length - 1, m = 0;
        for (int i = 1; i <= n; i++) if (f[i] > f[m]) m = i;
        int lo = m, hi = m;
        while (lo > 0 && f[lo] * f[lo] >= 0.5) lo--;
        while (hi < n && f[hi] * f[hi] >= 0.5) hi++;
        return (hi - lo) * 180.0 / n;
    }

    // ------------------------------------------------------------------ presets

    private static double presetLength(String preset) {
        switch (norm(preset)) {
            case "hertzian": return 0.01;
            case "short": return 0.1;
            case "quarterwave": case "quarter": return 0.25;
            case "halfwave": case "half": return 0.5;
            case "threequarter": case "threequarterwave": return 0.75;
            case "fullwave": case "full": return 1.0;
            case "threehalfwave": case "threehalf": case "1.5wave": return 1.5;
            case "twowave": case "2wave": return 2.0;
            default: return 0.5;
        }
    }

    private static String nameFor(double l) {
        String[][] names = {{"0.01", "Hertzian"}, {"0.1", "Short"}, {"0.25", "Quarter-wave"}, {"0.5", "Half-wave"},
                {"0.75", "Three-quarter-wave"}, {"1.0", "Full-wave"}, {"1.5", "3/2-wave"}, {"2.0", "Two-wave"}};
        for (String[] n : names) if (Math.abs(Double.parseDouble(n[0]) - l) < 1e-6) return n[1];
        return "Center-fed";
    }

    // ------------------------------------------------------------------ helpers

    private static String norm(String s) {
        return s == null ? "" : s.toLowerCase().replaceAll("[^a-z0-9.]", "");
    }

    /** "all", "current,charge", "all,-envelope" are all valid. */
    private static Set<String> parseShow(String spec) {
        Set<String> out = new LinkedHashSet<>();
        for (String tok : spec.toLowerCase().split("[,;\\s]+")) {
            if (tok.isEmpty()) continue;
            if (tok.equals("all")) { for (String a : ALL.split(",")) out.add(a); }
            else if (tok.startsWith("-")) out.remove(tok.substring(1));
            else out.add(tok);
        }
        return out;
    }

    private static double dbl(NTxNode n, String name, double def) {
        return NTxValue.of(n.getPropertyValue(name)).asDouble().orElse(def);
    }

    // NOTE: asString() is assumed to exist on NTxValue, like asDouble()/asPaint().
    private static String str(NTxNode n, String name, String def) {
        return NTxValue.of(n.getPropertyValue(name)).asString().orElse(def);
    }

    private static Paint paint(NTxNode n, String name, Paint def) {
        return NTxValue.of(n.getPropertyValue(name)).asPaint().orElse(def);
    }

    private static Paint alpha(Paint p, int a) {
        if (p instanceof Color) {
            Color c = (Color) p;
            return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, Math.min(255, a)));
        }
        return p;
    }

    // NOTE: assumes NTxGraphics exposes setStroke(Stroke) like Graphics2D; adapt if it differs.
    private static void stroke(NTxGraphics g, float width) {
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    /** Angle (from the axis, radians) of the strongest lobe, sampled 0..pi. */
    private static double mainLobe(double[] f) {
        int m = 0;
        for (int i = 1; i < f.length; i++) if (f[i] > f[m]) m = i;
        return m * Math.PI / (f.length - 1);
    }

    // NOTE: same assumption as stroke(): g.setStroke(Stroke) exists.
    private static void strokeButt(NTxGraphics g, float width) {
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    private static String trim(double v) {
        return (v == Math.rint(v)) ? String.valueOf((long) v) : String.valueOf(v);
    }
}
