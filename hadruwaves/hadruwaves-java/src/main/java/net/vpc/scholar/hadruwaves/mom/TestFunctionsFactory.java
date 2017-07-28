/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.AbstractFactory;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.GeometryFactory;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneTypeFilter;
import net.vpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadrumaths.meshalgo.triconsdes.MeshConsDesAlgo;
import net.vpc.scholar.hadruwaves.mom.testfunctions.ListTestFunctions;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpRWG;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.ArcheSinusPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.BoxModesPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.EchelonPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.PolyhedronPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.Rooftop2DPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.SicoCocoPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.SicoCosiPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.SinxCosxPattern;

/**
 *
 * @author vpc
 */
public class TestFunctionsFactory extends AbstractFactory {

    public static TestFunctionsBuilder createBuilder() {
        return new TestFunctionsBuilder();
    }


    public static TestFunctionsBuilder addGeometry(Point... points) {

        return createBuilder().addGeometry(new Polygon(points));
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
