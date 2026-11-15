package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.eval.NTxResolutionContext;
import net.thevpc.ntexup.lib.geometry3d.NTxNumberElement3;
import net.thevpc.ntexup.lib.geometry3d.impl.NTx3DUtils;
import net.thevpc.nuts.elem.NElement;

import java.util.*;

/**
 * Faithful physical translation of a 3D scene microwave structure into a
 * microstrip circuit graph and analytical/circuit network model.
 *
 * Automatically detects:
 * - Substrate parameters (height h, permittivity er, loss tangent)
 * - Excitation port (source box)
 * - Resonant patch elements (dimensions W, L, inset notches, RLC equivalent)
 * - Microstrip transmission line segments (widths, lengths, endpoints)
 * - Electrical connectivity and junctions
 *
 * Produces:
 * - Direct Qucs-Core netlist (.net)
 * - Analytical / FEM cascaded input impedance Zin(f) and S11(f)
 *
 * NO hardcoded dimensions, frequencies, or topologies.
 */
public class MicrostripCircuitModel {

    public static final double C_LIGHT = 2.99792458e8;
    /** Part of every cache hash built on this model: change it when the model changes. */
    public static final String VERSION = "5-no-notch-length";

    public static class ComplexNum {
        public final double re;
        public final double im;

        public ComplexNum(double re, double im) {
            this.re = re;
            this.im = im;
        }

        public static ComplexNum of(double re, double im) {
            return new ComplexNum(re, im);
        }

        public static ComplexNum of(double re) {
            return new ComplexNum(re, 0.0);
        }

        public ComplexNum plus(ComplexNum o) {
            return new ComplexNum(this.re + o.re, this.im + o.im);
        }

        public ComplexNum minus(ComplexNum o) {
            return new ComplexNum(this.re - o.re, this.im - o.im);
        }

        public ComplexNum mul(ComplexNum o) {
            return new ComplexNum(this.re * o.re - this.im * o.im, this.re * o.im + this.im * o.re);
        }

        public ComplexNum mul(double d) {
            return new ComplexNum(this.re * d, this.im * d);
        }

        public ComplexNum div(ComplexNum o) {
            double den = o.re * o.re + o.im * o.im;
            if (den == 0.0) {
                return new ComplexNum(1e12, 1e12);
            }
            return new ComplexNum((this.re * o.re + this.im * o.im) / den, (this.im * o.re - this.re * o.im) / den);
        }

        public ComplexNum div(double d) {
            return new ComplexNum(this.re / d, this.im / d);
        }

        public double abs() {
            return Math.hypot(re, im);
        }

        public double db() {
            double a = abs();
            return a > 1e-12 ? 20.0 * Math.log10(a) : -120.0;
        }
    }

    public static class Box3D {
        public double x1, y1, z1;
        public double x2, y2, z2;
        public String name;
        public boolean isGround;
        public boolean isSubstrate;
        public boolean isSource;
        public boolean isPatch;
        public boolean isAntenna;

        public Box3D(double x1, double y1, double z1, double x2, double y2, double z2, String name) {
            this.x1 = Math.min(x1, x2);
            this.y1 = Math.min(y1, y2);
            this.z1 = Math.min(z1, z2);
            this.x2 = Math.max(x1, x2);
            this.y2 = Math.max(y1, y2);
            this.z2 = Math.max(z1, z2);
            this.name = name != null ? name : "";
        }

        public double width() { return x2 - x1; }
        public double length() { return y2 - y1; }
        public double height() { return z2 - z1; }
        public double xMin() { return x1; }
        public double xMax() { return x2; }
        public double yMin() { return y1; }
        public double yMax() { return y2; }
    }

    public static class PatchElement {
        public int id;
        public double xMin, xMax, yMin, yMax;
        public double width;
        public double length;
        public double insetDepth;
        public double fr;
        public double rin;
        public double q = 35.0;
        public double cEq;
        public double lEq;
        public double feedX;
        public double feedY;
        public String nodeName;

