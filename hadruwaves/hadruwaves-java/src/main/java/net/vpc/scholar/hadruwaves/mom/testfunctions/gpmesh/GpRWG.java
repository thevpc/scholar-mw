package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.RWGPattern;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.vpc.scholar.hadrumaths.meshalgo.triconsdes.MeshConsDesAlgo;

public class GpRWG extends GpAdaptiveMesh {

    public GpRWG(GeometryList polygonsSerial, int trianglesCount) {
        this(polygonsSerial, TestFunctionsSymmetry.NO_SYMMETRY, trianglesCount);
    }
    
    public GpRWG(GeometryList polygonsSerial, TestFunctionsSymmetry symmetry, int trianglesCount) {
        super(polygonsSerial, new RWGPattern(), symmetry, new MeshConsDesAlgo(trianglesCount));
    }
}