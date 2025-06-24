package net.thevpc.scholar.hadrumaths;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.tson.TsonSerializable;
import net.thevpc.scholar.hadrumaths.geom.*;

import java.awt.geom.Path2D;

class DomainGeometry extends AbstractGeometry implements Cloneable,TsonSerializable {
    private final Domain domain;

    public DomainGeometry(Domain domain) {
        this.domain = domain;
        if (domain.getDimension() != 2 || domain.isInfinite()) {
            throw new IllegalArgumentException("Invalid Geometry");
        }
    }

    @Override
    public Path2D.Double getPath() {
        return domain.getPath();
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public boolean isRectangular() {
        return true;
    }

    @Override
    public boolean isPolygonal() {
        return true;
    }

    @Override
    public boolean isTriangular() {
        return false;
    }

    @Override
    public boolean isSingular() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return domain.isEmpty();
    }

    @Override
    public Geometry translateGeometry(double x, double y) {
        return domain.translateGeometry(x, y);
    }

    @Override
    public boolean contains(double x, double y) {
        return domain.contains(x, y);
    }

    @Override
    public Polygon toPolygon() {
        return domain.toPolygon();
    }

    @Override
    public Polygon[] toPolygons() {
        return new Polygon[]{toPolygon()};
    }

    @Override
    public Triangle toTriangle() {
        return domain.toTriangle();
    }

    @Override
    public int hashCode() {
        return domain != null ? domain.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomainGeometry that = (DomainGeometry) o;

        return domain != null ? domain.equals(that.domain) : that.domain == null;
    }

    @Override
    public String toString() {
        return domain.toString();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofObjectBuilder("Geometry").add(context.elem(domain)).build();
    }
}