        public ComplexNum computeZ(double freq) {
            double deltaF = (freq - fr) / fr;
            ComplexNum den = new ComplexNum(1.0, 2.0 * q * deltaF);
            return ComplexNum.of(rin).div(den);
        }
    }

    /**
     * Quasi-TEM parameters of a microstrip line.
     */
    public static class LineParams {
        public final double z0;
        public final double epsEff;

        public LineParams(double z0, double epsEff) {
            this.z0 = z0;
            this.epsEff = epsEff;
        }
    }

    public static class Point2D {
        public final double x;
        public final double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceTo(Point2D o) {
            return Math.hypot(this.x - o.x, this.y - o.y);
        }
    }

    public static class LineSegment {
        public int id;
        public double width;
        public double length;
        public boolean isHorizontal;
        public Point2D pA;
        public Point2D pB;
        public String nodeA;
        public String nodeB;

        public LineSegment(int id, double width, double length, boolean isHorizontal, Point2D pA, Point2D pB) {
            this.id = id;
            this.width = width;
            this.length = length;
            this.isHorizontal = isHorizontal;
            this.pA = pA;
            this.pB = pB;
        }
    }

    public double substrateH = 1.6e-3;
    public double copperT = 0.035e-3;
    public double epsilonR = 4.4;
    public double lossTangent = 0.02;
    public double z0Ref = 50.0;
    public boolean dispersion = true;
    public Point2D portLocation = new Point2D(0, 0);
    public double portWidth = 3.1e-3;

    public List<PatchElement> patches = new ArrayList<>();
    public List<LineSegment> lines = new ArrayList<>();
    public Map<String, List<LineSegment>> nodeToLines = new HashMap<>();
    public Map<String, PatchElement> nodeToPatch = new HashMap<>();
    private Map<Double, LineParams> solvedLineParams;

