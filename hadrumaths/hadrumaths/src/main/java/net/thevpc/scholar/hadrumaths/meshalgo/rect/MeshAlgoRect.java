package net.thevpc.scholar.hadrumaths.meshalgo.rect;


import net.thevpc.nuts.elem.NElement;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZoneShape;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZoneType;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 22 mai 2007 00:02:41
 */
public class MeshAlgoRect implements MeshAlgo, Cloneable {
    public static final MeshAlgoRect RECT_ALGO_LOW_RESOLUTION = new MeshAlgoRect(GridPrecision.LEAST_PRECISION);
    private static final long serialVersionUID = 1L;
    private final int dividerX = 2;
    private final int dividerY = 2;
    private double minRelativeSizeX = 0.01;
    private double minRelativeSizeY = 0.01;
    private double maxRelativeSizeX = 0.25;
    private double maxRelativeSizeY = 0.25;
    private GridPrecision gridPrecision;

    public MeshAlgoRect() {
        this(new GridPrecision(0, 4));
    }

    public MeshAlgoRect(GridPrecision gridPrecision) {
        setGridPrecision(gridPrecision);
    }

    public void setGridPrecision(GridPrecision gridPrecision) {
        this.gridPrecision = gridPrecision;
        int maxgx = gridPrecision == null ? 0 : gridPrecision.getXmax();
        int mingx = gridPrecision == null ? 0 : gridPrecision.getXmin();
//        if(mingx==0 || maxgx==0){
//            System.out.println("Null:"+System.identityHashCode(this)+":"+getStructure().getClass().getSimpleName()+":precision = " + precision);
//        }
        if (mingx > maxgx) {
            throw new IllegalArgumentException("gpMaxGridPrecisionX<gpMinGridPrecisionX : (" + mingx + " > " + maxgx + ")");
        }
        int maxgy = (gridPrecision == null ? 0 : gridPrecision.getYmax()) < 0 ? -1 : (gridPrecision == null ? 0 : gridPrecision.getYmax());
        int mingy = (gridPrecision == null ? 0 : gridPrecision.getYmin()) < 0 ? -1 : (gridPrecision == null ? 0 : gridPrecision.getYmin());

        if (mingy > maxgy) {
            throw new IllegalArgumentException("gpMaxGridPrecisionY<gpMinGridPrecisionY : (" + mingy + " > " + maxgy + ")");
        }

        setMinRelativeSizeX(1.0 / Maths.pow(2, maxgx));
        setMinRelativeSizeY(1.0 / Maths.pow(2, maxgy < 0 ? maxgx : maxgy));
        setMaxRelativeSizeX(1.0 / Maths.pow(2, mingx));
        setMaxRelativeSizeY(1.0 / Maths.pow(2, mingy < 0 ? mingx : mingy));
    }

    public double getMinRelativeSizeX() {
        return minRelativeSizeX;
    }
//    public String dump() {
//        Dumper h = new Dumper(getClass().getSimpleName());
//        h.add("dividerX", dividerX);
//        h.add("dividerY", dividerY);
//        h.add("minRelativeSizeX", minRelativeSizeX);
//        h.add("minRelativeSizeY", minRelativeSizeY);
//        h.add("maxRelativeSizeX", maxRelativeSizeX);
//        h.add("maxRelativeSizeY", maxRelativeSizeY);
//        return h.toString();
//    }

    public void setMinRelativeSizeX(double minRelativeSizeX) {
        if (minRelativeSizeX <= 0 || minRelativeSizeX <= 0) {
            throw new IllegalArgumentException("minRelativeSizeX <=0");
        }
        this.minRelativeSizeX = minRelativeSizeX;
    }

    @Override
    public NElement toElement() {
        return NElement.ofObjectBuilder(getClass().getSimpleName())
                .add("dividerX", NElementHelper.elem(dividerX))
                .add("dividerY", NElementHelper.elem(dividerY))
                .add("minRelativeSizeX", NElementHelper.elem(minRelativeSizeX))
                .add("minRelativeSizeY", NElementHelper.elem(minRelativeSizeY))
                .add("maxRelativeSizeX", NElementHelper.elem(maxRelativeSizeX))
                .add("maxRelativeSizeY", NElementHelper.elem(maxRelativeSizeY))
                .build();
    }

