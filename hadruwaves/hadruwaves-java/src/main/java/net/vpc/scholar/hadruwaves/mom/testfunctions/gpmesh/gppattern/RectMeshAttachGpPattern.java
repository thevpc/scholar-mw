package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneShape;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZoneTypeFilter;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 juin 2007 11:38:23
 */
public abstract class RectMeshAttachGpPattern extends AbstractGpPattern implements RectangularGpPattern {

    private MeshZoneTypeFilter filter;

    public RectMeshAttachGpPattern(boolean attachX, boolean attachY) {
        this(
                (attachX && attachY) ? MeshZoneType.FILTER_ALL :
                        (!attachX && !attachY) ? MeshZoneType.FILTER_M :
                                (attachX && !attachY) ? MeshZoneType.FILTER_MXEW :
                /*(!attachX&&attachY)?*/MeshZoneType.FILTER_MYNS
        );
    }

    public RectMeshAttachGpPattern(MeshZoneTypeFilter filter) {
        this.filter = filter == null ? MeshZoneType.FILTER_ALL : filter;
    }

    public RectMeshAttachGpPattern() {
        this(MeshZoneType.FILTER_ALL);
    }

    public RectMeshAttachGpPattern(boolean attach) {
        this(attach, attach);
    }

    /**
     * @param one
     * @param allOthers //     * @param direction 'N','S','W','E'
     * @return null if not found
     */
    public List<MeshZone> findAdgacents(MeshZone one, List<MeshZone> allOthers, char directionOne, char directionOther) {
        List<MeshZone> found = new ArrayList<MeshZone>();
        for (MeshZone meshZone : allOthers) {
            if (isAdgacent(one, meshZone, directionOne, directionOther)) {
                found.add(meshZone);
            }
        }
        return found;
    }

    public boolean isAdgacent(MeshZone one, MeshZone other, char directionOne, char directionOther) {
        if (one.getGeometry().isRectangular() && other.getGeometry().isRectangular()) {
            Domain bounds1 = one.getGeometry().getDomain();
            Domain bounds2 = other.getGeometry().getDomain();
            return isAdgacent(bounds1, bounds2, directionOne, directionOther);
        }
        return false;
    }

    public boolean isAdgacent(Domain bounds1, Domain bounds2, char directionOne, char directionOther) {
        double z0 = 0;
        double z1 = 0;
        double z2 = 0;
        double t1 = 0;
        double t2 = 0;
        double t3 = 0;
        double t4 = 0;
        switch (directionOne) {
            case 'N': {
                z1 = bounds1.getYMin();
                t1 = bounds1.getXMin();
                t2 = bounds1.getXMax();
                z0 = Math.min(bounds1.ywidth(), bounds2.ywidth());
                break;
            }
            case 'S': {
                z1 = bounds1.getYMax();
                t1 = bounds1.getXMin();
                t2 = bounds1.getXMax();
                z0 = Math.min(bounds1.ywidth(), bounds2.ywidth());
                break;
            }
            case 'W': {
                z1 = bounds1.getXMin();
                t1 = bounds1.getYMin();
                t2 = bounds1.getYMax();
                z0 = Math.min(bounds1.xwidth(), bounds2.xwidth());
                break;
            }
            case 'E': {
                z1 = bounds1.getXMax();
                t1 = bounds1.getYMin();
                t2 = bounds1.getYMax();
                z0 = Math.min(bounds1.xwidth(), bounds2.xwidth());
                break;
            }
        }
        switch (directionOther) {
            case 'N': {
                z2 = bounds2.getYMin();
                t3 = bounds1.getXMin();
                t4 = bounds1.getXMax();
                break;
            }
            case 'S': {
                z2 = bounds2.getYMax();
                t3 = bounds1.getXMin();
                t4 = bounds1.getXMax();
                break;
            }
            case 'W': {
                z2 = bounds2.getXMin();
                t3 = bounds1.getYMin();
                t4 = bounds1.getYMax();
                break;
            }
            case 'E': {
                z2 = bounds2.getXMax();
                t3 = bounds1.getYMin();
                t4 = bounds1.getYMax();
                break;
            }
        }
        double zratio = Math.abs(z1 - z2) / z0;
        if (zratio < 0.1) {
            double iratio = segmentIntersectionRatio(t1, t2, t3, t4);
            if (iratio < 0.1) {
                return true;
            }
        }
        return false;
        //return (Math.absdbl(z1 - z2) <= 1) && ((t1>t3 && t1<t4)  || (t2>t3 && t2<t4));
    }

