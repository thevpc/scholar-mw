package net.vpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadrumaths.geom.Triangle;

import java.util.List;

interface ConsDesPrecision extends HSerializable {
    boolean isMeshAllowed(List<Triangle> t, int iteration);
}
