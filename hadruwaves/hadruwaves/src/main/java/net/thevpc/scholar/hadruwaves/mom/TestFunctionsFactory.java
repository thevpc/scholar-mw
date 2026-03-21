/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.elem.NArrayElement;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NPairElement;
import net.thevpc.nuts.elem.NUpletElement;
import net.thevpc.nuts.log.NLog;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.scholar.hadrumaths.AbstractFactory;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.geom.DefaultPolygon;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadrumaths.GeometryFactory;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZoneTypeFilter;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshConsDesAlgo;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.ListTestFunctions;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpRWG;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.*;

import java.util.function.Function;

/**
 *
 * @author vpc
 */
public class TestFunctionsFactory extends AbstractFactory {

    public static TestFunctionsBuilder createBuilder() {
        return new TestFunctionsBuilder();
    }


    public static TestFunctionsBuilder addGeometry(Point... points) {

        return createBuilder().addGeometry(GeometryFactory.createPolygon(points));
    }

    public static TestFunctionsBuilder addGeometry(Domain geometry) {
        return createBuilder().addGeometry(geometry.toGeometry());
    }

    public static TestFunctionsBuilder addGeometry(Geometry geometry) {
        return createBuilder().addGeometry(geometry);
    }

    public static TestFunctionsBuilder setNormalized(boolean normalized) {
        return createBuilder().setNormalized(normalized);
    }

    public static TestFunctionsBuilder setAlwaysAttachForX(boolean alwaysAttachForX) {
        return createBuilder().setAlwaysAttachForX(alwaysAttachForX);
    }

    public static TestFunctionsBuilder setAlwaysAttachForY(boolean alwaysAttachForY) {
        return createBuilder().setAlwaysAttachForY(alwaysAttachForY);
    }

    public static TestFunctionsBuilder setInheritInvariance(boolean inheritInvariance) {
        return createBuilder().setInheritInvariance(inheritInvariance);
    }

    public static TestFunctionsBuilder setExcludedModes(int[] excludedModes) {
        return createBuilder().setExcludedModes(excludedModes);
    }

    public static TestFunctionsBuilder setIncludedModes(int[] includedModes) {
        return createBuilder().setIncludedModes(includedModes);
    }

    public static ListTestFunctions createList() {
        return new ListTestFunctions();
    }

    public static GpRWG createRWG(Geometry geometry, int trianglesCount) {
        return new GpRWG(GeometryFactory.createPolygonList(geometry), trianglesCount);
    }

