package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;

public abstract class SimpleFractalGeometryList extends DefaultFractalGeometryList {

    public SimpleFractalGeometryList(int level, Domain domain) {
        super(level, domainToPolygon(domain));
        rebuild();
    }

    public static Polygon domainToPolygon(Domain domain) {
        return domainToPolygon(domain, 1);
    }

    public static Polygon domainToPolygon(Domain domain, int color) {
        return new Polygon(
                new double[]{domain.xmin(), domain.xmax(), domain.xmax(), domain.xmin()},
                new double[]{domain.ymin(), domain.ymin(), domain.ymax(), domain.ymax()},
                color
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
