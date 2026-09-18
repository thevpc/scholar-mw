package net.thevpc.ntexup.extension.hadruwaves.analytical;

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

public class AnalyticalParser {

    public static AnalyticalModelInfo parse(NTxFunctionCallContext args) {
        AnalyticalModelInfo info = new AnalyticalModelInfo();
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
                    case "dispersion": {
                        info.dispersion = pv.asBooleanValue().orElse(false);
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
                context.log(NMsg.ofC("Analytical: 'geometry' %s could not be resolved in the current scope", finalGeometryId).asError());
            }
        }

        return info;
    }

    public static class BoxInfo {
        public double x1, y1, x2, y2;
        public BoxInfo(double x1, double y1, double x2, double y2) {
            this.x1 = Math.min(x1, x2);
            this.y1 = Math.min(y1, y2);
            this.x2 = Math.max(x1, x2);
            this.y2 = Math.max(y1, y2);
        }
        public double width() { return x2 - x1; }
        public double length() { return y2 - y1; }
        public double xMin() { return x1; }
        public double xMax() { return x2; }
        public double yMin() { return y1; }
        public double yMax() { return y2; }
    }

    private static void parseScene3D(NTxNode scene3D, NTxResolutionContext context, AnalyticalModelInfo info) {
        java.util.List<BoxInfo> antennaBoxes = new java.util.ArrayList<>();
        java.util.List<BoxInfo> sourceBoxes = new java.util.ArrayList<>();
        double subH = 1.6e-3;

        for (NTxNode child : scene3D.children()) {
            String nodeType = child.type();
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
                        double px = pp.x.asDoubleValue().orElse(0.0);
                        double py = pp.y.asDoubleValue().orElse(0.0);
                        double pz = pp.z.asDoubleValue().orElse(0.0);
                        double sx = ss.x.asDoubleValue().orElse(0.0);
                        double sy = ss.y.asDoubleValue().orElse(0.0);
                        double sz = ss.z.asDoubleValue().orElse(0.0);

                        if (isSubstrate) {
                            subH = Math.abs(sz);
                        } else if (isAntenna) {
                            antennaBoxes.add(new BoxInfo(px, py, px + sx, py + sy));
                        } else if (isSource) {
                            sourceBoxes.add(new BoxInfo(px, py, px + sx, py + sy));
                        }
                    }
                }
            }
        }

        info.height = subH;
        if (antennaBoxes.isEmpty()) {
            return;
        }

        BoxInfo src = sourceBoxes.isEmpty() ? null : sourceBoxes.get(0);
        double portY = src != null ? src.yMin() : antennaBoxes.get(0).yMin();
        double feedW = src != null ? src.width() : 3.1e-3;

        BoxInfo feedBox = null;
        double antXmin = Double.MAX_VALUE, antXmax = -Double.MAX_VALUE;
        double antYmin = Double.MAX_VALUE, antYmax = -Double.MAX_VALUE;

        for (BoxInfo b : antennaBoxes) {
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
            feedW = feedBox.width();
            portY = src != null ? src.yMin() : feedBox.yMin();
        }
        info.feedWidth = feedW;
        info.width = antXmax - antXmin;
        info.length = antYmax - antYmin;
        info.stubLength = antYmax - portY;

        double widthThreshold = 1.25 * feedW;
        double wideXmin = Double.MAX_VALUE, wideXmax = -Double.MAX_VALUE;
        double wideYmin = Double.MAX_VALUE, wideYmax = -Double.MAX_VALUE;
        boolean hasWideSection = false;

        for (BoxInfo b : antennaBoxes) {
            if (b.width() > widthThreshold) {
                hasWideSection = true;
                wideXmin = Math.min(wideXmin, b.xMin());
                wideXmax = Math.max(wideXmax, b.xMax());
                wideYmin = Math.min(wideYmin, b.yMin());
                wideYmax = Math.max(wideYmax, b.yMax());
            }
        }

        if (hasWideSection) {
            info.isResonator = true;
            info.resW = wideXmax - wideXmin;
            info.resL = wideYmax - wideYmin;
            info.feedLength = Math.max(0.0, wideYmin - portY);

            if (feedBox != null && feedBox.yMax() > wideYmin) {
                info.insetDepth = Math.min(info.resL, feedBox.yMax() - wideYmin);
            } else {
                for (BoxInfo b : antennaBoxes) {
                    if (b != feedBox && b.width() < (info.resW * 0.45) && b.yMin() <= wideYmin + 1e-6) {
                        info.insetDepth = Math.max(info.insetDepth, b.length());
                    }
                }
            }
        } else {
            info.isResonator = false;
        }
    }
}
