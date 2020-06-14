package net.vpc.scholar.hadrumaths.meshalgo.triflip;

import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadrumaths.geom.Triangle;

import java.util.List;

interface FlipPrecision extends HSerializable {
    boolean isPrecisionValide(List<Triangle> t);

}
