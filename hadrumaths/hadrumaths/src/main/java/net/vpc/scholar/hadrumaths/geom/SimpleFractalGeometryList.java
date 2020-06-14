package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.GeometryFactory;

public abstract class SimpleFractalGeometryList extends DefaultFractalGeometryList {

    public SimpleFractalGeometryList(int level, Domain domain) {
        super(level, domainToPolygon(domain));
        rebuild();
    }

    public static Polygon domainToPolygon(Domain domain) {
        return domainToPolygon(domain, 1);
    }

    public static Polygon domainToPolygon(Domain domain, int color) {
        return GeometryFactory.createPolygon(
                new Point(domain.xmin(),domain.ymin()),
                new Point(domain.xmax(),domain.ymin()),
                new Point(domain.xmax(),domain.ymax()),
                new Point(domain.xmin(),domain.ymax())
        );
    }

    @Override
    public DefaultFractalGeometryList newInstance(int level, Geometry domain) {
        return newInstance(level, domain.getDomain());
    }

    public DefaultFractalGeometryList newInstance(int level, Domain domain) {
        DefaultFractalGeometryList polygonList = (DefaultFractalGeometryList) this.clone();
        polygonList.setDomain(domain);
        polygonList.setLevel(level);
        polygonList.rebuild();
        return polygonList;
//        try {
//            return getClass().getConstructor(Integer.TYPE, DomainXY.class).newInstance(level, domain);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public abstract Polygon[] getMotif();
}
