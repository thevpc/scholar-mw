package net.vpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.geom.Triangle;

import java.util.List;

public class ConsDesTriangleCountPrecision implements ConsDesPrecision {
    private static final long serialVersionUID = 1L;
    int nbre;

    public ConsDesTriangleCountPrecision(int nbr) {
        nbre = nbr;
    }

    public boolean isMeshAllowed(List<Triangle> t, int iteration) {
        return t.size() < nbre;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.obj(getClass().getSimpleName());
        sb.add("count", context.elem(nbre));
        return sb.build();
    }

//    public String dump() {
//        Dumper h = new Dumper(this, Dumper.Type.SIMPLE);
//        h.add("count", nbre);
//        return h.toString();
//    }

}