    public double getMinRelativeSizeY() {
        return minRelativeSizeY;
    }

    public void setMinRelativeSizeY(double minRelativeSizeY) {
        if (minRelativeSizeY <= 0 || minRelativeSizeY <= 0) {
            throw new IllegalArgumentException("minRelativeSizeY <=0");
        }
        this.minRelativeSizeY = minRelativeSizeY;
    }

    public double getMaxRelativeSizeX() {
        return maxRelativeSizeX;
    }

    public void setMaxRelativeSizeX(double maxRelativeSizeX) {
        if (maxRelativeSizeX <= 0 || maxRelativeSizeX <= 0) {
            throw new IllegalArgumentException("maxRelativeSizeX <=0");
        }
        this.maxRelativeSizeX = maxRelativeSizeX;
    }

    public double getMaxRelativeSizeY() {
        return maxRelativeSizeY;
    }

    public void setMaxRelativeSizeY(double maxRelativeSizeY) {
        if (maxRelativeSizeY <= 0 || maxRelativeSizeY <= 0) {
            throw new IllegalArgumentException("maxRelativeSizeY <=0");
        }
        this.maxRelativeSizeY = maxRelativeSizeY;
    }

    private Rectangle2D.Double floatToDouble(Rectangle2D x) {
        return new Rectangle2D.Double(
                (x.getX()),
                (x.getY()),
                (x.getWidth()),
                (x.getHeight())
        );
    }

//    private Rectangle2D.Float doubleToFloat(Rectangle2D x) {
//        return new Rectangle2D.Float(
//                doubleToFloatX(x.getX()),
//                doubleToFloatY(x.getY()),
//                doubleToFloatW(x.getWidth()),
//                doubleToFloatH(x.getHeight())
//        );
//    }

