package net.vpc.scholar.hadrumaths.meshalgo.triflip;

import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.List;

public class FlipSegmentLengthPrecision implements FlipPrecision {
    double longueur;
    public FlipSegmentLengthPrecision(double lg){
        longueur=lg;
    }
    public boolean isPrecisionValide(List<Triangle> triangles){
        int k=0;
        for (Triangle triangle : triangles) {
            if ((triangle.p1.distance(triangle.p2) > longueur) || (triangle.p1.distance(triangle.p3) > longueur) || (triangle.p3.distance(triangle.p2) > longueur)) {
                k = 1;
            }
        }
        return k == 1;

    }

    public String dump() {
        Dumper h=new Dumper(this);
        h.add("segmentLength",longueur);
        return h.toString();
    }


}
