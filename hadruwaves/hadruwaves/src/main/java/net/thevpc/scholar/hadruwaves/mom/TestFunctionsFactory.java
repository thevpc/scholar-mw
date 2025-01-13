/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom;

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
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.ArcheSinusPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.BoxModesPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.EchelonPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.PolyhedronPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.Rooftop2DPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.SicoCocoPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.SicoCosiPattern;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.SinxCosxPattern;

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

}
