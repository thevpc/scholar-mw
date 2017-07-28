package net.vpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

import java.io.Serializable;
import java.util.List;

interface ConsDesPrecision extends Serializable, Dumpable {
    boolean isMeshAllowed(List<Triangle> t, int iteration);
}
