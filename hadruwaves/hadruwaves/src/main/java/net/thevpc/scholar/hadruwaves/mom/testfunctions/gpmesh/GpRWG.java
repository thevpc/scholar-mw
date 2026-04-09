package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.thevpc.scholar.hadrumaths.geom.HGeometryList;
import net.thevpc.scholar.hadrumaths.meshalgo.tri.MeshTriangulationAlgo;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.RWGPattern;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;

public class GpRWG extends GpAdaptiveMesh {

    public GpRWG(HGeometryList polygonsSerial, MeshTriangulationOptions options) {
        this(polygonsSerial, TestFunctionsSymmetry.NO_SYMMETRY, options);
    }
    
    public GpRWG(HGeometryList polygonsSerial, TestFunctionsSymmetry symmetry, MeshTriangulationOptions options) {
        super(polygonsSerial, new RWGPattern(), symmetry, new MeshTriangulationAlgo(options));
    }
}