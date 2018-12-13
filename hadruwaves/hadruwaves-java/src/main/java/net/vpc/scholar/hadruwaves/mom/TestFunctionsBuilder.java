/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneTypeFilter;
import net.vpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadrumaths.meshalgo.triconsdes.MeshConsDesAlgo;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.mom.testfunctions.ListTestFunctions;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.BoxModesPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.EchelonPattern;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;

/**
 * @author vpc
 */
public class TestFunctionsBuilder {

    private Geometry geometry;
    private ListTestFunctions allTestFunctions;
    private net.vpc.scholar.hadruwaves.mom.TestFunctions testFunctions;
    private int complexity = 1;
    private TestFunctionsSymmetry gpSymmetry;
    private MeshAlgo meshAlgo;
    private GpPattern pattern;
    private Axis invariance;
    private GridPrecision gridPrecision;
    private MeshZoneTypeFilter meshZoneTypeFilter = MeshZoneType.FILTER_ALL;
    private boolean alwaysAttachForX = false;
    private boolean alwaysAttachForY = false;
    private boolean inheritInvariance = true;
    private boolean normalized = false;
    private double factor = 1;
    private int[] includedModes;
    private int[] excludedModes;
    private int approxGridX = 10;
    private int approxGridY = 10;
    private Domain bounds;


    public TestFunctionsBuilder addGeometry(Point... points) {
        return addGeometry(new Polygon(points));
    }

    public TestFunctionsBuilder addGeometry(Domain geometry) {
        return addGeometry(geometry.toGeometry());
    }

    public TestFunctionsBuilder addGeometry(Geometry geometry) {
        if (this.geometry == null) {
            this.geometry = geometry;
            if (geometry != null && geometry instanceof GeometryList) {
                ((GeometryList) geometry).setDomain(getBounds());
            }
        } else {
            GeometryList geometryList = GeometryFactory.createPolygonList(this.geometry, geometry);
            geometryList.setDomain(getBounds());
            this.geometry = geometryList;
        }
        return this;
    }

    public TestFunctionsBuilder clearGeometry() {
        this.geometry = null;
        return this;
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions build() {
        if (testFunctions == null) {
            throw new RuntimeException("Missing Test functions, should createXYZ(...) function");
        }
//        if(setNormalized) {
//            if (!(this.testFunctions instanceof ListTestFunctions)) {
//                ListTestFunctions t2 = new ListTestFunctions();
//                t2.add(this.testFunctions);
//                this.testFunctions=t2;
//            }
//            ListTestFunctions t = (ListTestFunctions) this.testFunctions;
//            ListTestFunctions t2 = new ListTestFunctions();
//            for (IVDCxy cc : t.toList()) {
//                t2.add(cc.normalizeString().toDC());
//            }
//            testFunctions=t2;
//            return testFunctions;
//        }
        return testFunctions;
    }


    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildRWGs() {
        return applyRWGs().build();
    }

    public TestFunctionsBuilder applyRWGs() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(TestFunctionsFactory.createRWG(geometry, getComplexity()));
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildRooftops() {
        return applyRooftops().build();
    }

    public TestFunctionsBuilder applyRooftops() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(TestFunctionsFactory.createRooftops(geometry, getMeshZoneTypeFilter(), getInvariance(), getGpSymmetry(), getGridPrecision()));
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildRectangularGates() {
        return applyRectangularGates().build();
    }

    public TestFunctionsBuilder applyRectangularGates() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(TestFunctionsFactory.createRectangularGates(geometry, getGpSymmetry(), getGridPrecision()));
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildTriangularGates() {
        return applyTriangularGates().build();
    }

    public TestFunctionsBuilder applyTriangularGates() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(TestFunctionsFactory.createTriangularGates(geometry, getComplexity(), getGpSymmetry()));
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildMesh() {
        return applyMesh().build();
    }

    public TestFunctionsBuilder applyMesh() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(
                new GpAdaptiveMesh(GeometryFactory.createPolygonList(geometry),
                        pattern!=null?pattern:
                        new EchelonPattern(1, 1, null), gpSymmetry,
                        meshAlgo!=null?meshAlgo:new MeshConsDesAlgo(getComplexity())
                )
        );
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildBoxModes() {
        return applyBoxModes().build();
    }

    public TestFunctionsBuilder applyBoxModes() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(createBoxModes(geometry, getComplexity(), isInheritInvariance(), getInvariance(), getIncludedModes(), getExcludedModes(), getGpSymmetry(), getGridPrecision()));
    }

