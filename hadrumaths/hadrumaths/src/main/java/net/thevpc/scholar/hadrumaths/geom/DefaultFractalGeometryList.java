package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;

/**
 * @author : vpc
 * @creationtime 20 janv. 2006 10:49:49
 */
public abstract class DefaultFractalGeometryList extends DefaultGeometryList implements FractalAreaGeometryList {

    protected int level;
    protected Geometry basePolygon;

    public DefaultFractalGeometryList(int level, Geometry basePolygon) {
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
            Polygon[] motif = getMotif();
            for (Polygon aMotif : motif) {
                add(aMotif);
            }
        } else {
            Geometry[] transform = getTransform();
            for (Geometry r : transform) {
                FractalAreaGeometryList polygon = newInstance(
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

    public abstract Polygon[] getMotif();

    public abstract DefaultFractalGeometryList newInstance(int level, Geometry domain);

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder r = super.toTsonElement(context).toObject().builder();
        return r.add("level", context.elem(level))
                .add("base", context.elem(basePolygon))
                .build();
    }

    @Override
    public FractalAreaGeometryList clone() {
        return (FractalAreaGeometryList) super.clone();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        rebuild();
    }

    public Geometry getBasePolygon() {
        return basePolygon;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
