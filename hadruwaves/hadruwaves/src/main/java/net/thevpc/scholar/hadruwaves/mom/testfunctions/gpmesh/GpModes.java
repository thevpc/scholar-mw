package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.thevpc.scholar.hadrumaths.geom.HGeometryList;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.BoxModesPattern;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;

public class GpModes extends GpAdaptiveMesh {

    public GpModes(HGeometryList polygonsSerial, TestFunctionsSymmetry symmetry, int count) {
        this(polygonsSerial, symmetry,count,true);
    }

    public GpModes(HGeometryList polygonsSerial, TestFunctionsSymmetry symmetry, int count, boolean keepAllModes) {
        super(polygonsSerial, new BoxModesPattern(count).setKeepAllModes(keepAllModes), symmetry, MeshAlgoRect.RECT_ALGO_LOW_RESOLUTION);
    }
}
