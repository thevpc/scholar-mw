package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.DomainScaleTool;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Shape2D;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by vpc on 3/1/17.
 */
public abstract class AbstractGeometry implements Geometry {

    private Map<String, Object> properties;
    @Override
    public Geometry clone() {
        try {
            return (Geometry) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Geometry scale(Domain newDomain) {
        return DomainScaleTool.create(getDomain(), newDomain).rescale(this);
    }

    public Geometry scale(int width, int height) {
        return DomainScaleTool.create(getDomain(), Domain.ofBounds(0, width, 0, height)).rescale(this);
    }

    @Override
    public Geometry intersectGeometry(Geometry geometry) {
        return toSurface().intersectGeometry(geometry);
    }

    @Override
    public Geometry subtractGeometry(Geometry geometry) {
        return toSurface().subtractGeometry(geometry);
    }

    @Override
    public Geometry addGeometry(Geometry geometry) {
        return toSurface().addGeometry(geometry);
    }

    @Override
    public Geometry exclusiveOrGeometry(Geometry geometry) {
        return toSurface().exclusiveOrGeometry(geometry);
    }

    @Override
    public Surface toSurface() {
        return new Surface(getPath());
    }

    @Override
    public Expr toExpr() {
        if (isRectangular()) {
            return Maths.expr(1, getDomain());
        }
        return new Shape2D(1, this);
    }

    @Override
    public Expr mul(int value) {
        if (isRectangular()) {
            return Maths.expr(value, getDomain());
        }
        return new Shape2D(value, this);
    }

    @Override
    public Expr mul(double value) {
        if (isRectangular()) {
            return Maths.expr(value, getDomain());
        }
        return new Shape2D(value, this);
    }

    @Override
    public Expr mul(Expr value) {
        return toExpr().mul(value);
    }

    @Override
    public boolean containsDomain(Domain geometry) {
        return containsGeometry(geometry.toGeometry());
    }

    @Override
    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractGeometry that = (AbstractGeometry) o;
        return Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public boolean containsGeometry(Geometry geometry) {
        Geometry inter = intersectGeometry(geometry);
        return inter.equals(geometry);
    }
}
