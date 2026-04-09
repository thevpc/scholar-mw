package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.DomainScaleTool;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.meshalgo.tri.JTSHelper;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Shape2D;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 */
public abstract class AbstractHGeometry implements HGeometry {

    private Map<String, NElement> properties;
    @Override
    public HGeometry clone() {
        try {
            return (HGeometry) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public HGeometry scale(Domain newDomain) {
        return DomainScaleTool.create(getDomain(), newDomain).rescale(this);
    }

    public HGeometry scale(int width, int height) {
        return DomainScaleTool.create(getDomain(), Domain.ofBounds(0, width, 0, height)).rescale(this);
    }

    @Override
    public double area() {
        return JTSHelper.getArea(this);
    }

    @Override
    public HGeometry intersectGeometry(HGeometry geometry) {
        return JTSHelper.fromJtsGeometry(JTSHelper.intersect(JTSHelper.toJtsGeometry(this),JTSHelper.toJtsGeometry(geometry)));
    }

    @Override
    public HGeometry subtractGeometry(HGeometry geometry) {
        return JTSHelper.fromJtsGeometry(JTSHelper.minus(JTSHelper.toJtsGeometry(this),JTSHelper.toJtsGeometry(geometry)));
    }

    @Override
    public HGeometry addGeometry(HGeometry geometry) {
        return JTSHelper.fromJtsGeometry(JTSHelper.union(JTSHelper.toJtsGeometry(this),JTSHelper.toJtsGeometry(geometry)));
    }

    @Override
    public HGeometry exclusiveOrGeometry(HGeometry geometry) {
        return JTSHelper.fromJtsGeometry(JTSHelper.exclusiveOr(JTSHelper.toJtsGeometry(this),JTSHelper.toJtsGeometry(geometry)));
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
    public Map<String, NElement> getProperties() {
        if (properties == null) {
            properties = new HashMap<String, NElement>();
        }
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractHGeometry that = (AbstractHGeometry) o;
        return Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public boolean containsGeometry(HGeometry geometry) {
        HGeometry inter = intersectGeometry(geometry);
        return inter.equals(geometry);
    }
}
