package net.thevpc.scholar.hadrumaths.meshalgo.triflip;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.meshalgo.DefaultOption;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.util.List;

public class OptionFlip extends DefaultOption {
    private static final long serialVersionUID = 1L;
    FlipPrecision precision;

    public void setPrecision(FlipPrecision pr) {
        precision = pr;
    }    public boolean isMeshAllowed(List<HTriangle> t, int iteration) {
        return precision.isPrecisionValide(t) || enhancedMeshZone.isZoneValide(t);
    }

    @Override
    public NElement toElement() {
        return super.toElement().asObject().get().builder()
                .add("precision", NElementHelper.elem(precision))
                .build();
    }

    public HTriangle selectMeshTriangle(List<HTriangle> t, int iteration) {
        if (isMeshAllowed(t, iteration)) {
            if (precision.isPrecisionValide(t)) {
                return t.get(0);
            } else {
                return enhancedMeshZone.firstTriangleInZoneValid(t);
            }
        } else {
            return null;
        }
    }


}
