package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.DomainScaleTool;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.Shape;

/**
 * Created by vpc on 3/1/17.
 */
public abstract class AbstractGeometry implements Geometry {

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
        return DomainScaleTool.create(getDomain(), Domain.forBounds(0, width, 0, height)).rescale(this);
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
            return DoubleValue.valueOf(1, getDomain());
        }
        return new Shape(1, this);
    }

    @Override
    public Expr multiply(int value) {
        if (isRectangular()) {
            return DoubleValue.valueOf(value, getDomain());
        }
        return new Shape(value, this);
    }

    @Override
    public Expr multiply(double value) {
        if (isRectangular()) {
            return DoubleValue.valueOf(value, getDomain());
        }
        return new Shape(value, this);
    }

    @Override
    public Expr multiply(Expr value) {
        return toExpr().multiply(value);
    }

    @Override
    public boolean containsDomain(Domain geometry) {
        return containsGeometry(geometry.toGeometry());
    }

    @Override
    public boolean containsGeometry(Geometry geometry) {
        Geometry inter = intersectGeometry(geometry);
        return inter.equals(geometry);
    }
}
