package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 12 juin 2007 11:37:42
 */
public class FractalDPloygonListHolder extends DefaultHGeometryList implements FractalAreaHGeometryList, Cloneable {
    private final FractalAreaHGeometryList base;
    private final HPoint translation;


    public FractalDPloygonListHolder(FractalAreaHGeometryList base, Domain domain, HPoint translation) {
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
    public NElement toElement() {
        NObjectElementBuilder r = super.toElement().toObject().get().builder();
        return r.add("translation", NElementHelper.elem(translation))
                .add("base", NElementHelper.elem(base))
                .build();
    }

    @Override
    public FractalAreaHGeometryList clone() {
        return (FractalAreaHGeometryList) super.clone();
    }

    public int getLevel() {
        return base.getLevel();
    }

    public void setLevel(int level) {
        base.setLevel(level);
        clear();
        for (HGeometry polygon : base) {
            add(polygon.translate(translation.x, translation.y));
        }
    }


    public HGeometry[] getTransform() {
        HGeometry[] polygons = base.getTransform();
        for (int i = 0; i < polygons.length; i++) {
            polygons[i] = polygons[i].translate(translation.x, translation.y);
        }
        return polygons;
    }

}
