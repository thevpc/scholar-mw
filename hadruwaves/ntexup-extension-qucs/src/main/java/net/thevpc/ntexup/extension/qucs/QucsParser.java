package net.thevpc.ntexup.extension.qucs;

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

public class QucsParser {

    public static QucsModelInfo parse(NTxFunctionCallContext args) {
        QucsModelInfo info = new QucsModelInfo();
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
                    case "dispmodel":
                    case "dispersion": {
                        info.dispModel = pv.asStringValue().orElse("Kirschning");
                        break;
                    }
                    case "model": {
                        info.model = pv.asStringValue().orElse("Hammerstad");
                        break;
                    }
                    case "dockerimage":
                    case "image": {
                        info.dockerImage = pv.asStringValue().orElse(QucsProvisioner.DEFAULT_DOCKER_IMAGE);
                        break;
                    }
                    case "geometry": {
                        info.geometryId = pv.asStringValue().orNull();
                        break;
                    }
                }
            }
        }

        if (info.geometryId != null) {
            String finalGeometryId = info.geometryId;
            NTxNode scene3D = context.findNodeByProperty("name",
                    e -> e.isAnyStringOrName() && e.asStringValue().get().equals(finalGeometryId)
            ).orNull();

            if (scene3D != null && Objects.equals(scene3D.type(), "scene3d")) {
                parseScene3D(scene3D, context, info);
            } else {
                context.log(NMsg.ofC("Qucs: 'geometry' %s could not be resolved in the current scope", finalGeometryId).asError());
            }
        }

        return info;
    }

    private static void parseScene3D(NTxNode scene3D, NTxResolutionContext context, QucsModelInfo info) {
        double antYmin = Double.MAX_VALUE, antYmax = -Double.MAX_VALUE;
        double antXmin = Double.MAX_VALUE, antXmax = -Double.MAX_VALUE;
        double srcYmin = Double.MAX_VALUE, srcYmax = -Double.MAX_VALUE;
        double subH = 1.6e-3;
        double condT = 0.035e-3;

        for (NTxNode child : scene3D.children()) {
            String nodeType = child.type();
            boolean isSubstrate = NTxMwSimulationUtils.isSimulationNode(child, "substrate");
            boolean isAntenna = NTxMwSimulationUtils.isSimulationNode(child, "antenna")
                    || NTxMwSimulationUtils.isSimulationNode(child, "patch");
            boolean isSource = NTxMwSimulationUtils.isSimulationNode(child, "source");

            if ("box".equalsIgnoreCase(nodeType)) {
                NElement s = child.getPropertyValue("size").orNull();
                NElement p = child.getPropertyValue("position").orNull();
                if (s != null && p != null) {
                    NTxNumberElement3 ss = NTx3DUtils.resolveSize3DSI(context.evalExpression(s).orNull(), context);
                    NTxNumberElement3 pp = NTx3DUtils.resolveSize3DSI(context.evalExpression(p).orNull(), context);
                    if (ss != null && pp != null) {
                        double px = pp.x.asDoubleValue().orElse(0.0);
                        double py = pp.y.asDoubleValue().orElse(0.0);
                        double pz = pp.z.asDoubleValue().orElse(0.0);
                        double sx = ss.x.asDoubleValue().orElse(0.0);
                        double sy = ss.y.asDoubleValue().orElse(0.0);
                        double sz = ss.z.asDoubleValue().orElse(0.0);

                        if (isSubstrate) {
                            subH = Math.abs(sz);
                        } else if (isAntenna) {
                            antXmin = Math.min(antXmin, px);
                            antXmax = Math.max(antXmax, px + sx);
                            antYmin = Math.min(antYmin, py);
                            antYmax = Math.max(antYmax, py + sy);
                            condT = Math.abs(sz);
                        } else if (isSource) {
                            srcYmin = Math.min(srcYmin, py);
                            srcYmax = Math.max(srcYmax, py + sy);
                        }
                    }
                }
            }
        }

        if (antXmax > antXmin && antYmax > antYmin) {
            info.width = antXmax - antXmin;
            info.length = antYmax - antYmin;
            info.height = subH;
            info.thickness = condT;

            if (srcYmin <= antYmin + 1e-6) {
                info.stubLength = antYmax - antYmin;
            } else if (srcYmax > srcYmin) {
                double portYmid = (srcYmin + srcYmax) / 2.0;
                info.stubLength = antYmax - portYmid;
            } else {
                info.stubLength = antYmax - antYmin;
            }
        }
    }
}
