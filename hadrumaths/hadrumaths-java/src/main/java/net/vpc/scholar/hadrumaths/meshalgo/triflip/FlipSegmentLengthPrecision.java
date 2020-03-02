package net.vpc.scholar.hadrumaths.meshalgo.triflip;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.geom.Triangle;

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
        TsonObjectBuilder sb = Tson.obj(getClass().getSimpleName());
        sb.add("segmentLength", context.elem(longueur));
        return sb.build();
    }
//    public String dump() {
//        Dumper h = new Dumper(this);
//        h.add("segmentLength", longueur);
//        return h.toString();
//    }


}
