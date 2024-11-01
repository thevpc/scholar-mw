package net.thevpc.scholar.hadrumaths.meshalgo;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.geom.Triangle;

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
        return Tson.ofObj(getClass().getSimpleName())
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