    private double segmentIntersectionRatio(double a1, double b1, double a2, double b2) {
        // a1  < b1 < a2 < b2
        if (b1 < a2) {
            return 0;
        }

        // a2 < b2 < a1  < b1
        if (b2 < a1) {
            return 0;
        }
        double s = 0;
        if (a2 >= a1 && a2 < b1) {
            s = Math.min(b1, b2) - a2;
        } else if (b2 >= a1 && b2 < b1) {
            s = b2 - Math.max(a1, a2);
        }
        if (s == 0) {
            return 0;
        }
        double r = Math.min(b1 - a1, b2 - a2);
        if (r <= 0) {
            return 0;
        }
        return s / r;
    }

    protected List<MeshZone> addRectAttaches(List<MeshZone> all, Domain globalBounds) {
        ArrayList<MeshZone> added = new ArrayList<MeshZone>();
        MeshZone[] all2 = all.toArray(new MeshZone[all.size()]);
        for (int i = 0; i < all2.length; i++) {
            MeshZone area = all2[i];
            if (area.getGeometry().isRectangular()) {
                Geometry area1 = area.getGeometry();
                //area1.g
                Domain bounds1 = area1.getDomain();
                if (filter.accept(MeshZoneType.BORDER_NORTH)) {
                    if (isAdgacent(bounds1, globalBounds, 'N', 'N')) {
                        MeshZone zzone = new MeshZone(Domain.forPoints(bounds1.getXMin(), bounds1.getYMin(), bounds1.getXwidth(), bounds1.getYwidth() / 2), MeshZoneShape.RECTANGLE, MeshZoneType.BORDER_NORTH);
                        zzone.getProperties().put("edgeType", "Box");
                        added.add(zzone);
                    } else if (findAdgacents(area, all, 'N', 'S').size() == 0) {
                        MeshZone zzone = new MeshZone(Domain.forPoints(bounds1.getXMin(), bounds1.getYMin(), bounds1.getXwidth(), bounds1.getYwidth() / 2), MeshZoneShape.RECTANGLE, MeshZoneType.BORDER_NORTH);
                        zzone.getProperties().put("edgeType", "Nothing");
                        added.add(zzone);
                    }
                }
                if (filter.accept(MeshZoneType.BORDER_SOUTH)) {
                    if (isAdgacent(bounds1, globalBounds, 'S', 'S')) {
                        MeshZone zzone = new MeshZone(Domain.forPoints(bounds1.getXMin(), bounds1.getYMin() + bounds1.getYwidth() / 2, bounds1.getXwidth(), bounds1.getYwidth() / 2), MeshZoneShape.RECTANGLE, MeshZoneType.BORDER_SOUTH);
                        zzone.getProperties().put("edgeType", "Box");
                        added.add(zzone);
                    } else if (findAdgacents(area, all, 'S', 'N').size() == 0) {
                        MeshZone zzone = new MeshZone(Domain.forPoints(bounds1.getXMin(), bounds1.getYMin() + bounds1.getYwidth() / 2, bounds1.getXwidth(), bounds1.getYwidth() / 2), MeshZoneShape.RECTANGLE, MeshZoneType.BORDER_SOUTH);
                        zzone.getProperties().put("edgeType", "Nothing");
                        added.add(zzone);
                    }
                }
                if (filter.accept(MeshZoneType.BORDER_WEST)) {
                    if (isAdgacent(bounds1, globalBounds, 'W', 'W')) {
                        MeshZone zzone = new MeshZone(Domain.forPoints(bounds1.getXMin(), bounds1.getYMin(), bounds1.getXwidth() / 2, bounds1.getYwidth()), MeshZoneShape.RECTANGLE, MeshZoneType.BORDER_WEST);
                        zzone.getProperties().put("edgeType", "Box");
                        added.add(zzone);
                    } else if (findAdgacents(area, all, 'W', 'E').size() == 0) {
                        MeshZone zzone = new MeshZone(Domain.forPoints(bounds1.getXMin(), bounds1.getYMin(), bounds1.getXwidth() / 2, bounds1.getYwidth()), MeshZoneShape.RECTANGLE, MeshZoneType.BORDER_WEST);
                        zzone.getProperties().put("edgeType", "Nothing");
                        added.add(zzone);
                    }
                }
                if (filter.accept(MeshZoneType.BORDER_EAST)) {
                    if (isAdgacent(bounds1, globalBounds, 'E', 'E')) {
                        MeshZone zzone = new MeshZone(Domain.forPoints(bounds1.getXMin() + bounds1.getXwidth() / 2, bounds1.getYMin(), bounds1.getXwidth() / 2, bounds1.getYwidth()), MeshZoneShape.RECTANGLE, MeshZoneType.BORDER_EAST);
                        zzone.getProperties().put("edgeType", "Box");
                        added.add(zzone);
                    } else if (findAdgacents(area, all, 'E', 'W').size() == 0) {
                        MeshZone zzone = new MeshZone(Domain.forWidthXY(bounds1.getXMin() + bounds1.getXwidth() / 2, bounds1.getYMin(), bounds1.getXwidth() / 2, bounds1.getYwidth()), MeshZoneShape.RECTANGLE, MeshZoneType.BORDER_EAST);
                        zzone.getProperties().put("edgeType", "Nothing");
                        added.add(zzone);
                    }
                }
                for (int j = i + 1; j < all2.length; j++) {
                    MeshZone other = all2[j];
//                    new DebugComponent(area.getGeometry(),other.getGeometry()).showDialog();
                    if (other.getGeometry().isRectangular()) {
                        Domain bounds2 = other.getGeometry().getDomain();
                        if (filter.accept(MeshZoneType.ATTACHX)) {
                            double x0 = -1;
                            if (bounds1.getXMax() == bounds2.getXMin()) {
                                x0 = bounds1.getXMax();
                            } else if (bounds1.getXMin() == bounds2.getXMax()) {
                                x0 = bounds1.getXMin();
                            } else if (Math.abs(bounds1.getXMax() - bounds2.getXMin()) <= 1) {
                                x0 = bounds1.getXMax();
                            } else if (Math.abs(bounds1.getXMin() - bounds2.getXMax()) <= 1) {
                                x0 = bounds1.getXMax();
                            }
                            if (x0 > 0) {
                                double ww = Math.abs(Math.min(bounds1.getXwidth(), bounds2.getXwidth()));
                                double h1 = Math.max(bounds1.getYMin(), bounds2.getYMin());
                                double h2 = Math.min(bounds1.getYMax(), bounds2.getYMax());
                                double hh = h2 - h1;
                                if (ww > 1 && hh > 1) {
                                    MeshZone zzone = new MeshZone(Domain.forPoints(x0 - ww / 2, h1, ww, hh), MeshZoneShape.RECTANGLE, MeshZoneType.ATTACHX);
                                    zzone.setProperty("Attach For", "(" + i + "," + j + ")");
                                    added.add(zzone);
                                }
                            }
                        }
                        if (filter.accept(MeshZoneType.ATTACHY)) {

                            double y0 = -1;
                            if (bounds1.getYMax() == bounds2.getYMin()) {
                                y0 = bounds1.getYMax();
                            } else if (bounds1.getYMin() == bounds2.getYMax()) {
                                y0 = bounds1.getYMin();
                            } else if (Math.abs(bounds1.getYMax() - bounds2.getYMin()) <= 1) {
                                y0 = bounds1.getYMax();
                            } else if (Math.abs(bounds1.getYMin() - bounds2.getYMax()) <= 1) {
                                y0 = bounds1.getYMax();
                            }
                            if (y0 > 0) {
                                double hh = Math.abs(Math.min(bounds1.getYwidth(), bounds2.getYwidth()));
                                double w1 = Math.max(bounds1.getXMin(), bounds2.getXMin());
                                double w2 = Math.min(bounds1.getXMax(), bounds2.getXMax());
                                double ww = w2 - w1;
                                if (hh > 1 && ww > 1) {
                                    MeshZone zzone = new MeshZone(Domain.forPoints(w1, y0 - hh / 2, ww, hh), MeshZoneShape.RECTANGLE, MeshZoneType.ATTACHY);
                                    zzone.setProperty("Attach For", "(" + i + "," + j + ")");
                                    added.add(zzone);
                                }
                            }
                        }
                    }
                }

            }
        }
        all.addAll(added);
        return all;
    }

    @Override
    public List<MeshZone> transform(List<MeshZone> zones, Domain globalBounds) {
        return addRectAttaches(zones, globalBounds);
    }

    @Override
    public String toString() {
        String n = getClass().getSimpleName();
        String a = filter.dump();
        int cc = getCount();
        String c = cc > 1 ? ("(" + cc + ")") : "";
        return n + a + c;
    }

    @Override
    public Dumper getDumper() {
        Dumper h = super.getDumper();
        h.add("filter", filter);
        return h;
    }

    public MeshZoneTypeFilter getFilter() {
        return filter;
    }

}
