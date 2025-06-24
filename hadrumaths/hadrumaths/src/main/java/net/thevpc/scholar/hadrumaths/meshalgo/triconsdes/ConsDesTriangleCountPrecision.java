package net.thevpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.geom.Triangle;

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
        TsonObjectBuilder sb = Tson.ofObjectBuilder(getClass().getSimpleName());
        sb.add("count", context.elem(nbre));
        return sb.build();
    }

//    public String dump() {
//        Dumper h = new Dumper(this, Dumper.Type.SIMPLE);
//        h.add("count", nbre);
//        return h.toString();
//    }

}
