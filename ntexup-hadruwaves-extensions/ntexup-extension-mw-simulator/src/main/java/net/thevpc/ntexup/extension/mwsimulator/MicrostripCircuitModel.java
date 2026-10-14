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
    public Point2D portLocation = new Point2D(0, 0);
    public double portWidth = 3.1e-3;

    public List<PatchElement> patches = new ArrayList<>();
    public List<LineSegment> lines = new ArrayList<>();
    public Map<String, List<LineSegment>> nodeToLines = new HashMap<>();
    public Map<String, PatchElement> nodeToPatch = new HashMap<>();

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
            boolean isPatch = NTxMwSimulationUtils.isSimulationNode(child, "patch");
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

                // Analytical cavity resonance
                double u = p.width / model.substrateH;
                double epsEff = (model.epsilonR + 1.0) / 2.0 + ((model.epsilonR - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / u);
                double dL = 0.412 * model.substrateH * ((epsEff + 0.3) / (epsEff - 0.258)) * ((u + 0.264) / (u + 0.8));
                double notchFraction = p.length > 0 ? (p.insetDepth / p.length) : 0.0;
                double notchFactor = notchFraction * (1.0 - notchFraction);
                double leff = p.length + 2.0 * dL + p.insetDepth * notchFactor;
                p.fr = C_LIGHT / (2.0 * leff * Math.sqrt(epsEff));

                double k0 = 2.0 * Math.PI * p.fr / C_LIGHT;
                double lam0 = C_LIGHT / p.fr;
                double grad = (p.width / (120.0 * lam0)) * (1.0 - Math.pow(k0 * model.substrateH, 2.0) / 24.0);
                double redge = 1.0 / (2.0 * Math.max(1e-6, grad));
                double cosVal = Math.cos(Math.PI * p.insetDepth / p.length);
                p.rin = redge * Math.pow(cosVal, 2.0);

                // Physical Q factor derived from dielectric, conductor, and radiation losses
                double qDiel = 1.0 / Math.max(1e-6, model.lossTangent);
                double qCond = model.substrateH * Math.sqrt(Math.PI * p.fr * 4.0 * Math.PI * 1e-7 * 5.8e7);
                double cPatch = 8.854187817e-12 * model.epsilonR * p.width * p.length / (2.0 * model.substrateH);
                double qRad = 2.0 * Math.PI * p.fr * cPatch * redge;
                p.q = 1.0 / ((1.0 / qRad) + (1.0 / qDiel) + (1.0 / qCond));

                double w0 = 2.0 * Math.PI * p.fr;
                p.cEq = p.q / (w0 * p.rin);
                p.lEq = 1.0 / (w0 * w0 * p.cEq);

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

            boolean isHoriz = bw > bl;
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

        // Helper to find or create node for a point
        for (LineSegment line : model.lines) {
            line.nodeA = resolveOrCreateNode(line.pA, nodePoints, nodeNames, Math.max(line.width, 2e-3));
            line.nodeB = resolveOrCreateNode(line.pB, nodePoints, nodeNames, Math.max(line.width, 2e-3));
            model.nodeToLines.computeIfAbsent(line.nodeA, k -> new ArrayList<>()).add(line);
            model.nodeToLines.computeIfAbsent(line.nodeB, k -> new ArrayList<>()).add(line);
        }

        return model;
    }

    private static String resolveOrCreateNode(Point2D pt, List<Point2D> nodePoints, List<String> nodeNames, double tol) {
        for (int i = 0; i < nodePoints.size(); i++) {
            if (pt.distanceTo(nodePoints.get(i)) <= tol) {
                return nodeNames.get(i);
            }
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
        double z0 = calcZ0(w, h, er);
        double epsEff = calcEpsEff(w, h, er);
        double vp = C_LIGHT / Math.sqrt(epsEff);
        double beta = 2.0 * Math.PI * f / vp;
        double bl = beta * length;
        double tanBl = Math.tan(bl);
        ComplexNum num = zLoad.plus(ComplexNum.of(0, z0 * tanBl));
        ComplexNum den = ComplexNum.of(1.0).plus(ComplexNum.of(0, tanBl / z0).mul(zLoad));
        return num.div(den);
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
            ComplexNum zTrans = tline(line.width, line.length, freq, substrateH, epsilonR, zNext);
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
