package net.vpc.scholar.hadrumaths.meshalgo.triflip;

import net.vpc.scholar.hadrumaths.geom.GeomUtils;
import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.List;

public class FlipSurfacePrecision implements FlipPrecision {
    double airmax;

    public FlipSurfacePrecision(double air) {
        airmax = air;
    }

    public boolean isPrecisionValide(List<Triangle> t) {
        return GeomUtils.smallest(t).getSurface() > airmax;

    }

    public String dump() {
        Dumper h = new Dumper(this);
        h.add("surface", airmax);
        return h.toString();
    }

}
