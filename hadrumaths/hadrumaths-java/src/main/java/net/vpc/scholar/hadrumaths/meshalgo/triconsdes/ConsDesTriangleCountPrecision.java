package net.vpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.List;

public class ConsDesTriangleCountPrecision implements ConsDesPrecision {
    private static final long serialVersionUID = 1L;
    int nbre;
    public ConsDesTriangleCountPrecision(int nbr){
        nbre=nbr;
    }
    public boolean isMeshAllowed(List<Triangle> t, int iteration){
        return t.size() < nbre;
    }

    public String dump() {
        Dumper h=new Dumper(this,Dumper.Type.SIMPLE);
        h.add("count",nbre);
        return h.toString();
    }

}
