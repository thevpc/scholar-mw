package net.vpc.scholar.hadruwaves.mom.testfunctions;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 11 juil. 2007 22:38:33
 */
public class TestFunctionCell implements HSerializable {
    private MeshAlgo meshAlgo;
    private GpPattern pattern;
    private Domain domain;
//    private DomainXY globalDomain;
    private String name;
    private Axis invariance;
    private TestFunctionsSymmetry symmetry = TestFunctionsSymmetry.NO_SYMMETRY;
    private GeometryList areaGeometryList;

    public TestFunctionCell(GeometryList polygons, GpPattern pattern, TestFunctionsSymmetry symmetry, MeshAlgo meshAlgo, Axis invariance) {
        this("NONAME", polygons.getSmallestBounds(), polygons, pattern, symmetry,meshAlgo,invariance);
    }

    @Deprecated
    public TestFunctionCell(Domain domain, GeometryList polygons, GpPattern pattern, TestFunctionsSymmetry symmetry, MeshAlgo meshAlgo, Axis invariance) {
        this("NONAME", domain, polygons, pattern, symmetry,meshAlgo,invariance);
    }

    //TODO
    //ramener tout ca dans RectAreaShape
    @Deprecated
    public TestFunctionCell(String name, Domain domain, GeometryList polygons, GpPattern pattern, TestFunctionsSymmetry symmetry, MeshAlgo meshAlgo, Axis invariance) {
        this.domain = domain;
        this.pattern = pattern;
        this.invariance = invariance;
        this.meshAlgo = meshAlgo;
        this.symmetry = symmetry;
        this.areaGeometryList = polygons;
        this.name = name;
    }

    public Domain getDomain() {
        return domain;
    }

    public GpPattern getPattern() {
        return pattern;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj(getClass().getSimpleName())
                .add("name", context.elem(name))
                .add("domain", context.elem(domain))
                .add("pattern", context.elem(pattern))
                .add("polygonList", context.elem(areaGeometryList))
                .add("symmetry", context.elem(symmetry))
                .add("meshAlgo", context.elem(meshAlgo))
                .add("invariance", context.elem(invariance))
                .build();
    }

    public MeshAlgo getMeshAlgo() {
        return meshAlgo;
    }

    public void setMeshAlgo(MeshAlgo meshAlgo) {
        this.meshAlgo = meshAlgo;
    }

    public String getName() {
        return name;
    }

    public TestFunctionsSymmetry getSymmetry() {
        return symmetry;
    }

    public void setSymmetry(TestFunctionsSymmetry symmetry) {
        this.symmetry = symmetry;
    }

    public GeometryList getAreaGeometryList() {
        return areaGeometryList;
    }
}
