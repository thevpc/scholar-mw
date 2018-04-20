package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.awt.geom.Path2D;
import java.util.*;

/**
 * @author : vpc
 * @creationtime 18 janv. 2006 16:04:29
 */
public class DefaultGeometryList extends AbstractGeometry implements GeometryList {
    public static final long serialVersionUID = -1231231231240000009L;
    private Domain domain = null;
    private Domain smallestDomain = null;
    private ArrayList<Geometry> list;
    private Map<String, Object> attributes;

    public DefaultGeometryList(Iterable<Geometry> polygons) {
        list = new ArrayList<Geometry>();
        for (Geometry polygon : polygons) {
            list.add(polygon);
        }
        rebuildSmallestDomain();
        this.domain = null;
    }

    public DefaultGeometryList(Geometry... polygons) {
        list = new ArrayList<Geometry>(Arrays.asList(polygons));
        rebuildSmallestDomain();
        this.domain = null;
    }

    public DefaultGeometryList(Domain domain, Geometry... polygons) {
        this(domain);
        for (Geometry polygon : polygons) {
            add(polygon);
        }
    }

    protected void rebuildSmallestDomain() {
        Domain domain = Domain.EMPTYXY;
        for (Geometry polygon : this) {
            domain = domain.expand(polygon.getDomain());
        }
        smallestDomain = domain;
    }

    public DefaultGeometryList(Domain domain) {
        this.domain = domain;
        if (domain == null) {
            throw new NullPointerException();
        }
        list = new ArrayList<Geometry>();
    }

    public DefaultGeometryList(Domain domain, Collection<? extends Geometry> c) {
        this(domain);
        addAll(c);
    }

    public final String dump() {
        return getDumpStringHelper().toString();
    }

    public Dumper getDumpStringHelper() {
        Dumper h = new Dumper(getClass().getSimpleName());
        h.add("domain", domain);
        h.add("polygons", list);
        h.add("attributes", attributes);
        return h;
    }

    public Geometry set(int index, Geometry element) {
        smallestDomain = null;
        return list.set(index, element);
    }

    public Geometry remove(int index) {
        smallestDomain = null;
        return list.remove(index);
    }

    public void add(int index, Geometry element) {
        smallestDomain = null;
        list.add(index, element);
    }

    public boolean add(Geometry o) {
        smallestDomain = null;
        boolean b = list.add(o);
        return b;
    }

    public boolean remove(Object o) {
        smallestDomain = null;
        return list.remove(o);
    }

    public void clear() {
        smallestDomain = null;
        list.clear();
    }

    public boolean addAll(GeometryList c) {
        smallestDomain = null;
        for (Geometry polygon : c) {
            list.add(polygon);
        }
        return c.size() > 0;
    }

    public boolean addAll(Collection<? extends Geometry> c) {
        smallestDomain = null;
        return list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Geometry> c) {
        smallestDomain = null;
        return list.addAll(index, c);
    }

    public Domain getDomain(Domain rectangle2D, Domain domain) {
        Domain bounds = getDomain();
        return Domain.forBounds(
                (rectangle2D.getXMin() - bounds.getXMin()) / bounds.getXwidth() * domain.xwidth() + domain.xmin(),
                (rectangle2D.getXMax() - bounds.getXMin()) / bounds.getXwidth() * domain.xwidth() + domain.xmin(), (rectangle2D.getYMin() - bounds.getYMin()) / bounds.getYwidth() * domain.ywidth() + domain.ymin(),
                (rectangle2D.getYMax() - bounds.getYMin()) / bounds.getYwidth() * domain.ywidth() + domain.ymin()
        );
    }


    public GeometryList clone() {
        DefaultGeometryList l = (DefaultGeometryList) super.clone();
        l.list = new ArrayList<Geometry>(list.size());
        for (int i = 0; i < list.size(); i++) {
            Geometry polygon = list.get(i);
            l.list.add((Geometry) polygon.clone());
        }
        return l;
    }


    public Iterator<Geometry> iterator() {
        return list.iterator();
    }

    public int size() {
        return list.size();
    }

    public Geometry get(int i) {
        return list.get(i);
    }

    public void setAttribute(String name, Object value) {
        if (name != null) {
            if (value != null) {
                if (attributes == null) {
                    attributes = new HashMap<String, Object>();
                }
                attributes.put(name, value);
            } else {
                if (attributes != null) {
                    attributes.remove(name);
                }
            }
        }
    }

    public Object getAttribute(String name) {
        return (name == null || attributes == null) ? null : attributes.get(name);
    }


    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public GeometryList getDual() {
        return null;
    }

    public Collection<Geometry> toCollection() {
        return (Collection<Geometry>) list.clone();
    }

    public Domain getSmallestBounds() {
        if (smallestDomain == null) {
            rebuildSmallestDomain();
        }
        return smallestDomain;
    }

    @Override
    public boolean contains(double x, double y) {
        for (Geometry polygon : list) {
            if (polygon.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Geometry translateGeometry(double x, double y) {
        DefaultGeometryList list = new DefaultGeometryList();
        for (Geometry geometry : list) {
            list.add(geometry.translateGeometry(x, y));
        }
        return list;
    }

    public Domain getBounds() {
        return domain;
    }

    @Override
    public Domain getDomain() {
        return domain != null ? domain : getSmallestBounds();
    }

    @Override
    public boolean isRectangular() {
        if (list.size() != 1) {
            return false;
        }
        return list.get(0).isRectangular();
    }

    @Override
    public boolean isPolygonal() {
        return list.size() == 1;
    }

    @Override
    public boolean isTriangular() {
        return isPolygonal() && list.get(0).isTriangular();
    }

    @Override
    public Surface toSurface() {
        if (list.isEmpty()) {
            return Domain.EMPTYXY.toGeometry().toSurface();
        }
        Surface s = list.get(0).toSurface();
        for (int i = 1; i < list.size(); i++) {
            s = s.addGeometry(list.get(i));
        }
        return s;
    }

    @Override
    public boolean isSingular() {
        return list.size() < 2;
    }

    @Override
    public boolean isEmpty() {
        return toSurface().isEmpty();
    }

    @Override
    public Polygon toPolygon() {
        if (isPolygonal()) {
            return list.get(0).toPolygon();
        }
        throw new IllegalArgumentException("Not Polygonal");
    }

    @Override
    public Triangle toTriangle() {
        if (isTriangular()) {
            toPolygon().toTriangle();
        }
        throw new IllegalArgumentException("Not Triangular");
    }


    @Override
    public Path2D.Double getPath() {
        return toSurface().getPath();
    }
}
