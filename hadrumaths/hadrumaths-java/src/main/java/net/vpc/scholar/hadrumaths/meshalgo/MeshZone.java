package net.vpc.scholar.hadrumaths.meshalgo;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 19 janv. 2007 11:36:27
 */
public class MeshZone {

    private static final Comparator<Domain> domainsComparator = new Comparator<Domain>() {

        public int compare(Domain o1, Domain o2) {
            //d'abord les structures les plus grandes ensuite selon position (y puis x)
            double a1 = o1.xwidth() * o1.ywidth();
            double a2 = o2.xwidth() * o2.ywidth();
            if (a1 < a2) {
                return 1;
            } else if (a1 > a2) {
                return -1;
            }
            a1 = o1.ymin();
            a2 = o2.ymin();
            if (a1 < a2) {
                return -1;
            } else if (a1 > a2) {
                return 1;
            }
            a1 = o1.xmin();
            a2 = o2.xmin();
            if (a1 < a2) {
                return -1;
            } else if (a1 > a2) {
                return 1;
            }
            return 0;
        }
    };
    public static final Comparator<MeshZone> ZONES_COMPARATOR = new Comparator<MeshZone>() {

        public int compare(MeshZone o1, MeshZone o2) {
            Integer type1 = o1.getType().getValue();
            Integer type2 = o1.getType().getValue();
            int c = type1.compareTo(type2);
            if (c != 0) {
                return c;
            }
//            if(o1==null || o2==null){
//                return o1.equals(o2)?0:o1.hashCode()-o2.hashCode();
//            }
            int i = o1.type.compareTo(o2.type);
            if (i != 0) {
                return i;
            }
            if (o1.domain == null || o2.domain == null) {
                return o1.equals(o2) ? 0 : o1.hashCode() - o2.hashCode();
            }
            return domainsComparator.compare(o1.domain, o2.domain);
        }
    };
    private final MeshZoneShape shape;
    private boolean enabled = true;
    private Geometry geometry;
    private final MeshZoneType type;
    private Domain domain;
    private Polygon polygon;
    private Domain globalDomain;
    private Domain domain0;
    private Map<String, Object> userProperties;
    private boolean scale = true;

    public MeshZone(Domain domain) {
        this(domain.toGeometry(), MeshZoneShape.RECTANGLE, MeshZoneType.MAIN);
        setDomain(domain);
    }

    public MeshZone(Geometry geometry, MeshZoneShape shape, MeshZoneType type) {
        this.geometry = geometry;
        this.shape = shape;
        this.type = type;
        setProperty("MeshZoneType", type);
        setProperty("MeshZoneShape", shape);
        Domain r = geometry.getDomain();
        setProperty("Bounds", "[x=" + r.xmin() + ",y=" + r.ymin() + ",w=" + r.xwidth() + ",h=" + r.ywidth() + "]");
    }

    public void setProperty(String name, Object value) {
        if (value != null) {
            if (userProperties == null) {
                userProperties = new HashMap<String, Object>();
            }
            userProperties.put(name, value);
        } else {
            if (userProperties != null) {
                userProperties.remove(name);
            }
        }
    }

