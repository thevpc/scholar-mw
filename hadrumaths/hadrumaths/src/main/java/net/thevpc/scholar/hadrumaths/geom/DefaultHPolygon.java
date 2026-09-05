package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.nuts.elem.NArrayElementBuilder;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.nuts.math.NDoubleRange;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.meshalgo.tri.JTSHelper;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import org.locationtech.jts.geom.Polygon;

import java.awt.geom.Path2D;
import java.util.*;

/**
 * @author : vpc
 * @creationtime 17 janv. 2006 00:46:37
 */
public class DefaultHPolygon extends AbstractHGeometry implements Cloneable, HPolygon {

    private static final long serialVersionUID = 1L;
    private Domain _domain;
    private Boolean _rect;
    private Boolean _triangular;
    private Boolean _4edges;
    private int color = 1;
    private final List<HPoint> points;
    private Polygon _jtsPolygon;

    public DefaultHPolygon(Domain d) {
        this(new double[]{d.xmin(), d.xmax(), d.xmax(), d.xmin()}, new double[]{d.ymin(), d.ymin(), d.ymax(), d.ymax()}, 1);
    }

    public DefaultHPolygon(double[] x, double[] y, int color) {
        this.points = new ArrayList<>();
        this.color = color;
        for (int i = 0; i < x.length; i++) {
            points.add(HPoint.create(x[i], y[i]));
        }
    }

    private Polygon asJtsPolygon() {
        if (_jtsPolygon == null) {
            _jtsPolygon = (Polygon) JTSHelper.toJtsPolygon(points);
        }
        return _jtsPolygon;
    }


    @Override
    public NElement toElement() {
        NObjectElementBuilder b = NElement.ofObjectBuilder("Polygon");
        b.add("color", color);
        NArrayElementBuilder arr = NElement.ofArrayBuilder();
        for (int i = 0; i < points.size(); i++) {
            HPoint pi = points.get(i);
            arr.add(NElementHelper.ofTuple(
                    NElement.ofDouble(pi.x),
                    NElement.ofDouble(pi.y)
            ));
        }
        b.add("points", arr.build());
        b.addIf("properties", NElementHelper.elem(getProperties()), NElementHelper.blankPredicate());
        return b.build();
    }

    public DefaultHPolygon(double[] x, double[] y) {
        this(x, y, 1);
    }


    public DefaultHPolygon(List<HPoint> p) {
        this(p.toArray(new HPoint[0]));
    }

    public DefaultHPolygon(HPoint... p) {
        this.points = new ArrayList<>(Arrays.asList(p));
    }

    @Override
    public List<HPoint> getPoints() {
        return Collections.unmodifiableList(points);
    }

//    public Path2D.Double getPath(double dx, double dy, double multiplier) {
//        Path2D.Double p = new Path2D.Double();
//        float xx = (float) ((xpoints[0] + dx) * multiplier);
//        float yy = (float) ((ypoints[0] + dy) * multiplier);
//        p.moveTo(xx, yy);
//        for (int i = 1; i < xpoints.length; i++) {
//            xx = (float) ((xpoints[i] + dx) * multiplier);
//            yy = (float) ((ypoints[i] + dy) * multiplier);
//            p.lineTo(xx, yy);
//        }
//        p.closePath();
//        return p;
//    }

    public HPoint getPoint(int index) {
        return points.get(index);
    }

    @Override
    public HGeometry clone() {
        return super.clone();
    }

    public Path2D.Double getPath() {
        return JTSHelper.getPath(asJtsPolygon());
    }

    public Domain getDomain() {
        if (_domain == null) {
            NDoubleRange xx = NDoubleRange.of();
            NDoubleRange yy = NDoubleRange.of();
            for (HPoint point : points) {
                xx.add(point.x);
                yy.add(point.y);
            }
            _domain = Domain.ofBounds(xx.min(), xx.max(), yy.min(), yy.max());
        }
        return _domain;
    }

    public boolean isRectangular() {
        if (_rect == null) {
            _rect = JTSHelper.isRectangular(asJtsPolygon());
        }
        return _rect;
    }

    @Override
    public boolean isPolygonal() {
        return true;
    }

    @Override
    public boolean isTriangular() {
        if (_triangular == null) {
            _triangular = JTSHelper.isTriangular(asJtsPolygon());
        }
        return _triangular;
    }

    @Override
    public boolean isSingular() {
        return JTSHelper.isSingular(asJtsPolygon());
    }

    @Override
    public boolean isEmpty() {
        return asJtsPolygon().isEmpty();
    }

    @Override
    public HGeometry translate(double x, double y) {
        return translate(HPoint.create(x, y));
    }

    public boolean contains(double x, double y) {
        return JTSHelper.contains(asJtsPolygon(),x,y);
    }

    @Override
    public HPolygon[] toPolygons() {
        return new HPolygon[]{this};
    }

    @Override
    public HPolygon toPolygon() {
        return this;
    }

    @Override
    public HTriangle toTriangle() {
        if (isTriangular()) {
            return new DefaultHTriangle(getPoint(0), getPoint(1), getPoint(2));
        }
        throw new IllegalArgumentException("Not a Triangle");
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultHPolygon that = (DefaultHPolygon) o;
        return  color == that.color &&
                Objects.equals(points, that.points);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), color, points);
    }

    @Override
    public String toString() {
        return FormatFactory.format(this);
    }

    public boolean is4Edges() {
        if(_4edges==null){
            _4edges=JTSHelper.is4Edges(asJtsPolygon());
        }
        return _4edges;
    }

    public HPolygon translate(HPoint v) {
        List<HPoint> dPoints = new ArrayList<>(getPoints());
        for (int i = 0; i < dPoints.size(); i++) {
            HPoint p = dPoints.get(i);
            dPoints.set(i, HPoint.create(p.x + v.x, p.y + v.y));
        }
        return (new DefaultHPolygon(dPoints));
    }

}
