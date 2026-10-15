package net.thevpc.ntexup.extension.antennas.phys;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.eval.NTxValue;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.DoubleUnaryOperator;

/**
 * Shared helpers for the ntexup-antennas components: parameter access, animation / print mode,
 * styling, field maps, polar plots, arrows, small complex-number class.
 *
 * Conventions used everywhere:
 *  - "psi" = direction angle in the screen plane, counter-clockwise from +x (math convention, y up)
 *  - "theta" = angle from the vertical axis (dipole / loop axis)
 *  - phasors use exp(+j w t), so exp(j(wt - kr)) travels outward
 */
public final class AntennaKit {
    private AntennaKit() {
    }

    // ------------------------------------------------------------------ parameters

    private static final String[] COMMON = {
            "show", "show-print", "show-animate", "phase", "phase-print", "animate-period", "lang",
            "wire-color", "text-color", "grid-color", "pattern-color", "wave-color", "wave-neg-color",
            "charge-pos-color", "charge-neg-color", "line-width", "wire-width", "font-size",
            "wave-count", "wave-style", "wave-cell", "wave-rings", "rays", "pattern-scale", "pattern-width",
            "bulge", "arrows"
    };

    /** The parameters every component understands, plus the component's own. */
    public static String[] params(String... extra) {
        String[] out = new String[COMMON.length + extra.length];
        System.arraycopy(COMMON, 0, out, 0, COMMON.length);
        System.arraycopy(extra, 0, out, COMMON.length, extra.length);
        return out;
    }

    public static double dbl(NTxNode n, String name, double def) {
        return NTxValue.of(n.getPropertyValue(name)).asDouble().orElse(def);
    }

    // NOTE: asString()/asBoolean() are assumed to exist on NTxValue, like asDouble()/asPaint().
    public static String str(NTxNode n, String name, String def) {
        return NTxValue.of(n.getPropertyValue(name)).asString().orElse(def);
    }

    public static boolean bool(NTxNode n, String name, boolean def) {
        return NTxValue.of(n.getPropertyValue(name)).asBoolean().orElse(def);
    }

    public static Paint paint(NTxNode n, String name, Paint def) {
        return NTxValue.of(n.getPropertyValue(name)).asPaint().orElse(def);
    }

    /** Comma/space separated numbers, e.g. "0.495, 0.473, 0.44". Returns def when absent or malformed. */
    public static double[] list(NTxNode n, String name, double[] def) {
        String s = str(n, name, null);
        if (s == null || s.trim().isEmpty()) return def;
        String[] t = s.trim().replaceAll("[()\\[\\]]", "").split("[,;\\s]+");
        double[] out = new double[t.length];
        try {
            for (int i = 0; i < t.length; i++) out[i] = Double.parseDouble(t[i]);
        } catch (NumberFormatException e) {
            return def;
        }
        return out;
    }

