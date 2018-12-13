package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 12 juin 2007 11:37:42
 */
public class FractalDPloygonListHolder extends DefaultGeometryList implements FractalAreaGeometryList, Cloneable {
    private FractalAreaGeometryList base;
    private Point translation;


    public FractalDPloygonListHolder(FractalAreaGeometryList base, Domain domain, Point translation) {
        super(domain);
        this.base = base;
        this.translation = translation;
    }

    @Override
    public Dumper getDumpStringHelper() {
        Dumper h = super.getDumpStringHelper();
        h.add("base", base);
        h.add("translation", translation);
        return h;
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
