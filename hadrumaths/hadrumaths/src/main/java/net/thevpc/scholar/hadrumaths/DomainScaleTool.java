package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.geom.*;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by vpc on 4/1/16.
 */
public abstract class DomainScaleTool {

    public static DomainScaleTool create(Domain from, Domain to) {
        if (from == null || to == null || from.equals(to)) {
            return createIdentity();
        } else {
            return new SimpleDomainScaleTool(from, to);
        }
    }

    /**
     * Rescales multiple geometries together, preserving their relative
     * positions and sizes, by computing a shared bounding domain across all of
     * them and mapping it uniformly to the target domain.
     *
     * @param geometries the geometries to rescale
     * @param to the target domain
     * @return a list of rescaled geometries in the same order as the input
     */
    public static List<HGeometry> rescaleAll(List<HGeometry> geometries, Domain to) {
        if (geometries == null || geometries.isEmpty()) {
            return new ArrayList<>();
        }

        // Compute the union bounding domain across all geometries
        Domain unified = geometries.stream()
                .map(HGeometry::getDomain)
                .filter(Objects::nonNull)
                .reduce(Domain::expand)
                .orElseThrow(() -> new IllegalArgumentException("No valid domains found in geometries"));

        // One shared tool for all — this is what preserves relative layout
        DomainScaleTool tool = DomainScaleTool.create(unified, to);

        return geometries.stream()
                .map(tool::rescale)
                .collect(Collectors.toList());
    }

    /**
     * Varargs overload for convenience.
     */
    public static List<HGeometry> rescaleAll(Domain to, HGeometry... geometries) {
        return rescaleAll(Arrays.asList(geometries), to);
    }

    public abstract Domain rescale(Domain domain);

    public static DomainScaleTool createIdentity() {
        return new NoDomainScaleTool();
    }

    public static HGeometry rescale(HGeometry g, Domain to) {
        return DomainScaleTool.create(g.getDomain(), to).rescale(g);
    }

    public abstract HGeometry rescale(HGeometry area);

    public static HGeometry rescale(HGeometry g, int x, int y) {
        return DomainScaleTool.create(g.getDomain(), Domain.ofBounds(0, x, 0, y)).rescale(g);
    }

    public abstract double rescaleX(double x);

    public abstract double rescaleW(double w);

    public abstract double rescaleH(double h);

    public abstract double rescaleY(double y);

//    public abstract java.awt.geom.Area rescale(java.awt.geom.Area area);
    public abstract DomainScaleTool inv();

    public abstract Path2D.Double rescale(Path2D.Double path);

    public abstract Path2D.Double rescale(PathIterator area);

    public abstract HPoint rescale(HPoint point);

    public abstract HPoint[] rescale(HPoint[] points);

    public abstract List<HPoint> rescale(List<HPoint> points);

    public abstract HPolygon rescale(HPolygon polygon);

    private static class NoDomainScaleTool extends DomainScaleTool {

        @Override
        public double rescaleX(double x) {
            return x;
        }

        @Override
        public double rescaleW(double w) {
            return w;
        }

        @Override
        public double rescaleH(double h) {
            return h;
        }

        @Override
        public double rescaleY(double y) {
            return y;
        }

        @Override
        public HGeometry rescale(HGeometry area) {
            return area;
        }

        @Override
        public DomainScaleTool inv() {
            return this;
        }

        @Override
        public Path2D.Double rescale(Path2D.Double path) {
            return path;
        }

        @Override
        public Path2D.Double rescale(PathIterator area) {
            return GeomUtils.pathIteratorToPath(area);
        }

//        @Override
//        public java.awt.geom.Area rescale(java.awt.geom.Area area) {
//            return area;
//        }
        @Override
        public HPoint rescale(HPoint point) {
            return point;
        }

        @Override
        public Domain rescale(Domain domain) {
            return domain;
        }

        @Override
        public HPoint[] rescale(HPoint[] points) {
            return points;
        }

        @Override
        public List<HPoint> rescale(List<HPoint> points) {
            return new ArrayList<HPoint>(points);
        }

        @Override
        public HPolygon rescale(HPolygon polygon) {
            return polygon;
        }
    }

    private static class SimpleDomainScaleTool extends DomainScaleTool {

        private final Domain from;
        private final Domain to;

