package net.vpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.geom.Triangle;

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
