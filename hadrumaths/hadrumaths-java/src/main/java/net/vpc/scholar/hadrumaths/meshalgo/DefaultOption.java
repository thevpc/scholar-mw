package net.vpc.scholar.hadrumaths.meshalgo;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.geom.Triangle;

import java.util.List;

public class DefaultOption implements MeshOptions {
    private static final long serialVersionUID = 1L;
    protected EnhancedMeshPolygons enhancedMeshZone;

    public boolean isMeshAllowed(List<Triangle> t, int iteration) {
        return false;
    }

    public Triangle selectMeshTriangle(List<Triangle> t, int iteration) {
        return null;
    }

    public void setZone(EnhancedMeshPolygons zone) {
        enhancedMeshZone = zone;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj(getClass().getSimpleName())
                .add("enhancedMeshZone", context.elem(enhancedMeshZone))
                .build();
    }

//    public final String dump() {
//        return getDumpStringHelper().toString();
//    }
//
//    public Dumper getDumpStringHelper() {
//        Dumper h = new Dumper(getClass().getSimpleName());
//        h.add("enhancedMeshZone", enhancedMeshZone);
//        return h;
//    }

}