    public static MicrostripCircuitModel parse(NTxNode scene3D, NTxResolutionContext context, double defaultEr, double defaultTanD, double defaultZ0) {
        MicrostripCircuitModel model = new MicrostripCircuitModel();
        model.epsilonR = defaultEr > 0 ? defaultEr : 4.4;
        model.lossTangent = defaultTanD > 0 ? defaultTanD : 0.02;
        model.z0Ref = defaultZ0 > 0 ? defaultZ0 : 50.0;

        if (scene3D == null) {
            return model;
        }

        List<Box3D> rawAntennaBoxes = new ArrayList<>();
        Box3D srcBox = null;

        for (NTxNode child : scene3D.children()) {
            String nodeType = child.type();
            String name = child.getName() == null ? "" : child.getName().trim();

            boolean isGround = NTxMwSimulationUtils.isSimulationNode(child, "ground");
            boolean isSubstrate = NTxMwSimulationUtils.isSimulationNode(child, "substrate");
            // The flanks either side of an inset feed are patch metal: read as lines they left the
            // patch with the length above the inset only, and its resonance far above the band.
            boolean isPatch = NTxMwSimulationUtils.isSimulationNode(child, "patch")
                    || NTxMwSimulationUtils.isSimulationNode(child, "left-flank")
                    || NTxMwSimulationUtils.isSimulationNode(child, "right-flank");
            boolean isSource = NTxMwSimulationUtils.isSimulationNode(child, "source");
            boolean isAntenna = NTxMwSimulationUtils.isSimulationNode(child, "antenna")
                    || isPatch
                    || NTxMwSimulationUtils.isSimulationNode(child, "feed")
                    || NTxMwSimulationUtils.isSimulationNode(child, "feedline")
                    || NTxMwSimulationUtils.isSimulationNode(child, "left-flank")
                    || NTxMwSimulationUtils.isSimulationNode(child, "right-flank");

            if ("box".equalsIgnoreCase(nodeType)) {
                NElement s = child.getPropertyValue("size").orNull();
                NElement p = child.getPropertyValue("position").orNull();
                if (s != null && p != null) {
                    NTxNumberElement3 ss = NTx3DUtils.resolveSize3DSI(context.evalExpression(s).orNull(), context);
                    NTxNumberElement3 pp = NTx3DUtils.resolveSize3DSI(context.evalExpression(p).orNull(), context);
                    if (ss != null && pp != null) {
                        double x1 = pp.x.asDoubleValue().orElse(0.0);
                        double y1 = pp.y.asDoubleValue().orElse(0.0);
                        double z1 = pp.z.asDoubleValue().orElse(0.0);
                        double xw = ss.x.asDoubleValue().orElse(0.0);
                        double yw = ss.y.asDoubleValue().orElse(0.0);
                        double zw = ss.z.asDoubleValue().orElse(0.0);

                        Box3D box = new Box3D(x1, y1, z1, x1 + xw, y1 + yw, z1 + zw, name);
                        box.isGround = isGround;
                        box.isSubstrate = isSubstrate;
                        box.isSource = isSource;
                        box.isPatch = isPatch;
                        box.isAntenna = isAntenna;

                        if (isSubstrate) {
                            if (box.height() > 0.0001) {
                                model.substrateH = box.height();
                            }
                        } else if (isSource) {
                            srcBox = box;
                        } else if (isAntenna && !isGround && !isSubstrate) {
                            rawAntennaBoxes.add(box);
                        }
                    }
                }
            }
        }

        if (srcBox != null) {
            model.portLocation = new Point2D((srcBox.x1 + srcBox.x2) / 2.0, srcBox.y1);
            model.portWidth = srcBox.width();
        }

        // 1. Separate patch candidate boxes vs line boxes
        List<Box3D> patchBoxes = new ArrayList<>();
        List<Box3D> lineBoxes = new ArrayList<>();

        for (Box3D b : rawAntennaBoxes) {
            if (b.isPatch) {
                patchBoxes.add(b);
            } else if (b.width() > 2.5 * model.portWidth && b.length() > 2.5 * model.portWidth) {
                patchBoxes.add(b);
            } else {
                lineBoxes.add(b);
            }
        }

        // 2. Cluster touching patch boxes into unified PatchElement objects
        if (!patchBoxes.isEmpty()) {
            patchBoxes.sort(Comparator.comparingDouble(Box3D::xMin));
            List<List<Box3D>> clusters = new ArrayList<>();
            for (Box3D b : patchBoxes) {
                boolean added = false;
                for (List<Box3D> c : clusters) {
                    for (Box3D member : c) {
                        if (Math.abs(b.xMin() - member.xMax()) < 4e-3 || Math.abs(b.xMax() - member.xMin()) < 4e-3
                                || (b.xMin() <= member.xMax() && b.xMax() >= member.xMin())) {
                            c.add(b);
                            added = true;
                            break;
                        }
                    }
                    if (added) break;
                }
                if (!added) {
                    List<Box3D> c = new ArrayList<>();
                    c.add(b);
                    clusters.add(c);
                }
            }

            int pIdx = 1;
            for (List<Box3D> c : clusters) {
                double xmin = Double.MAX_VALUE, xmax = -Double.MAX_VALUE;
                double ymin = Double.MAX_VALUE, ymax = -Double.MAX_VALUE;
                double notchY = Double.MAX_VALUE;

                for (Box3D b : c) {
                    xmin = Math.min(xmin, b.xMin());
                    xmax = Math.max(xmax, b.xMax());
                    ymin = Math.min(ymin, b.yMin());
                    ymax = Math.max(ymax, b.yMax());
                    if (b.yMin() > ymin) {
                        notchY = Math.min(notchY, b.yMin());
                    }
                }

                PatchElement p = new PatchElement();
                p.id = pIdx++;
                p.xMin = xmin;
                p.xMax = xmax;
                p.yMin = ymin;
                p.yMax = ymax;
                p.width = xmax - xmin;
                p.length = ymax - ymin;
                p.insetDepth = notchY < Double.MAX_VALUE ? (notchY - ymin) : 0.0;
                p.feedX = (xmin + xmax) / 2.0;
                p.feedY = ymin;
                p.nodeName = "_p" + p.id;

                model.computePatchParameters(p);

                model.patches.add(p);
                model.nodeToPatch.put(p.nodeName, p);
            }
        }

        // 3. Create LineSegments from lineBoxes
        int lineId = 1;
        for (Box3D b : lineBoxes) {
            double bw = b.width();
            double bl = b.length();
            if (bw <= 0.0001 || bl <= 0.0001) continue;

            boolean isHoriz = isHorizontalLine(b, rawAntennaBoxes, srcBox);
            double w = isHoriz ? bl : bw;
            double len = isHoriz ? bw : bl;

            Point2D pA, pB;
            if (isHoriz) {
                double ym = (b.y1 + b.y2) / 2.0;
                pA = new Point2D(b.x1, ym);
                pB = new Point2D(b.x2, ym);
            } else {
                double xm = (b.x1 + b.x2) / 2.0;
                pA = new Point2D(xm, b.y1);
                pB = new Point2D(xm, b.y2);
            }

            model.lines.add(new LineSegment(lineId++, w, len, isHoriz, pA, pB));
        }

        // Automatically split lines that have T-junctions on their interior
        boolean splitOccurred = true;
        while (splitOccurred) {
            splitOccurred = false;
            for (int i = 0; i < model.lines.size(); i++) {
                LineSegment l = model.lines.get(i);
                Point2D splitPt = null;
                for (LineSegment other : model.lines) {
                    if (other == l) continue;
                    for (Point2D ep : new Point2D[]{other.pA, other.pB}) {
                        if (l.isHorizontal) {
                            double xmin = Math.min(l.pA.x, l.pB.x);
                            double xmax = Math.max(l.pA.x, l.pB.x);
                            if (ep.x > xmin + 1e-3 && ep.x < xmax - 1e-3 && Math.abs(ep.y - l.pA.y) <= Math.max(l.width, 2.5e-3)) {
                                splitPt = new Point2D(ep.x, l.pA.y);
                                break;
                            }
                        } else {
                            double ymin = Math.min(l.pA.y, l.pB.y);
                            double ymax = Math.max(l.pA.y, l.pB.y);
                            if (ep.y > ymin + 1e-3 && ep.y < ymax - 1e-3 && Math.abs(ep.x - l.pA.x) <= Math.max(l.width, 2.5e-3)) {
                                splitPt = new Point2D(l.pA.x, ep.y);
                                break;
                            }
                        }
                    }
                    if (splitPt != null) break;
                }
                if (splitPt != null) {
                    model.lines.remove(i);
                    LineSegment l1 = new LineSegment(lineId++, l.width, l.isHorizontal ? Math.abs(splitPt.x - l.pA.x) : Math.abs(splitPt.y - l.pA.y), l.isHorizontal, l.pA, splitPt);
                    LineSegment l2 = new LineSegment(lineId++, l.width, l.isHorizontal ? Math.abs(l.pB.x - splitPt.x) : Math.abs(l.pB.y - splitPt.y), l.isHorizontal, splitPt, l.pB);
                    model.lines.add(l1);
                    model.lines.add(l2);
                    splitOccurred = true;
                    break;
                }
            }
        }

        // 4. Assign electrical nodes by proximity clustering
        List<Point2D> nodePoints = new ArrayList<>();
        List<String> nodeNames = new ArrayList<>();

        // Node 0: portLocation
        nodePoints.add(model.portLocation);
        nodeNames.add("_net0");

        // Patch nodes:
        for (PatchElement p : model.patches) {
            nodePoints.add(new Point2D(p.feedX, p.feedY));
            nodeNames.add(p.nodeName);
        }

        // Where two lines end on the same point, that point is a junction: register it before
        // any tolerance matching. Otherwise an 18.9 mm feed, whose tolerance is its 6.1 mm
        // width, ends 5.1 mm from the patch node and snaps to it, dropping the transformer.
        for (LineSegment a : model.lines) {
            for (Point2D ep : new Point2D[]{a.pA, a.pB}) {
                for (LineSegment b : model.lines) {
                    if (b != a && (ep.distanceTo(b.pA) < 1e-9 || ep.distanceTo(b.pB) < 1e-9)) {
                        resolveOrCreateNode(ep, nodePoints, nodeNames, 1e-9);
                        break;
                    }
                }
            }
        }

        // Helper to find or create node for a point
        for (LineSegment line : model.lines) {
            // Capped by the length: the two ends of a short wide line must stay two nodes
            double tol = Math.min(Math.max(line.width, 2e-3), 0.45 * line.length);
            line.nodeA = insetFeedNode(model, line.pA, tol);
            if (line.nodeA == null) {
                line.nodeA = resolveOrCreateNode(line.pA, nodePoints, nodeNames, tol);
            }
            line.nodeB = insetFeedNode(model, line.pB, tol);
            if (line.nodeB == null) {
                line.nodeB = resolveOrCreateNode(line.pB, nodePoints, nodeNames, tol);
            }
            model.nodeToLines.computeIfAbsent(line.nodeA, k -> new ArrayList<>()).add(line);
            model.nodeToLines.computeIfAbsent(line.nodeB, k -> new ArrayList<>()).add(line);
        }

        return model;
    }

