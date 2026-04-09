package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;


/**
 * @author : vpc
 * @creationtime 20 janv. 2006 10:49:49
 */
public abstract class DefaultFractalHGeometryList extends DefaultHGeometryList implements FractalAreaHGeometryList {

    protected int level;
    protected HGeometry basePolygon;

    public DefaultFractalHGeometryList(int level, HGeometry basePolygon) {
        super(basePolygon.getDomain());
        this.level = level;
        this.basePolygon = basePolygon;
        rebuild();
    }

    public void rebuild() {
        clear();
        if (level < 0) {
            throw new IllegalArgumentException("Impossible");
            //dans le cas ou on veut modeliser
//            DPolygon[] eval = getTransform();
//            for (DPolygon rectangle2D : eval) {
//                add(rectangle2D
////                        new Polygon(
////                        new double[]{rectangle2D.getX(), rectangle2D.getX() + rectangle2D.getWidth(), rectangle2D.getX() + rectangle2D.getWidth(), rectangle2D.getX()},
////                        new double[]{rectangle2D.getY(), rectangle2D.getY(), rectangle2D.getY() + rectangle2D.getHeight(), rectangle2D.getY() + rectangle2D.getHeight()}
////                )
//                );
//            }
        } else if (level == 0) {
            HPolygon[] motif = getMotif();
            for (HPolygon aMotif : motif) {
                add(aMotif);
            }
        } else {
            HGeometry[] transform = getTransform();
            for (HGeometry r : transform) {
                FractalAreaHGeometryList polygon = newInstance(
                        level - 1,
                        r
                );
                if (polygon == null) {
                    add(r);
                } else {
                    addAll(polygon);
                }
            }
        }
    }

//    @Override
//    public Dumper getDumpStringHelper() {
//        Dumper h = super.getDumpStringHelper();
//        h.add("level", level);
//        h.add("base", basePolygon);
//        return h;
//    }

    public abstract HPolygon[] getMotif();

    public abstract DefaultFractalHGeometryList newInstance(int level, HGeometry domain);

    @Override
    public NElement toElement() {
        NObjectElementBuilder r = super.toElement().toObject().get().builder();
        return r.add("level", NElementHelper.elem(level))
                .add("base", NElementHelper.elem(basePolygon))
                .build();
    }

    @Override
    public FractalAreaHGeometryList clone() {
        return (FractalAreaHGeometryList) super.clone();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        rebuild();
    }

    public HGeometry getBasePolygon() {
        return basePolygon;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