    public GpAdaptiveMesh createBoxModes(Geometry geometry, int complexity, boolean inheritInvariance, Axis axisInvariance, int[] includedModes, int[] excludedModes, TestFunctionsSymmetry gpSymmetry, GridPrecision gridPrecision) {
        GeometryList geometry2 = GeometryFactory.createPolygonList(geometry);
        if (bounds != null) {
            geometry2.setDomain(bounds);
        }
        return new GpAdaptiveMesh(geometry2, new BoxModesPattern(complexity, inheritInvariance, axisInvariance, includedModes, excludedModes), gpSymmetry, new MeshAlgoRect(gridPrecision));
    }


    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildArchSine() {
        return applyArchSine().build();
    }

    public TestFunctionsBuilder applyArchSine() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(TestFunctionsFactory.createArcheSinus(geometry, getMeshZoneTypeFilter(), getFactor(), getInvariance(), getIncludedModes(), getExcludedModes(), getGpSymmetry(), getGridPrecision()));
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildSine(String borders, int m, int n) {
        return applySine(borders, m, n).build();
    }

    public TestFunctionsBuilder applySine(String borders, int m, int n) {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        ListTestFunctions list = TestFunctionsFactory.createList();
        Domain d = null;
        if (geometry instanceof Domain) {
            d = (Domain) geometry;
        } else if (geometry instanceof Polygon) {
            if (((Polygon) geometry).isRectangular()) {
                d = ((Polygon) geometry).getDomain();
            }
        }
        if (d == null) {
            throw new NullPointerException("Non rectangular Geometry");
        }

        list.add(Maths.sineSeq(borders, m, n, d));
        return apply(list);
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildPolyhedron() {
        return applyPolyhedron().build();
    }

    public TestFunctionsBuilder applyPolyhedron() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(TestFunctionsFactory.createPolyhedron(geometry, getApproxGridX(), getApproxGridY(), getInvariance(), getGpSymmetry(), getGridPrecision()));
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildSicoCoco() {
        return applySicoCoco().build();
    }

    public TestFunctionsBuilder applySicoCoco() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(TestFunctionsFactory.createSicoCoco(geometry, getComplexity(), getGpSymmetry(), getGridPrecision()));
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildSicoCosi() {
        return applySicoCosi().build();
    }

    public TestFunctionsBuilder applySicoCosi() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(TestFunctionsFactory.createSicoCosi(geometry, getComplexity(), getGpSymmetry(), getGridPrecision()));
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions buildSisiSisi() {
        return applySisiSisi().build();
    }

    public TestFunctionsBuilder applySisiSisi() {
        if (geometry == null) {
            throw new NullPointerException("Missing Geometry");
        }
        return apply(TestFunctionsFactory.createSisiSisi(geometry, getComplexity(), getGpSymmetry(), getGridPrecision()));
    }

    public int[] getExcludedModes() {
        return excludedModes;
    }

    public TestFunctionsBuilder setExcludedModes(int[] excludedModes) {
        this.excludedModes = excludedModes;
        return this;

    }

    public Domain getBounds() {
        return bounds;
    }

    public TestFunctionsBuilder setBounds(Domain domain) {
        this.bounds = domain;
        return this;
    }

    public int[] getIncludedModes() {
        return includedModes;
    }

    public TestFunctionsBuilder setIncludedModes(int[] includedModes) {
        this.includedModes = includedModes;
        return this;

    }

    public boolean isAlwaysAttachForX() {
        return alwaysAttachForX;
    }

    public TestFunctionsBuilder setAlwaysAttachForX(boolean alwaysAttachForX) {
        this.alwaysAttachForX = alwaysAttachForX;
        return this;

    }

    public boolean isAlwaysAttachForY() {
        return alwaysAttachForY;
    }

    public TestFunctionsBuilder setAlwaysAttachForY(boolean alwaysAttachForY) {
        this.alwaysAttachForY = alwaysAttachForY;
        return this;

    }

    public boolean isInheritInvariance() {
        return inheritInvariance;
    }

    public TestFunctionsBuilder setInheritInvariance(boolean inheritInvariance) {
        this.inheritInvariance = inheritInvariance;
        return this;

    }

    public boolean isNormalized() {
        return normalized;
    }

    public TestFunctionsBuilder setNormalized(boolean normalized) {
        this.normalized = normalized;
        return this;
    }

    public Axis getInvariance() {
        return invariance;
    }

    public TestFunctionsBuilder setInvariance(Axis invariance) {
        this.invariance = invariance;
        return this;

    }

    public int getComplexity() {
        return complexity;
    }

    public TestFunctionsBuilder setComplexity(int complexity) {
        this.complexity = complexity;
        return this;

    }

    public TestFunctionsSymmetry getGpSymmetry() {
        return gpSymmetry;
    }

    public TestFunctionsBuilder setGpSymmetry(TestFunctionsSymmetry gpSymmetry) {
        this.gpSymmetry = gpSymmetry;
        return this;

    }

    public GridPrecision getGridPrecision() {
        return gridPrecision;
    }

    public TestFunctionsBuilder setGridPrecision(GridPrecision gridPrecision) {
        this.gridPrecision = gridPrecision;
        return this;

    }

    public MeshZoneTypeFilter getMeshZoneTypeFilter() {
        return meshZoneTypeFilter;
    }

    public TestFunctionsBuilder setMeshZoneTypeFilter(MeshZoneTypeFilter meshZoneTypeFilter) {
        this.meshZoneTypeFilter = meshZoneTypeFilter;
        return this;

    }

    public int getApproxGridX() {
        return approxGridX;
    }

    public TestFunctionsBuilder setApproxGridX(int approxGridX) {
        this.approxGridX = approxGridX;
        return this;
    }

    public double getFactor() {
        return factor;
    }

    public TestFunctionsBuilder setFactor(double factor) {
        this.factor = factor;
        return this;

    }

    public int getApproxGridY() {
        return approxGridY;
    }

    public TestFunctionsBuilder setApproxGridY(int approxGridY) {
        this.approxGridY = approxGridY;
        return this;
    }

    public MeshAlgo getMeshAlgo() {
        return meshAlgo;
    }

    public TestFunctionsBuilder setMeshAlgo(MeshAlgo meshAlgo) {
        this.meshAlgo = meshAlgo;
        return this;
    }

    public GpPattern getPattern() {
        return pattern;
    }

    public TestFunctionsBuilder setPattern(GpPattern pattern) {
        this.pattern = pattern;
        return this;
    }

    private net.vpc.scholar.hadruwaves.mom.TestFunctions normalizeIfApplicable(net.vpc.scholar.hadruwaves.mom.TestFunctions f) {
        if (f == null) {
            return null;
        }
        if (normalized) {
            if (!(f instanceof ListTestFunctions)) {
                ListTestFunctions t2 = new ListTestFunctions();
                t2.add(f);
                f = t2;
            }
            ListTestFunctions t = (ListTestFunctions) f;
            ListTestFunctions t2 = new ListTestFunctions();
            for (Expr cc : t.toList()) {
                t2.add(cc.normalize().toDC());
            }
            f = t2;
        }
        return f;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    private TestFunctionsBuilder apply(net.vpc.scholar.hadruwaves.mom.TestFunctions fct) {
        fct = normalizeIfApplicable(fct);
        if (testFunctions == null) {
            this.testFunctions = fct;
        } else {
            ListTestFunctions curr;
            if (this.testFunctions instanceof ListTestFunctions) {
                curr = (ListTestFunctions) this.testFunctions;
            } else {
                curr = new ListTestFunctions();
                curr.add(this.testFunctions);
            }
            if (fct instanceof ListTestFunctions) {
                curr.addAll((ListTestFunctions) fct);
            } else {
                curr.add(fct);
            }
            this.testFunctions = curr;
        }
        geometry = null;
        pattern = null;
        meshAlgo = null;
        return this;
    }

}
