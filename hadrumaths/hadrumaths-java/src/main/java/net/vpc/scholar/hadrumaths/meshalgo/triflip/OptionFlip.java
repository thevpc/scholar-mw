package net.vpc.scholar.hadrumaths.meshalgo.triflip;

import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.meshalgo.DefaultOption;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.List;

public class OptionFlip extends DefaultOption {
    private static final long serialVersionUID = 1L;
    FlipPrecision precision;

    public boolean isMeshAllowed(List<Triangle> t, int iteration) {
        return precision.isPrecisionValide(t) || enhancedMeshZone.isZoneValide(t);
    }

    public Dumper getDumpStringHelper() {
        Dumper h=super.getDumpStringHelper();
        h.add("precision",precision);
        return h;
    }

    public Triangle selectMeshTriangle(List<Triangle> t, int iteration) {
        if (isMeshAllowed(t, iteration)) {
            if (precision.isPrecisionValide(t)) {
                return t.get(0);
            } else {
                return enhancedMeshZone.firstTriangleInZoneValide(t);
            }
        } else {
            return null;
        }
    }

    public void setPrecision(FlipPrecision pr) {
        precision = pr;
    }
}
