package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.MathsBase;

import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpc on 8/2/14.
 */
public class EllipticPolygon extends AbstractGeometry implements PolygonBuilder, Cloneable, Serializable {
    private Point center = new Point(0, 0);
    private double xradius = 1;
    private double yradius = 1;
    private int sides = 24;
    private double arcRatio = 1;
    private double phase;

    public Point getCenter() {
        return center;
    }

    public EllipticPolygon setCenter(Point center) {
        this.center = center;
        return this;
    }

    @Override
    public Geometry translateGeometry(double x, double y) {
        EllipticPolygon ellipticPolygon = new EllipticPolygon();
        ellipticPolygon.setCenter(center.translate(x, y));
        ellipticPolygon.setSides(sides);
        ellipticPolygon.setArcRatio(arcRatio);
        ellipticPolygon.setXRadius(xradius);
        ellipticPolygon.setYRadius(yradius);
        ellipticPolygon.setPhase(phase);
        return ellipticPolygon;
    }

    public EllipticPolygon setCenter(double x, double y) {
        return setCenter(Point.create(x, y));
    }

    public double getXRadius() {
        return xradius;
    }

    public EllipticPolygon setRadius(double xradius) {
        return setRadius(xradius, xradius);
    }

    public EllipticPolygon setRadius(double xradius, double yradius) {
        setXRadius(xradius);
        setYRadius(yradius);
        return this;
    }

    public EllipticPolygon setXRadius(double xradius) {
        this.xradius = xradius;
        return this;
    }

    public double getYRadius() {
        return yradius;
    }

    public EllipticPolygon setYRadius(double yradius) {
        this.yradius = yradius;
        return this;
    }

    public int getSides() {
        return sides;
    }

    public EllipticPolygon setSides(int sides) {
        this.sides = sides;
        return this;
    }

    public double getArcRatio() {
        return arcRatio;
    }

    public EllipticPolygon setArcRatio(double arcRatio) {
        this.arcRatio = arcRatio;
        return this;
    }

    public double getPhase() {
        return phase;
    }

    public EllipticPolygon setPhase(double phase) {
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
        if (xradius < 0 || Double.isInfinite(xradius) || Double.isNaN(xradius)) {
            throw new IllegalArgumentException("invalid xradius " + xradius);
        }
        if (xradius < 0 || Double.isInfinite(xradius) || Double.isNaN(xradius)) {
            throw new IllegalArgumentException("invalid xradius " + xradius);
        }
        List<Point> all = new ArrayList<Point>();
        double arcRatio = getValidArcRatio();
        int max = (int) MathsBase.ceil(arcRatio * sides);
        if (max < 3) {
            throw new IllegalArgumentException("ratio too low " + arcRatio);
        }
        if (max == sides) {
            max = sides - 1;
        }
        double dblpi = 2 * MathsBase.PI;
        for (int i = 0; i <= max; i++) {
            if ((((double) i) / sides) > arcRatio) {
                break;
            }
            double x = center.x + xradius * MathsBase.cos2(i * dblpi / sides + phase);
            double y = center.y + yradius * MathsBase.sin2(i * dblpi / sides + phase);
            all.add(Point.create(x, y));
        }
        return new Polygon(all.toArray(new Point[0]));
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

    @Override
    public boolean isPolygonal() {
        return true;
    }

    @Override
    public boolean isTriangular() {
        return toPolygon().isTriangular();
    }

    @Override
    public Geometry clone() {
        return (Geometry) super.clone();
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
