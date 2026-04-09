package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.symbolic.Range;
import net.thevpc.scholar.hadrumaths.uicomponents.AreaComponent;
import net.thevpc.scholar.hadrumaths.uicomponents.MultiAreaComponent;
import net.thevpc.scholar.hadrumaths.util.OIndex;

import java.util.List;
import java.util.Objects;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 14:29:58
 */
public final class RWG extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;
    public final double max;
    public final HPolygon polygon1;
    public final HPolygon polygon2;
    public final HTriangle tr1;
    public final HTriangle tr2;
    public final Domain domain;
    public final Axis axis;
    public final double edgeLength;

    public RWG(Axis axis, double max, HPolygon polygon) {
        if (!GeomUtils.is4Edges(polygon)) {
            throw new IllegalArgumentException("Not an RWG polygon");
        }
        List<HPoint> points = polygon.getPoints();
        FinalInfo i = init(max,
                GeometryFactory.createPolygon(points.get(0), points.get(1), points.get(3)),
                GeometryFactory.createPolygon(points.get(2), points.get(1), points.get(3))
        );
        this.axis = axis;
        this.max = i.max;
        this.tr1 = i.tr1;
        this.tr2 = i.tr2;
        this.polygon1 = i.polygon1;
        this.polygon2 = i.polygon2;
        this.domain = i.domain;
        edgeLength = tr1.p2().distance(tr1.p3());
    }

    public RWG(Axis axis, double max, HPolygon triange1, HPolygon triange2) {
        FinalInfo i = init(max, triange1, triange2);
        this.axis = axis;
        this.domain = i.domain;
        this.max = i.max;
        this.tr1 = i.tr1;
        this.tr2 = i.tr2;
        this.polygon1 = i.polygon1;
        this.polygon2 = i.polygon2;
        edgeLength = tr1.p2().distance(tr1.p3());
    }

    private RWG(Axis axis, double max,
                Domain domain,
                HPolygon polygon1,
                HPolygon polygon2,
                HTriangle tr1,
                HTriangle tr2) {
        this.axis = axis;
        this.domain = domain;
        this.max = max;
        this.tr1 = tr1;
        this.tr2 = tr2;
        this.polygon1 = polygon1;
        this.polygon2 = polygon2;
        edgeLength = tr1.p2().distance(tr1.p3());
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Rwg");
    }

    private FinalInfo init(double max, HPolygon triangle1, HPolygon triangle2) {
        FinalInfo i = new FinalInfo();
        i.max = max;
        i.domain = max == 0 ? Domain.EMPTYXY : triangle1.getDomain().expand(triangle2.getDomain());
//        name=("RWGFunctionXY");
        if (triangle1.isTriangular() && triangle2.isTriangular()) {
            i.polygon1 = triangle1;
            i.polygon2 = triangle2;
            HTriangle t1 = triangle1.toTriangle();
            HTriangle t2 = triangle2.toTriangle();
            OIndex x, y;
            if ((x = t2.indexOfPoint(t1.p1())).value() > 0 && (y = t2.indexOfPoint(t1.p2())).value() > 0) {
                i.tr1 = new DefaultHTriangle(t1.p3(), t1.p1(), t1.p2());
                i.tr2 = new DefaultHTriangle(t2.getPoint((x.value() != 1 && y.value() != 1) ? OIndex._1 : (x.value() != 2 && y.value() != 2) ? OIndex._2 : OIndex._3), t2.getPoint(x), t2.getPoint(y));
            } else if ((x = t2.indexOfPoint(t1.p2())).value() > 0 && (y = t2.indexOfPoint(t1.p3())).value() > 0) {
                i.tr1 = new DefaultHTriangle(t1.p1(), t1.p2(), t1.p3());
                i.tr2 = new DefaultHTriangle(t2.getPoint((x.value() != 1 && y.value() != 1) ? OIndex._1 : (x.value() != 2 && y.value() != 2) ? OIndex._2 : OIndex._3), t2.getPoint(x), t2.getPoint(y));
            } else if ((x = t2.indexOfPoint(t1.p3())).value() > 0 && (y = t2.indexOfPoint(t1.p1())).value() > 0) {
                i.tr1 = new DefaultHTriangle(t1.p2(), t1.p3(), t1.p1());
                i.tr2 = new DefaultHTriangle(t2.getPoint((x.value() != 1 && y.value() != 1) ? OIndex._1 : (x.value() != 2 && y.value() != 2) ? OIndex._2 : OIndex._3), t2.getPoint(x), t2.getPoint(y));
            } else {
                throw new IllegalArgumentException("RWG need neighborhood triangles");
            }
        } else {
            throw new IllegalArgumentException("RWG need triangles");
        }

        // After init, verify T+/T- orientation using edge normal
        HPoint M = HPoint.create(
                (i.tr1.p2().x + i.tr1.p3().x) / 2.0,
                (i.tr1.p2().y + i.tr1.p3().y) / 2.0
        );

// Edge vector (p2 -> p3)
        double ex = i.tr1.p3().x - i.tr1.p2().x;
        double ey = i.tr1.p3().y - i.tr1.p2().y;

// Outward normal from tr1 (perpendicular to edge, pointing toward tr1 tip)
        double nx = -ey;
        double ny = ex;

// Vector from M to each tip
        double d1x = i.tr1.p1().x - M.x;
        double d1y = i.tr1.p1().y - M.y;
        double d2x = i.tr2.p1().x - M.x;
        double d2y = i.tr2.p1().y - M.y;

// tr1 tip and tr2 tip must be on OPPOSITE sides of the shared edge
// dot(normal, tip1-M) and dot(normal, tip2-M) must have opposite signs
        double dot1 = nx * d1x + ny * d1y;
        double dot2 = nx * d2x + ny * d2y;

        if (dot1 * dot2 > 0) {
            // Same side — swap
            DefaultHTriangle tmp = i.tr1;
            i.tr1 = i.tr2;
            i.tr2 = tmp;
        }

//        double edgeLength = i.tr1.p2().distance(i.tr1.p3());
//        double fromTr1 = max * (M.x - i.tr1.p1().x) * (edgeLength / (2.0 * i.tr1.area()));
//        double fromTr2 = max * (i.tr2.p1().x - M.x) * (edgeLength / (2.0 * i.tr2.area()));
//        System.out.println("RWG continuity check X: tr1=" + fromTr1 + " tr2=" + fromTr2);
//        System.out.println("  tr1.tip=" + i.tr1.p1() + " tr2.tip=" + i.tr2.p1() + " M=" + M);
        return i;
//        AreaComponent.showDialog("1", triange1.toArea(0.002, 0, 1E5), triange2.toArea(0.002, 0, 1E5));
    }


    @Override
    public HGeometry getDomainGeometry() {
        return this.polygon1.addGeometry(polygon2);
    }


    public HTriangle getTriangle1() {
        return tr1;
    }

    public HTriangle getTriangle2() {
        return tr2;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    public AbstractDoubleToDouble mul(double factor, Domain newDomain) {
        return new RWG(axis, max * factor, newDomain == null ? domain : domain.intersect(newDomain), polygon1, polygon2, tr1, tr2);
//        return new Polyhedron(factor * max,
//                newDomain == null ? domain : domain.intersect(newDomain)
//        );
    }

    public AbstractDoubleToDouble toXOpposite() {
        return this;
    }

    public AbstractDoubleToDouble toYOpposite() {
        return this;
    }

    public AbstractDoubleToDouble getSymmetricX() {
        return this;
    }

    public AbstractDoubleToDouble getSymmetricY() {
        return this;
    }

    public AbstractDoubleToDouble translate(double deltaX, double deltaY) {
        return new RWG(axis, max, domain.translate(deltaX, deltaY),
                (HPolygon) polygon1.translate(deltaX, deltaY),
                (HPolygon) polygon2.translate(deltaX, deltaY),
                (HTriangle) tr1.translate(deltaX, deltaY),
                (HTriangle) tr2.translate(deltaX, deltaY)
        );
//        return new Polyhedron(max, domain.translate(deltaX, deltaY));
    }

    @Override
    public boolean contains(double x) {
        throw new MissingAxisException(Axis.Y);
    }

    @Override
    public boolean contains(double x, double y) {
        if (tr1.contains(x, y)) {
            return true;
        } else return tr2.contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return super.contains(x, y, z);
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        throw new MissingAxisException(Axis.Y);
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        switch (axis) {
            case X:
                return evalX(x, y, defined);
            case Y:
                return evalY(x, y, defined);
        }
        //never
        return 0;
    }


    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        switch (axis) {
            case X:
                return evalX(x, y, defined);
            case Y:
                return evalY(x, y, defined);
        }
        //never
        return 0;
    }


    public double evalX(double x, double y, BooleanMarker defined) {
        if (tr1.contains(x, y)) {
            defined.set();
            return max * (x - tr1.p1().x) * (edgeLength / (2.0 * tr1.area()));
        } else if (tr2.contains(x, y)) {
            defined.set();
            return max * (tr2.p1().x - x) * (edgeLength / (2.0 * tr2.area()));
        }
        return 0;
    }

    public double evalY(double x, double y, BooleanMarker defined) {
        if (tr1.contains(x, y)) {
            defined.set();
            return max * (y - tr1.p1().y) * (edgeLength / (2.0 * tr1.area()));
        } else if (tr2.contains(x, y)) {
            defined.set();
            return max * (tr2.p1().y - y) * (edgeLength / (2.0 * tr2.area()));
        }
        return 0;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return max == 0;
    }

    public boolean isZero() {
        return max == 0;
    }

    public boolean isNaN() {
        return Double.isNaN(max);
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    public boolean isInfinite() {
        return Double.isInfinite(max);
    }

    @Override
    public Expr mul(double other) {
        return new RWG(axis, max * other, domain, polygon1, polygon2, tr1, tr2);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + Double.hashCode(max);
        result = 31 * result + (axis != null ? axis.hashCode() : 0);
        result = 31 * result + (polygon1 != null ? polygon1.hashCode() : 0);
        result = 31 * result + (polygon2 != null ? polygon2.hashCode() : 0);
        result = 31 * result + (tr1 != null ? tr1.hashCode() : 0);
        result = 31 * result + (tr2 != null ? tr2.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RWG)) return false;

        RWG rwg = (RWG) o;

        if (Double.compare(rwg.max, max) != 0) return false;
        if (!Objects.equals(axis, rwg.axis)) return false;
        if (!Objects.equals(polygon1, rwg.polygon1)) return false;
        if (!Objects.equals(polygon2, rwg.polygon2)) return false;
        if (!Objects.equals(tr1, rwg.tr1)) return false;
        return Objects.equals(tr2, rwg.tr2);
    }

    @Override
    public double[] evalDouble(double[] x, Domain d0, Out<Range> range) {
        throw new MissingAxisException(Axis.Y);
    }

    private class FinalInfo {
        DefaultHTriangle tr1;
        DefaultHTriangle tr2;
        HPolygon polygon1;
        HPolygon polygon2;
        Domain domain;
        double max;
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for " + getClass().getName());
    }

    public Axis getAxis() {
        return axis;
    }
}
