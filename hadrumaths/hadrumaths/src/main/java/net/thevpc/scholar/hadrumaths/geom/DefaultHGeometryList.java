package net.thevpc.scholar.hadrumaths.geom;


import net.thevpc.nuts.elem.NElement;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.meshalgo.tri.JTSHelper;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import org.locationtech.jts.geom.Geometry;

import java.awt.geom.Path2D;
import java.util.*;

/**
 * @author : vpc
 * @creationtime 18 janv. 2006 16:04:29
 */
public class DefaultHGeometryList extends AbstractHGeometry implements HGeometryList {
    private static final long serialVersionUID = 1L;
    private Domain domain = null;
    private Domain smallestDomain = null;
    private ArrayList<HGeometry> list;
    private Map<String, Object> attributes;

    public DefaultHGeometryList(Iterable<HGeometry> polygons) {
        list = new ArrayList<HGeometry>();
        for (HGeometry polygon : polygons) {
            list.add(polygon);
        }
        rebuildSmallestDomain();
        this.domain = null;
    }

    protected void rebuildSmallestDomain() {
        Domain domain = Domain.EMPTYXY;
        for (HGeometry polygon : this) {
            domain = domain.expand(polygon.getDomain());
        }
        smallestDomain = domain;
    }

    public DefaultHGeometryList(HGeometry... polygons) {
        list = new ArrayList<HGeometry>(Arrays.asList(polygons));
        rebuildSmallestDomain();
        this.domain = null;
    }

    public DefaultHGeometryList(Domain domain, HGeometry... polygons) {
        this(domain);
        for (HGeometry polygon : polygons) {
            add(polygon);
        }
    }

    public DefaultHGeometryList(Domain domain) {
        this.domain = domain;
        if (domain == null) {
            throw new NullPointerException();
        }
        list = new ArrayList<HGeometry>();
    }

    public DefaultHGeometryList(Domain domain, Collection<? extends HGeometry> c) {
        this(domain);
        addAll(c);
    }

    @Override
    public NElement toElement() {
        return NElement.ofObjectBuilder("geometries").addAll(
                NElement.ofPair("domain", NElementHelper.elem(domain)),
                NElement.ofPair("polygons", NElementHelper.elem(list)),
                NElement.ofPair("attributes", NElementHelper.elem(attributes)),
                NElement.ofPair("properties", NElementHelper.elem(getProperties()))
        ).build();
    }

//    public final String dump() {
//        return getDumpStringHelper().toString();
//    }

//    public Dumper getDumpStringHelper() {
//        Dumper h = new Dumper(getClass().getSimpleName());
//        h.add("domain", domain);
//        h.add("polygons", list);
//        h.add("attributes", attributes);
//        return h;
//    }

    public HGeometry set(int index, HGeometry element) {
        smallestDomain = null;
        return list.set(index, element);
    }

    public HGeometry remove(int index) {
        smallestDomain = null;
        return list.remove(index);
    }

    public void add(int index, HGeometry element) {
        smallestDomain = null;
        list.add(index, element);
    }

    public boolean add(HGeometry o) {
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

    public boolean addAll(HGeometryList c) {
        smallestDomain = null;
        for (HGeometry polygon : c) {
            list.add(polygon);
        }
        return c.size() > 0;
    }

    public boolean addAll(Collection<? extends HGeometry> c) {
        smallestDomain = null;
        return list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends HGeometry> c) {
        smallestDomain = null;
        return list.addAll(index, c);
    }

    public Domain getDomain(Domain rectangle2D, Domain domain) {
        Domain bounds = getDomain();
        return Domain.ofBounds(
                (rectangle2D.getXMin() - bounds.getXMin()) / bounds.getXwidth() * domain.xwidth() + domain.xmin(),
                (rectangle2D.getXMax() - bounds.getXMin()) / bounds.getXwidth() * domain.xwidth() + domain.xmin(), (rectangle2D.getYMin() - bounds.getYMin()) / bounds.getYwidth() * domain.ywidth() + domain.ymin(),
                (rectangle2D.getYMax() - bounds.getYMin()) / bounds.getYwidth() * domain.ywidth() + domain.ymin()
        );
    }

    public Domain getBounds() {
        return domain;
    }

    public int size() {
        return list.size();
    }

    public HGeometry get(int i) {
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

    public HGeometryList getDual() {
        return null;
    }

    public Collection<HGeometry> toCollection() {
        return (Collection<HGeometry>) list.clone();
    }

    public Domain getSmallestBounds() {
        if (smallestDomain == null) {
            rebuildSmallestDomain();
        }
        return smallestDomain;
    }

    public HGeometryList clone() {
        DefaultHGeometryList l = (DefaultHGeometryList) super.clone();
        l.list = new ArrayList<HGeometry>(list.size());
        for (int i = 0; i < list.size(); i++) {
            HGeometry polygon = list.get(i);
            l.list.add(polygon.clone());
        }
        return l;
    }


    public Iterator<HGeometry> iterator() {
        return list.iterator();
    }

    @Override
    public Path2D.Double getPath() {
        return JTSHelper.getPath(asJtsGeometry());
    }

    @Override
    public Domain getDomain() {
        return domain != null ? domain : getSmallestBounds();
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
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
        return JTSHelper.isPolygonal(asJtsGeometry());
    }

    @Override
    public boolean isTriangular() {
        return JTSHelper.isTriangular(asJtsGeometry());
    }

    @Override
    public boolean isSingular() {
        return JTSHelper.isSingular(asJtsGeometry());
    }

    @Override
    public boolean isEmpty() {
        return asJtsGeometry().isEmpty();
    }

    private Geometry asJtsGeometry() {
        return JTSHelper.toJtsGeometry(this);
    }

    @Override
    public HGeometry translate(double x, double y) {
        DefaultHGeometryList newList = new DefaultHGeometryList();
        for (HGeometry geometry : this.list) {
            newList.add(geometry.translate(x, y));
        }
        return newList;
    }

    @Override
    public boolean contains(double x, double y) {
        for (HGeometry polygon : list) {
            if (polygon.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HPolygon toPolygon() {
        if (isPolygonal()) {
            return list.get(0).toPolygon();
        }
        throw new IllegalArgumentException("Not Polygonal");
    }

    @Override
    public HTriangle toTriangle() {
        if (isTriangular()) {
            return toPolygon().toTriangle();
        }
        throw new IllegalArgumentException("Not Triangular");
    }

    @Override
    public HPolygon[] toPolygons() {
        List<HPolygon> all=new ArrayList<>();
        for (HGeometry geometry : list) {
            all.addAll(Arrays.asList(geometry.toPolygons()));
        }
        return all.toArray(new HPolygon[0]);
    }
}