    /**
     * @return the distinct conductor widths of the circuit: lines and patches
     */
    public Set<Double> conductorWidths() {
        Set<Double> widths = new TreeSet<>();
        for (LineSegment l : lines) {
            widths.add(l.width);
        }
        for (PatchElement p : patches) {
            widths.add(p.width);
        }
        return widths;
    }

    /**
     * Replaces the closed-form line formulas with externally solved parameters,
     * and derives the patches from them again.
     *
     * @param params parameters for every width of {@link #conductorWidths()}
     */
    public void useLineParams(Map<Double, LineParams> params) {
        for (double w : conductorWidths()) {
            if (!params.containsKey(w)) {
                throw new IllegalArgumentException("no line parameters for width " + w);
            }
        }
        solvedLineParams = new HashMap<>(params);
        for (PatchElement p : patches) {
            computePatchParameters(p);
        }
    }

    private LineParams lineParams(double width) {
        if (solvedLineParams != null) {
            return solvedLineParams.get(width);
        }
        return new LineParams(calcZ0(width, substrateH, epsilonR), calcEpsEff(width, substrateH, epsilonR));
    }

    /**
     * The line's permittivity at freq. The quasi-static value (closed form or FEM) is low on a
     * thick substrate: at f h = 25 GHz mm it is 3.59 against 4.07 for a 14 mm strip on 3.2 mm FR4,
     * which puts the cavity resonance 5.6% above the full-wave solvers.
     */
    private LineParams lineParams(double width, double freq) {
        LineParams p = lineParams(width);
        if (!dispersion) {
            return p;
        }
        return new LineParams(p.z0, epsEffDispersion(freq, substrateH, width / substrateH, epsilonR, p.epsEff));
    }

