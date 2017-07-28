package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.Rooftop2DPattern;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;

public class GpRooftop extends GpAdaptiveMesh {

    public GpRooftop(GeometryList polygonsSerial, TestFunctionsSymmetry symmetry, GridPrecision precision) {
        super(polygonsSerial,new Rooftop2DPattern(true, true), symmetry, new MeshAlgoRect(precision));
    }
}