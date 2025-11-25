package net.thevpc.scholar.hadrumaths.meshalgo.triconsdes;


import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.geom.Triangle;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

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
    public NElement toElement() {
        NObjectElementBuilder sb = NElement.ofObjectBuilder(getClass().getSimpleName());
        sb.add("count", NElementHelper.elem(nbre));
        return sb.build();
    }

//    public String dump() {
//        Dumper h = new Dumper(this, Dumper.Type.SIMPLE);
//        h.add("count", nbre);
//        return h.toString();
//    }

}
