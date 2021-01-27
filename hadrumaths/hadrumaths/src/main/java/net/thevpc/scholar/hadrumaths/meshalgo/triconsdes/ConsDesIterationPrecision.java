package net.thevpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.geom.Triangle;

import java.util.List;


public class ConsDesIterationPrecision implements ConsDesPrecision {
    private static final long serialVersionUID = 1L;
    int iteration;

    public ConsDesIterationPrecision(int iteration) {
        this.iteration = iteration <= 0 ? 0 : iteration;
    }

    public boolean isMeshAllowed(List<Triangle> t, int iteration) {
        return iteration <= this.iteration;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.obj(getClass().getSimpleName());
        sb.add("complexity", context.elem(iteration));
        return sb.build();
    }

//    public String dump() {
//        Dumper h = new Dumper(this);
//        h.add("iteration", iteration);
//        return h.toString();
//    }

}
