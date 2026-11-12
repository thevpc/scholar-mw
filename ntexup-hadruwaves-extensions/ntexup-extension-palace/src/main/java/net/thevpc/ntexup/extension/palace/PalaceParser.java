package net.thevpc.ntexup.extension.palace;

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
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;

import java.util.Objects;

/**
 * Reads a palace-solver call:
 * <pre>
 * palace-solver(
 *     geometry:"antenna-patch", frequency:f, epsilonr:εr, losstangent:tanδ,
 *     port:{ type:wave, extension:20mm, mode:1 },
 *     mesh:{ metal:0.4mm, substrate:1.5mm, air:3.5mm, order:2, padding:33mm, air-height:15mm },
 *     adaptive-tol:1e-3, processes:16, linear-solver:ams,
 *     results:{ allS11:S11(frequency:{from:6.9GHz, to:9.8GHz, count:81}) }
 * )
 * </pre>
 * The ground box must be tagged @(infinite): the wave port is a face of the domain, which the
 * substrate and ground must cross. A finite board is refused, not substituted.
 */
public class PalaceParser {

    public static PalaceModelInfo parse(NTxFunctionCallContext args) {
        PalaceModelInfo info = new PalaceModelInfo();
        NTxResolutionContext context = args.scopedContext();
        for (NTxFunctionArg arg : args.args()) {
            NElement a = arg.eval();
            if (!a.isNamedPair()) {
                continue;
            }
            NPairElement p = a.asNamedPair().get();
            String key = NTxUtils.uid(p.key().asStringValue().orElse(""));
            NElement v = context.evalExpression(p.value()).orElse(p.value());
            switch (key) {
                case "freq":
                case "frequency": {
                    NOptional<Double> d = v.asNumber().flatMap(NTxNumberUtils::toHertz);
                    if (d.isPresent()) {
                        info.frequency = d.get();
                    }
                    break;
                }
                case "epsilonr":
                case "permittivity": {
                    info.epsilonR = v.asDoubleValue().orElse(info.epsilonR);
                    break;
                }
                case "losstangent":
                case "loss-tangent":
                case "tand":
                case "tan-delta": {
                    info.lossTangent = v.asDoubleValue().orElse(info.lossTangent);
                    break;
                }
                case "geometry": {
                    info.geometryId = v.asStringValue().orNull();
                    break;
                }
                case "port": {
                    parsePort(v, context, info);
                    break;
                }
                case "mesh": {
                    parseMesh(v, context, info);
                    break;
                }
                case "adaptive-tol": {
                    info.adaptiveTol = v.asDoubleValue().orElse(info.adaptiveTol);
                    break;
                }
                case "processes":
                case "threads": {
                    info.processes = Math.max(1, v.asIntValue().orElse(info.processes));
                    break;
                }
                case "linear-solver": {
                    String t = NNameFormat.LOWER_KEBAB_CASE.format(v.asStringValue().orElse(""));
                    switch (t) {
                        case "ams": info.linearSolver = "AMS"; break;
                        case "superlu": case "super-lu": info.linearSolver = "SuperLU"; break;
                        case "mumps": info.linearSolver = "MUMPS"; break;
                        case "strumpack": info.linearSolver = "STRUMPACK"; break;
                        case "default": info.linearSolver = "Default"; break;
                        default:
                            throw new IllegalArgumentException("Palace: 'linear-solver' expects ams, superlu, mumps, strumpack or default");
                    }
                    break;
                }
            }
        }
        if (info.geometryId == null) {
            info.geometryId = NTxMwSimulationUtils.findSceneGeometryId(args);
        }
        if (info.geometryId != null) {
            String id = info.geometryId;
            NTxNode scene3D = context.findNodeByProperty("name",
                    e -> e.isAnyStringOrName() && e.asStringValue().get().equals(id)
            ).orNull();
            if (scene3D != null && Objects.equals(scene3D.type(), "scene3d")) {
                parseScene3D(scene3D, context, info);
            } else {
                context.log(NMsg.ofC("Palace: 'geometry' %s could not be resolved in the current scope", id).asError());
            }
        }
        if (!info.infiniteGround) {
            throw new IllegalArgumentException("Palace: the wave port needs an infinite ground: tag the ground box @(infinite)");
        }
        if (info.substrateBoxes.isEmpty() || info.antennaBoxes.isEmpty() || info.sourceBoxes.isEmpty()) {
            throw new IllegalArgumentException("Palace: the geometry needs a substrate, antenna metal and a source box (the port's reference plane)");
        }
        return info;
    }

