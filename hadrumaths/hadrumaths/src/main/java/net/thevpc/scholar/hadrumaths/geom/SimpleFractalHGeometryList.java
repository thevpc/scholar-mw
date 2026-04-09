package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.GeometryFactory;

public abstract class SimpleFractalHGeometryList extends DefaultFractalHGeometryList {

    public SimpleFractalHGeometryList(int level, Domain domain) {
        super(level, domainToPolygon(domain));
        rebuild();
    }

    public static HPolygon domainToPolygon(Domain domain) {
        return domainToPolygon(domain, 1);
    }

    public static HPolygon domainToPolygon(Domain domain, int color) {
        return GeometryFactory.createPolygon(
                new HPoint(domain.xmin(),domain.ymin()),
                new HPoint(domain.xmax(),domain.ymin()),
                new HPoint(domain.xmax(),domain.ymax()),
                new HPoint(domain.xmin(),domain.ymax())
        );
    }

    @Override
    public DefaultFractalHGeometryList newInstance(int level, HGeometry domain) {
        return newInstance(level, domain.getDomain());
    }

    public DefaultFractalHGeometryList newInstance(int level, Domain domain) {
        DefaultFractalHGeometryList polygonList = (DefaultFractalHGeometryList) this.clone();
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
    public abstract HPolygon[] getMotif();
}