    public MeshZone(Domain domain, MeshZoneType type) {
        this(domain.toGeometry(), MeshZoneShape.RECTANGLE, type);
        setDomain(domain);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public MeshZoneShape getShape() {
        return shape;
    }

    public MeshZoneType getType() {
        return type;
    }

    public MeshZone clone() {
        try {
            MeshZone o = (MeshZone) super.clone();
            o.geometry = o.geometry.clone();
            return o;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MeshZone{" + "shape=" + shape);
        if (geometry != null) {
            if (type != null) {
                sb.append(",type=").append(type);
            }
            if (domain != null) {
                sb.append(",domain=").append(domain);
            }
            if (geometry != null) {
                sb.append(",geometry=").append(geometry);
            }
            if (polygon != null) {
                sb.append(",polygon=").append(polygon);
            }
            if (globalDomain != null) {
                sb.append(",globalDomain=").append(globalDomain);
            }
            if (domain0 != null) {
                sb.append(",domain0=").append(domain0);
            }
            sb.append(",userProperties=").append(userProperties);
        }
        return sb.toString();
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
        if (domain.isEmpty()) {
            throw new IllegalArgumentException("Zone Domain could not be null");
        }
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setDomainRelative(Domain domain0, Domain globalDomain) {
        this.globalDomain = globalDomain;
        this.domain0 = domain0;
        if (domain0 == null || domain0.equals(globalDomain)) {
            polygon = geometry.toPolygon();
            setDomain(polygon.getDomain());
        } else {
            Domain rectangle2D = getGeometry().getDomain();
            double x0 = rectangle2D.getXMin();
            double x1 = rectangle2D.getXMax();
            double y0 = rectangle2D.getYMin();
            double y1 = rectangle2D.getYMax();
            final double EPSILON = 1E-2;
            if (Math.abs(x0 - domain0.getXMin()) < EPSILON) {
                x0 = domain0.getXMin();
            }
            if (Math.abs(x1 - domain0.getXMax()) < EPSILON) {
                x1 = domain0.getXMax();
            }
            if (Math.abs(y0 - domain0.getYMin()) < EPSILON) {
                y0 = domain0.getYMin();
            }
            if (Math.abs(y1 - domain0.getYMax()) < EPSILON) {
                y1 = domain0.getYMax();
            }
            double dxmin = (x0 - domain0.getXMin()) / domain0.getXwidth() * globalDomain.xwidth() + globalDomain.xmin();
            double dymin = (y0 - domain0.getYMin()) / domain0.getYwidth() * globalDomain.ywidth() + globalDomain.ymin();
            double dxmax = (x1 - domain0.getXMin()) / domain0.getXwidth() * globalDomain.xwidth() + globalDomain.xmin();
            double dymax = (y1 - domain0.getYMin()) / domain0.getYwidth() * globalDomain.ywidth() + globalDomain.ymin();
            Polygon p = geometry.toPolygon();
            double[] x = p.xpoints;
            double[] y = p.ypoints;
            for (int i = 0; i < x.length; i++) {
                x[i] = (x[i] - domain0.xmin()) / domain0.xwidth() * globalDomain.xwidth() + globalDomain.xmin();
                y[i] = (y[i] - domain0.ymin()) / domain0.ywidth() * globalDomain.ywidth() + globalDomain.ymin();
            }
            polygon = new Polygon(x, y);
            Domain domainXY = Domain.ofBounds(dxmin, dxmax, dymin, dymax);
            Domain intersect = domainXY.intersect(polygon.getDomain());
            if (intersect.isEmpty()) {
                intersect = domainXY.intersect(polygon.getDomain());
            }
            setDomain(intersect);
        }
        scale = !this.globalDomain.equals(domain0);
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Point toEffectivePoint(Point p) {
        if (!scale) {
            return p;
        }
        return Point.create(toEffectiveX(p.x), toEffectiveY(p.y));
    }

    public double toEffectiveX(double x) {
        if (!scale) {
            return x;
        }
        return (x - domain0.getXMin()) / domain0.getXwidth() * globalDomain.xwidth() + globalDomain.xmin();
    }

    public double toEffectiveY(double y) {
        if (!scale) {
            return y;
        }
        return (y - domain0.getYMin()) / domain0.getYwidth() * globalDomain.ywidth() + globalDomain.ymin();
    }

    public MeshZone getSymmetricY(double y0) {
        MeshZone zone = new MeshZone(geometry, shape, type);
        zone.setDomain(domain.getSymmetricY(y0));
        return zone;
    }

    public MeshZone getSymmetricX(double x0) {
        MeshZone zone = new MeshZone(geometry, shape, type);
        zone.setDomain(domain.getSymmetricX(x0));
        return zone;
    }

    public Map getProperties() {
        if (userProperties == null) {
            userProperties = new HashMap<String, Object>();
        }
        return userProperties;
    }

    public Object getProperty(String name) {
        return userProperties == null ? null : userProperties.get(name);
    }
}
