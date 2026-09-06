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

        return info;
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

        sb.append(String.format(Locale.US, "  <FDTD NumberOfTimesteps=\"%d\" endCriteria=\"%e\">\n", info.numberOfTimesteps, info.endCriteria));
        sb.append(String.format(Locale.US, "    <Excitation Type=\"0\" f0=\"%e\" fc=\"%e\" />\n", info.frequency, info.fc));
        sb.append("    <BoundaryCond xmin=\"PML_8\" xmax=\"PML_8\" ymin=\"PML_8\" ymax=\"PML_8\" zmin=\"PML_8\" zmax=\"PML_8\" />\n");
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
        int zSubSteps = 6;
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

        xCoords.add(round3(xmin - 5.0));
        xCoords.add(round3(xmin - 12.0));
        xCoords.add(round3(xmin - 22.0));
        xCoords.add(round3(xmin - 35.0));
        xCoords.add(round3(xmax + 5.0));
        xCoords.add(round3(xmax + 12.0));
        xCoords.add(round3(xmax + 22.0));
        xCoords.add(round3(xmax + 35.0));

        yCoords.add(round3(ymin - 5.0));
        yCoords.add(round3(ymin - 12.0));
        yCoords.add(round3(ymin - 22.0));
        yCoords.add(round3(ymin - 35.0));
        yCoords.add(round3(ymax + 5.0));
        yCoords.add(round3(ymax + 12.0));
        yCoords.add(round3(ymax + 22.0));
        yCoords.add(round3(ymax + 35.0));

        zCoords.add(round3(zmin - 5.0));
        zCoords.add(round3(zmin - 12.0));
        zCoords.add(round3(zmin - 22.0));
        zCoords.add(round3(zmax + 3.0));
        zCoords.add(round3(zmax + 8.0));
        zCoords.add(round3(zmax + 16.0));
        zCoords.add(round3(zmax + 28.0));
        zCoords.add(round3(zmax + 42.0));

        List<Double> finalX = refineGrid(xCoords, 0.20, 0.75, 3.0, antXmin - 0.5, antXmax + 0.5);
        List<Double> finalY = refineGrid(yCoords, 0.20, 1.0, 3.0, antYmin - 0.5, antYmax + 0.5);
        List<Double> finalZ = refineGrid(zCoords, 0.20, 3.0, 3.0, 0, 0);

        sb.append("    <RectilinearGrid DeltaUnit=\"1e-3\">\n");
        sb.append("      <XLines>").append(formatLines(finalX)).append("</XLines>\n");
        sb.append("      <YLines>").append(formatLines(finalY)).append("</YLines>\n");
        sb.append("      <ZLines>").append(formatLines(finalZ)).append("</ZLines>\n");
        sb.append("    </RectilinearGrid>\n\n");

        sb.append("    <Properties>\n\n");

        // Ground
        sb.append("      <Metal Name=\"ground\">\n");
        sb.append("        <Primitives>\n");
        for (OpenEMSBox g : info.groundBoxes) {
            sb.append(String.format(Locale.US, "          <Box Priority=\"10\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    g.x1, g.y1, g.z1, g.x2, g.y2, g.z2));
        }
        sb.append("        </Primitives>\n");
        sb.append("      </Metal>\n\n");

        // Substrate
        sb.append("      <Material Name=\"substrate\">\n");
        sb.append(String.format(Locale.US, "        <Property Epsilon=\"%.3f\" Kappa=\"%e\" />\n", info.epsilonR, kappa));
        sb.append("        <Primitives>\n");
        for (OpenEMSBox s : info.substrateBoxes) {
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

        // Source / Lumped Port
        if (!info.sourceBoxes.isEmpty()) {
            OpenEMSBox src = info.sourceBoxes.get(0);
            double portZ1 = subZmin;
            double portZ2 = subZmax;
            double portYmid = (src.y1 + src.y2) / 2.0;

            // Excitation
            sb.append("      <Excitation Name=\"port1_exc\" Type=\"0\" Excite=\"0,0,-1\">\n");
            sb.append("        <Primitives>\n");
            sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    src.x1, src.y1, portZ1, src.x2, src.y2, portZ2));
            sb.append("        </Primitives>\n");
            sb.append("      </Excitation>\n\n");

            // Lumped 50-ohm resistor
            sb.append("      <LumpedElement Name=\"port1_R\" R=\"50\" Direction=\"2\" Caps=\"1\">\n");
            sb.append("        <Primitives>\n");
            sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    src.x1, src.y1, portZ1, src.x2, src.y2, portZ2));
            sb.append("        </Primitives>\n");
            sb.append("      </LumpedElement>\n\n");

            // Voltage probe (line along Z)
            double xmid = (src.x1 + src.x2) / 2.0;
            sb.append("      <ProbeBox Name=\"port1_V\" Type=\"0\" Weight=\"-1\">\n");
            sb.append("        <Primitives>\n");
            sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    xmid, portYmid, portZ1, xmid, portYmid, portZ2));
            sb.append("        </Primitives>\n");
            sb.append("      </ProbeBox>\n\n");

            // Current probe (cross-section in X-Y along Z)
            double zMid = (portZ1 + portZ2) / 2.0;
            sb.append("      <ProbeBox Name=\"port1_I\" Type=\"1\" Weight=\"1\" NormDir=\"2\">\n");
            sb.append("        <Primitives>\n");
            sb.append(String.format(Locale.US, "          <Box Priority=\"100\"><P1 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /><P2 X=\"%.3f\" Y=\"%.3f\" Z=\"%.3f\" /></Box>\n",
                    src.x1, src.y1, zMid, src.x2, src.y2, zMid));
            sb.append("        </Primitives>\n");
            sb.append("      </ProbeBox>\n\n");
        }

        sb.append("    </Properties>\n");
        sb.append("  </ContinuousStructure>\n\n");
        sb.append("</openEMS>\n");

        return sb.toString();
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
                sb.append(String.format(Locale.US, "%.3f", val));
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
