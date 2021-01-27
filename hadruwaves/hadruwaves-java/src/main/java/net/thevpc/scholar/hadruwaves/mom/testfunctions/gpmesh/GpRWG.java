package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.thevpc.scholar.hadrumaths.geom.GeometryList;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.RWGPattern;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshConsDesAlgo;

public class GpRWG extends GpAdaptiveMesh {

    public GpRWG(GeometryList polygonsSerial, int trianglesCount) {
        this(polygonsSerial, TestFunctionsSymmetry.NO_SYMMETRY, trianglesCount);
    }
    
    public GpRWG(GeometryList polygonsSerial, TestFunctionsSymmetry symmetry, int trianglesCount) {
        super(polygonsSerial, new RWGPattern(), symmetry, new MeshConsDesAlgo(trianglesCount));
    }
}