package net.thevpc.scholar.hadrumaths.meshalgo.triflip;

import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;

import java.util.List;

interface FlipPrecision extends HSerializable {
    boolean isPrecisionValide(List<HTriangle> t);

}
