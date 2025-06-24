package net.thevpc.scholar.hadrumaths.meshalgo.triflip;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.geom.GeomUtils;
import net.thevpc.scholar.hadrumaths.geom.Triangle;

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
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.ofObjectBuilder(getClass().getSimpleName());
        sb.add("surface", context.elem(airmax));
        return sb.build();
    }

//    public String dump() {
//        Dumper h = new Dumper(this);
//        h.add("surface", airmax);
//        return h.toString();
//    }

}
