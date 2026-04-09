package net.thevpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;

import java.util.List;

interface ConsDesPrecision extends HSerializable {
    boolean isMeshAllowed(List<HTriangle> t, int iteration);
}
