package net.thevpc.scholar.hadrumaths.meshalgo.triflip;

import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.geom.Triangle;
import net.thevpc.scholar.hadrumaths.meshalgo.DefaultOption;

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
    public TsonElement toTsonElement(TsonObjectContext context) {
        return super.toTsonElement(context).toObject().builder()
                .add("precision", context.elem(precision))
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
