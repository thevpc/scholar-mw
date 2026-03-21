package net.thevpc.ntexup.extension.hadruwaves;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.document.style.NTxProp;
import net.thevpc.ntexup.api.document.style.NTxPropName;
import net.thevpc.ntexup.api.eval.NTxFunctionArg;
import net.thevpc.ntexup.api.eval.NTxFunctionCallContext;
import net.thevpc.ntexup.api.eval.NTxResolutionContext;
import net.thevpc.ntexup.api.util.NTxNumberUtils;
import net.thevpc.ntexup.api.util.NTxUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.lib.geometry3d.NTxNumberElement3;
import net.thevpc.ntexup.lib.geometry3d.impl.NTx3DUtils;
import net.thevpc.nuts.elem.*;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.nuts.util.NStringUtils;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.geom.DefaultPolygon;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.*;
import net.thevpc.scholar.hadruwaves.mom.modes.BoxModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.sources.Sources;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.ListTestFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MomParser {
    static MomStructure createMomStructure(NTxFunctionCallContext args) {
        //h will populate from node later
        MomStructure str = new MomStructure();
        str.setLog(args.scopedContext().log());
        String geometryId = null;
        NTxResolutionContext context = args.scopedContext();
        geometryId = NTxMwSimulationUtils.findSceneGeometryId(args);

        if (geometryId == null) {
            context.log().log(NMsg.ofC("missing 'geometry'").asError());
            return null;
        }
        str.setName(geometryId);
        str.setProjectType(ProjectType.PLANAR_STRUCTURE);
        String finalGeometryId = geometryId;
        NTxNode scene3D = context.findNodeByProperty("name",
                e -> e.isAnyStringOrName() && e.asStringValue().get().equals(finalGeometryId)
        ).orNull();
        if (scene3D == null || !Objects.equals(scene3D.type(), "scene3d")) {
            context.log().log(NMsg.ofC("'geometry' %s could not be resolved in the current scope", finalGeometryId).asError());
            return null;
        }
        NTxNumberElement3 sceneSize = NTxMwSimulationUtils.findSceneSize(scene3D, context).orDefault();
        NTxNumberElement3 scenePosition = NTxMwSimulationUtils.findScenePosition(scene3D, context).orDefault();

        MoMSolverQueryInfo moMSolverQueryInfo = parseMoMSolverQuery(args, scene3D, sceneSize, scenePosition);
        str.setBorders(moMSolverQueryInfo.boundaries.lateral);
        str.setFrequency(moMSolverQueryInfo.frequency);
        str.setFirstBoxSpace(moMSolverQueryInfo.boundaries.bottom);
        str.setSecondBoxSpace(moMSolverQueryInfo.boundaries.top);
        str.setModeFunctions(moMSolverQueryInfo.modes);
        str.setDomain(parseSubstrateDomain(args, scene3D));
        str.setSources(moMSolverQueryInfo.sources);
        boolean t = !isEmptyTestFunctions(moMSolverQueryInfo.testFunctions);
        boolean b = !isEmptyTestFunctions(moMSolverQueryInfo.basisFunctions);
        if (t && b) {
            context.log().log(NMsg.ofC("only galerkin is implemented for now. using basis functions and ignoring test functions").asError());
            str.setTestFunctions(moMSolverQueryInfo.basisFunctions);
        } else if (t) {
            str.setTestFunctions(moMSolverQueryInfo.testFunctions);
        } else if (b) {
            str.setTestFunctions(moMSolverQueryInfo.basisFunctions);
        } else {
            Geometry geometry = parseDefaultGeometry(args, scene3D, sceneSize, scenePosition);
            if (geometry != null) {
                str.setTestFunctions(TestFunctionsFactory.createRWG(geometry, 100));
            } else {
                context.log().log(NMsg.ofC("missing 'geometry'").asError());
            }
        }
        return str;
    }

    private static boolean isEmptyTestFunctions(TestFunctions testFunctions) {
        if (testFunctions == null) return true;
        if (testFunctions instanceof ListTestFunctions) {
            return ((ListTestFunctions) testFunctions).isEmpty();
        }
        return false;
    }

    private static MoMSolverQueryInfo parseMoMSolverQuery(NTxFunctionCallContext args, NTxNode scene3D, NTxNumberElement3 sceneSize, NTxNumberElement3 scenePosition) {
        MoMSolverQueryInfo query = new MoMSolverQueryInfo();
        NTxResolutionContext context = args.scopedContext();
        for (NTxFunctionArg arg : args.args()) {
            NElement a = arg.eval();
            if (a.isNamedPair()) {
                NPairElement p = a.asNamedPair().get();
                NElement pv = args.scopedContext().evalExpression(p.value()).get();
                switch (NTxUtils.uid(p.key().asStringValue().orElse(""))) {
                    case "geometry": {
                        query.geometry = context.evalExpression(pv).flatMap(NElement::asStringValue).orNull();
                        break;
                    }
                    case "frequency": {
                        NNumberElement ne = context.evalExpression(pv).flatMap(NElement::asNumber).orNull();
                        if (ne != null) {
                            query.frequency = NTxNumberUtils.toHertz(ne).get();
                        }
                        break;
                    }
                    case "modes": {
                        query.modes = BoxModeFunctions.parse(pv, e -> context.evalExpression(e).orElse(e)).orDefault();
                        break;
                    }
                    case "boundaries": {
                        query.boundaries = parseMoMSolverQueryBoundaries(pv, args, scene3D);
                        break;
                    }
                    case "test-functions":
                    case "test": {
                        query.testFunctions = parseTestFunctions(pv, args, scene3D, sceneSize, scenePosition);
                        break;
                    }
                    case "basis-functions":
                    case "basis": {
                        query.basisFunctions = parseTestFunctions(pv, args, scene3D, sceneSize, scenePosition);
                        break;
                    }
                    case "sources": {
                        NOptional<Sources> s = SourceFactory.parseSources(pv, geometryResolver(args, scene3D, sceneSize, scenePosition));
                        if (!s.isPresent()) {
                            context.log().log(NMsg.ofC("missing 'sources'").asError());
                        } else {
                            query.sources = s.get();
                        }
                        break;
                    }
                }
            }
        }
        if (query.modes == null) {
            query.modes = new BoxModeFunctions().setSize(1024);
        }
        if (query.sources == null) {
            context.log().log(NMsg.ofC("missing 'sources'").asError());
        }
        if (query.basisFunctions == null && query.testFunctions == null) {
            context.log().log(NMsg.ofC("missing 'basisFunctions' and/or 'testFunctions'").asError());
        }
        return query;
    }


    private static TestFunctions parseTestFunctions(NElement value, NTxFunctionCallContext args, NTxNode scene3D, NTxNumberElement3 sceneSize, NTxNumberElement3 scenePosition) {
        Function<NElement, NElement> evaluator = x -> args.scopedContext().evalExpression(x).orElse(x);
        return TestFunctionsFactory.parseTestFunctions(value, evaluator, geometryResolver(args, scene3D, sceneSize, scenePosition)).orDefault();
    }

    private static Function<NElement, Geometry> geometryResolver(NTxFunctionCallContext args, NTxNode scene3D, NTxNumberElement3 sceneSize, NTxNumberElement3 scenePosition) {
        return new Function<NElement, Geometry>() {
            @Override
            public Geometry apply(NElement element) {
                if (element == null) {
                    return parseDefaultGeometry(args, scene3D, sceneSize, scenePosition);
                }
                return parsePlanarGeometryByName(element, args, scene3D, sceneSize, scenePosition);
            }
        };
    }


    private static Geometry parsePlanarGeometry(NTxNode node, NTxFunctionCallContext args, NTxNode scene3D, NTxNumberElement3 sceneSize, NTxNumberElement3 scenePosition) {
        String t = node.type();
        NTxResolutionContext context = args.scopedContext();

        switch (NTxUtils.uid(NStringUtils.trim(t))) {
            case "box": {
                NElement s = node.getPropertyValue("size").orNull();
                NElement p = node.getPropertyValue("position").orNull();
                if (s != null && p != null) {
                    p = context.evalExpression(p).orNull();
                    NTxNumberElement3 ss = NTx3DUtils.resolveSize3DSI(context.evalExpression(s).orNull(), context);
                    NTxNumberElement3 pp = NTx3DUtils.resolveSize3DSI(context.evalExpression(p).orNull(), context);
                    List<Point> realPoints = new ArrayList<>();
                    if (ss != null && pp != null) {
                        double zs = ss.z.asDoubleValue().orElse(0.0);
                        double zp = pp.z.asDoubleValue().orElse(0.0);
                        double xmin = pp.x.asDoubleValue().orElse(0.0);
                        double ymin = pp.y.asDoubleValue().orElse(0.0);
                        double xw = ss.x.asDoubleValue().orElse(0.0);
                        double yw = ss.y.asDoubleValue().orElse(0.0);
                        double xmax = xmin + xw;
                        double ymax = ymin + yw;
                        realPoints.add(new Point(xmin, ymin));
                        realPoints.add(new Point(xmax, ymin));
                        realPoints.add(new Point(xmax, ymax));
                        realPoints.add(new Point(xmin, ymax));
                        return new DefaultPolygon(realPoints);
                    }
                }
                break;
            }
            case "polygon": {
                NOptional<NTxProp> points = node.getProperty(NTxPropName.POINTS);
                if (points.isPresent()) {
                    NOptional<NElement> pointsEv = context.evalExpression(points.get().getValue());
                    if (pointsEv.isPresent()) {
                        NElement pointsValue = pointsEv.get();
                        if (pointsValue.isArray()) {
                            NArrayElement arr = pointsValue.asArray().get();
                            List<Point> realPoints = new ArrayList<>();
                            for (NElement child : arr.children()) {
                                NTxNumberElement3 e = NTx3DUtils.resolveSize3D(child, context);
                                if (e == null) {
                                    return null;
                                }
                                realPoints.add(
                                        new Point(
                                                NTxNumberUtils.evalMeterPosition(e.x, sceneSize.x, scenePosition.x),
                                                NTxNumberUtils.evalMeterPosition(e.y, sceneSize.y, scenePosition.y)
                                        )
                                );
                            }
                            return new DefaultPolygon(realPoints);
                        }
                    }
                }
            }
        }
        return null;
    }


    private static BoxSpace findBottomSpaceFromScene(NTxNode scene3D, NTxFunctionCallContext args) {
        NTxResolutionContext context = args.scopedContext();
        for (NTxNode child : scene3D.children()) {
            if (NTxMwSimulationUtils.isSimulationNode(child, "ground")) {
                NElement s = child.getPropertyValue("size").orNull();
                NElement p = child.getPropertyValue("position").orNull();
                if (s != null && p != null) {
                    p = context.evalExpression(p).orNull();
                    NTxNumberElement3 ss = NTx3DUtils.resolveSize3DSI(context.evalExpression(s).orNull(), context);
                    NTxNumberElement3 pp = NTx3DUtils.resolveSize3DSI(context.evalExpression(p).orNull(), context);
                    if (ss != null && pp != null) {
                        double zs = ss.z.asDoubleValue().orElse(0.0);
                        double zp = pp.z.asDoubleValue().orElse(0.0);
                        double p2 = zs + zp;
                        if (p2 <= 0) {
                            return BoxSpace.shortCircuit(Material.PEC, Math.abs(p2));
                        }
                    }
                }
            }
        }
        return null;
    }

    private static Geometry parseDefaultGeometry(NTxFunctionCallContext args, NTxNode scene3D, NTxNumberElement3 sceneSize, NTxNumberElement3 scenePosition) {
        List<Geometry> allGeometries = new ArrayList<>();
        for (NTxNode child : scene3D.children()) {
            if (NTxMwSimulationUtils.isSimulationNode(child, "antenna")
            ) {
                Geometry geometry = parsePlanarGeometry(child, args, scene3D, sceneSize, scenePosition);
                if (geometry == null) {
                    args.scopedContext().log().log(NMsg.ofC("unable to resolve %s as default antenna geometry", child).asError());
                } else {
                    allGeometries.add(geometry);
                }
            }
        }
        if (allGeometries.isEmpty()) {
            return null;
        }
        if (allGeometries.size() == 1) {
            return allGeometries.get(0);
        }
        Geometry geometry = allGeometries.get(0);
        for (int i = 1; i < allGeometries.size(); i++) {
            geometry = geometry.addGeometry(allGeometries.get(i));
        }
        return geometry;
    }

    private static Domain parseSubstrateDomain(NTxFunctionCallContext args, NTxNode scene3D) {
        NTxResolutionContext context = args.scopedContext();
        for (NTxNode child : scene3D.children()) {
            if (NTxMwSimulationUtils.isSimulationNode(child, "substrate")) {
                if (child.type().equalsIgnoreCase("box")) {
                    NElement s = child.getPropertyValue("size").orNull();
                    NElement p = child.getPropertyValue("position").orNull();
                    if (s != null && p != null) {
                        p = context.evalExpression(p).orNull();
                        NTxNumberElement3 ss = NTx3DUtils.resolveSize3DSI(context.evalExpression(s).orNull(), context);
                        NTxNumberElement3 pp = NTx3DUtils.resolveSize3DSI(context.evalExpression(p).orNull(), context);
                        if (ss != null && pp != null) {
                            return Domain.ofWidth(
                                    pp.x.asDoubleValue().get(),
                                    ss.x.asDoubleValue().get(),
                                    pp.y.asDoubleValue().get(),
                                    ss.y.asDoubleValue().get()
                            );
                        }
                    }
                }
            }
        }
        return null;
    }

    private static Geometry parsePlanarGeometryByName(NElement value, NTxFunctionCallContext args, NTxNode scene3D, NTxNumberElement3 sceneSize, NTxNumberElement3 scenePosition) {
        NOptional<NElement> evaluated = args.scopedContext().evalExpression(value);
        if (!evaluated.isPresent()) {
            return null;
        }
        NOptional<String> v = evaluated.flatMap(NElement::asStringValue);
        if (!v.isPresent()) {
            args.scopedContext().log().log(NMsg.ofC("invalid geometry name :  %s", value).asError());
            return null;
        }
        for (NTxNode child : scene3D.children()) {
            if (NTxMwSimulationUtils.isSimulationNode(child, v.get())) {
                Geometry g = parsePlanarGeometry(child, args, scene3D, sceneSize, scenePosition);
                if (g == null) {
                    args.scopedContext().log().log(NMsg.ofC("unable to parse geometry named :  %s from %s", v.get(), child).asError());
                }
                return g;
            }
        }
        args.scopedContext().log().log(NMsg.ofC("not found geometry named :  %s", v.get()).asError());
        return null;
    }

    private static MoMSolverQueryBoundaries parseMoMSolverQueryBoundaries(NElement value, NTxFunctionCallContext args, NTxNode scene3D) {
        NTxResolutionContext context = args.scopedContext();
        NOptional<NElement> b = context.evalExpression(value);
        MoMSolverQueryBoundaries bb = new MoMSolverQueryBoundaries();
        if (b.isPresent()) {
            if (b.get().isObject()) {
                for (NElement child : b.get().asObject().get().children()) {
                    if (child.isNamedPair()) {
                        NPairElement p = child.asPair().get();
                        switch (NTxUtils.uid(p.key().asStringValue().orElse(""))) {
                            case "bottom": {
                                bb.bottom = BoxSpace.parse(p.value(),
                                        _compiler(context)
                                ).ifEmptyUse(() -> NOptional.of(BoxSpace.shortCircuit(Material.PEC, 0))).orDefault();
                                BoxSpace bs = findBottomSpaceFromScene(scene3D, args);
                                if (bs != null) {
                                    bb.bottom = new BoxSpace(
                                            bb.bottom.getLimit(),
                                            bb.bottom.getMaterial(),
                                            bs.getWidth()
                                    );
                                }
                                break;
                            }
                            case "top": {
                                bb.top = BoxSpace.parse(p.value(), _compiler(context)).ifEmptyUse(() -> NOptional.of(BoxSpace.matchedLoad())).orDefault();
                                break;
                            }
                            case "lateral": {
                                bb.lateral = WallBorders.of(p.value().asStringValue().orElse(""));
                                break;
                            }
                            case "front":
                            case "south": {
                                Boundary boundary = parseBoundary(p.value(), args);
                                if (bb.lateral == null) {
                                    bb.lateral = WallBorders.of(boundary, boundary, boundary, boundary);
                                } else {
                                    bb.lateral = WallBorders.of(bb.lateral.north, bb.lateral.west, boundary, bb.lateral.west);
                                }
                                break;
                            }
                            case "back":
                            case "north": {
                                Boundary boundary = parseBoundary(p.value(), args);
                                if (bb.lateral == null) {
                                    bb.lateral = WallBorders.of(boundary, boundary, boundary, boundary);
                                } else {
                                    bb.lateral = WallBorders.of(boundary, bb.lateral.east, bb.lateral.south, bb.lateral.west);
                                }
                                break;
                            }
                            case "left":
                            case "west": {
                                Boundary boundary = parseBoundary(p.value(), args);
                                if (bb.lateral == null) {
                                    bb.lateral = WallBorders.of(boundary, boundary, boundary, boundary);
                                } else {
                                    bb.lateral = WallBorders.of(bb.lateral.north, bb.lateral.east, bb.lateral.south, boundary);
                                }
                                break;
                            }
                            case "right":
                            case "east": {
                                Boundary boundary = parseBoundary(p.value(), args);
                                if (bb.lateral == null) {
                                    bb.lateral = WallBorders.of(boundary, boundary, boundary, boundary);
                                } else {
                                    bb.lateral = WallBorders.of(bb.lateral.north, boundary, bb.lateral.south, bb.lateral.west);
                                }
                                break;
                            }
                        }
                    }
                }
            } else {
                context.log().log(NMsg.ofC("'boundaries' should be an object. found %s", b).asError());
            }
        }
        if (bb.bottom == null) {
            bb.bottom = BoxSpace.shortCircuit(Material.PEC, 1E-10);
        }
        if (bb.top == null) {
            bb.top = BoxSpace.matchedLoad();
        }

        if (bb.lateral == null) {
            bb.lateral = WallBorders.EEEE;
        }

        return bb;
    }

    private static Function<NElement, NElement> _compiler(NTxResolutionContext context) {
        return a -> context.evalExpression(a).orNull();
    }

    private static Boundary parseBoundary(NElement value, NTxFunctionCallContext args) {
        return Boundary.parse(value.asStringValue().get()).ifNonPresent(
                () -> args.scopedContext().log().log(NMsg.ofC("invalid 'boundary'  %s", value).asError())
        ).orElse(Boundary.NOTHING);
    }


    private static class MoMSolverQueryBoundaries {
        WallBorders lateral;
        BoxSpace top;
        BoxSpace bottom;
    }

    private static class MoMSolverQueryInfo {
        double frequency;
        String geometry;
        ModeFunctions modes;
        MoMSolverQueryBoundaries boundaries;
        TestFunctions testFunctions;
        TestFunctions basisFunctions;
        Sources sources;
    }
}
