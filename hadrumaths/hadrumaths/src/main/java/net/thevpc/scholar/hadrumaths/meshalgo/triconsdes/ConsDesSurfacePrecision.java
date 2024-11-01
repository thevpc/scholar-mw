package net.thevpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.geom.GeomUtils;
import net.thevpc.scholar.hadrumaths.geom.Triangle;

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

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.ofObj(getClass().getSimpleName());
        sb.add("surface", context.elem(airmax));
        return sb.build();
    }

//    public String dump() {
//        Dumper h = new Dumper(this);
//        h.add("surface", airmax);
//        return h.toString();
//    }

}
