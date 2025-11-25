package net.thevpc.scholar.hadrumaths.meshalgo.triflip;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.scholar.hadrumaths.geom.Triangle;
import net.thevpc.scholar.hadrumaths.meshalgo.DefaultOption;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.util.List;

public class OptionFlip extends DefaultOption {
    private static final long serialVersionUID = 1L;
    FlipPrecision precision;

    public void setPrecision(FlipPrecision pr) {
        precision = pr;
    }    public boolean isMeshAllowed(List<Triangle> t, int iteration) {
        return precision.isPrecisionValide(t) || enhancedMeshZone.isZoneValide(t);
    }

    @Override
    public NElement toElement() {
        return super.toElement().asObject().get().builder()
                .add("precision", NElementHelper.elem(precision))
                .build();
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


}