    private static void parsePort(NElement port, NTxResolutionContext context, PalaceModelInfo info) {
        if (!port.isAnyObject()) {
            throw new IllegalArgumentException("Palace: 'port' expects an object, as in port:{type:wave}");
        }
        for (NElement c : port.asObject().get().children()) {
            if (!c.isNamedPair()) {
                continue;
            }
            NPairElement p = c.asNamedPair().get();
            String key = NTxUtils.uid(p.key().asStringValue().orElse(""));
            NElement v = context.evalExpression(p.value()).orElse(p.value());
            switch (key) {
                case "type": {
                    String t = NNameFormat.LOWER_KEBAB_CASE.format(v.asStringValue().orElse(""));
                    if (!t.equals("wave")) {
                        throw new IllegalArgumentException("Palace: port type '" + t + "' is not supported, use wave");
                    }
                    break;
                }
                case "extension": {
                    info.portExtension = lengthMm(v, key);
                    break;
                }
                case "mode": {
                    info.portMode = Math.max(1, v.asIntValue().orElse(info.portMode));
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Palace: unknown port option '" + key + "'");
                }
            }
        }
    }

    private static void parseMesh(NElement mesh, NTxResolutionContext context, PalaceModelInfo info) {
        if (!mesh.isAnyObject()) {
            throw new IllegalArgumentException("Palace: 'mesh' expects an object, as in mesh:{metal:0.4mm}");
        }
        for (NElement c : mesh.asObject().get().children()) {
            if (!c.isNamedPair()) {
                continue;
            }
            NPairElement p = c.asNamedPair().get();
            String key = NTxUtils.uid(p.key().asStringValue().orElse(""));
            NElement v = context.evalExpression(p.value()).orElse(p.value());
            switch (key) {
                case "metal": {
                    info.meshMetal = lengthMm(v, key);
                    break;
                }
                case "substrate": {
                    info.meshSubstrate = lengthMm(v, key);
                    break;
                }
                case "air": {
                    info.meshAir = lengthMm(v, key);
                    break;
                }
                case "order": {
                    info.order = Math.max(1, v.asIntValue().orElse(info.order));
                    break;
                }
                case "padding": {
                    info.padding = lengthMm(v, key);
                    break;
                }
                case "air-height": {
                    info.airHeight = lengthMm(v, key);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Palace: unknown mesh option '" + key + "'");
                }
            }
        }
    }

    private static double lengthMm(NElement value, String key) {
        double metres = NTxNumberUtils.toMeter(value).orElse(0.0);
        if (metres <= 0) {
            throw new IllegalArgumentException("Palace: option '" + key + "' expects a positive length, as in 0.4mm");
        }
        return metres * 1e3;
    }

    private static void parseScene3D(NTxNode scene3D, NTxResolutionContext context, PalaceModelInfo info) {
        for (NTxNode child : scene3D.children()) {
            if (!"box".equalsIgnoreCase(child.type())) {
                continue;
            }
            NElement s = child.getPropertyValue("size").orNull();
            NElement p = child.getPropertyValue("position").orNull();
            if (s == null || p == null) {
                continue;
            }
            NTxNumberElement3 ss = NTx3DUtils.resolveSize3DSI(context.evalExpression(s).orNull(), context);
            NTxNumberElement3 pp = NTx3DUtils.resolveSize3DSI(context.evalExpression(p).orNull(), context);
            if (ss == null || pp == null) {
                continue;
            }
            double x1 = pp.x.asDoubleValue().orElse(0.0) * 1000.0;
            double y1 = pp.y.asDoubleValue().orElse(0.0) * 1000.0;
            double z1 = pp.z.asDoubleValue().orElse(0.0) * 1000.0;
            PalaceModelInfo.PalaceBox box = new PalaceModelInfo.PalaceBox(x1, y1, z1,
                    x1 + ss.x.asDoubleValue().orElse(0.0) * 1000.0,
                    y1 + ss.y.asDoubleValue().orElse(0.0) * 1000.0,
                    z1 + ss.z.asDoubleValue().orElse(0.0) * 1000.0,
                    child.getName() == null ? "" : child.getName().trim());
            if (NTxMwSimulationUtils.isSimulationNode(child, "ground")) {
                info.groundBoxes.add(box);
                if (NTxMwSimulationUtils.isInfiniteGround(child)) {
                    info.infiniteGround = true;
                }
            } else if (NTxMwSimulationUtils.isSimulationNode(child, "substrate")) {
                info.substrateBoxes.add(box);
            } else if (NTxMwSimulationUtils.isSimulationNode(child, "source")) {
                info.sourceBoxes.add(box);
            } else if (NTxMwSimulationUtils.isSimulationNode(child, "antenna")) {
                info.antennaBoxes.add(box);
            }
        }
    }
}