    private List<Geometry> autoFusion(Geometry polygon, List<Geometry> all) {
        Domain domain = polygon.getDomain();
        double maxRelativeSizeXAbsolute = maxRelativeSizeX * domain.xwidth();
        double maxRelativeSizeYAbsolute = maxRelativeSizeY * domain.ywidth();
        boolean changes = true;
        while (changes) {
            changes = false;
            Geometry[] all2 = all.toArray(new Geometry[0]);
            for (int i = 0; i < all2.length; i++) {
                Geometry area = all2[i];
                Geometry fusion = null;
                Geometry union = null;
                if (area.isRectangular()) {
                    for (int j = i + 1; j < all2.length; j++) {
                        Geometry other = all2[j];
                        if (other.isRectangular()) {
                            Geometry a2 = area.clone();
                            a2 = a2.addGeometry(other);
                            if (a2.isRectangular()) {
                                Domain d = a2.getDomain();
                                double dw = d.getXwidth();
                                double dh = d.getYwidth();
                                if (dw <= maxRelativeSizeXAbsolute && dh <= maxRelativeSizeYAbsolute) {
                                    fusion = other;
                                    union = a2;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (fusion != null) {
                    all.add(union);
                    all.remove(area);
                    all.remove(fusion);
                    changes = true;
                    break;
                }
            }
        }
        //_debug_autoFusion(all);
        return all;
    }

    public Collection<MeshZone> meshPolygon(Geometry polygon) {
        Domain domain = polygon.getDomain();
        double maxAbsoluteSizeX = maxRelativeSizeX * domain.xwidth();
        double maxAbsoluteSizeY = maxRelativeSizeY * domain.ywidth();
        double minAbsoluteSizeX = minRelativeSizeX * domain.xwidth();
        double minAbsoluteSizeY = minRelativeSizeY * domain.ywidth();
        List<Geometry> res = mesh(polygon, maxAbsoluteSizeX, maxAbsoluteSizeY, minAbsoluteSizeX, minAbsoluteSizeY);
        res = autoFusion(polygon, res);
        ArrayList<MeshZone> zones = new ArrayList<MeshZone>();
        for (Geometry re : res) {
            zones.add(new MeshZone(re, MeshZoneShape.RECTANGLE, MeshZoneType.MAIN));
        }
        return zones;
    }

    private List<Geometry> mesh(Geometry p, double maxAbsoluteSizeX, double maxAbsoluteSizeY, double minAbsoluteSizeX, double minAbsoluteSizeY) {
        if (minAbsoluteSizeX <= 0 || minAbsoluteSizeY <= 0) {
            throw new IllegalArgumentException("min size <=0");
        }
        Domain d = p.getDomain();
        double dw = d.getXwidth();
        double dh = d.getYwidth();
        double dmx = d.getXMin();
        double dmy = d.getYMin();

        int maxx = ((int) Maths.round(dw / maxAbsoluteSizeX));
        int maxy = ((int) Maths.round(dh / maxAbsoluteSizeY));
        List<Geometry> areas = new ArrayList<Geometry>();

        if (maxAbsoluteSizeX < minAbsoluteSizeX && maxAbsoluteSizeY < minAbsoluteSizeY) {
            //JOptionPane.showMessageDialog(null,gridPrecision +" : No meshing "+maxAbsoluteSizeX +"<" +minAbsoluteSizeX +" && " +maxAbsoluteSizeY +"<" + minAbsoluteSizeY);
//            System.out.println("dw,dh,midSizeX,minAbsoluteSizeX,midSizeY,minAbsoluteSizeY = " + dw+","+dh+","+midSizeX + "," + minAbsoluteSizeX + "," + midSizeY + "," + minAbsoluteSizeY);
            return areas;
        }

        for (int i = 0; i < maxx; i++) {
            for (int j = 0; j < maxy; j++) {
                Domain r = Domain.ofWidth(
                        dmx + (i * maxAbsoluteSizeX),
                        maxAbsoluteSizeX,
                        dmy + (j * maxAbsoluteSizeY),
                        maxAbsoluteSizeY
                );
                Geometry a = r.toGeometry();
                String prefix = "i,j :" + i + "," + j + " : " + gridPrecision + " : ";
                //AreaComponent.showDialog(prefix+"before",a,p);
                a = a.intersectGeometry(p);
                //AreaComponent.showDialog(prefix+"after",a,p);
                if (!a.isEmpty()) {
                    //AreaComponent.showDialog(prefix+"not empty",a,p);
                    if (a.isSingular()) {
                        //AreaComponent.showDialog(prefix+"singular",a,p);
                        if (a.isRectangular()) {
                            //AreaComponent.showDialog(prefix+"rect",a,p);
                            areas.add(a);
                        } else {
                            //AreaComponent.showDialog(prefix+"non rect",a,p);
                            List<Geometry> sub = mesh(a, r.getXwidth() / dividerX, r.getYwidth() / dividerY, minAbsoluteSizeX, minAbsoluteSizeY);
                            if (sub.size() > 0) {
                                areas.addAll(sub);
                            }
//                            all.addAll(mesh(a, minGridSizeX, minGridSizeY, minAbsoluteSizeX, minAbsoluteSizeY));
                        }
                    } else {
                        //AreaComponent.showDialog(prefix+"non singular",a,p);
                        List<Geometry> sub = mesh(a, r.getXwidth() / dividerX, r.getYwidth() / dividerY, minAbsoluteSizeX, minAbsoluteSizeY);
                        if (sub.size() > 0) {
                            areas.addAll(sub);
                        }
                        System.out.println("what to do if not singular ?");
                        //todo : what to do if not singular????
                    }
                } else {
                    //AreaComponent.showDialog(prefix+"empty",a,p);
                }
            }
        }
        return areas;
    }

    public MeshAlgoRect clone() {
        try {
            return (MeshAlgoRect) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Never");
        }
    }

    @Override
    public String toString() {
        return dump();
//        Dumper h = new Dumper(getClass().getSimpleName(), Dumper.Type.SIMPLE);
//        h.add("divider", dividerX + "," + dividerY);
//        h.add("relativeSize", minRelativeSizeX + "->" + maxRelativeSizeX + "," + minRelativeSizeY + "->" + maxRelativeSizeY);
//        return h.toString();
    }
}
