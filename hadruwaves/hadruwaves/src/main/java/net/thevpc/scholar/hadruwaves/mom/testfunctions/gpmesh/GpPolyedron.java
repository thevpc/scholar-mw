package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.thevpc.scholar.hadrumaths.geom.GeometryList;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshConsDesAlgo;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.PolyhedronPattern;

public class GpPolyedron extends GpAdaptiveMesh {

    public GpPolyedron(GeometryList polygonsSerial, int trianglesCount) {
        this(polygonsSerial, TestFunctionsSymmetry.NO_SYMMETRY, trianglesCount);
    }

    public GpPolyedron(GeometryList polygonsSerial, TestFunctionsSymmetry symmetry, int trianglesCount) {
        super(polygonsSerial, new PolyhedronPattern(true, true, trianglesCount), symmetry, new MeshConsDesAlgo(trianglesCount));
    }
}
