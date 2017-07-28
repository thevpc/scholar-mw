package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.BoxModesPattern;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;

public class GpModes extends GpAdaptiveMesh {

    public GpModes(GeometryList polygonsSerial, TestFunctionsSymmetry symmetry, int count) {
        this(polygonsSerial, symmetry,count,true);
    }

    public GpModes(GeometryList polygonsSerial, TestFunctionsSymmetry symmetry, int count, boolean keepAllModes) {
        super(polygonsSerial, new BoxModesPattern(count).setKeepAllModes(keepAllModes), symmetry, MeshAlgoRect.RECT_ALGO_LOW_RESOLUTION);
    }
}
