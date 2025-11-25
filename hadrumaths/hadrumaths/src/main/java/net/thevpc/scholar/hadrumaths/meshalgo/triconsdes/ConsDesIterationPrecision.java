package net.thevpc.scholar.hadrumaths.meshalgo.triconsdes;


import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.geom.Triangle;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

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
    public NElement toElement() {
        NObjectElementBuilder sb = NElement.ofObjectBuilder(getClass().getSimpleName());
        sb.add("complexity", NElementHelper.elem(iteration));
        return sb.build();
    }

//    public String dump() {
//        Dumper h = new Dumper(this);
//        h.add("iteration", iteration);
//        return h.toString();
//    }

}
