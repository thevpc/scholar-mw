package net.vpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.geom.GeomUtils;
import net.vpc.scholar.hadrumaths.geom.Triangle;

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
        TsonObjectBuilder sb = Tson.obj(getClass().getSimpleName());
        sb.add("surface", context.elem(airmax));
        return sb.build();
    }

//    public String dump() {
//        Dumper h = new Dumper(this);
//        h.add("surface", airmax);
//        return h.toString();
//    }

}
