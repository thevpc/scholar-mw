package net.vpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.vpc.scholar.hadrumaths.geom.GeomUtils;
import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.dump.Dumper;

import java.util.List;


public class ConsDesSurfacePrecision implements ConsDesPrecision {
    private static final long serialVersionUID = 1L;
    double airmax;

    public ConsDesSurfacePrecision(double air) {
        airmax = air;
    }

    public boolean isMeshAllowed(List<Triangle> t, int iteration) {
        return GeomUtils.smallest(t).getSurface() > airmax;
    }

    public String dump() {
        Dumper h = new Dumper(this);
        h.add("surface", airmax);
        return h.toString();
    }

}
