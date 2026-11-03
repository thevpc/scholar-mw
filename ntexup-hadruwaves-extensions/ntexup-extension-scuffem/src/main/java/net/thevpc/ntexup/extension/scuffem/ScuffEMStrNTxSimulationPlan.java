package net.thevpc.ntexup.extension.scuffem;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlanImpl;
import net.thevpc.ntexup.extension.mwsimulator.NTxSolverRun;
import net.thevpc.ntexup.extension.scuffem.solvers.NTxScuffEMS11Solver;
import net.thevpc.ntexup.extension.scuffem.solvers.NTxScuffEMZinSolver;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Maths;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ScuffEMStrNTxSimulationPlan extends NTxSimulationPlanImpl {

    public ScuffEMModelInfo modelInfo;
    private boolean solved = false;

    private final TreeSet<Double> requestedFrequencies = new TreeSet<>();
    private final TreeMap<Double, Complex> s11Results = new TreeMap<>();
    private final TreeMap<Double, Complex> zinResults = new TreeMap<>();

    /** The trace outline in metres, plus the feed edge the port must be taken from. */
    private static final class Outline {
        final List<double[]> points = new ArrayList<>();
        double portY;
        double portX1;
        double portX2;
        /** Where S11 is reported: the port edge, or the original one when the feed is extended. */
        double refY;
        /** With a de-embedded port, the y of the stub's top (gap) and bottom edges; NaN otherwise. */
        double stubTop = Double.NaN;
        double stubBottom = Double.NaN;
    }

    public static class RebuiltGeometry {
        public double feedW = 0.0;
        public double feedCenter = 0.0;
        public double lineLength = 0.0;
        public double portY = 0.0;
        public double portX1 = 0.0;
        public double portX2 = 0.0;
        public double h = 0.0;

        public boolean isResonator = false;
        public double resW = 0.0;
        public double resL = 0.0;
        public double feedLength = 0.0;
        public double insetDepth = 0.0;
        public int numElements = 1;
    }

    public ScuffEMStrNTxSimulationPlan(String id, String name, NTxRendererContext rendererContext) {
        super(id, name, rendererContext);
    }

    @Override
    public NTxSolverRun createItem(String computeName, String solverName) {
        switch (NNameFormat.LOWER_KEBAB_CASE.format(solverName)) {
            case "s11":
            case "sparam":
            case "sparams":
            case "s-param":
            case "s-params": {
                return new NTxScuffEMS11Solver(computeName, solverName, this);
            }
            case "zin":
            case "z-in":
            case "input-impedance": {
                return new NTxScuffEMZinSolver(computeName, solverName, this);
            }
        }
        return null;
    }

    public void recordRequestedFrequencies(double[] freqs) {
        if (freqs != null) {
            for (double f : freqs) {
                requestedFrequencies.add(f);
            }
        }
    }

    public RebuiltGeometry rebuildGeometry() {
        RebuiltGeometry geom = new RebuiltGeometry();
        if (modelInfo == null) {
            return geom;
        }

        for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.substrateBoxes) {
            double sh = b.height();
            if (sh > 0.0001) {
                geom.h = sh;
            }
        }

        ScuffEMModelInfo.ScuffEMBox src = modelInfo.sourceBoxes.isEmpty() ? null : modelInfo.sourceBoxes.get(0);
        if (src != null) {
            geom.feedW = src.width();
            geom.portY = src.yMin();
            geom.portX1 = src.xMin();
            geom.portX2 = src.xMax();
            geom.feedCenter = (src.xMin() + src.xMax()) / 2.0;
        }

        ScuffEMModelInfo.ScuffEMBox feedBox = null;
        double antXmin = Double.MAX_VALUE, antXmax = -Double.MAX_VALUE;
        double antYmin = Double.MAX_VALUE, antYmax = -Double.MAX_VALUE;

        for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.antennaBoxes) {
            antXmin = Math.min(antXmin, b.xMin());
            antXmax = Math.max(antXmax, b.xMax());
            antYmin = Math.min(antYmin, b.yMin());
            antYmax = Math.max(antYmax, b.yMax());

            if (src != null && b.xMax() >= src.xMin() - 1e-6 && b.xMin() <= src.xMax() + 1e-6
                    && b.yMax() >= src.yMin() - 1e-6 && b.yMin() <= src.yMax() + 1e-6) {
                if (feedBox == null || b.length() > feedBox.length()) {
                    feedBox = b;
                }
            }
        }

        if (feedBox != null) {
            geom.feedW = feedBox.width();
            geom.portY = src != null ? src.yMin() : feedBox.yMin();
            geom.portX1 = feedBox.xMin();
            geom.portX2 = feedBox.xMax();
            geom.feedCenter = (feedBox.xMin() + feedBox.xMax()) / 2.0;
        } else if (geom.feedW <= 0) {
            geom.feedW = 3.1e-3;
            geom.portX1 = -geom.feedW / 2.0;
            geom.portX2 = geom.feedW / 2.0;
        }

        double totalLength = antYmax > antYmin ? (antYmax - geom.portY) : 30e-3;
        geom.lineLength = totalLength;

        java.util.List<ScuffEMModelInfo.ScuffEMBox> candidateBoxes = new java.util.ArrayList<>();
        for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.antennaBoxes) {
            if (b.isPatch) {
                candidateBoxes.add(b);
            }
        }
        if (candidateBoxes.isEmpty()) {
            double widthThreshold = 1.25 * geom.feedW;
            for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.antennaBoxes) {
                if (b.width() > widthThreshold && b.length() > 1.5 * geom.feedW) {
                    candidateBoxes.add(b);
                }
            }
        }

        if (!candidateBoxes.isEmpty()) {
            candidateBoxes.sort(java.util.Comparator.comparingDouble(ScuffEMModelInfo.ScuffEMBox::xMin));
            java.util.List<ScuffEMModelInfo.ScuffEMBox> clusters = new java.util.ArrayList<>();
            for (ScuffEMModelInfo.ScuffEMBox b : candidateBoxes) {
                if (clusters.isEmpty()) {
                    clusters.add(new ScuffEMModelInfo.ScuffEMBox(b.x1, b.y1, b.z1, b.x2, b.y2, b.z2, b.name, b.type));
                } else {
                    ScuffEMModelInfo.ScuffEMBox last = clusters.get(clusters.size() - 1);
                    if (b.xMin() <= last.xMax() + 4e-3) {
                        last.x1 = Math.min(last.x1, b.xMin());
                        last.x2 = Math.max(last.x2, b.xMax());
                        last.y1 = Math.min(last.y1, b.yMin());
                        last.y2 = Math.max(last.y2, b.yMax());
                    } else {
                        clusters.add(new ScuffEMModelInfo.ScuffEMBox(b.x1, b.y1, b.z1, b.x2, b.y2, b.z2, b.name, b.type));
                    }
                }
            }

            geom.isResonator = true;
            geom.numElements = clusters.size();
            geom.resW = clusters.get(0).width();
            geom.resL = clusters.get(0).length();
            geom.feedLength = Math.max(0.0, clusters.get(0).yMin() - geom.portY);

            if (feedBox != null && feedBox.yMax() > clusters.get(0).yMin()) {
                geom.insetDepth = Math.min(geom.resL, feedBox.yMax() - clusters.get(0).yMin());
            } else {
                for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.antennaBoxes) {
                    if (b != feedBox && b.width() < (geom.resW * 0.45) && b.yMin() <= clusters.get(0).yMin() + 1e-6) {
                        geom.insetDepth = Math.max(geom.insetDepth, b.length());
                    }
                }
            }
        } else {
            geom.isResonator = false;
            geom.numElements = 1;
        }

        return geom;
    }

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        NTxMwSimulationUtils.addDigestSource(d, "SCUFFEM_BEM".getBytes(StandardCharsets.UTF_8));
        if (modelInfo != null) {
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.frequency).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.epsilonR).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.lossTangent).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.meshResolution).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, ("port:" + modelInfo.portDeembed).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, "mesh:narrow-strips;gap-port".getBytes(StandardCharsets.UTF_8));
            if (modelInfo.geometryId != null) {
                NTxMwSimulationUtils.addDigestSource(d, modelInfo.geometryId.getBytes(StandardCharsets.UTF_8));
            }
            for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.antennaBoxes) {
                NTxMwSimulationUtils.addDigestSource(d, (b.x1 + "," + b.y1 + "," + b.x2 + "," + b.y2).getBytes(StandardCharsets.UTF_8));
            }
            for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.sourceBoxes) {
                NTxMwSimulationUtils.addDigestSource(d, (b.x1 + "," + b.y1 + "," + b.x2 + "," + b.y2).getBytes(StandardCharsets.UTF_8));
            }
        }
        for (NTxSolverRun item : items) {
            NTxMwSimulationUtils.addDigestSource(d, item.toElement().toString().getBytes(StandardCharsets.UTF_8));
        }
        return d.computeString();
    }

    private String generateGmshGeo(Outline o) {
        StringBuilder sb = new StringBuilder();
        sb.append("SetFactory(\"OpenCASCADE\");\n");
        sb.append("Mesh.MshFileVersion = 2.2;\n");
        double lc = modelInfo.meshResolution > 0 ? modelInfo.meshResolution : 2.5;
        sb.append(String.format(Locale.US, "lc = %.4f;\n", lc));
        sb.append("Mesh.CharacteristicLengthMax = lc;\n");

        // A strip narrower than 2 lc gets one or two triangles across and reads the wrong
        // impedance: on the patch deck a 1.8 mm transformer at lc 1.5 put the null about
        // 70 MHz high. Each point takes half the distance to the nearest non-adjacent edge.
        int n = o.points.size();
        double[] size = new double[n];
        double minSize = lc;
        for (int i = 0; i < n; i++) {
            size[i] = Math.min(lc, 0.5 * localFeatureSize(o.points, i) * 1000.0);
            minSize = Math.min(minSize, size[i]);
        }
        sb.append(String.format(Locale.US, "Mesh.CharacteristicLengthMin = %.4f;\n", minSize));
        for (int i = 0; i < n; i++) {
            sb.append(String.format(Locale.US, "Point(%d) = {%.4f, %.4f, 0, %.4f};\n",
                    i + 1, o.points.get(i)[0] * 1000.0, o.points.get(i)[1] * 1000.0, size[i]));
        }
        for (int i = 0; i < n; i++) {
            sb.append(String.format(Locale.US, "Line(%d) = {%d, %d};\n", i + 1, i + 1, (i + 1) % n + 1));
        }
        sb.append("Curve Loop(1) = {");
        for (int i = 0; i < n; i++) {
            sb.append(i + 1).append(i + 1 == n ? "};\n" : ", ");
        }
        sb.append("Plane Surface(1) = {1};\n");
        if (Double.isNaN(o.stubTop)) {
            sb.append("Physical Surface(\"Antenna\") = {1};\n");
            return sb.toString();
        }
        // The gap edge is the stub's first line. scuff's RWGSurface::GetEdgeByIndex bounds-checks
        // half-RWG edges with (-ne+1) instead of -(ne+1), so a port terminal on the last two
        // boundary edges aborts with "internal error"; a 3 mm stub drawn gap edge last did.
        double x1 = o.portX1 * 1000.0, x2 = o.portX2 * 1000.0;
        double top = o.stubTop * 1000.0, bot = o.stubBottom * 1000.0;
        double ss = Math.min(lc, 0.5 * GAP_PORT_STUB * 1000.0);
        double[][] stub = {{x2, top}, {x1, top}, {x1, bot}, {x2, bot}};
        for (int i = 0; i < 4; i++) {
            sb.append(String.format(Locale.US, "Point(%d) = {%.4f, %.4f, 0, %.4f};\n", n + 1 + i, stub[i][0], stub[i][1], ss));
        }
        for (int i = 0; i < 4; i++) {
            sb.append(String.format(Locale.US, "Line(%d) = {%d, %d};\n", n + 1 + i, n + 1 + i, n + 1 + (i + 1) % 4));
        }
        sb.append(String.format(Locale.US, "Curve Loop(2) = {%d, %d, %d, %d};\n", n + 1, n + 2, n + 3, n + 4));
        sb.append("Plane Surface(2) = {2};\n");
        sb.append("Physical Surface(\"Antenna\") = {1, 2};\n");
        return sb.toString();
    }

    /** Distance from outline point i to the nearest edge that does not end on it. */
    private static double localFeatureSize(List<double[]> pts, int i) {
        int n = pts.size();
        double[] p = pts.get(i);
        double best = Double.MAX_VALUE;
        for (int k = 0; k < n; k++) {
            int k2 = (k + 1) % n;
            if (k == i || k2 == i) {
                continue;
            }
            double[] a = pts.get(k), b = pts.get(k2);
            double dx = b[0] - a[0], dy = b[1] - a[1];
            double len2 = dx * dx + dy * dy;
            double t = len2 == 0 ? 0 : Math.max(0, Math.min(1, ((p[0] - a[0]) * dx + (p[1] - a[1]) * dy) / len2));
            best = Math.min(best, Math.hypot(p[0] - a[0] - t * dx, p[1] - a[1] - t * dy));
        }
        return best;
    }

    /**
     * The conductor boxes merged into one rectilinear outline, swept band by band along Y.
     *
     * gmsh can boolean-union edge-touching rectangles, but the result keeps the shared edges as
     * internal boundaries whose node rows differ on either side; SCUFF-EM then aborts inside
     * SommerfeldIntegrateP. Outlining the union as a single closed curve removes the
     * non-conforming seam entirely: gmsh meshes one surface, so every edge is shared by exactly
     * two triangles except on the outline.
     */
    private Outline buildOutline(RebuiltGeometry geom) {
        List<ScuffEMModelInfo.ScuffEMBox> boxes = modelInfo.antennaBoxes;
        if (boxes.isEmpty()) {
            throw new IllegalStateException("SCUFF-EM: no conductor surface to mesh");
        }
        double tol = 1e-9;
        TreeSet<Double> ys = new TreeSet<>();
        for (ScuffEMModelInfo.ScuffEMBox b : boxes) {
            ys.add(b.yMin());
            ys.add(b.yMax());
        }
        // Abutting boxes can differ by rounding, which would leave a zero-height band
        List<Double> yEdge = new ArrayList<>();
        for (double y : ys) {
            if (yEdge.isEmpty() || y - yEdge.get(yEdge.size() - 1) > tol) {
                yEdge.add(y);
            }
        }
        int nb = yEdge.size() - 1;
        double[] left = new double[nb];
        double[] right = new double[nb];
        for (int k = 0; k < nb; k++) {
            double ya = yEdge.get(k);
            double yb = yEdge.get(k + 1);
            double ym = (ya + yb) / 2.0;
            List<double[]> spans = new ArrayList<>();
            for (ScuffEMModelInfo.ScuffEMBox b : boxes) {
                if (b.yMin() - tol <= ym && ym <= b.yMax() + tol) {
                    spans.add(new double[]{b.xMin(), b.xMax()});
                }
            }
            if (spans.isEmpty()) {
                throw new IllegalStateException(String.format(
                        "SCUFF-EM: conductor is disconnected in Y at %.4f..%.4f mm; the trace outline must be one connected surface",
                        ya * 1000.0, yb * 1000.0));
            }
            spans.sort(Comparator.comparingDouble(a -> a[0]));
            double lo = spans.get(0)[0];
            double hi = spans.get(0)[1];
            for (int i = 1; i < spans.size(); i++) {
                double[] s = spans.get(i);
                if (s[0] - hi > tol) {
                    throw new IllegalStateException(String.format(
                            "SCUFF-EM: two separate conductors at y=%.4f mm (x %.4f..%.4f and %.4f..%.4f mm);"
                                    + " a multi-strip trace mesh needs one physical surface per strip",
                            ym * 1000.0, lo * 1000.0, hi * 1000.0, s[0] * 1000.0, s[1] * 1000.0));
                }
                hi = Math.max(hi, s[1]);
            }
            left[k] = lo;
            right[k] = hi;
        }

        Outline o = new Outline();
        // counter-clockwise: bottom edge, right side going up, top edge, left side coming down
        o.points.add(new double[]{left[0], yEdge.get(0)});
        o.points.add(new double[]{right[0], yEdge.get(0)});
        for (int k = 0; k < nb; k++) {
            double yTop = yEdge.get(k + 1);
            o.points.add(new double[]{right[k], yTop});
            if (k + 1 < nb && Math.abs(right[k + 1] - right[k]) > tol) {
                o.points.add(new double[]{right[k + 1], yTop});
            }
        }
        o.points.add(new double[]{left[nb - 1], yEdge.get(nb)});
        for (int k = nb - 1; k >= 0; k--) {
            o.points.add(new double[]{left[k], yEdge.get(k)});
            if (k - 1 >= 0 && Math.abs(left[k - 1] - left[k]) > tol) {
                o.points.add(new double[]{left[k - 1], yEdge.get(k)});
            }
        }
        while (o.points.size() > 1 && near(o.points.get(o.points.size() - 1), o.points.get(0), tol)) {
            o.points.remove(o.points.size() - 1);
        }
        o.portY = yEdge.get(0);
        o.portX1 = left[0];
        o.portX2 = right[0];
        o.refY = o.portY;
        if (Math.abs(o.portY - geom.portY) > 1e-6) {
            rendererContext().log(NMsg.ofC("[SCUFF-EM] port y=%.4f mm is not the trace bottom y=%.4f mm;"
                    + " feeding at the trace bottom instead", geom.portY * 1000.0, o.portY * 1000.0).asWarning());
        }
        if (modelInfo.portDeembed > 0) {
            // The substrate and ground are infinite in scuff, so the feed can simply be drawn
            // longer: points 0 and 1 are the two ends of the bottom (port) edge.
            double ext = modelInfo.portDeembed / 1000.0;
            o.points.get(0)[1] -= ext;
            o.points.get(1)[1] -= ext;
            o.portY -= ext;
            // The feed then ends on a gap with a short stub beyond it, and the port drives the
            // gap. scuff's edge port ends the strip on a free charge, a vertical dipole over the
            // ground that launches a strong TM0 wave on a thick substrate. That wave reaches the
            // patch directly, so the de-embedded S11 moved by up to 0.3 GHz with the extension
            // length. Across a gap the charges cancel: the reading then holds within about 1.5 dB
            // from 25 to 50 mm, and a 3 mm stub reads like a 10 mm one.
            o.stubTop = o.portY - GAP_PORT_GAP;
            o.stubBottom = o.stubTop - GAP_PORT_STUB;
        }
        return o;
    }

    private static final double GAP_PORT_GAP = 0.3e-3;
    private static final double GAP_PORT_STUB = 3e-3;

    private static boolean near(double[] a, double[] b, double tol) {
        return Math.abs(a[0] - b[0]) <= tol && Math.abs(a[1] - b[1]) <= tol;
    }

    /** A lumped port is only valid along one mesh edge, so it is chosen from the mesh, not from the geometry. */
    private static final class MeshPorts {
        final Map<Integer, double[]> nodes = new HashMap<>();
        final List<int[]> edges = new ArrayList<>();
        final Set<Integer> surfaces = new HashSet<>();
        int triangles;
    }

    private MeshPorts readMesh(NPath msh) {
        MeshPorts m = new MeshPorts();
        String section = null;
        List<String> pending = null;
        int remaining = 0;
        for (String line : msh.readString().split("\n")) {
            String s = line.trim();
            if (s.startsWith("$")) {
                section = s.substring(1);
                if ("$Nodes".equals(s) || "$Elements".equals(s)) {
                    pending = new ArrayList<>();
                    remaining = -1;
                } else {
                    pending = null;
                }
                continue;
            }
            if (s.endsWith("EndNodes") || s.endsWith("EndElements")) {
                section = null;
                continue;
            }
            if (pending == null) {
                continue;
            }
            if (remaining == -1) {
                remaining = Integer.parseInt(s);
                continue;
            }
            pending.add(s);
            if (--remaining == 0) {
                if ("Nodes".equals(section)) {
                    for (String row : pending) {
                        String[] p = row.split("\\s+");
                        // gmsh 2.2 node rows are "<tag> x y z", or "<tag> dim param orient x y z"
                        // when the mesh keeps its entity blocks.
                        int b;
                        if (p.length == 4) {
                            b = 0;
                        } else if (p.length == 7) {
                            b = 3;
                        } else {
                            throw new IllegalStateException("SCUFF-EM: unparsable gmsh node row: " + row);
                        }
                        m.nodes.put(Integer.parseInt(p[0]),
                                new double[]{Double.parseDouble(p[b + 1]),
                                        Double.parseDouble(p[b + 2]), Double.parseDouble(p[b + 3])});
                    }
                } else {
                    for (String row : pending) {
                        String[] p = row.split("\\s+");
                        int type = Integer.parseInt(p[1]);
                        // gmsh 2.2 element rows are "<tag> <type> <physical> <elementary> <n> <nodes...>",
                        // where gmsh 4.x always writes <n> as 1 and the node count is given by the type.
                        int nn = type == 15 ? 1 : (type == 1 ? 2 : (type == 2 ? 3 : 0));
                        if (nn == 0) {
                            throw new IllegalStateException("SCUFF-EM: unexpected gmsh element type " + type
                                    + "; AddMetalTraceMesh needs a 2D triangle mesh (gmsh -2)");
                        }
                        if (p.length != 5 + nn) {
                            throw new IllegalStateException("SCUFF-EM: unparsable gmsh element row: " + row);
                        }
                        if (type != 2) {
                            continue;   // points and 1D boundary elements carry no surface topology
                        }
                        m.surfaces.add(Integer.parseInt(p[2]));
                        m.triangles++;
                        int a = Integer.parseInt(p[5]);
                        int b = Integer.parseInt(p[6]);
                        int c = Integer.parseInt(p[7]);
                        m.edges.add(new int[]{a, b});
                        m.edges.add(new int[]{b, c});
                        m.edges.add(new int[]{c, a});
                    }
                }
                pending = new ArrayList<>();
                remaining = -1;
            }
        }
        return m;
    }

    /**
     * Fail loudly rather than let SCUFF-EM report a meaningless curve: the trace mesh must be one
     * connected 2D surface, and the port span must be carried by boundary edges of it.
     *
     * A lumped port in scuff-em is a gap in the trace whose terminals are mesh nodes; the official
     * MicrostripDevices example spans a 10 mm port across several mesh edges, so the requirement is
     * that the whole span is covered by a contiguous chain of boundary edges, not that it be one edge.
     */
    private void validatePort(MeshPorts m, Outline o) {
        if (m.triangles == 0) {
            throw new IllegalStateException("SCUFF-EM: gmsh produced no triangles");
        }
        if (m.surfaces.size() > 1) {
            throw new IllegalStateException("SCUFF-EM: trace mesh spans " + m.surfaces.size()
                    + " physical surfaces " + m.surfaces + ", expected one connected conductor");
        }
        TreeMap<Integer, Integer> share = new TreeMap<>();
        for (int[] e : m.edges) {
            share.merge(edgeKey(e[0], e[1]), 1, Integer::sum);
        }
        for (Map.Entry<Integer, Integer> en : share.entrySet()) {
            if (en.getValue() > 2) {
                throw new IllegalStateException("SCUFF-EM: mesh edge " + en.getKey() + " is used by "
                        + en.getValue() + " triangles, the trace surface is not manifold");
            }
        }
        // gmsh writes the mesh in the millimetres the .geo was given, while Outline keeps
        // the scene geometry in metres.
        checkPortSpan(m, share, o.portY * 1000.0, o.portX1 * 1000.0, o.portX2 * 1000.0, "port");
        if (!Double.isNaN(o.stubTop)) {
            checkPortSpan(m, share, o.stubTop * 1000.0, o.portX1 * 1000.0, o.portX2 * 1000.0, "port return (gap)");
        }
    }

    private void checkPortSpan(MeshPorts m, Map<Integer, Integer> share, double portY, double portX1, double portX2, String what) {
        double tol = 1e-6;
        List<double[]> chain = new ArrayList<>();
        for (int[] e : m.edges) {
            double[] a = m.nodes.get(e[0]);
            double[] b = m.nodes.get(e[1]);
            if (a == null || b == null || share.get(edgeKey(e[0], e[1])) != 1) {
                continue; // an interior edge, not a boundary edge
            }
            if (Math.abs(a[1] - portY) > tol || Math.abs(b[1] - portY) > tol || Math.abs(a[2]) > tol || Math.abs(b[2]) > tol) {
                continue;
            }
            double lo = Math.max(Math.min(a[0], b[0]), portX1 - tol);
            double hi = Math.min(Math.max(a[0], b[0]), portX2 + tol);
            if (hi > lo) {
                chain.add(new double[]{lo, hi});
            }
        }
        chain.sort(Comparator.comparingDouble(a -> a[0]));
        double cursor = portX1;
        int used = 0;
        for (double[] seg : chain) {
            if (seg[0] > cursor + tol) {
                break;
            }
            if (seg[1] > cursor + tol) {
                cursor = seg[1];
                used++;
            }
            if (cursor >= portX2 - tol) {
                break;
            }
        }
        if (cursor < portX2 - tol) {
            throw new IllegalStateException(String.format(
                    "SCUFF-EM: the " + what + " span y=%.4f mm, x=%.4f..%.4f mm is not carried by mesh boundary edges"
                            + " (covered only to x=%.4f mm); the feed edge is missing from the trace mesh",
                    portY, portX1, portX2, cursor));
        }
        rendererContext().log(NMsg.ofC("[SCUFF-EM] %s: %d boundary edge(s) over %.4f mm of feed at y=%.4f mm",
                what, used, portX2 - portX1, portY).asInfo());
    }

    private static Integer edgeKey(int a, int b) {
        return a < b ? a * 1000003 + b : b * 1000003 + a;
    }

    private String generatePythonScript(Outline o, List<Double> freqs) {
        StringBuilder sb = new StringBuilder();
        sb.append("import scuff, math, sys\n\n");
        sb.append("Solver = scuff.scuffSolver()\n");
        sb.append("Solver.AddMetalTraceMesh(\"/sim/mesh.msh\")\n");

        String pEdge = String.format(Locale.US, "[%.6f, %.6f, %.6f, %.6f, %.6f, %.6f]",
                o.portX1 * 1000.0, o.portY * 1000.0, 0.0, o.portX2 * 1000.0, o.portY * 1000.0, 0.0);
        if (Double.isNaN(o.stubTop)) {
            sb.append("Solver.AddPort(").append(pEdge).append(")\n");
        } else {
            sb.append("Solver.AddPort(").append(pEdge).append(String.format(Locale.US, ", [%.6f, %.6f, %.6f, %.6f, %.6f, %.6f])\n",
                    o.portX1 * 1000.0, o.stubTop * 1000.0, 0.0, o.portX2 * 1000.0, o.stubTop * 1000.0, 0.0));
        }
        // scuff runs exp(-iwt), so a lossy substrate is eps' (1 + i tan(delta)). A real
        // epsilon here drops the loss tangent and leaves the null about 12 dB too deep.
        sb.append(String.format(Locale.US, "Solver.SetSubstratePermittivity(complex(%.6f, %.6f))\n",
                modelInfo.epsilonR, modelInfo.epsilonR * modelInfo.lossTangent));
        sb.append(String.format(Locale.US, "Solver.SetSubstrateThickness(%.6f)\n\n", substrateThickness_mm()));
        if (modelInfo.portDeembed <= 0) {
            return appendFrequencyLoop(sb, freqs);
        }
        // The fit window is the extension minus its first fifth, which holds the port's near field.
        double ext = modelInfo.portDeembed;
        double refY = o.refY * 1000.0;
        sb.append("freqs = [");
        for (int i = 0; i < freqs.size(); i++) {
            sb.append(i > 0 ? ", " : "").append(String.format(Locale.US, "%.6e", freqs.get(i)));
        }
        sb.append("]\n");
        sb.append(String.format(Locale.US, "DE_X0, DE_X1 = %.6f, %.6f\n", o.portX1 * 1000.0 - 0.05, o.portX2 * 1000.0 + 0.05));
        sb.append(String.format(Locale.US, "DE_Y0, DE_Y1, DE_DY = %.6f, %.6f, %.6f\n", refY - 0.8 * ext, refY, deembedBin()));
        sb.append(String.format(Locale.US, "DE_YREF = %.6f\n", refY));
        sb.append(String.format(Locale.US, "DE_EPS, DE_TAND, DE_H = %.6f, %.6f, %.6f\n\n",
                modelInfo.epsilonR, modelInfo.lossTangent, substrateThickness_mm()));
        try (java.io.InputStream in = getClass().getResourceAsStream("deembed.py")) {
            if (in == null) {
                throw new IllegalStateException("SCUFF-EM: deembed.py resource is missing");
            }
            java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            for (int n; (n = in.read(buf)) > 0; ) {
                bo.write(buf, 0, n);
            }
            sb.append(new String(bo.toByteArray(), StandardCharsets.UTF_8));
        } catch (java.io.IOException ex) {
            throw new IllegalStateException("SCUFF-EM: cannot read deembed.py: " + ex.getMessage(), ex);
        }
        return sb.toString();
    }

    /** Current bins of about 1 mm, but at least 12 of them over the fit window. */
    private double deembedBin() {
        return Math.min(1.0, 0.8 * modelInfo.portDeembed / 12.0);
    }

    private double substrateThickness_mm() {
        for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.substrateBoxes) {
            if (b.height() > 1e-4) {
                return b.height() * 1000.0;
            }
        }
        return 0.0;
    }

    private String appendFrequencyLoop(StringBuilder sb, List<Double> freqs) {
        sb.append("freqs = [");
        for (int i = 0; i < freqs.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(String.format(Locale.US, "%.6e", freqs.get(i)));
        }
        sb.append("]\n\n");

        sb.append("with open(\"/sim/results.dat\", \"w\") as f:\n");
        sb.append("    for freq in freqs:\n");
        sb.append("        f_ghz = freq / 1e9\n");
        sb.append("        # scuffSolver::AssembleSystemMatrix(double Omega) takes the angular\n");
        sb.append("        # frequency in scuff's own scale: FREQ2OMEGA = 2*pi/300 per GHz\n");
        sb.append("        # (libs/libscuffSolver/scuffSolver.h). Passing f_GHz directly drives the\n");
        sb.append("        # solve at 300/(2*pi) = 47.7 times the intended frequency.\n");
        sb.append("        Solver.AssembleSystemMatrix(f_ghz * 2.0 * math.pi / 300.0)\n");
        sb.append("        # GetZMatrix(None, True) returns the total in row 0 and its terms T1, T2, T3\n");
        sb.append("        # in rows 1..3. The total is not usable: T3 double-counts the port potential\n");
        sb.append("        # and the sign is flipped, so we use Z = -conj(T1 + T2), the conj taking\n");
        sb.append("        # scuff's exp(-iwt) to the engineering exp(+jwt). Checked against the closed\n");
        sb.append("        # form of a 50 Ohm open stub (-76.0j vs -75.5j at 0.5 GHz; the total gives -138.6j).\n");
        sb.append("        z_mat = Solver.GetZMatrix(None, True)\n");
        sb.append("        z11 = -(z_mat.GetEntry(1, 0) + z_mat.GetEntry(2, 0)).conjugate()\n");
        sb.append("        s11 = (z11 - 50.0) / (z11 + 50.0)\n");
        sb.append("        f.write(f\"{freq:.6e} {s11.real:.8e} {s11.imag:.8e} {z11.real:.8e} {z11.imag:.8e}\\n\")\n");
        sb.append("        f.flush()\n");
        return sb.toString();
    }


    public synchronized void ensureSolved() {
        if (solved) {
            return;
        }
        solved = true;
        if (modelInfo == null) {
            modelInfo = new ScuffEMModelInfo();
        }

        RebuiltGeometry geom = rebuildGeometry();
        String hash = computeHash();

        List<Double> simFreqs = selectSimulationFrequencies();

        NPath workDir = NPath.ofTempFolder("scuffem-sim-");
        workDir.mkdirs();

        if (!ScuffEMProvisioner.ensureDocker(modelInfo.dockerImage, rendererContext(), hash)) {
            throw new IllegalStateException(
                    "SCUFF-EM: Docker image '" + modelInfo.dockerImage + "' is not available");
        }
        try {
            Outline outline = buildOutline(geom);
            NPath geoFile = workDir.resolve("mesh.geo");
            geoFile.writeString(generateGmshGeo(outline));

            // 1. Run Gmsh
            int gmshCode = ScuffEMProvisioner.runInDocker(
                    modelInfo.dockerImage,
                    workDir,
                    "/sim",
                    Arrays.asList("gmsh", "-2", "/sim/mesh.geo", "-o", "/sim/mesh.msh"),
                    rendererContext(),
                    hash
            );
            if (gmshCode != 0) {
                throw new IllegalStateException("gmsh mesh generation failed with exit code " + gmshCode);
            }

            // 2. Gate on the mesh, then place the port on an edge that really exists in it
            NPath mshFile = workDir.resolve("mesh.msh");
            if (!mshFile.exists()) {
                throw new IllegalStateException("gmsh produced no /sim/mesh.msh");
            }
            validatePort(readMesh(mshFile), outline);

            NPath pyFile = workDir.resolve("simulate.py");
            pyFile.writeString(generatePythonScript(outline, simFreqs));

            // 3. Run SCUFF-EM python solver
            // The image keeps scuff's bindings in /usr/local site-packages, which the
            // distro python3 does not search by default.
            int scuffCode = ScuffEMProvisioner.runInDocker(
                    modelInfo.dockerImage,
                    workDir,
                    "/sim",
                    Arrays.asList("sh", "-c",
                            "PYTHONPATH=$(ls -d /usr/local/lib/python*/site-packages 2>/dev/null | head -1)"
                                    + " exec python3 /sim/simulate.py"),
                    rendererContext(),
                    hash
            );
            if (scuffCode != 0) {
                throw new IllegalStateException("solver failed with exit code " + scuffCode);
            }

            // 3. Parse results.dat
            NPath resFile = workDir.resolve("results.dat");
            double epsMin = Double.MAX_VALUE, epsMax = -Double.MAX_VALUE, residMax = 0;
            if (resFile.exists()) {
                String resStr = new String(resFile.readBytes(), StandardCharsets.UTF_8);
                for (String line : resStr.split("\n")) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 5) {
                            double f = Double.parseDouble(parts[0]);
                            double s11Re = Double.parseDouble(parts[1]);
                            double s11Im = Double.parseDouble(parts[2]);
                            double zRe = Double.parseDouble(parts[3]);
                            double zIm = Double.parseDouble(parts[4]);
                            Complex zin;
                            Complex s11;
                            if (modelInfo.portDeembed > 0 && parts.length >= 7) {
                                zin = Complex.of(zRe, zIm);
                                s11 = Complex.of(s11Re, s11Im);
                                double eps = Double.parseDouble(parts[5]);
                                epsMin = Math.min(epsMin, eps);
                                epsMax = Math.max(epsMax, eps);
                                residMax = Math.max(residMax, Double.parseDouble(parts[6]));
                            } else if (geom.isResonator) {
                                zin = Complex.of(zRe, zIm);
                                s11 = Complex.of(s11Re, s11Im);
                            } else {
                                zin = Complex.of(zRe + 50.0, zIm);
                                Complex z0 = Complex.of(50.0);
                                s11 = zin.minus(z0).div(zin.plus(z0));
                            }
                            s11Results.put(f, s11);
                            zinResults.put(f, zin);
                        }
                    }
                }
            }
            if (modelInfo.portDeembed > 0 && epsMax > 0) {
                NMsg msg = NMsg.ofC("[SCUFF-EM] de-embedded port: feed extended %.3f mm, reference y=%.4f mm,"
                                + " line eps_eff %.3f..%.3f, worst fit residual %.4f",
                        modelInfo.portDeembed, outline.refY * 1000.0, epsMin, epsMax, residMax);
                // a residual this large means the strip current is not two travelling waves
                // per mode: the extension is too short or not uniform
                rendererContext().log(residMax > 0.1 ? msg.asWarning() : msg.asInfo());
            }
        } catch (Exception ex) {
            s11Results.clear();
            zinResults.clear();
            if (rendererContext() != null) {
                rendererContext().log(NMsg.ofC("[SCUFF-EM][%s] Simulation failed: %s", hash, ex.getMessage()).asError());
            }
            throw new IllegalStateException("SCUFF-EM simulation failed: " + ex.getMessage(), ex);
        }

        if (s11Results.isEmpty()) {
            throw new IllegalStateException(
                    "SCUFF-EM produced no results at " + simFreqs.size() + " requested frequency point(s)");
        }
        for (Map.Entry<Double, Complex> en : zinResults.entrySet()) {
            if (en.getValue().realdbl() < 0) {
                throw new IllegalStateException(String.format(
                        "SCUFF-EM: Z11 = %.3f %+.3fj at %.4f GHz has a negative real part, which a passive one-port"
                                + " cannot have - the solve produced no physical result, so no curve is reported",
                        en.getValue().realdbl(), en.getValue().imagdbl(), en.getKey() / 1e9));
            }
        }
    }

    private List<Double> selectSimulationFrequencies() {
        List<Double> list = new ArrayList<>();
        if (requestedFrequencies.isEmpty()) {
            list.add(modelInfo.frequency);
            return list;
        }

        if (requestedFrequencies.size() <= 12) {
            list.addAll(requestedFrequencies);
            return list;
        }

        // Downsample to at most 12 anchor frequencies across range
        List<Double> all = new ArrayList<>(requestedFrequencies);
        double fmin = all.get(0);
        double fmax = all.get(all.size() - 1);
        int nAnchors = 10;
        for (int i = 0; i < nAnchors; i++) {
            double f = fmin + (fmax - fmin) * (double) i / (double) (nAnchors - 1);
            list.add(f);
        }
        if (!list.contains(modelInfo.frequency)) {
            list.add(modelInfo.frequency);
            Collections.sort(list);
        }
        return list;
    }

    public Complex computeS11(double freq) {
        ensureSolved();
        return interpolate(s11Results, freq);
    }

    public Complex computeZin(double freq) {
        ensureSolved();
        return interpolate(zinResults, freq);
    }

    private Complex interpolate(TreeMap<Double, Complex> map, double freq) {
        if (map.isEmpty()) {
            return Complex.ZERO;
        }
        if (map.containsKey(freq)) {
            return map.get(freq);
        }

        Map.Entry<Double, Complex> lower = map.floorEntry(freq);
        Map.Entry<Double, Complex> upper = map.ceilingEntry(freq);

        if (lower == null) return upper.getValue();
        if (upper == null) return lower.getValue();

        double f1 = lower.getKey();
        double f2 = upper.getKey();
        if (Math.abs(f2 - f1) < 1e-6) {
            return lower.getValue();
        }

        double t = (freq - f1) / (f2 - f1);
        Complex v1 = lower.getValue();
        Complex v2 = upper.getValue();

        double re = v1.getReal() + t * (v2.getReal() - v1.getReal());
        double im = v1.getImag() + t * (v2.getImag() - v1.getImag());
        return Complex.of(re, im);
    }
}
