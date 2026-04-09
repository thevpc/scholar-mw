package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.nuts.elem.NUpletElement;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.GeometryFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 8/2/14.
 */
public class EllipticHPolygon extends AbstractHGeometry implements HPolygonBuilder, Cloneable, Serializable {
    private HPoint center = new HPoint(0, 0);
    private double xradius = 1;
    private double yradius = 1;
    private int sides = 24;
    private double arcRatio = 1;
    private double phase;


    @Override
    public NElement toElement() {
        NObjectElementBuilder b = NElement.ofObjectBuilder("EllipticPolygon");
        b.add("sides", sides);
        b.add("arcRatio", arcRatio);
        b.add("phase", phase);
        b.add("center", ue(center));
        b.add("radius", NElement.ofUplet(
                NElement.ofDouble(xradius),
                NElement.ofDouble(yradius)
        ));
        b.addIf("properties", NElementHelper.elem(getProperties()), NElementHelper.blankPredicate());
        return b.build();
    }

    private NUpletElement ue(HPoint p) {
        return NElement.ofUplet(
                NElement.ofDouble(p.getX()),
                NElement.ofDouble(p.getY())
        );
    }

    public HPoint getCenter() {
        return center;
    }

    public EllipticHPolygon setCenter(HPoint center) {
        this.center = center;
        return this;
    }

    public EllipticHPolygon setCenter(double x, double y) {
        return setCenter(HPoint.create(x, y));
    }

    public double getXRadius() {
        return xradius;
    }

    public EllipticHPolygon setXRadius(double xradius) {
        this.xradius = xradius;
        return this;
    }

    public EllipticHPolygon setRadius(double xradius) {
        return setRadius(xradius, xradius);
    }

    public EllipticHPolygon setRadius(double xradius, double yradius) {
        setXRadius(xradius);
        setYRadius(yradius);
        return this;
    }

    public double getYRadius() {
        return yradius;
    }

    public EllipticHPolygon setYRadius(double yradius) {
        this.yradius = yradius;
        return this;
    }

    public int getSides() {
        return sides;
    }

    public EllipticHPolygon setSides(int sides) {
        this.sides = sides;
        return this;
    }

    public double getArcRatio() {
        return arcRatio;
    }

    public EllipticHPolygon setArcRatio(double arcRatio) {
        this.arcRatio = arcRatio;
        return this;
    }

    public double getPhase() {
        return phase;
    }

    public EllipticHPolygon setPhase(double phase) {
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
        return toPolygon().isTriangular();
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
        EllipticHPolygon ellipticPolygon = new EllipticHPolygon();
        ellipticPolygon.setCenter(center.translate(x, y));
        ellipticPolygon.setSides(sides);
        ellipticPolygon.setArcRatio(arcRatio);
        ellipticPolygon.setXRadius(xradius);
        ellipticPolygon.setYRadius(yradius);
        ellipticPolygon.setPhase(phase);
        return ellipticPolygon;
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
        if (xradius < 0 || Double.isInfinite(xradius) || Double.isNaN(xradius)) {
            throw new IllegalArgumentException("invalid xradius " + xradius);
        }
        if (yradius < 0 || Double.isInfinite(yradius) || Double.isNaN(yradius)) {
            throw new IllegalArgumentException("invalid yradius " + yradius);
        }
        List<HPoint> all = new ArrayList<HPoint>();
        double arcRatio = getValidArcRatio();
        int max = (int) Maths.ceil(arcRatio * sides);
        if (max < 3) {
            throw new IllegalArgumentException("ratio too low " + arcRatio);
        }
        if (max == sides) {
            max = sides - 1;
        }
        double dblpi = 2 * Maths.PI;
        for (int i = 0; i <= max; i++) {
            if ((((double) i) / sides) > arcRatio) {
                break;
            }
            double x = center.x + xradius * Maths.cos2(i * dblpi / sides + phase);
            double y = center.y + yradius * Maths.sin2(i * dblpi / sides + phase);
            all.add(HPoint.create(x, y));
        }
        return GeometryFactory.createPolygon(all.toArray(new HPoint[0]));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EllipticHPolygon that = (EllipticHPolygon) o;
        return Double.compare(xradius, that.xradius) == 0 && Double.compare(yradius, that.yradius) == 0 && sides == that.sides && Double.compare(arcRatio, that.arcRatio) == 0 && Double.compare(phase, that.phase) == 0 && Objects.equals(center, that.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), center, xradius, yradius, sides, arcRatio, phase);
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
}