        public SimpleDomainScaleTool(Domain from, Domain to) {
            this.from = from;
            this.to = to;
        }

        public double rescaleX(double x) {
            return (x - from.xmin()) / from.xwidth() * to.xwidth() + to.xmin();
        }

        public double rescaleW(double w) {
            return w / from.xwidth() * to.xwidth();
        }

        public double rescaleH(double h) {
            return h / from.ywidth() * to.ywidth();
        }

        public double rescaleZ(double z) {
            return z;
        }

        public double rescaleY(double y) {
            if (from.ywidth() == 0) {
                return (y - from.ymin()) * to.ywidth() + to.ymin();
            }
            return (y - from.ymin()) / from.ywidth() * to.ywidth() + to.ymin();
        }

        public HGeometry rescale(HGeometry area) {
            return HGeometry.fromPath(
                    rescale(area.getPath())
            );
        }

        @Override
        public Path2D.Double rescale(Path2D.Double pi) {
            return rescale(pi.getPathIterator(null));
        }

        @Override
        public Path2D.Double rescale(PathIterator pi) {
            Path2D.Double d = new Path2D.Double();
            d.setWindingRule(pi.getWindingRule());
            double[] coords = new double[6];
            while (!pi.isDone()) {
                switch (pi.currentSegment(coords)) {
                    case PathIterator.SEG_MOVETO:
                        d.moveTo(rescaleX(coords[0]), rescaleY(coords[1]));
                        break;
                    case PathIterator.SEG_LINETO:
                        d.lineTo(rescaleX(coords[0]), rescaleY(coords[1]));
                        break;
                    case PathIterator.SEG_QUADTO:
                        d.quadTo(rescaleX(coords[0]), rescaleY(coords[1]), rescaleX(coords[2]), rescaleY(coords[3]));
                        break;
                    case PathIterator.SEG_CUBICTO:
                        d.curveTo(rescaleX(coords[0]), rescaleY(coords[1]), rescaleX(coords[2]), rescaleY(coords[3]), rescaleX(coords[4]), rescaleY(coords[5]));
                        break;
                    case PathIterator.SEG_CLOSE:
                        d.closePath();
                        break;
                    default: {
                        throw new RuntimeException("Error");
                    }
                }
                pi.next();
            }
            return d;
        }

        public HPoint rescale(HPoint point) {

            double x = rescaleX(point.x);
            double y = rescaleY(point.y);
            return new HPoint(x, y);
        }

        public HPoint[] rescale(HPoint[] points) {
            HPoint[] all = new HPoint[points.length];
            for (int i = 0; i < all.length; i++) {
                all[i] = rescale(points[i]);
            }
            return all;
        }

        @Override
        public List<HPoint> rescale(List<HPoint> points) {
            List<HPoint> all = new ArrayList<HPoint>();

            for (int i = 0; i < points.size(); i++) {
                all.add(rescale(points.get(i)));
            }
            return all;
        }

        public Domain rescale(Domain domain) {
            switch (domain.getDimension()) {
                case 1: {
                    return Domain.ofBounds(
                            rescaleX(domain.xmin()),
                            rescaleX(domain.xmax())
                    );

                }
                case 2: {
                    return Domain.ofBounds(
                            rescaleX(domain.xmin()),
                            rescaleX(domain.xmax()),
                            rescaleY(domain.ymin()),
                            rescaleY(domain.ymax())
                    );

                }
                case 3: {
                    return Domain.ofBounds(
                            rescaleX(domain.xmin()),
                            rescaleX(domain.xmax()),
                            rescaleY(domain.ymin()),
                            rescaleY(domain.ymax()),
                            rescaleZ(domain.zmin()),
                            rescaleZ(domain.zmax())
                    );

                }
            }
            return Domain.ofBounds(
                    rescaleX(domain.xmin()),
                    rescaleX(domain.xmax()),
                    rescaleY(domain.ymin()),
                    rescaleY(domain.ymax()),
                    rescaleZ(domain.zmin()),
                    rescaleZ(domain.zmax())
            );
        }

        public HPolygon rescale(HPolygon polygon) {
            return GeometryFactory.createPolygon(
                    rescale(polygon.getPoints())
            );
        }

        @Override
        public DomainScaleTool inv() {
            return new SimpleDomainScaleTool(to, from);
        }
    }

}