    /**
     * Kirschning-Jansen (1982), in its GHz mm form; valid up to f h = 25 GHz mm.
     */
    public static double epsEffDispersion(double f, double h, double u, double er, double epsEff0) {
        double fn = (f * 1e-9) * (h * 1e3);
        double p1 = 0.27488 + (0.6315 + 0.525 / Math.pow(1.0 + 0.0157 * fn, 20)) * u
                - 0.065683 * Math.exp(-8.7513 * u);
        double p2 = 0.33622 * (1.0 - Math.exp(-0.03442 * er));
        double p3 = 0.0363 * Math.exp(-4.6 * u) * (1.0 - Math.exp(-Math.pow(fn / 38.7, 4.97)));
        double p4 = 1.0 + 2.751 * (1.0 - Math.exp(-Math.pow(er / 15.916, 8)));
        double p = p1 * p2 * Math.pow((0.1844 + p3 * p4) * fn, 1.5763);
        return er - (er - epsEff0) / (1.0 + p);
    }

    private void computePatchParameters(PatchElement p) {
        // Analytical cavity resonance, at the permittivity of its own frequency
        double u = p.width / substrateH;
        // The inset notches add no resonant length: OpenEMS and Palace put the inset patch of
        // compare-patch at 7.045 GHz, this model at 6.99 GHz without a notch term (6.41 with one).
        double epsEff = lineParams(p.width).epsEff;
        p.fr = 0;
        for (int i = 0; i < 30; i++) {
            double dL = 0.412 * substrateH * ((epsEff + 0.3) / (epsEff - 0.258)) * ((u + 0.264) / (u + 0.8));
            double leff = p.length + 2.0 * dL;
            double fr = C_LIGHT / (2.0 * leff * Math.sqrt(epsEff));
            boolean converged = Math.abs(fr - p.fr) < 1e3;
            p.fr = fr;
            if (!dispersion || converged) {
                break;
            }
            epsEff = lineParams(p.width, fr).epsEff;
        }

        double k0 = 2.0 * Math.PI * p.fr / C_LIGHT;
        double lam0 = C_LIGHT / p.fr;
        double grad = (p.width / (120.0 * lam0)) * (1.0 - Math.pow(k0 * substrateH, 2.0) / 24.0);
        double redge = 1.0 / (2.0 * Math.max(1e-6, grad));
        double cosVal = Math.cos(Math.PI * p.insetDepth / p.length);
        p.rin = redge * Math.pow(cosVal, 2.0);

        // Physical Q factor derived from dielectric, conductor, and radiation losses
        double qDiel = 1.0 / Math.max(1e-6, lossTangent);
        double qCond = substrateH * Math.sqrt(Math.PI * p.fr * 4.0 * Math.PI * 1e-7 * 5.8e7);
        double cPatch = 8.854187817e-12 * epsilonR * p.width * p.length / (2.0 * substrateH);
        double qRad = 2.0 * Math.PI * p.fr * cPatch * redge;
        p.q = 1.0 / ((1.0 / qRad) + (1.0 / qDiel) + (1.0 / qCond));

        double w0 = 2.0 * Math.PI * p.fr;
        p.cEq = p.q / (w0 * p.rin);
        p.lEq = 1.0 / (w0 * w0 * p.cEq);
    }

