package net.vpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.List;


public class ConsDesIterationPrecision implements ConsDesPrecision {
    private static final long serialVersionUID = -1010101010101001023L;
    int iteration;

    public ConsDesIterationPrecision(int iteration) {
        this.iteration = iteration <= 0 ? 0 : iteration;
    }

    public boolean isMeshAllowed(List<Triangle> t, int iteration) {
        return iteration<=this.iteration;
    }

    public String dump() {
        Dumper h = new Dumper(this);
        h.add("iteration", iteration);
        return h.toString();
    }

}
