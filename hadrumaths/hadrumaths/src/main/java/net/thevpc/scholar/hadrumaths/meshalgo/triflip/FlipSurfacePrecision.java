package net.thevpc.scholar.hadrumaths.meshalgo.triflip;


import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.geom.GeomUtils;
import net.thevpc.scholar.hadrumaths.geom.Triangle;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.util.List;

public class FlipSurfacePrecision implements FlipPrecision {
    double airmax;

    public FlipSurfacePrecision(double air) {
        airmax = air;
    }

    public boolean isPrecisionValide(List<Triangle> t) {
        return GeomUtils.smallest(t).getSurface() > airmax;

    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder sb = NElement.ofObjectBuilder(getClass().getSimpleName());
        sb.add("surface", NElementHelper.elem(airmax));
        return sb.build();
    }

//    public String dump() {
//        Dumper h = new Dumper(this);
//        h.add("surface", airmax);
//        return h.toString();
//    }

}