    public static String norm(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9.]", "");
    }

    public static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    public static String fmt(double v, int decimals) {
        return String.format(Locale.ROOT, "%." + decimals + "f", v);
    }

    public static String tr(String lang, String en, String fr) {
        return "fr".equalsIgnoreCase(lang) ? fr : en;
    }

    // ------------------------------------------------------------------ mode (animate / print)

    public static final class Mode {
        public boolean print;      // export / print: static frame
        public boolean anim;       // live presentation mode
        public boolean live;       // anim && page is showing: the clock runs
        public double wt;          // phase of the source, radians
        public Set<String> show;
    }

    /**
     * Reads the animation clock and the list of aspects to show.
     * "show" is the base list; "show-print" / "show-animate" override it in their mode
     * (inside an override, the token "base" expands to the base list). A leading "-" removes an aspect, "all" adds all.
     */
    public static Mode mode(NTxRendererContext rc, String defaultShow, String allKeys) {
        NTxNode node = rc.node();
        Mode m = new Mode();
        m.print = rc.isPrint();
        m.anim = rc.isAnimate() && !m.print;
        m.live = m.anim && rc.isPageShowing();
        double periodMs = dbl(node, "animate-period", 2000.0);
        if (periodMs <= 0) periodMs = 2000.0;
        double deg = dbl(node, "phase", 0);
        if (m.print) deg = dbl(node, "phase-print", deg);
        m.wt = Math.toRadians(deg);
        if (m.live) {
            double elapsedMs = rc.pageElapsedSeconds() * 1000.0;
            m.wt += 2 * Math.PI * ((elapsedMs % periodMs) / periodMs);
        }
        String base = str(node, "show", defaultShow);
        String spec = base;
        if (m.print) spec = str(node, "show-print", base).replaceAll("\\bbase\\b", base);
        else if (m.anim) spec = str(node, "show-animate", base).replaceAll("\\bbase\\b", base);
        m.show = parseShow(spec, allKeys);
        return m;
    }

    static Set<String> parseShow(String spec, String allKeys) {
        Set<String> out = new LinkedHashSet<>();
        for (String tok : spec.toLowerCase(Locale.ROOT).split("[,;\\s]+")) {
            if (tok.isEmpty()) continue;
            if (tok.equals("all")) {
                for (String a : allKeys.split(",")) out.add(a);
            } else if (tok.startsWith("-")) {
                out.remove(tok.substring(1));
            } else {
                out.add(tok);
            }
        }
        return out;
    }

    // ------------------------------------------------------------------ style

    public static final class Style {
        public Paint current, wire, pos, neg, wavePos, waveNeg, pattern, grid, text;
        public float lineW, wireW;
        public int fontSize;
        public String lang;
    }

    public static Style style(NTxRendererContext rc, double h) {
        NTxNode n = rc.node();
        Style s = new Style();
        s.current = rc.getLineColor(true);
        s.wire = paint(n, "wire-color", Color.DARK_GRAY);
        s.pos = paint(n, "charge-pos-color", new Color(0xD32F2F));
        s.neg = paint(n, "charge-neg-color", new Color(0x1976D2));
        s.wavePos = paint(n, "wave-color", new Color(0xE65100));
        s.waveNeg = paint(n, "wave-neg-color", new Color(0x0277BD));
        s.pattern = paint(n, "pattern-color", new Color(0x2E7D32));
        s.grid = paint(n, "grid-color", new Color(128, 128, 128));
        s.text = paint(n, "text-color", Color.DARK_GRAY);
        s.lineW = (float) dbl(n, "line-width", 2.0);
        s.wireW = (float) dbl(n, "wire-width", 3.0);
        s.fontSize = (int) dbl(n, "font-size", Math.max(10, h * 0.04));
        s.lang = str(n, "lang", "en");
        return s;
    }

    public static Paint alpha(Paint p, int a) {
        if (p instanceof Color) {
            Color c = (Color) p;
            return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, Math.min(255, a)));
        }
        return p;
    }

    // NOTE: assumes NTxGraphics exposes setStroke(Stroke) like Graphics2D; adapt if it differs.
    public static void stroke(NTxGraphics g, float w) {
        g.setStroke(new BasicStroke(w, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    public static void strokeButt(NTxGraphics g, float w) {
        g.setStroke(new BasicStroke(w, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
    }

    public static void dashed(NTxGraphics g, float w) {
        g.setStroke(new BasicStroke(w, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{6f, 4f}, 0f));
    }

    // NOTE: assumes g.setFont(Font) and g.drawString(String,float,float).
    public static void text(NTxGraphics g, String s, double x, double y, int size, Paint p, boolean bold) {
        g.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, size));
        g.setPaint(p);
        g.drawString(s, (float) x, (float) y);
    }


    /** Top-down block of text lines starting at yTop (first baseline = yTop + fontSize). */
    public static void textBlock(NTxGraphics g, double x, double yTop, int fs, java.util.List<Object[]> lines) {
        double y = yTop + fs;
        for (Object[] l : lines) {
            text(g, (String) l[0], x, y, fs, (Paint) l[1], (Boolean) l[2]);
            y += fs * 1.3;
        }
    }

    public static Object[] line(String s, Paint p, boolean bold) {
        return new Object[]{s, p, bold};
    }

    /** Rough text width (no font metrics available through the ntexup graphics API). */
    public static double textWidth(String s, int size) {
        return 0.58 * size * s.length();
    }

    /** Adds an arrow (shaft + head) from (x1,y1) to (x2,y2) to the path. */
    public static void arrow(GeneralPath p, double x1, double y1, double x2, double y2, double head) {
        double dx = x2 - x1, dy = y2 - y1, len = Math.hypot(dx, dy);
        if (len < 1e-6) return;
        dx /= len;
        dy /= len;
        double hd = Math.min(head, len * 0.6), nx = -dy, ny = dx;
        p.moveTo(x1, y1);
        p.lineTo(x2, y2);
        p.moveTo(x2 - dx * hd + nx * hd * 0.6, y2 - dy * hd + ny * hd * 0.6);
        p.lineTo(x2, y2);
        p.lineTo(x2 - dx * hd - nx * hd * 0.6, y2 - dy * hd - ny * hd * 0.6);
    }

    // ------------------------------------------------------------------ complex numbers

    // ------------------------------------------------------------------ axisymmetric patterns (theta from the axis)

    /** 361 samples over theta in [0,pi], normalized to the maximum. */
    public static double[] sampleAxi(DoubleUnaryOperator f) {
        int n = 360;
        double[] a = new double[n + 1];
        double max = 0;
        for (int i = 0; i <= n; i++) {
            a[i] = Math.abs(f.applyAsDouble(i * Math.PI / n));
            max = Math.max(max, a[i]);
        }
        if (max > 0) for (int i = 0; i <= n; i++) a[i] /= max;
        return a;
    }

    /** Pattern value at any angle theta (folded by symmetry), linear interpolation. */
    public static double axiAt(double[] f, double a) {
        a = Math.abs(a % (2 * Math.PI));
        if (a > Math.PI) a = 2 * Math.PI - a;
        double x = a / Math.PI * (f.length - 1);
        int i = (int) Math.floor(x);
        if (i >= f.length - 1) return f[f.length - 1];
        double t = x - i;
        return f[i] * (1 - t) + f[i + 1] * t;
    }

    /** D = 2 / integral(F^2 sin(theta) dtheta) for an axisymmetric, normalized pattern. */
    public static double axiDirectivity(double[] f) {
        int n = f.length - 1;
        double d = Math.PI / n, sum = 0;
        for (int i = 0; i < n; i++) {
            double a = f[i] * f[i] * Math.sin(i * d);
            double c = f[i + 1] * f[i + 1] * Math.sin((i + 1) * d);
            sum += 0.5 * (a + c) * d;
        }
        return sum > 0 ? 2.0 / sum : 1.0;
    }

    public static double axiHpbwDeg(double[] f) {
        int n = f.length - 1, m = 0;
        for (int i = 1; i <= n; i++) if (f[i] > f[m]) m = i;
        int lo = m, hi = m;
        while (lo > 0 && f[lo] * f[lo] >= 0.5) lo--;
        while (hi < n && f[hi] * f[hi] >= 0.5) hi++;
        return (hi - lo) * 180.0 / n;
    }

    public static double axiMainLobe(double[] f) {
        int m = 0;
        for (int i = 1; i < f.length; i++) if (f[i] > f[m]) m = i;
        return m * Math.PI / (f.length - 1);
    }

    // ------------------------------------------------------------------ drawing: waves, rays, polar plots

    public interface Field {
        /** Signed instantaneous field in [-1,1] at the screen point (x,y). */
        double at(double x, double y);
    }

    /** Colour map of a signed field (crest = pos colour, trough = neg colour), drawn as short thick strokes. */
    public static void fieldMap(NTxGraphics g, double cx, double cy, double rMax, double r0, double cell,
                         Field f, Paint pos, Paint neg) {
        strokeButt(g, (float) cell);
        GeneralPath dash = new GeneralPath();
        for (double py = cy - rMax; py <= cy + rMax; py += cell) {
            for (double px = cx - rMax; px <= cx + rMax; px += cell) {
                double r = Math.hypot(px - cx, py - cy);
                if (r < r0 || r > rMax) continue;
                double e = f.at(px, py);
                int a = (int) (240 * Math.min(1.0, Math.abs(e)));
                if (a < 6) continue;
                dash.reset();
                dash.moveTo(px - cell / 2, py);
                dash.lineTo(px + cell / 2, py);
                g.setPaint(alpha(e > 0 ? pos : neg, a));
                g.draw(dash);
            }
        }
    }

    /** Far field of an axisymmetric source: E ~ F(theta)/r * sin(wt - kr), theta from the vertical axis. */
    public static Field axisField(double cx, double cy, double r0, double lambdaPx, double wt, double[] pat) {
        return (px, py) -> {
            double dx = px - cx, dy = cy - py, r = Math.hypot(dx, dy);
            double theta = Math.atan2(dx, dy);
            return axiAt(pat, theta) * Math.min(1.0, r0 / r) * Math.sin(wt - 2 * Math.PI * r / lambdaPx);
        };
    }

    /** Wavefront circles (crests/troughs), amplitude given by ampOfPsi(psi). */
    public static void rings(NTxGraphics g, double cx, double cy, double rMax, double r0, double lambdaPx, double wt,
                      Paint pos, Paint neg, float lineW, int maxRings, DoubleUnaryOperator ampOfPsi) {
        double frac = wt / (2 * Math.PI);
        int j = (int) Math.ceil((r0 / lambdaPx - frac + 0.25) * 2);
        strokeButt(g, lineW);
        for (int drawn = 0; drawn < maxRings; j++, drawn++) {
            double r = lambdaPx * (frac - 0.25 + 0.5 * j);
            if (r > rMax) break;
            if (r < r0) continue;
            Paint base = (Math.floorMod(j, 2) == 0) ? pos : neg;
            double fade = Math.min(1.0, r0 / r);
            int seg = 90;
            for (int s = 0; s < seg; s++) {
                double a1 = 2 * Math.PI * s / seg, a2 = 2 * Math.PI * (s + 1) / seg;
                int alpha = (int) (235 * ampOfPsi.applyAsDouble((a1 + a2) / 2) * fade);
                if (alpha < 8) continue;
                GeneralPath piece = new GeneralPath();
                piece.moveTo(cx + r * Math.cos(a1), cy - r * Math.sin(a1));
                piece.lineTo(cx + r * Math.cos(a2), cy - r * Math.sin(a2));
                g.setPaint(alpha(base, alpha));
                g.draw(piece);
            }
        }
    }

    /** Outward propagation arrows, length proportional to ampOfPsi(psi). */
    public static void rays(NTxGraphics g, double cx, double cy, double rb, double maxLen, int n,
                     Paint p, float lineW, DoubleUnaryOperator ampOfPsi) {
        stroke(g, Math.max(1.5f, lineW * 0.8f));
        g.setPaint(p);
        GeneralPath path = new GeneralPath();
        for (int k = 0; k < n; k++) {
            double psi = 2 * Math.PI * k / n;
            double f = ampOfPsi.applyAsDouble(psi);
            if (f < 0.06) continue;
            double dx = Math.cos(psi), dy = -Math.sin(psi), len = maxLen * f;
            arrow(path, cx + dx * rb, cy + dy * rb, cx + dx * (rb + len), cy + dy * (rb + len), Math.min(6, len * 0.4));
        }
        g.draw(path);
    }

    /** Polar plot; f(psi) in [0,1]. scale: "field" | "power" | "db" (30 dB dynamic range). */
    public static void polar(NTxGraphics g, double pcx, double pcy, double R, DoubleUnaryOperator f, String scale,
                      Paint curve, Paint grid, float lineW) {
        boolean db = "db".equalsIgnoreCase(scale), power = "power".equalsIgnoreCase(scale);
        stroke(g, 1.0f);
        g.setPaint(alpha(grid, 110));
        double[] rings = db ? new double[]{1.0, 2.0 / 3, 1.0 / 3} : new double[]{0.5, 1.0};
        for (double rr : rings) {
            GeneralPath circle = new GeneralPath();
            for (int s = 0; s <= 72; s++) {
                double a = 2 * Math.PI * s / 72;
                double x = pcx + R * rr * Math.cos(a), y = pcy - R * rr * Math.sin(a);
                if (s == 0) circle.moveTo(x, y);
                else circle.lineTo(x, y);
            }
            g.draw(circle);
        }
        GeneralPath axes = new GeneralPath();
        axes.moveTo(pcx, pcy - R * 1.08);
        axes.lineTo(pcx, pcy + R * 1.08);
        axes.moveTo(pcx - R * 1.08, pcy);
        axes.lineTo(pcx + R * 1.08, pcy);
        g.draw(axes);

        GeneralPath lobe = new GeneralPath();
        int n = 240;
        for (int s = 0; s <= n; s++) {
            double a = 2 * Math.PI * s / n;
            double v = f.applyAsDouble(a);
            if (db) v = Math.max(0, (20 * Math.log10(Math.max(v, 1e-6)) + 30) / 30);
            else if (power) v = v * v;
            double x = pcx + R * v * Math.cos(a), y = pcy - R * v * Math.sin(a);
            if (s == 0) lobe.moveTo(x, y);
            else lobe.lineTo(x, y);
        }
        stroke(g, lineW);
        g.setPaint(curve);
        g.draw(lobe);
    }

    // ------------------------------------------------------------------ Bessel J1 (loop antennas)

    public static double j1(double x) {
        int n = 64; // Simpson on (1/pi) * integral_0^pi cos(t - x sin t) dt
        double h = Math.PI / n, s = 0;
        for (int i = 0; i <= n; i++) {
            double t = i * h;
            double v = Math.cos(t - x * Math.sin(t));
            s += (i == 0 || i == n) ? v : (i % 2 == 1 ? 4 * v : 2 * v);
        }
        return s * h / 3 / Math.PI;
    }
}