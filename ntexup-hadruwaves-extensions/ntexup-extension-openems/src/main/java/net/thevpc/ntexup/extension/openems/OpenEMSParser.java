package net.thevpc.ntexup.extension.openems;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.eval.NTxFunctionArg;
import net.thevpc.ntexup.api.eval.NTxFunctionCallContext;
import net.thevpc.ntexup.api.eval.NTxResolutionContext;
import net.thevpc.ntexup.api.util.NTxNumberUtils;
import net.thevpc.ntexup.api.util.NTxUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.lib.geometry3d.NTxNumberElement3;
import net.thevpc.ntexup.lib.geometry3d.impl.NTx3DUtils;
import net.thevpc.ntexup.api.document.style.NTxProp;
import net.thevpc.ntexup.api.document.style.NTxPropName;
import net.thevpc.nuts.elem.NArrayElement;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NPairElement;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.nuts.util.NStringUtils;

import java.io.IOException;
import java.util.*;

public class OpenEMSParser {

    public static class OpenEMSBox {
        public double x1, y1, z1;
        public double x2, y2, z2;
        public String name;
        public String type;

        public OpenEMSBox(double x1, double y1, double z1, double x2, double y2, double z2, String name, String type) {
            this.x1 = Math.min(x1, x2);
            this.y1 = Math.min(y1, y2);
            this.z1 = Math.min(z1, z2);
            this.x2 = Math.max(x1, x2);
            this.y2 = Math.max(y1, y2);
            this.z2 = Math.max(z1, z2);
            this.name = name;
            this.type = type;
        }

        public double xMin() { return x1; }
        public double xMax() { return x2; }
        public double yMin() { return y1; }
        public double yMax() { return y2; }
        public double zMin() { return z1; }
        public double zMax() { return z2; }
    }

    public static class OpenEMSModelInfo {
        public double frequency = 2.4e9;
        public double fc = 2.0e9;
        public double epsilonR = 4.4;
        public double lossTangent = 0.02;
        public int numberOfTimesteps = 50000;
        public double endCriteria = 1e-5;
        public int numThreads = 2;
        /**
         * Subdivides every grid interval. At 1 the patch null reads about 300 MHz low: 7.48,
         * 7.66, 7.72, 7.74 GHz at 1 to 4 on a 14 x 6.85 mm patch, about 7.8 GHz extrapolated.
         */
        public int meshRefinement = 3;
        public int meshSubstrateCells = 6;
        // mm, before the refinement subdivides them
        public double meshMaxCellX = 0.75;
        public double meshMaxCellY = 1.0;
        public double meshMaxCellZ = 3.0;
        public double meshMaxCellOutside = 3.0;
        public double meshMinCell = 0.20;
        /**
         * mm. When positive, Z is read from V and I probes on the feed line instead of at the
         * lumped port, and moved to the plane this far from the port's outer edge. The feed must
         * run straight and uniform over that length. Default 0 keeps the lumped port reading.
         */
        public double portDeembed = 0.0;
        /**
         * "lumped": 50 ohm lumped port across the substrate at the source box (default).
         * "msl": openEMS's own microstrip port (AddMSLPort / CalcPort, python interface): the feed
         * enters from the domain edge and S11 is moved to the source box's outer edge with
         * openEMS's ref_plane_shift. Needs an infinite ground.
         */
        public String portType = "lumped";
        /** mm, msl port: the measurement plane sits this far before the reference plane */
        public double portMeasureBefore = 10.0;
        /**
         * Ground box tagged @(infinite): the ground is the PEC domain boundary below the substrate and
         * the substrate runs to the domain edges (through the PML). Otherwise the boxes are a finite board.
         */
        public boolean infiniteGround = false;
        /** PML cells on the absorbing faces; 0 = 8 */
        public int pmlCells = 0;
        /** ohm: lumped port resistance and S11 reference; 50 by default */
        public double z0Ref = 50.0;
        /**
         * mm: the graded air lines beyond the structure end this far out sideways (padding),
         * above it (airHeight) and below a finite board (airBelow); 0 = 35, 42 and 22, with the
         * lines in between scaled from the default ladders.
         */
        public double meshPadding = 0;
        public double meshAirHeight = 0;
        public double meshAirBelow = 0;
        /** msl port: frequencies openEMS's CalcPort evaluates over the excited band; 0 = 1201 */
        public int portSamples = 0;
        public String xmlPath;
        public String geometryId;
        public List<OpenEMSBox> groundBoxes = new ArrayList<>();
        public List<OpenEMSBox> substrateBoxes = new ArrayList<>();
        public List<OpenEMSBox> antennaBoxes = new ArrayList<>();
        public List<OpenEMSBox> sourceBoxes = new ArrayList<>();
    }

