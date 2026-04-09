package net.thevpc.scholar.hadrumaths.meshalgo.triflip;


import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.util.List;

public class FlipSegmentLengthPrecision implements FlipPrecision {
    double longueur;

    public FlipSegmentLengthPrecision(double lg) {
        longueur = lg;
    }

    public boolean isPrecisionValide(List<HTriangle> triangles) {
        int k = 0;
        for (HTriangle triangle : triangles) {
            if (triangle.longestEdge() > longueur) {
                k = 1;
            }
        }
        return k == 1;

    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder sb = NElement.ofObjectBuilder(getClass().getSimpleName());
        sb.add("segmentLength", NElementHelper.elem(longueur));
        return sb.build();
    }
//    public String dump() {
//        Dumper h = new Dumper(this);
//        h.add("segmentLength", longueur);
//        return h.toString();
//    }


}