    public static GpAdaptiveMesh createRooftops(Geometry geometry, MeshZoneTypeFilter meshZoneTypeFilter, Axis invariance, TestFunctionsSymmetry gpSymmetry, GridPrecision gridPrecision) {
        return new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry), new Rooftop2DPattern(meshZoneTypeFilter, invariance), gpSymmetry, new MeshAlgoRect(gridPrecision));
    }

    public static GpAdaptiveMesh createRectangularGates(Geometry geometry, TestFunctionsSymmetry gpSymmetry, GridPrecision gridPrecision) {
        return new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry), new EchelonPattern(1, 1, null), gpSymmetry, new MeshAlgoRect(gridPrecision));
    }

    public static GpAdaptiveMesh createTriangularGates(Geometry geometry, int trianglesCount, TestFunctionsSymmetry gpSymmetry) {
        return new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry), new EchelonPattern(1, 1, null), gpSymmetry, new MeshConsDesAlgo(trianglesCount));
    }

    public static GpAdaptiveMesh createBoxModes(Geometry geometry, int complexity, boolean inheritInvariance, Axis axisInvariance, int[] includedModes, int[] excludedModes, TestFunctionsSymmetry gpSymmetry, GridPrecision gridPrecision) {
        return new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry), new BoxModesPattern(complexity, inheritInvariance, axisInvariance, includedModes, excludedModes), gpSymmetry, new MeshAlgoRect(gridPrecision));
    }

    public static GpAdaptiveMesh createArcheSinus(Geometry geometry, MeshZoneTypeFilter meshZoneTypeFilter, double factor, Axis axisInvariance, int[] includedModes, int[] excludedModes, TestFunctionsSymmetry gpSymmetry, GridPrecision gridPrecision) {
        return new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry), new ArcheSinusPattern(meshZoneTypeFilter, factor), gpSymmetry, new MeshAlgoRect(gridPrecision));
    }

    public static GpAdaptiveMesh createPolyhedron(Geometry geometry, int gridx, int gridy, Axis axisInvariance, TestFunctionsSymmetry gpSymmetry, GridPrecision gridPrecision) {
        boolean x = axisInvariance == null || axisInvariance == Axis.Y;
        boolean y = axisInvariance == null || axisInvariance == Axis.X;
        return new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry), new PolyhedronPattern(x, y, gridx, gridy), gpSymmetry, new MeshAlgoRect(gridPrecision));
    }

    public static GpAdaptiveMesh createSicoCoco(Geometry geometry, int complexity, TestFunctionsSymmetry gpSymmetry, GridPrecision gridPrecision) {
        return new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry), new SicoCocoPattern(complexity), gpSymmetry, new MeshAlgoRect(gridPrecision));
    }

    public static GpAdaptiveMesh createSicoCosi(Geometry geometry, int complexity, TestFunctionsSymmetry gpSymmetry, GridPrecision gridPrecision) {
        return new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry), new SicoCosiPattern(complexity), gpSymmetry, new MeshAlgoRect(gridPrecision));
    }

    public static GpAdaptiveMesh createSisiSisi(Geometry geometry, int complexity, TestFunctionsSymmetry gpSymmetry, GridPrecision gridPrecision) {
        return new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry), new SinxCosxPattern(complexity), gpSymmetry, new MeshAlgoRect(gridPrecision));
    }


    public static NOptional<TestFunctions> parseTestFunctions(NElement value, Function<NElement, NElement> evaluator, Function<NElement, Geometry> geometryResolver) {
        if (value.isNamedUplet()) {
            NUpletElement u = value.asUplet().get();
            switch (NNameFormat.LOWER_KEBAB_CASE.format(u.name().get())) {
//                case "sine-functions":
//                case "sine":
//                case "sinus": {
//                    int count = 6;
//                    Geometry geometry = null;
//                    TestFunctionsSymmetry symmetry = TestFunctionsSymmetry.NO_SYMMETRY;
//                    GridPrecision grid = GridPrecision.LEAST_PRECISION;
//                    for (NElement param : u.params()) {
//                        if (param.isNamedPair()) {
//                            NPairElement p = param.asPair().get();
//                            NElement pv = evaluator.apply(p.value());
//                            switch (NNameFormat.LOWER_KEBAB_CASE.format(p.key().asStringValue().orElse(""))) {
//                                case "count": {
//                                    count = parsePositiveInt(pv, count);
//                                    break;
//                                }
//                                case "geometry": {
//                                    geometry = geometryResolver.apply(pv);
//                                    break;
//                                }
//                                case "symmetry": {
//                                    symmetry = TestFunctionsSymmetry.parse(pv).orDefault();
//                                    break;
//                                }
//                                case "grid":
//                                case "grid-recision": {
//                                    grid = GridPrecision.parse(pv, evaluator).orDefault();
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    if (geometry == null) {
//                        geometry = geometryResolver.apply(null);
//                    }
//                    if (geometry == null) {
//                        NLog.ofScoped(TestFunctionsFactory.class).log(NMsg.ofC("missing 'geometry'").asError());
//                        return NOptional.<TestFunctions>ofError(NMsg.ofC("missing 'geometry'").asError())
//                                .withDefault(TestFunctionsFactory.createList());
//                    }
//                    switch (NNameFormat.LOWER_KEBAB_CASE.format(u.name().get())){
//                        case "sisi-sisi": {
//                            return NOptional.of(TestFunctionsFactory.createSisiSisi(geometry, count, symmetry, grid));
//                        }
//                        case "sico-cosi": {
//                            return NOptional.of(TestFunctionsFactory.createSicoCosi(geometry, count, symmetry, grid));
//                        }
//                        case "sico-coco": {
//                            return NOptional.of(TestFunctionsFactory.createSicoCoco(geometry, count, symmetry, grid));
//                        }
//                    }
//                    return NOptional.of(TestFunctionsFactory.createSicoCoco(geometry, count, symmetry, grid));
//                }
                case "sines":
                case "sinus": {
                    int count = 6;
                    Geometry geometry = null;
                    TestFunctionsSymmetry symmetry = TestFunctionsSymmetry.NO_SYMMETRY;
                    GridPrecision grid = GridPrecision.LEAST_PRECISION;
                    CellBoundaries xBoundaries = null;
                    CellBoundaries yBoundaries = null;
                    for (NElement param : u.params()) {
                        if (param.isNamedPair()) {
                            NPairElement p = param.asPair().get();
                            NElement pv = evaluator.apply(p.value());
                            switch (NNameFormat.LOWER_KEBAB_CASE.format(p.key().asStringValue().orElse(""))) {
                                case "count":
                                case "complexity": {
                                    count = parsePositiveInt(pv, count);
                                    break;
                                }
                                case "x": {
                                    NOptional<CellBoundaries> uu = CellBoundaries.parse(pv);
                                    if (uu.isEmpty()) {
                                        xBoundaries = null;
                                    } else if (uu.isPresent()) {
                                        xBoundaries = uu.get();
                                    } else {
                                        //error
                                    }
                                    break;
                                }
                                case "y": {
                                    NOptional<CellBoundaries> uu = CellBoundaries.parse(pv);
                                    if (uu.isEmpty()) {
                                        yBoundaries = null;
                                    } else if (uu.isPresent()) {
                                        yBoundaries = uu.get();
                                    } else {
                                        //error
                                    }
                                    break;
                                }
                                case "geometry": {
                                    geometry = _resolveGeometry(pv, geometryResolver).orNull();
                                    break;
                                }
                                case "symmetry": {
                                    symmetry = TestFunctionsSymmetry.parse(pv).orDefault();
                                    break;
                                }
                                case "grid":
                                case "grid-recision": {
                                    grid = GridPrecision.parse(pv, evaluator).orDefault();
                                    break;
                                }
                            }
                        }
                    }
                    if (geometry == null) {
                        geometry = _resolveGeometry(null, geometryResolver).orNull();
                    }
                    if (geometry == null) {
                        NMsg msg = NMsg.ofC("missing 'geometry'").asError();
                        NLog.ofScoped(TestFunctionsFactory.class).log(msg);
                        return NOptional.<TestFunctions>ofError(msg)
                                .withDefault(TestFunctionsFactory.createList());
                    }
                    UserSinePattern p = new UserSinePattern(count, xBoundaries, yBoundaries);
                    GpAdaptiveMesh am = new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry), p, symmetry, new MeshAlgoRect(grid));
                    return NOptional.of(am);
                }
                case "rooftop":
                case "rooftops": {
                    Geometry geometry = null;
                    MeshZoneTypeFilter meshZoneTypeFilter = null;
                    Axis axis = Axis.X;
                    TestFunctionsSymmetry symmetry = TestFunctionsSymmetry.NO_SYMMETRY;
                    GridPrecision grid = GridPrecision.LEAST_PRECISION;
                    for (NElement param : u.params()) {
                        if (param.isNamedPair()) {
                            NPairElement p = param.asPair().get();
                            NElement pv = evaluator.apply(p.value());
                            switch (NNameFormat.LOWER_KEBAB_CASE.format(p.key().asStringValue().orElse(""))) {
                                case "axis": {
                                    axis = Axis.parse(pv).orDefault();
                                    break;
                                }
                                case "geometry": {
                                    geometry = _resolveGeometry(pv, geometryResolver).orNull();
                                    break;
                                }
                                case "symmetry": {
                                    symmetry = TestFunctionsSymmetry.parse(pv).orDefault();
                                    break;
                                }
                                case "grid":
                                case "grid-recision": {
                                    grid = GridPrecision.parse(pv, evaluator).orDefault();
                                    break;
                                }
                            }
                        }
                    }
                    if (geometry == null) {
                        geometry = _resolveGeometry(null, geometryResolver).orNull();
                    }
                    if (geometry == null) {
                        NMsg msg = NMsg.ofC("missing 'geometry'").asError();
                        NLog.ofScoped(TestFunctionsFactory.class).log(msg);
                        return NOptional.<TestFunctions>ofError(msg)
                                .withDefault(TestFunctionsFactory.createList());
                    }
                    return NOptional.of(TestFunctionsFactory.createRooftops(geometry, meshZoneTypeFilter, axis, symmetry, grid));
                }
                case "modes": {
                    int complexity = 6;
                    Geometry geometry = null;
                    Axis axis = null;//Axis.X;
                    boolean inheritInvariance = false;
                    int[] includedModes = null;
                    int[] excludedModes = null;
                    TestFunctionsSymmetry symmetry = TestFunctionsSymmetry.NO_SYMMETRY;
                    GridPrecision grid = GridPrecision.LEAST_PRECISION;
                    for (NElement param : u.params()) {
                        if (param.isNamedPair()) {
                            NPairElement p = param.asPair().get();
                            NElement pv = evaluator.apply(p.value());
                            switch (NNameFormat.LOWER_KEBAB_CASE.format(p.key().asStringValue().orElse(""))) {
                                case "complexity":
                                case "count": {
                                    complexity = parsePositiveInt(pv, complexity);
                                    break;
                                }
                                case "inheritInvariance": {
                                    inheritInvariance = parseBoolean(pv, inheritInvariance);
                                    break;
                                }
                                case "include-modes":
                                case "included-modes": {
                                    includedModes = parseIntArray(pv, includedModes);
                                    break;
                                }
                                case "exclude-modes":
                                case "excluded-modes": {
                                    excludedModes = parseIntArray(pv, excludedModes);
                                    break;
                                }
                                case "axis":
                                case "axis-invariance": {
                                    axis = Axis.parse(evaluator.apply(pv)).orDefault();
                                    break;
                                }
                                case "geometry": {
                                    geometry = _resolveGeometry(pv, geometryResolver).orNull();
                                    break;
                                }
                                case "symmetry": {
                                    symmetry = TestFunctionsSymmetry.parse(pv).orDefault();
                                    break;
                                }
                                case "grid":
                                case "grid-recision": {
                                    grid = GridPrecision.parse(pv, evaluator).orDefault();
                                    break;
                                }
                            }
                        }
                    }
                    if (geometry == null) {
                        geometry = _resolveGeometry(null, geometryResolver).orNull();
                    }
                    if (geometry == null) {
                        NMsg msg = NMsg.ofC("missing 'geometry'").asError();
                        NLog.ofScoped(TestFunctionsFactory.class).log(msg);
                        return NOptional.<TestFunctions>ofError(msg)
                                .withDefault(TestFunctionsFactory.createList());
                    }
                    return NOptional.of(TestFunctionsFactory.createBoxModes(geometry, complexity, inheritInvariance, axis, includedModes, excludedModes, symmetry, grid));
                }
                case "rwg": {
                    int count = 6;
                    Geometry geometry = null;
                    for (NElement param : u.params()) {
                        if (param.isNamedPair()) {
                            NPairElement p = param.asPair().get();
                            NElement pv = evaluator.apply(p.value());
                            switch (NNameFormat.LOWER_KEBAB_CASE.format(p.key().asStringValue().orElse(""))) {
                                case "count": {
                                    count = parsePositiveInt(pv, count);
                                    break;
                                }
                                case "geometry": {
                                    geometry = _resolveGeometry(pv, geometryResolver).orNull();
                                    break;
                                }
                            }
                        }
                    }
                    if (geometry == null) {
                        geometry = _resolveGeometry(null, geometryResolver).orNull();
                    }
                    if (geometry == null) {
                        NMsg msg = NMsg.ofC("missing 'geometry'").asError();
                        NLog.ofScoped(TestFunctionsFactory.class).log(msg);
                        return NOptional.<TestFunctions>ofError(msg)
                                .withDefault(TestFunctionsFactory.createList());
                    }
                    return NOptional.of(TestFunctionsFactory.createRWG(geometry, count));
                }
            }
            NLog.ofScoped(TestFunctionsFactory.class).log(NMsg.ofC("invalid test/basis functions %s", value).asError());
            return NOptional.<TestFunctions>ofError(NMsg.ofC("invalid test/basis functions %s", value).asError()).withDefault(() -> {
                Geometry defaultGeometry = _resolveGeometry(null, geometryResolver).orNull();
                if (defaultGeometry == null) {
                    return createList();
                }
                return createRWG(defaultGeometry, 6);
            });
        } else if (value.isObject() || value.isArray()) {
            ListTestFunctions li = TestFunctionsFactory.createList();
            for (NElement child : value.asListContainer().get().children()) {
                NOptional<TestFunctions> y = parseTestFunctions(child, evaluator, geometryResolver);
                li.add(y.orDefault());
            }
            return NOptional.of(li);
        } else {
            NLog.ofScoped(TestFunctionsFactory.class).log(NMsg.ofC("invalid test/basis functions %s", value).asError());
            return NOptional.<TestFunctions>ofError(NMsg.ofC("invalid test/basis functions %s", value).asError()).withDefault(() -> {
                Geometry defaultGeometry = _resolveGeometry(null, geometryResolver).orNull();
                if (defaultGeometry == null) {
                    return createList();
                }
                return createRWG(defaultGeometry, 6);
            });
        }
    }

    private static NOptional<Geometry> _resolveGeometry(NElement pv, Function<NElement, Geometry> geometryResolver) {
        Geometry geometry = geometryResolver.apply(pv);
        if (geometry == null) {
            if (pv == null) {
                NMsg msg = NMsg.ofC("missing default 'geometry'").asError();
                NLog.ofScoped(TestFunctionsFactory.class).log(msg);
                return NOptional.ofError(msg);
            }
            NMsg msg = NMsg.ofC("missing 'geometry' : %s", pv).asError();
            NLog.ofScoped(TestFunctionsFactory.class).log(msg);
            return NOptional.ofError(msg);
        }
        return NOptional.of(geometry);
    }


    private static int parsePositiveInt(NElement value, int defaultCount) {
        return value.asIntValue()
                .filter(i -> i > 0)
                .ifNonPresent(() -> {
                    NLog.ofScoped(TestFunctionsFactory.class).log(NMsg.ofC("invalid positive value(...) :  %s", value).asError());
                }).orElse(defaultCount)
                ;
    }

    private static int[] parseIntArray(NElement value, int[] defaultValue) {
        NOptional<NArrayElement> v = value.asArray();
        if (v.isPresent()) {
            int[] r = new int[v.get().size()];
            for (int i = 0; i < r.length; i++) {
                NElement u = v.get().get(i).get();
                if (u.isNumber()) {
                    r[i] = u.asIntValue().get();
                } else {
                    NLog.ofScoped(TestFunctionsFactory.class).log(NMsg.ofC("invalid in array :  %s", value).asError());
                    return defaultValue;
                }
            }
            return r;
        }
        NLog.ofScoped(TestFunctionsFactory.class).log(NMsg.ofC("invalid in array :  %s", value).asError());
        return defaultValue;
    }

    private static boolean parseBoolean(NElement value, boolean defaultValue) {
        return value.asBooleanValue()
                .ifNonPresent(() -> {
                    NLog.ofScoped(TestFunctionsFactory.class).log(NMsg.ofC("invalid boolean value(...) :  %s", value).asError());
                })
                .orElse(defaultValue);
    }

}
