package net.vpc.scholar.hadrumaths.meshalgo.triflip;

import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

import java.util.List;

interface FlipPrecision extends Dumpable {
    boolean isPrecisionValide(List<Triangle> t);

}
