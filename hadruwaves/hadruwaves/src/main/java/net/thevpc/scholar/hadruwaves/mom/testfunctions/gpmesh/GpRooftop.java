package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.thevpc.scholar.hadrumaths.geom.HGeometryList;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.Rooftop2DPattern;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;

public class GpRooftop extends GpAdaptiveMesh {

    public GpRooftop(HGeometryList polygonsSerial, TestFunctionsSymmetry symmetry, GridPrecision precision) {
        super(polygonsSerial,new Rooftop2DPattern(true, true), symmetry, new MeshAlgoRect(precision));
    }
}