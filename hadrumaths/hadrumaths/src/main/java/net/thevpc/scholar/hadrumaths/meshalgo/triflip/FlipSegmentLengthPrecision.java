package net.thevpc.scholar.hadrumaths.meshalgo.triflip;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.geom.Triangle;

import java.util.List;

public class FlipSegmentLengthPrecision implements FlipPrecision {
    double longueur;

    public FlipSegmentLengthPrecision(double lg) {
        longueur = lg;
    }

    public boolean isPrecisionValide(List<Triangle> triangles) {
        int k = 0;
        for (Triangle triangle : triangles) {
            if ((triangle.p1.distance(triangle.p2) > longueur) || (triangle.p1.distance(triangle.p3) > longueur) || (triangle.p3.distance(triangle.p2) > longueur)) {
                k = 1;
            }
        }
        return k == 1;

    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.ofObj(getClass().getSimpleName());
        sb.add("segmentLength", context.elem(longueur));
        return sb.build();
    }
//    public String dump() {
//        Dumper h = new Dumper(this);
//        h.add("segmentLength", longueur);
//        return h.toString();
//    }


}
