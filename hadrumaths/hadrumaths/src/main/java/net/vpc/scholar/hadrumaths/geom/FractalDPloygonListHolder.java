package net.vpc.scholar.hadrumaths.geom;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.Domain;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 12 juin 2007 11:37:42
 */
public class FractalDPloygonListHolder extends DefaultGeometryList implements FractalAreaGeometryList, Cloneable {
    private final FractalAreaGeometryList base;
    private final Point translation;


    public FractalDPloygonListHolder(FractalAreaGeometryList base, Domain domain, Point translation) {
        super(domain);
        this.base = base;
        this.translation = translation;
    }

//    @Override
//    public Dumper getDumpStringHelper() {
//        Dumper h = super.getDumpStringHelper();
//        h.add("base", base);
//        h.add("translation", translation);
//        return h;
//    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder r = super.toTsonElement(context).toObject().builder();
        return r.add("translation", context.elem(translation))
                .add("base", context.elem(base))
                .build();
    }

    @Override
    public FractalAreaGeometryList clone() {
        return (FractalAreaGeometryList) super.clone();
    }

    public int getLevel() {
        return base.getLevel();
    }

    public void setLevel(int level) {
        base.setLevel(level);
        clear();
        for (Geometry polygon : base) {
            add(polygon.translateGeometry(translation.x, translation.y));
        }
    }


    public Geometry[] getTransform() {
        Geometry[] polygons = base.getTransform();
        for (int i = 0; i < polygons.length; i++) {
            polygons[i] = polygons[i].translateGeometry(translation.x, translation.y);
        }
        return polygons;
    }

}
