package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.thevpc.scholar.hadrumaths.geom.HGeometryList;
import net.thevpc.scholar.hadrumaths.meshalgo.tri.MeshTriangulationAlgo;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.PolyhedronPattern;

public class GpPolyedron extends GpAdaptiveMesh {

    public GpPolyedron(HGeometryList polygonsSerial, int trianglesCount) {
        this(polygonsSerial, TestFunctionsSymmetry.NO_SYMMETRY, trianglesCount);
    }

    public GpPolyedron(HGeometryList polygonsSerial, TestFunctionsSymmetry symmetry, int trianglesCount) {
        super(polygonsSerial, new PolyhedronPattern(true, true, trianglesCount), symmetry, new MeshTriangulationAlgo(trianglesCount));
    }
}
