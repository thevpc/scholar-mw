package net.thevpc.ntexup.extension.scuffem;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.eval.NTxFunctionArg;
import net.thevpc.ntexup.api.eval.NTxFunctionCallContext;
import net.thevpc.ntexup.api.eval.NTxResolutionContext;
import net.thevpc.ntexup.api.util.NTxNumberUtils;
import net.thevpc.ntexup.api.util.NTxUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.lib.geometry3d.NTxNumberElement3;
import net.thevpc.ntexup.lib.geometry3d.impl.NTx3DUtils;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NPairElement;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NOptional;

import java.util.Objects;

public class ScuffEMParser {

    public static ScuffEMModelInfo parse(NTxFunctionCallContext args) {
        ScuffEMModelInfo info = new ScuffEMModelInfo();
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
                    case "z0":
                    case "z":
                    case "zref": {
                        info.z0Ref = pv.asDoubleValue().orElse(50.0);
                        break;
                    }
                    case "meshresolution":
                    case "resolution": {
                        info.meshResolution = pv.asDoubleValue().orElse(2.5);
                        break;
                    }
                    case "dockerimage":
                    case "image": {
                        info.dockerImage = pv.asStringValue().orElse(ScuffEMProvisioner.DEFAULT_DOCKER_IMAGE);
                        break;
                    }
                    case "mode": {
                        info.mode = pv.asStringValue().orElse("auto").toLowerCase();
                        break;
                    }
                    case "geometry": {
                        info.geometryId = pv.asStringValue().orNull();
                        break;
                    }
                }
            }
        }

        if (info.geometryId == null) {
            info.geometryId = NTxMwSimulationUtils.findSceneGeometryId(args);
        }

        if (info.geometryId != null) {
            String finalGeometryId = info.geometryId;
            NTxNode scene3D = context.findNodeByProperty("name",
                    e -> e.isAnyStringOrName() && e.asStringValue().get().equals(finalGeometryId)
            ).orNull();

            if (scene3D != null && Objects.equals(scene3D.type(), "scene3d")) {
                info.sceneNode = scene3D;
                info.resolutionContext = context;
                parseScene3D(scene3D, context, info);
            } else {
                context.log(NMsg.ofC("SCUFF-EM: 'geometry' %s could not be resolved in the current scope", finalGeometryId).asError());
            }
        }

        return info;
    }

    private static void parseScene3D(NTxNode scene3D, NTxResolutionContext context, ScuffEMModelInfo info) {
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
                        double x1 = pp.x.asDoubleValue().orElse(0.0);
                        double y1 = pp.y.asDoubleValue().orElse(0.0);
                        double z1 = pp.z.asDoubleValue().orElse(0.0);
                        double xw = ss.x.asDoubleValue().orElse(0.0);
                        double yw = ss.y.asDoubleValue().orElse(0.0);
                        double zw = ss.z.asDoubleValue().orElse(0.0);
                        double x2 = x1 + xw;
                        double y2 = y1 + yw;
                        double z2 = z1 + zw;

                        ScuffEMModelInfo.ScuffEMBox box = new ScuffEMModelInfo.ScuffEMBox(x1, y1, z1, x2, y2, z2, name, nodeType);
                        box.isPatch = NTxMwSimulationUtils.isSimulationNode(child, "patch");
                        if (isGround) {
                            info.groundBoxes.add(box);
                        } else if (isSubstrate || (!isAntenna && !isSource && z2 <= 0 && zw > 0.0001)) {
                            info.substrateBoxes.add(box);
                        } else if (isSource) {
                            info.sourceBoxes.add(box);
                        } else if (isAntenna || (!hasExplicitAntenna && !isGround && !isSubstrate && z1 >= -1e-6)) {
                            info.antennaBoxes.add(box);
                        }
                    }
                }
            }
        }

        if (info.groundBoxes.isEmpty() && !info.substrateBoxes.isEmpty()) {
            ScuffEMModelInfo.ScuffEMBox sub = info.substrateBoxes.get(0);
            info.groundBoxes.add(new ScuffEMModelInfo.ScuffEMBox(sub.x1, sub.y1, sub.z1 - 0.035e-3, sub.x2, sub.y2, sub.z1, "ground", "ground"));
        }

        if (info.sourceBoxes.isEmpty() && !info.antennaBoxes.isEmpty()) {
            ScuffEMModelInfo.ScuffEMBox ant = info.antennaBoxes.get(0);
            double portYlen = Math.min(2e-3, (ant.y2 - ant.y1) * 0.1);
            if (portYlen <= 0) portYlen = 2e-3;
            info.sourceBoxes.add(new ScuffEMModelInfo.ScuffEMBox(ant.x1, ant.y1, ant.z1, ant.x2, ant.y1 + portYlen, ant.z2, "source", "source"));
        }
    }
}