    public static OpenEMSModelInfo parse(NTxFunctionCallContext args) {
        OpenEMSModelInfo info = new OpenEMSModelInfo();
        NTxResolutionContext context = args.scopedContext();

        for (NTxFunctionArg arg : args.args()) {
            NElement a = arg.eval();
            if (a.isNamedPair()) {
                NPairElement p = a.asNamedPair().get();
                String key = NTxUtils.uid(p.key().asStringValue().orElse(""));
                NElement pv = context.evalExpression(p.value()).orElse(p.value());
                switch (key) {
                    case "freq":
                    case "frequency": {
                        NOptional<Double> d = pv.asNumber().flatMap(NTxNumberUtils::toHertz);
                        if (d.isPresent()) {
                            info.frequency = d.get();
                        }
                        break;
                    }
                    case "fc": {
                        NOptional<Double> d = pv.asNumber().flatMap(NTxNumberUtils::toHertz);
                        if (d.isPresent()) {
                            info.fc = d.get();
                        }
                        break;
                    }
                    case "xml":
                    case "file": {
                        info.xmlPath = pv.asStringValue().orNull();
                        break;
                    }
                    case "timesteps":
                    case "numberoftimesteps": {
                        info.numberOfTimesteps = pv.asIntValue().orElse(50000);
                        break;
                    }
                    case "endcriteria": {
                        info.endCriteria = pv.asDoubleValue().orElse(1e-5);
                        break;
                    }
                    case "numthreads":
                    case "threads": {
                        info.numThreads = pv.asIntValue().orElse(2);
                        break;
                    }
                    case "mesh": {
                        parseMesh(pv, context, info);
                        break;
                    }
                    case "port": {
                        parsePort(pv, context, info);
                        break;
                    }
                    case "boundary": {
                        parseBoundary(pv, context, info);
                        break;
                    }
                    case "z0":
                    case "z":
                    case "zref": {
                        info.z0Ref = pv.asDoubleValue().orElse(0.0);
                        if (!(info.z0Ref > 0)) {
                            throw new IllegalArgumentException("OpenEMS: 'z0' expects a positive impedance in ohm, as in z0:50");
                        }
                        break;
                    }
                    case "epsilonr":
                    case "permittivity": {
                        info.epsilonR = pv.asDoubleValue().orElse(4.4);
                        break;
                    }
                    case "losstangent":
                    case "loss-tangent":
                    case "tand":
                    case "tan-delta": {
                        info.lossTangent = pv.asDoubleValue().orElse(0.02);
                        break;
                    }
                    case "geometry": {
                        info.geometryId = pv.asStringValue().orNull();
                        break;
                    }
                }
            }
        }

        if (info.xmlPath == null && info.geometryId == null) {
            info.geometryId = NTxMwSimulationUtils.findSceneGeometryId(args);
        }

        if (info.xmlPath == null && info.geometryId != null) {
            String finalGeometryId = info.geometryId;
            NTxNode scene3D = context.findNodeByProperty("name",
                    e -> e.isAnyStringOrName() && e.asStringValue().get().equals(finalGeometryId)
            ).orNull();

            if (scene3D != null && Objects.equals(scene3D.type(), "scene3d")) {
                parseScene3D(scene3D, context, info);
            } else {
                context.log(NMsg.ofC("OpenEMS: 'geometry' %s could not be resolved in the current scope", finalGeometryId).asError());
            }
        }
        if (info.portType.equals("msl")) {
            if (!info.infiniteGround) {
                // over a finite ground sheet MSLPort read the 50 ohm feed as a different line (Z0, eps_eff off)
                throw new IllegalArgumentException("OpenEMS: port type msl needs an infinite ground: tag the ground box @(infinite)");
            }
            if (info.portDeembed > 0) {
                throw new IllegalArgumentException("OpenEMS: port deembed applies to the lumped port only; the msl port de-embeds itself");
            }
            if (info.xmlPath != null) {
                throw new IllegalArgumentException("OpenEMS: port type msl needs the geometry, not an xml file");
            }
        }

        return info;
    }

    private static void parseMesh(NElement mesh, NTxResolutionContext context, OpenEMSModelInfo info) {
        if (!mesh.isAnyObject()) {
            throw new IllegalArgumentException("OpenEMS: 'mesh' expects an object, as in mesh:{refinement:3}");
        }
        for (NElement c : mesh.asObject().get().children()) {
            if (!c.isNamedPair()) {
                continue;
            }
            NPairElement p = c.asNamedPair().get();
            String key = NTxUtils.uid(p.key().asStringValue().orElse(""));
            NElement v = context.evalExpression(p.value()).orElse(p.value());
            switch (key) {
                case "refinement": {
                    info.meshRefinement = Math.max(1, v.asIntValue().orElse(info.meshRefinement));
                    break;
                }
                case "substrate-cells": {
                    info.meshSubstrateCells = Math.max(1, v.asIntValue().orElse(info.meshSubstrateCells));
                    break;
                }
                case "max-cell-x": {
                    info.meshMaxCellX = meshLengthMm(v, key);
                    break;
                }
                case "max-cell-y": {
                    info.meshMaxCellY = meshLengthMm(v, key);
                    break;
                }
                case "max-cell-z": {
                    info.meshMaxCellZ = meshLengthMm(v, key);
                    break;
                }
                case "max-cell-outside": {
                    info.meshMaxCellOutside = meshLengthMm(v, key);
                    break;
                }
                case "min-cell": {
                    info.meshMinCell = meshLengthMm(v, key);
                    break;
                }
                case "padding": {
                    info.meshPadding = meshLengthMm(v, key);
                    break;
                }
                case "air-height": {
                    info.meshAirHeight = meshLengthMm(v, key);
                    break;
                }
                case "air-below": {
                    info.meshAirBelow = meshLengthMm(v, key);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("OpenEMS: unknown mesh option '" + key + "' (expected refinement, substrate-cells,"
                            + " max-cell-x, max-cell-y, max-cell-z, max-cell-outside, min-cell, padding, air-height or air-below)");
                }
            }
        }
    }