    /**
     * A line runs towards what it connects to, which a short wide line's aspect ratio
     * gets wrong. The aspect ratio only decides when the contacts do not.
     */
    private static boolean isHorizontalLine(Box3D b, List<Box3D> conductors, Box3D source) {
        boolean contactX = false;
        boolean contactY = false;
        List<Box3D> others = new ArrayList<>(conductors);
        if (source != null) {
            others.add(source);
        }
        for (Box3D o : others) {
            if (o == b) continue;
            boolean overlapX = o.xMin() < b.xMax() - 1e-9 && o.xMax() > b.xMin() + 1e-9;
            boolean overlapY = o.yMin() < b.yMax() - 1e-9 && o.yMax() > b.yMin() + 1e-9;
            if (overlapY && (Math.abs(o.xMin() - b.xMax()) < 1e-6 || Math.abs(o.xMax() - b.xMin()) < 1e-6)) {
                contactX = true;
            }
            if (overlapX && (Math.abs(o.yMin() - b.yMax()) < 1e-6 || Math.abs(o.yMax() - b.yMin()) < 1e-6
                    || Math.abs(o.yMin() - b.yMin()) < 1e-6 || Math.abs(o.yMax() - b.yMax()) < 1e-6)) {
                contactY = true;
            }
        }
        if (contactX != contactY) {
            return contactX;
        }
        return b.width() > b.length();
    }

    /**
     * A feed drawn as one box up to the end of an inset notch ends inside the patch, up to the
     * inset depth away from the patch edge where the patch node sits: it still feeds that patch.
     */
    private static String insetFeedNode(MicrostripCircuitModel model, Point2D pt, double tol) {
        for (PatchElement p : model.patches) {
            if (p.insetDepth > 0 && Math.abs(pt.x - p.feedX) <= tol
                    && pt.y >= p.feedY - 1e-9 && pt.y <= p.feedY + p.insetDepth + 1e-9) {
                return p.nodeName;
            }
        }
        return null;
    }

