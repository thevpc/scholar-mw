package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.GeometryFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 8/2/14.
 */
public class RegularHPolygon extends AbstractHGeometry implements HPolygonBuilder {
    private HPoint center;
    private double radius;
    private int sides;
    private float arcRatio;
    private double phase;

    public HPoint getCenter() {
        return center;
    }


    @Override
    public NElement toElement() {
        NObjectElementBuilder b = NElement.ofObjectBuilder("RegularPolygon");
        b.add("center",NElement.ofUplet(
                NElement.ofDouble(center.getX()),
                NElement.ofDouble(center.getY())
        ));
        b.add("radius",radius);
        b.add("sides",sides);
        b.add("arcRatio",arcRatio);
        b.add("phase",phase);
        b.addIf("properties", NElementHelper.elem(getProperties()), NElementHelper.blankPredicate());
        return b.build();
    }


    public RegularHPolygon setCenter(HPoint center) {
        this.center = center;
        return this;
    }

    public RegularHPolygon setCenter(double x, double y) {
        return setCenter(HPoint.create(x, y));
    }

    public double getRadius() {
        return radius;
    }

    public RegularHPolygon setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public int getSides() {
        return sides;
    }

    public RegularHPolygon setSides(int sides) {
        this.sides = sides;
        return this;
    }

    public float getArcRatio() {
        return arcRatio;
    }

    public RegularHPolygon setArcRatio(float arcRatio) {
        this.arcRatio = arcRatio;
        return this;
    }

    public double getPhase() {
        return phase;
    }

    public RegularHPolygon setPhase(double phase) {
        this.phase = phase;
        return this;
    }

    @Override
    public HGeometry clone() {
        return super.clone();
    }

    @Override
    public Path2D.Double getPath() {
        return toPolygon().getPath();
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
        return sides == 3 && getValidArcRatio() == 1.0;
    }

    @Override
    public boolean isSingular() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return toPolygon().isEmpty();
    }

    @Override
    public HGeometry translate(double x, double y) {
        RegularHPolygon poly = new RegularHPolygon();
        poly.setCenter(center.translate(x, y));
        poly.setSides(sides);
        poly.setArcRatio(arcRatio);
        poly.setPhase(phase);
        poly.setRadius(radius);
        return poly;
    }

    @Override
    public boolean contains(double x, double y) {
        return toPolygon().contains(x, y);
    }

    @Override
    public HPolygon toPolygon() {
        if (center == null) {
            throw new IllegalArgumentException("Missing center");
        }
        if (sides < 3) {
            throw new IllegalArgumentException("Complexity must be >2 ");
        }
        if (radius < 0 || Double.isInfinite(radius) || Double.isNaN(radius)) {
            throw new IllegalArgumentException("invalid radius " + radius);
        }
        int max = (int) Maths.ceil(getValidArcRatio() * sides);
        if (max < 3) {
            throw new IllegalArgumentException("ratio too low " + arcRatio);
        }
        if (max == sides) {
            max = sides - 1;
        }
        List<HPoint> all = new ArrayList<HPoint>();
        double dblpi = 2 * Maths.PI;
        for (int i = 0; i <= max; i++) {
            double x = center.x + radius * Maths.cos2(i * dblpi / sides + phase);
            double y = center.y + radius * Maths.sin2(i * dblpi / sides + phase);
            all.add(HPoint.create(x, y));
        }
        return GeometryFactory.createPolygon(all.toArray(new HPoint[0]));
    }

    @Override
    public HTriangle toTriangle() {
        throw new IllegalArgumentException("Not Triangular");
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
    public HPolygon[] toPolygons() {
        return new HPolygon[]{toPolygon()};
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegularHPolygon that = (RegularHPolygon) o;
        return Double.compare(radius, that.radius) == 0 && sides == that.sides && Float.compare(arcRatio, that.arcRatio) == 0 && Double.compare(phase, that.phase) == 0 && Objects.equals(center, that.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), center, radius, sides, arcRatio, phase);
    }
}