    private static void parseBoundary(NElement b, NTxResolutionContext context, OpenEMSModelInfo info) {
        if (!b.isAnyObject()) {
            throw new IllegalArgumentException("OpenEMS: 'boundary' expects an object, as in boundary:{pml:8}");
        }
        for (NElement c : b.asObject().get().children()) {
            if (!c.isNamedPair()) {
                continue;
            }
            NPairElement p = c.asNamedPair().get();
            String key = NTxUtils.uid(p.key().asStringValue().orElse(""));
            NElement v = context.evalExpression(p.value()).orElse(p.value());
            if (!"pml".equals(key)) {
                throw new IllegalArgumentException("OpenEMS: unknown boundary option '" + key + "' (expected pml)");
            }
            int n = v.asIntValue().orElse(0);
            if (n < 4 || n > 20) {
                throw new IllegalArgumentException("OpenEMS: boundary option 'pml' expects 4 to 20 cells, as in boundary:{pml:8}");
            }
            info.pmlCells = n;
        }
    }

    /** openEMS's name of the absorbing boundary */
    public static String pml(OpenEMSModelInfo info) {
        return "PML_" + (info.pmlCells > 0 ? info.pmlCells : 8);
    }

    /** the default ladder, or the same ladder scaled to end at extent */
    private static double[] ladder(double[] base, double extent) {
        if (!(extent > 0)) {
            return base;
        }
        double[] out = new double[base.length];
        for (int i = 0; i < base.length; i++) {
            out[i] = base[i] * extent / base[base.length - 1];
        }
        return out;
    }

    private static void parsePort(NElement port, NTxResolutionContext context, OpenEMSModelInfo info) {
        if (!port.isAnyObject()) {
            throw new IllegalArgumentException("OpenEMS: 'port' expects an object, as in port:{deembed:15mm}");
        }
        for (NElement c : port.asObject().get().children()) {
            if (!c.isNamedPair()) {
                continue;
            }
            NPairElement p = c.asNamedPair().get();
            String key = NTxUtils.uid(p.key().asStringValue().orElse(""));
            NElement v = context.evalExpression(p.value()).orElse(p.value());
            switch (key) {
                case "deembed": {
                    info.portDeembed = meshLengthMm(v, key);
                    break;
                }
                case "type": {
                    String t = NNameFormat.LOWER_KEBAB_CASE.format(v.asStringValue().orElse(""));
                    if (!t.equals("lumped") && !t.equals("msl")) {
                        throw new IllegalArgumentException("OpenEMS: port type '" + t + "' is not supported, use lumped or msl");
                    }
                    info.portType = t;
                    break;
                }
                case "measure-before": {
                    info.portMeasureBefore = meshLengthMm(v, key);
                    break;
                }
                case "samples": {
                    info.portSamples = v.asIntValue().orElse(0);
                    if (info.portSamples < 2) {
                        throw new IllegalArgumentException("OpenEMS: port option 'samples' expects an integer >= 2, as in port:{type:msl, samples:2401}");
                    }
                    break;
                }
                default: {
                    throw new IllegalArgumentException("OpenEMS: unknown port option '" + key
                            + "' (expected deembed, type, measure-before or samples)");
                }
            }
        }
    }

    /**
     * Probe layout for a de-embedded port, in mm. s runs along the feed from the port towards
     * the antenna: three V probes at c - dx, c, c + dx, two I probes at c -+ dx/2, and the
     * reference plane at s = refDistance from c.
     */
    public static class DeembedProbes {
        public boolean alongX;
        public double sign;
        public double c;
        public double dx;
        public double refDistance;
        public double w1, w2, wMid;
        public double zGround, zStrip, zStripTop;
        public double iMargin;

        public double at(double s) {
            return c + sign * s;
        }
    }

    public static DeembedProbes deembedProbes(OpenEMSModelInfo info) {
        if (info.portDeembed <= 0 || info.sourceBoxes.isEmpty() || info.substrateBoxes.isEmpty()) {
            return null;
        }
        OpenEMSBox src = info.sourceBoxes.get(0);
        OpenEMSBox sub = info.substrateBoxes.get(0);
        DeembedProbes p = new DeembedProbes();
        // the port is thin along the feed and spans the strip across it
        p.alongX = (src.x2 - src.x1) < (src.y2 - src.y1);
        double u1 = p.alongX ? src.x1 : src.y1;
        double u2 = p.alongX ? src.x2 : src.y2;
        double antLo = Double.MAX_VALUE, antHi = -Double.MAX_VALUE;
        double top = src.z2;
        for (OpenEMSBox b : info.antennaBoxes) {
            antLo = Math.min(antLo, p.alongX ? b.x1 : b.y1);
            antHi = Math.max(antHi, p.alongX ? b.x2 : b.y2);
            top = Math.max(top, b.z2);
        }
        p.sign = (antLo + antHi) / 2.0 >= (u1 + u2) / 2.0 ? 1.0 : -1.0;
        double outer = p.sign > 0 ? u1 : u2;
        double inner = p.sign > 0 ? u2 : u1;
        double span = info.portDeembed - (u2 - u1);
        if (span <= 0) {
            throw new IllegalArgumentException("OpenEMS: port deembed " + info.portDeembed + " mm does not reach past the port box");
        }
        // probes sit past the port's near field and short of the reference plane
        p.c = inner + p.sign * 0.6 * span;
        p.dx = Math.min(2.0, 0.15 * span);
        p.refDistance = (outer + p.sign * info.portDeembed - p.c) * p.sign;
        p.w1 = p.alongX ? src.y1 : src.x1;
        p.w2 = p.alongX ? src.y2 : src.x2;
        p.wMid = (p.w1 + p.w2) / 2.0;
        p.zGround = sub.zMin();
        p.zStrip = sub.zMax();
        p.zStripTop = top;
        p.iMargin = (sub.zMax() - sub.zMin()) / 2.0;
        return p;
    }

