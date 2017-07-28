package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Maths;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpc on 8/2/14.
 */
public class RegularPolygon extends AbstractGeometry implements PolygonBuilder {
    private Point center;
    private double radius;
    private int sides;
    private float arcRatio;
    private double phase;

    @Override
    public Geometry translateGeometry(double x, double y) {
        RegularPolygon poly = new RegularPolygon();
        poly.setCenter(center.translate(x, y));
        poly.setSides(sides);
        poly.setArcRatio(arcRatio);
        poly.setPhase(phase);
        return poly;
    }
    public Point getCenter() {
        return center;
    }

    public RegularPolygon setCenter(double x, double y) {
        return setCenter(Point.create(x, y));
    }

    public RegularPolygon setCenter(Point center) {
        this.center = center;
        return this;
    }

    public double getRadius() {
        return radius;
    }

    public RegularPolygon setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public int getSides() {
        return sides;
    }

    public RegularPolygon setSides(int sides) {
        this.sides = sides;
        return this;
    }

    public float getArcRatio() {
        return arcRatio;
    }

    public RegularPolygon setArcRatio(float arcRatio) {
        this.arcRatio = arcRatio;
        return this;
    }

    public double getPhase() {
        return phase;
    }

    public RegularPolygon setPhase(double phase) {
        this.phase = phase;
        return this;
    }

    @Override
    public Polygon toPolygon() {
        if (center == null) {
            throw new IllegalArgumentException("Missing center");
        }
        if (sides < 3) {
            throw new IllegalArgumentException("Complexity must be >2 ");
        }
        if (radius < 0 || Double.isInfinite(radius) || Double.isNaN(radius)) {
            throw new IllegalArgumentException("invalid radius " + radius);
        }
        int max = (int) Math.ceil(getValidArcRatio() * sides);
        if (max < 3) {
            throw new IllegalArgumentException("ratio too low " + arcRatio);
        }
        if (max == sides) {
            max = sides - 1;
        }
        List<Point> all = new ArrayList<Point>();
        double dblpi = 2 * Math.PI;
        for (int i = 0; i <= max; i++) {
            double x = center.x + radius * Maths.cos2(i * dblpi / sides + phase);
            double y = center.y + radius * Maths.sin2(i * dblpi / sides + phase);
            all.add(Point.create(x, y));
        }
        return new Polygon(all.toArray(new Point[all.size()]));
    }

    @Override
    public boolean contains(double x, double y) {
        return toPolygon().contains(x, y);
    }

    @Override
    public Domain getDomain() {
        return toPolygon().getDomain();
    }

    @Override
    public boolean isRectangular() {
        return sides == 4 && getValidArcRatio() == 1.0 && toPolygon().isRectangular();
    }


    public double getValidArcRatio() {
        double arcRatio = this.arcRatio;
        if (arcRatio <= 0) {
            arcRatio = 1;
        }
        if (arcRatio > 1) {
            arcRatio = 1;
        }
        return arcRatio;
    }

    @Override
    public boolean isPolygonal() {
        return true;
    }

    @Override
    public boolean isTriangular() {
        return sides == 3;
    }

    @Override
    public Geometry clone() {
        return (Geometry) super.clone();
    }

    @Override
    public Surface toSurface() {
        return toPolygon().toSurface();
    }

    @Override
    public boolean isSingular() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return toSurface().isEmpty();
    }

    @Override
    public Triangle toTriangle() {
        throw new IllegalArgumentException("Not Triangular");
    }


    @Override
    public Path2D.Double getPath() {
        return toPolygon().getPath();
    }
}
