package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.nuts.elem.NArrayElementBuilder;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.meshalgo.tri.JTSHelper;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import java.awt.geom.Path2D;
import java.util.*;

/**
 * @author : vpc
 * @creationtime 17 janv. 2006 00:46:37
 */
public class DefaultHPolygonWithHoles extends AbstractHGeometry implements Cloneable, HPolygonWithHoles {

    private static final long serialVersionUID = 1L;
    private int color = 1;
    private final HPolygon exteriorRing;
    private List<HPolygon> holes = new ArrayList<>();
    private Geometry jtsGeometry;

    public DefaultHPolygonWithHoles(HPolygon exteriorRing, List<HPolygon> holes) {
        this.color = color;
        this.exteriorRing = exteriorRing;
        this.holes = holes;
    }

    @Override
    public HPolygon getExteriorRing() {
        return exteriorRing;
    }

    @Override
    public List<HPolygon> getHoles() {
        return Collections.unmodifiableList(holes);
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder b = NElement.ofObjectBuilder("PolygonWithHoles");
        b.add("exteriorRing", exteriorRing.toElement());
        b.add("color", color);

        NArrayElementBuilder arr = NElement.ofArrayBuilder();
        for (int i = 0; i < holes.size(); i++) {
            arr.add(holes.get(i).toElement());
        }
        b.addIf("properties", NElementHelper.elem(getProperties()), NElementHelper.blankPredicate());
        return b.build();
    }

    @Override
    public List<HPoint> getPoints() {
        return exteriorRing.getPoints();
    }

    @Override
    public HGeometry clone() {
        return super.clone();
    }

    public Path2D.Double getPath() {
        return getExteriorRing().getPath();
    }

    public Domain getDomain() {
        return getExteriorRing().getDomain();
    }

    public boolean isRectangular() {
        return getExteriorRing().isRectangular();
    }

    @Override
    public boolean isPolygonal() {
        return holes.isEmpty();
    }

    @Override
    public boolean isTriangular() {
        if (!holes.isEmpty()) {
            return false;
        }
        return getExteriorRing().isTriangular();
    }

    @Override
    public boolean isSingular() {
        return holes.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return _asJtsGeometry().isEmpty();
    }

    @Override
    public HGeometry translate(double x, double y) {
        return JTSHelper.fromJtsGeometry(JTSHelper.translate(_asJtsGeometry(),x,y));
    }

    public boolean contains(double x, double y) {
        return JTSHelper.contains(_asJtsGeometry(), x, y);
    }

    private Geometry _asJtsGeometry() {
        if (jtsGeometry == null) {
            jtsGeometry = JTSHelper.toJtsGeometry(this);
        }
        return jtsGeometry;
    }

    @Override
    public HPolygon[] toPolygons() {
        return new HPolygon[]{toPolygon()};
    }

    @Override
    public HPolygon toPolygon() {
        if (holes.isEmpty()) {
            return exteriorRing;
        }
        throw new IllegalArgumentException("Not a Polygon");
    }

    @Override
    public HTriangle toTriangle() {
        return toPolygon().toTriangle();
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
        DefaultHPolygonWithHoles that = (DefaultHPolygonWithHoles) o;
        return
                color == that.color &&
                        Objects.equals(exteriorRing, that.exteriorRing) &&
                        Objects.equals(holes, that.holes);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), color, exteriorRing, holes);
    }

    @Override
    public String toString() {
        return FormatFactory.format(this);
    }

    public boolean is4Edges() {
        return isPolygonal() && toPolygon().is4Edges();
    }

}