    private static void appendDeembedProbes(StringBuilder sb, DeembedProbes p) {
        double[] vs = {-p.dx, 0, p.dx};
        for (int k = 0; k < vs.length; k++) {
            double u = p.at(vs[k]);
            double x = p.alongX ? u : p.wMid;
            double y = p.alongX ? p.wMid : u;
            sb.append(String.format(Locale.US, "      <ProbeBox Name=\"port1_V%d\" Type=\"0\" Weight=\"-1\">\n", k + 1));
            sb.append("        <Primitives>\n");
            sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    x, y, p.zGround, x, y, p.zStrip));
            sb.append("        </Primitives>\n");
            sb.append("      </ProbeBox>\n\n");
        }
        double[] is = {-p.dx / 2.0, p.dx / 2.0};
        for (int k = 0; k < is.length; k++) {
            double u = p.at(is[k]);
            double a1 = p.w1 - p.iMargin, a2 = p.w2 + p.iMargin;
            double z1 = p.zStrip - p.iMargin, z2 = p.zStripTop + p.iMargin;
            // a plane across the feed: H around it is the strip current, positive along +axis
            sb.append(String.format(Locale.US, "      <ProbeBox Name=\"port1_I%d\" Type=\"1\" Weight=\"%.0f\">\n", k + 1, p.sign));
            sb.append("        <Primitives>\n");
            if (p.alongX) {
                sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                        u, a1, z1, u, a2, z2));
            } else {
                sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                        a1, u, z1, a2, u, z2));
            }
            sb.append("        </Primitives>\n");
            sb.append("      </ProbeBox>\n\n");
        }
    }

    private static double meshLengthMm(NElement value, String key) {
        double metres = NTxNumberUtils.toMeter(value).orElse(0.0);
        if (metres <= 0) {
            throw new IllegalArgumentException("OpenEMS: mesh option '" + key + "' expects a positive length, as in 0.75mm");
        }
        return metres * 1e3;
    }

    private static void parseScene3D(NTxNode scene3D, NTxResolutionContext context, OpenEMSModelInfo info) {
        NTxNumberElement3 sceneSize = NTxMwSimulationUtils.findSceneSize(scene3D, context).orDefault();
        NTxNumberElement3 scenePosition = NTxMwSimulationUtils.findScenePosition(scene3D, context).orDefault();

        boolean hasExplicitAntenna = false;
        for (NTxNode child : scene3D.children()) {
            if (NTxMwSimulationUtils.isSimulationNode(child, "antenna")
                    || NTxMwSimulationUtils.isSimulationNode(child, "patch")
                    || NTxMwSimulationUtils.isSimulationNode(child, "feed")
                    || NTxMwSimulationUtils.isSimulationNode(child, "feedline")
                    || NTxMwSimulationUtils.isSimulationNode(child, "left-flank")
                    || NTxMwSimulationUtils.isSimulationNode(child, "right-flank")) {
                hasExplicitAntenna = true;
                break;
            }
        }

        for (NTxNode child : scene3D.children()) {
            String nodeType = child.type();
            String name = child.getName() == null ? "" : child.getName().trim();

            boolean isGround = NTxMwSimulationUtils.isSimulationNode(child, "ground");
            boolean isSubstrate = NTxMwSimulationUtils.isSimulationNode(child, "substrate");
            boolean isAntenna = NTxMwSimulationUtils.isSimulationNode(child, "antenna")
                    || NTxMwSimulationUtils.isSimulationNode(child, "patch")
                    || NTxMwSimulationUtils.isSimulationNode(child, "feed")
                    || NTxMwSimulationUtils.isSimulationNode(child, "feedline")
                    || NTxMwSimulationUtils.isSimulationNode(child, "left-flank")
                    || NTxMwSimulationUtils.isSimulationNode(child, "right-flank");
            boolean isSource = NTxMwSimulationUtils.isSimulationNode(child, "source");

            if ("box".equalsIgnoreCase(nodeType)) {
                NElement s = child.getPropertyValue("size").orNull();
                NElement p = child.getPropertyValue("position").orNull();
                if (s != null && p != null) {
                    NTxNumberElement3 ss = NTx3DUtils.resolveSize3DSI(context.evalExpression(s).orNull(), context);
                    NTxNumberElement3 pp = NTx3DUtils.resolveSize3DSI(context.evalExpression(p).orNull(), context);
                    if (ss != null && pp != null) {
                        double x1 = pp.x.asDoubleValue().orElse(0.0) * 1000.0;
                        double y1 = pp.y.asDoubleValue().orElse(0.0) * 1000.0;
                        double z1 = pp.z.asDoubleValue().orElse(0.0) * 1000.0;
                        double xw = ss.x.asDoubleValue().orElse(0.0) * 1000.0;
                        double yw = ss.y.asDoubleValue().orElse(0.0) * 1000.0;
                        double zw = ss.z.asDoubleValue().orElse(0.0) * 1000.0;
                        double x2 = x1 + xw;
                        double y2 = y1 + yw;
                        double z2 = z1 + zw;

                        OpenEMSBox box = new OpenEMSBox(x1, y1, z1, x2, y2, z2, name, nodeType);
                        if (isGround) {
                            info.groundBoxes.add(box);
                            if (NTxMwSimulationUtils.hasStyleClass(child, "infinite")) {
                                info.infiniteGround = true;
                            }
                        } else if (isSubstrate || (!isAntenna && !isSource && z2 <= 0 && zw > 0.1)) {
                            info.substrateBoxes.add(box);
                        } else if (isSource) {
                            info.sourceBoxes.add(box);
                        } else if (isAntenna || (!hasExplicitAntenna && !isGround && !isSubstrate && z1 >= 0)) {
                            info.antennaBoxes.add(box);
                        }
                    }
                }
            } else if ("polygon".equalsIgnoreCase(nodeType)) {
                NOptional<NTxProp> points = child.getProperty(NTxPropName.POINTS);
                if (points.isPresent()) {
                    NOptional<NElement> pointsEv = context.evalExpression(points.get().getValue());
                    if (pointsEv.isPresent() && pointsEv.get().isArray()) {
                        NArrayElement arr = pointsEv.get().asArray().get();
                        double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
                        double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
                        double minZ = Double.POSITIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
                        for (NElement ptElem : arr.children()) {
                            NTxNumberElement3 e = NTx3DUtils.resolveSize3D(ptElem, context);
                            if (e != null) {
                                double px = e.x.asDoubleValue().orElse(0.0) * 1000.0;
                                double py = e.y.asDoubleValue().orElse(0.0) * 1000.0;
                                double pz = e.z.asDoubleValue().orElse(0.0) * 1000.0;
                                minX = Math.min(minX, px); maxX = Math.max(maxX, px);
                                minY = Math.min(minY, py); maxY = Math.max(maxY, py);
                                minZ = Math.min(minZ, pz); maxZ = Math.max(maxZ, pz);
                            }
                        }
                        if (minX <= maxX && minY <= maxY) {
                            if (minZ == maxZ) {
                                maxZ = minZ + 0.035;
                            }
                            OpenEMSBox box = new OpenEMSBox(minX, minY, minZ, maxX, maxY, maxZ, name, nodeType);
                            if (isGround) {
                                info.groundBoxes.add(box);
                            } else if (isSubstrate) {
                                info.substrateBoxes.add(box);
                            } else if (isSource) {
                                info.sourceBoxes.add(box);
                            } else if (isAntenna || (!hasExplicitAntenna && !isGround && !isSubstrate && minZ >= 0)) {
                                info.antennaBoxes.add(box);
                            }
                        }
                    }
                }
            }
        }

        if (info.groundBoxes.isEmpty() && !info.substrateBoxes.isEmpty()) {
            OpenEMSBox sub = info.substrateBoxes.get(0);
            info.groundBoxes.add(new OpenEMSBox(sub.x1, sub.y1, sub.z1 - 0.035, sub.x2, sub.y2, sub.z1, "ground", "ground"));
        }

        if (info.sourceBoxes.isEmpty() && !info.antennaBoxes.isEmpty()) {
            OpenEMSBox ant = info.antennaBoxes.get(0);
            double portYlen = Math.min(2.0, (ant.y2 - ant.y1) * 0.1);
            if (portYlen <= 0) portYlen = 2.0;
            info.sourceBoxes.add(new OpenEMSBox(ant.x1, ant.y1, ant.z1, ant.x2, ant.y1 + portYlen, ant.z2, "source", "source"));
        }
    }

    public static String generateOpenEMSXml(OpenEMSModelInfo info) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<openEMS>\n\n");

        sb.append(String.format(Locale.US, "  <FDTD NumberOfTimesteps=\"%d\" endCriteria=\"%e\">\n", info.numberOfTimesteps * info.meshRefinement, info.endCriteria));
        sb.append(String.format(Locale.US, "    <Excitation Type=\"0\" f0=\"%e\" fc=\"%e\" />\n", info.frequency, info.fc));
        String pml = pml(info);
        sb.append("    <BoundaryCond xmin=\"" + pml + "\" xmax=\"" + pml + "\" ymin=\"" + pml + "\" ymax=\"" + pml + "\" zmin=\"" + (info.infiniteGround ? "PEC" : pml) + "\" zmax=\"" + pml + "\" />\n");
        sb.append("  </FDTD>\n\n");

        sb.append("  <ContinuousStructure CoordSystem=\"0\">\n\n");

        // Rectilinear grid
        double kappa = 2 * Math.PI * info.frequency * 8.854187817e-12 * info.epsilonR * info.lossTangent;

        // Collect coordinates
        TreeSet<Double> xCoords = new TreeSet<>();
        TreeSet<Double> yCoords = new TreeSet<>();
        TreeSet<Double> zCoords = new TreeSet<>();

        List<OpenEMSBox> allBoxes = new ArrayList<>();
        allBoxes.addAll(info.groundBoxes);
        allBoxes.addAll(info.substrateBoxes);
        allBoxes.addAll(info.antennaBoxes);
        allBoxes.addAll(info.sourceBoxes);

        for (OpenEMSBox b : allBoxes) {
            xCoords.add(round3(b.x1));
            xCoords.add(round3(b.x2));
            yCoords.add(round3(b.y1));
            yCoords.add(round3(b.y2));
            zCoords.add(round3(b.z1));
            zCoords.add(round3(b.z2));
        }
        if (!info.sourceBoxes.isEmpty()) {
            OpenEMSBox src = info.sourceBoxes.get(0);
            xCoords.add(round3((src.x1 + src.x2) / 2.0));
            yCoords.add(round3((src.y1 + src.y2) / 2.0));
        }
        DeembedProbes probes = deembedProbes(info);
        if (probes != null) {
            // probes must sit on mesh lines, or openEMS snaps them to the nearest one
            TreeSet<Double> along = probes.alongX ? xCoords : yCoords;
            TreeSet<Double> across = probes.alongX ? yCoords : xCoords;
            for (double s : new double[]{-probes.dx, -probes.dx / 2.0, 0, probes.dx / 2.0, probes.dx}) {
                along.add(round3(probes.at(s)));
            }
            across.add(round3(probes.wMid));
            across.add(round3(probes.w1 - probes.iMargin));
            across.add(round3(probes.w2 + probes.iMargin));
            zCoords.add(round3(probes.zStrip - probes.iMargin));
            zCoords.add(round3(probes.zStripTop + probes.iMargin));
        }

        double antXmin = Double.MAX_VALUE, antXmax = -Double.MAX_VALUE;
        double antYmin = Double.MAX_VALUE, antYmax = -Double.MAX_VALUE;
        for (OpenEMSBox b : info.antennaBoxes) {
            antXmin = Math.min(antXmin, b.xMin());
            antXmax = Math.max(antXmax, b.xMax());
            antYmin = Math.min(antYmin, b.yMin());
            antYmax = Math.max(antYmax, b.yMax());
        }
        for (OpenEMSBox b : info.sourceBoxes) {
            antXmin = Math.min(antXmin, b.xMin());
            antXmax = Math.max(antXmax, b.xMax());
            antYmin = Math.min(antYmin, b.yMin());
            antYmax = Math.max(antYmax, b.yMax());
        }
        if (antXmin > antXmax) {
            antXmin = -15;
            antXmax = 15;
            antYmin = -15;
            antYmax = 15;
        }

        if (xCoords.isEmpty()) {
            xCoords.addAll(Arrays.asList(-30.0, 0.0, 30.0));
        }
        if (yCoords.isEmpty()) {
            yCoords.addAll(Arrays.asList(-30.0, 0.0, 30.0));
        }
        if (zCoords.isEmpty()) {
            zCoords.addAll(Arrays.asList(-1.6, 0.0, 1.0));
        }

        // Substrate bottom & top
        double subZmin = -1.6;
        double subZmax = 0.0;
        if (!info.substrateBoxes.isEmpty()) {
            subZmin = info.substrateBoxes.get(0).zMin();
            subZmax = info.substrateBoxes.get(0).zMax();
        }

        // Add intermediate Z lines inside substrate (6 cells across substrate thickness)
        int zSubSteps = info.meshSubstrateCells;
        double dzSub = (subZmax - subZmin) / (double) zSubSteps;
        for (int i = 1; i < zSubSteps; i++) {
            zCoords.add(round3(subZmin + i * dzSub));
        }

        // Margins for PML and air region
        double xmin = xCoords.first();
        double xmax = xCoords.last();
        double ymin = yCoords.first();
        double ymax = yCoords.last();
        double zmin = zCoords.first();
        double zmax = zCoords.last();

        for (double d : ladder(new double[]{5.0, 12.0, 22.0, 35.0}, info.meshPadding)) {
            xCoords.add(round3(xmin - d));
            xCoords.add(round3(xmax + d));
            yCoords.add(round3(ymin - d));
            yCoords.add(round3(ymax + d));
        }

        if (!info.infiniteGround) {
            // air below a finite board; an infinite ground is the domain's bottom face
            for (double d : ladder(new double[]{5.0, 12.0, 22.0}, info.meshAirBelow)) {
                zCoords.add(round3(zmin - d));
            }
        }
        for (double d : ladder(new double[]{3.0, 8.0, 16.0, 28.0, 42.0}, info.meshAirHeight)) {
            zCoords.add(round3(zmax + d));
        }

        List<Double> finalX = subdivideGrid(refineGrid(xCoords, info.meshMinCell, info.meshMaxCellX, info.meshMaxCellOutside, antXmin - 0.5, antXmax + 0.5), info.meshRefinement);
        List<Double> finalY = subdivideGrid(refineGrid(yCoords, info.meshMinCell, info.meshMaxCellY, info.meshMaxCellOutside, antYmin - 0.5, antYmax + 0.5), info.meshRefinement);
        List<Double> finalZ = subdivideGrid(refineGrid(zCoords, info.meshMinCell, info.meshMaxCellZ, info.meshMaxCellOutside, 0, 0), info.meshRefinement);

        sb.append("    <RectilinearGrid DeltaUnit=\"1e-3\">\n");
        sb.append("      <XLines>").append(formatLines(finalX)).append("</XLines>\n");
        sb.append("      <YLines>").append(formatLines(finalY)).append("</YLines>\n");
        sb.append("      <ZLines>").append(formatLines(finalZ)).append("</ZLines>\n");
        sb.append("    </RectilinearGrid>\n\n");

        sb.append("    <Properties>\n\n");

        // Ground
        sb.append("      <Metal Name=\"ground\">\n");
        sb.append("        <Primitives>\n");
        for (OpenEMSBox g0 : info.groundBoxes) {
            OpenEMSBox g = info.infiniteGround ? toDomainEdges(g0, finalX, finalY) : g0;
            sb.append(String.format(Locale.US, "          <Box Priority=\"10\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    g.x1, g.y1, g.z1, g.x2, g.y2, g.z2));
        }
        sb.append("        </Primitives>\n");
        sb.append("      </Metal>\n\n");

        // Substrate
        sb.append("      <Material Name=\"substrate\">\n");
        sb.append(String.format(Locale.US, "        <Property Epsilon=\"%.3f\" Kappa=\"%e\" />\n", info.epsilonR, kappa));
        sb.append("        <Primitives>\n");
        for (OpenEMSBox s0 : info.substrateBoxes) {
            OpenEMSBox s = info.infiniteGround ? toDomainEdges(s0, finalX, finalY) : s0;
            sb.append(String.format(Locale.US, "          <Box Priority=\"1\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    s.x1, s.y1, s.z1, s.x2, s.y2, s.z2));
        }
        sb.append("        </Primitives>\n");
        sb.append("      </Material>\n\n");

        // Antenna
        sb.append("      <Metal Name=\"antenna\">\n");
        sb.append("        <Primitives>\n");
        for (OpenEMSBox a : info.antennaBoxes) {
            sb.append(String.format(Locale.US, "          <Box Priority=\"10\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    a.x1, a.y1, a.z1, a.x2, a.y2, a.z2));
        }
        sb.append("        </Primitives>\n");
        sb.append("      </Metal>\n\n");

        // Source / Lumped Port (an msl port is added by openEMS's python interface, see msl_driver.py)
        if (!info.sourceBoxes.isEmpty() && info.portType.equals("lumped")) {
            OpenEMSBox src = info.sourceBoxes.get(0);
            double portZ1 = subZmin;
            double portZ2 = subZmax;
            double xmid = (src.x1 + src.x2) / 2.0;
            double ymid = (src.y1 + src.y2) / 2.0;
            double zmid = (portZ1 + portZ2) / 2.0;

            // Excitation
            sb.append("      <Excitation Name=\"port1_exc\" Type=\"0\" Excite=\"0,0,-1\">\n");
            sb.append("        <Primitives>\n");
            sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    src.x1, src.y1, portZ1, src.x2, src.y2, portZ2));
            sb.append("        </Primitives>\n");
            sb.append("      </Excitation>\n\n");

            // Lumped 50-ohm resistor
            String r = info.z0Ref != 50.0 ? String.format(Locale.US, "%.6g", info.z0Ref) : "50";
            sb.append("      <LumpedElement Name=\"port1_R\" R=\"" + r + "\" Direction=\"2\" Caps=\"1\">\n");
            sb.append("        <Primitives>\n");
            sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    src.x1, src.y1, portZ1, src.x2, src.y2, portZ2));
            sb.append("        </Primitives>\n");
            sb.append("      </LumpedElement>\n\n");

            // Voltage probe (line along Z through port center)
            sb.append("      <ProbeBox Name=\"port1_V\" Type=\"0\" Weight=\"-1\">\n");
            sb.append("        <Primitives>\n");
            sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    xmid, ymid, portZ1, xmid, ymid, portZ2));
            sb.append("        </Primitives>\n");
            sb.append("      </ProbeBox>\n\n");

            // Current probe (horizontal plane perpendicular to Z at zmid)
            sb.append("      <ProbeBox Name=\"port1_I\" Type=\"1\" Weight=\"1\">\n");
            sb.append("        <Primitives>\n");
            sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    src.x1, src.y1, zmid, src.x2, src.y2, zmid));
            sb.append("        </Primitives>\n");
            sb.append("      </ProbeBox>\n\n");
        }
        if (probes != null) {
            appendDeembedProbes(sb, probes);
        }

        sb.append("    </Properties>\n");
        sb.append("  </ContinuousStructure>\n\n");
        sb.append("</openEMS>\n");

        return sb.toString();
    }

    private static OpenEMSBox toDomainEdges(OpenEMSBox b, List<Double> xs, List<Double> ys) {
        return new OpenEMSBox(xs.get(0), ys.get(0), b.z1, xs.get(xs.size() - 1), ys.get(ys.size() - 1), b.z2, b.name, b.type);
    }

    /**
     * The geometry and mesh alone (the ContinuousStructure element), for openEMS's python
     * interface to read with CSX.ReadFromXML.
     */
    public static String generateGeometryXml(OpenEMSModelInfo info) {
        String all = generateOpenEMSXml(info);
        int a = all.indexOf("<ContinuousStructure");
        int b = all.indexOf("</ContinuousStructure>") + "</ContinuousStructure>".length();
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + all.substring(a, b) + "\n";
    }

    /**
     * Where the msl port goes, in mm: the feed runs along x (alongX) or y; the port strip enters
     * from the domain edge on the side away from the antenna and ends at the reference plane, the
     * source box's outer edge, spanning the source box across the feed.
     */
    public static Map<String, Object> mslPortLayout(OpenEMSModelInfo info) {
        if (info.sourceBoxes.isEmpty() || info.substrateBoxes.isEmpty()) {
            throw new IllegalArgumentException("OpenEMS: port type msl needs a source box and a substrate box");
        }
        OpenEMSBox src = info.sourceBoxes.get(0);
        OpenEMSBox sub = info.substrateBoxes.get(0);
        boolean alongX = (src.x2 - src.x1) < (src.y2 - src.y1);
        double u1 = alongX ? src.x1 : src.y1;
        double u2 = alongX ? src.x2 : src.y2;
        double antLo = Double.MAX_VALUE, antHi = -Double.MAX_VALUE;
        for (OpenEMSBox b : info.antennaBoxes) {
            antLo = Math.min(antLo, alongX ? b.x1 : b.y1);
            antHi = Math.max(antHi, alongX ? b.x2 : b.y2);
        }
        double sign = (antLo + antHi) / 2.0 >= (u1 + u2) / 2.0 ? 1.0 : -1.0;
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("prop_dir", alongX ? "x" : "y");
        m.put("exc_dir", "z");
        m.put("sign", sign);
        m.put("reference", sign > 0 ? u1 : u2);
        m.put("w1", alongX ? src.y1 : src.x1);
        m.put("w2", alongX ? src.y2 : src.x2);
        m.put("z_strip", sub.zMax());
        m.put("z_ground", sub.zMin());
        m.put("measure_before", info.portMeasureBefore);
        return m;
    }

    private static List<Double> subdivideGrid(List<Double> lines, int factor) {
        List<Double> out = new ArrayList<>();
        for (int i = 0; i + 1 < lines.size(); i++) {
            for (int k = 0; k < factor; k++) {
                out.add(lines.get(i) + (lines.get(i + 1) - lines.get(i)) * k / factor);
            }
        }
        if (!lines.isEmpty()) {
            out.add(lines.get(lines.size() - 1));
        }
        return out;
    }

    private static List<Double> refineGrid(TreeSet<Double> sortedCoords, double minAllowedStep, double maxStepInside, double maxStepOutside, double regionMin, double regionMax) {
        List<Double> raw = new ArrayList<>(sortedCoords);
        List<Double> clean = new ArrayList<>();
        if (raw.isEmpty()) return clean;

        // 1. Collapse/filter points that are closer than minAllowedStep to prevent Courant collapse
        for (double c : raw) {
            if (clean.isEmpty()) {
                clean.add(c);
            } else {
                double prev = clean.get(clean.size() - 1);
                if (c - prev >= minAllowedStep) {
                    clean.add(c);
                }
            }
        }

        // 2. Refine intervals
        List<Double> output = new ArrayList<>();
        for (int i = 0; i < clean.size() - 1; i++) {
            double c1 = clean.get(i);
            double c2 = clean.get(i + 1);
            output.add(c1);
            double diff = c2 - c1;
            boolean inside = (c1 < regionMax && c2 > regionMin);
            double maxStep = inside ? maxStepInside : maxStepOutside;
            if (diff > maxStep) {
                int segments = (int) Math.ceil(diff / maxStep);
                double step = diff / segments;
                for (int s = 1; s < segments; s++) {
                    output.add(round3(c1 + s * step));
                }
            }
        }
        output.add(clean.get(clean.size() - 1));
        return output;
    }

    private static String formatLines(List<Double> lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) sb.append(",");
            double val = lines.get(i);
            if (val == Math.floor(val)) {
                sb.append(String.format(Locale.US, "%.0f", val));
            } else {
                sb.append(String.format(Locale.US, "%.5f", val));
            }
        }
        return sb.toString();
    }

    private static double round3(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }

    public static double[][] readProbeData(NPath file) throws IOException {
        String content = file.readString();
        String[] lines = content.split("\\R");
        List<Double> tList = new ArrayList<>();
        List<Double> vList = new ArrayList<>();
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("%")) {
                continue;
            }
            String[] parts = line.split("\\s+");
            if (parts.length >= 2) {
                try {
                    tList.add(Double.parseDouble(parts[0]));
                    vList.add(Double.parseDouble(parts[1]));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        double[] t = new double[tList.size()];
        double[] val = new double[vList.size()];
        for (int i = 0; i < t.length; i++) {
            t[i] = tList.get(i);
            val[i] = vList.get(i);
        }
        return new double[][]{t, val};
    }
}