    private static String resolveOrCreateNode(Point2D pt, List<Point2D> nodePoints, List<String> nodeNames, double tol) {
        // nearest, not first: a wide line's tolerance can reach past a short neighbour
        int best = -1;
        for (int i = 0; i < nodePoints.size(); i++) {
            double d = pt.distanceTo(nodePoints.get(i));
            if (d <= tol && (best < 0 || d < pt.distanceTo(nodePoints.get(best)))) {
                best = i;
            }
        }
        if (best >= 0) {
            return nodeNames.get(best);
        }
        String newName = "_net" + nodeNames.size();
        nodePoints.add(pt);
        nodeNames.add(newName);
        return newName;
    }

    public static double calcEpsEff(double w, double h, double er) {
        double u = w / h;
        return (er + 1.0) / 2.0 + ((er - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / u);
    }

    public static double calcZ0(double w, double h, double er) {
        double u = w / h;
        double epsEff = calcEpsEff(w, h, er);
        double eta0 = 376.730313668;
        if (u <= 1.0) {
            return (eta0 / (2.0 * Math.PI * Math.sqrt(epsEff))) * Math.log(8.0 / u + 0.25 * u);
        } else {
            return (eta0 / Math.sqrt(epsEff)) / (u + 1.393 + 0.667 * Math.log(u + 1.444));
        }
    }

    public static ComplexNum tline(double w, double length, double f, double h, double er, ComplexNum zLoad) {
        if (length <= 0) return zLoad;
        return tline(new LineParams(calcZ0(w, h, er), calcEpsEff(w, h, er)), length, f, zLoad);
    }

    private static ComplexNum tline(LineParams line, double length, double f, ComplexNum zLoad) {
        if (length <= 0) return zLoad;
        double z0 = line.z0;
        double vp = C_LIGHT / Math.sqrt(line.epsEff);
        double beta = 2.0 * Math.PI * f / vp;
        double bl = beta * length;
        double tanBl = Math.tan(bl);
        ComplexNum num = zLoad.plus(ComplexNum.of(0, z0 * tanBl));
        ComplexNum den = ComplexNum.of(1.0).plus(ComplexNum.of(0, tanBl / z0).mul(zLoad));
        return num.div(den);
    }

    /** Copper resistivity, the value the Qucs netlist carries (rho). */
    private static final double RHO_CONDUCTOR = 2.44e-8;

    /**
     * Attenuation of a line in Np/m: the dielectric loss of the quasi-TEM mode (filling factor
     * eps_r (eps_eff - 1) / (eps_eff (eps_r - 1))) plus the conductor loss Rs / (Z0 w).
     */
    private double lineAttenuation(LineParams line, double width, double f) {
        double k0 = 2.0 * Math.PI * f / C_LIGHT;
        double alphaD = epsilonR > 1.0
                ? k0 * epsilonR * (line.epsEff - 1.0) * lossTangent / (2.0 * Math.sqrt(line.epsEff) * (epsilonR - 1.0))
                : 0.0;
        double rs = Math.sqrt(Math.PI * f * 4.0e-7 * Math.PI * RHO_CONDUCTOR);
        double alphaC = rs / (line.z0 * width);
        return alphaD + alphaC;
    }

    /**
     * A lossy line: Zin = Z0 (ZL + Z0 tanh(g l)) / (Z0 + ZL tanh(g l)), g = alpha + j beta.
     * Lossless, an open stub reflects everything, where the full-wave and circuit solvers
     * read -0.6 to -1 dB on 30 mm of FR4 at 3.4 GHz.
     */
    private ComplexNum lossyTline(LineParams line, double width, double length, double f, ComplexNum zLoad) {
        if (length <= 0) return zLoad;
        double a = lineAttenuation(line, width, f) * length;
        double b = 2.0 * Math.PI * f * Math.sqrt(line.epsEff) / C_LIGHT * length;
        // tanh(a + jb) = (sinh 2a + j sin 2b) / (cosh 2a + cos 2b)
        double den = Math.cosh(2.0 * a) + Math.cos(2.0 * b);
        if (Math.abs(den) < 1e-300) {
            den = 1e-300;
        }
        ComplexNum t = ComplexNum.of(Math.sinh(2.0 * a) / den, Math.sin(2.0 * b) / den);
        ComplexNum z0 = ComplexNum.of(line.z0);
        ComplexNum num = zLoad.plus(t.mul(line.z0));
        ComplexNum d = z0.plus(zLoad.mul(t));
        return z0.mul(num.div(d));
    }

    public ComplexNum computeZin(double freq) {
        Set<LineSegment> visited = new HashSet<>();
        return computeNodeZin("_net0", null, freq, visited);
    }

    private ComplexNum computeNodeZin(String node, String fromNode, double freq, Set<LineSegment> visited) {
        List<ComplexNum> branchLoads = new ArrayList<>();

        PatchElement patch = nodeToPatch.get(node);
        if (patch != null) {
            branchLoads.add(patch.computeZ(freq));
        }

        List<LineSegment> connected = nodeToLines.getOrDefault(node, Collections.emptyList());
        for (LineSegment line : connected) {
            if (visited.contains(line)) continue;
            String nextNode = line.nodeA.equals(node) ? line.nodeB : line.nodeA;
            if (nextNode.equals(fromNode)) continue;

            visited.add(line);
            ComplexNum zNext = computeNodeZin(nextNode, node, freq, visited);
            ComplexNum zTrans = lossyTline(lineParams(line.width, freq), line.width, line.length, freq, zNext);
            branchLoads.add(zTrans);
        }

        if (branchLoads.isEmpty()) {
            // Open end
            double z0 = calcZ0(portWidth, substrateH, epsilonR);
            return ComplexNum.of(0, -1e6);
        }

        if (branchLoads.size() == 1) {
            return branchLoads.get(0);
        }

        // Parallel combination of branches
        ComplexNum yTot = ComplexNum.of(0, 0);
        for (ComplexNum z : branchLoads) {
            if (z.abs() > 1e-12) {
                yTot = yTot.plus(ComplexNum.of(1.0, 0.0).div(z));
            }
        }
        return ComplexNum.of(1.0, 0.0).div(yTot);
    }

    public ComplexNum computeS11(double freq) {
        ComplexNum zin = computeZin(freq);
        ComplexNum z0 = ComplexNum.of(z0Ref);
        return zin.minus(z0).div(zin.plus(z0));
    }

    public String generateQucsNetlist(double fmin, double fmax, int count) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US,
                "# Qucs Netlist translated from 3D Scene Geometry\n" +
                ".SP:SP1 Type=\"lin\" Start=\"%.6eHz\" Stop=\"%.6eHz\" Points=\"%d\"\n" +
                "Pac:P1 _net0 gnd Num=\"1\" Z=\"%.2fOhm\" P=\"0.001\" f=\"1e+09Hz\" Temp=\"26.85\"\n",
                fmin, fmax, count, z0Ref));

        String mlinParams = "Subst=\"Sub1\" DispModel=\"Kirschning\" Model=\"Hammerstad\"\n";

        for (LineSegment l : lines) {
            sb.append(String.format(Locale.US,
                    "MLIN:Line%d %s %s W=\"%.4f mm\" L=\"%.4f mm\" ",
                    l.id, l.nodeA, l.nodeB, l.width * 1e3, l.length * 1e3))
                    .append(mlinParams);
        }

        for (PatchElement p : patches) {
            sb.append(String.format(Locale.US,
                    "R:R%d %s gnd R=\"%.2fOhm\" Temp=\"26.85\"\n" +
                    "L:L%d %s gnd L=\"%.6eH\"\n" +
                    "C:C%d %s gnd C=\"%.6eF\"\n",
                    p.id, p.nodeName, p.rin,
                    p.id, p.nodeName, p.lEq,
                    p.id, p.nodeName, p.cEq
            ));
        }

        sb.append(String.format(Locale.US,
                "SUBST:Sub1 er=\"%.4f\" h=\"%.4f mm\" t=\"%.4f mm\" tand=\"%.4f\" rho=\"2.44e-08\" D=\"1e-06\"\n",
                epsilonR, substrateH * 1e3, copperT * 1e3, lossTangent));

        return sb.toString();
    }
}
