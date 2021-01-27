package net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import net.thevpc.scholar.hadruplot.libraries.calc3d.engine3d.Camera3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.log.Logger;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

import java.util.ArrayList;

public class Clip {

    final static int VISIBILITY_UNCLIPPED = 0;
    final static int VISIBILITY_CLIPPED = 1;
    final static int VISIBILITY_OFFSCREEN = 2;
    final static int zLT = 32;
    final static int zGT = 16;
    final static int xLT = 8;
    final static int xGT = 4;
    final static int yLT = 2;
    final static int yGT = 1;
    private static Logger LOG = Logger.getLogger(Camera3D.class.getName());
    private double xmin, xmax, ymin, ymax, zmin, zmax;
    private Vector3D box_centre;

    public Clip(double xmin, double xmax, double ymin, double ymax,
                double zmin, double zmax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.zmin = zmin;
        this.xmax = xmax;
        this.ymax = ymax;
        this.zmax = zmax;
        box_centre = new Vector3D((xmax + xmin) / 2, (ymax + ymin) / 2, (zmax + zmin) / 2);
    }

    public Clip(Box3D clipBox) {
        this.xmin = clipBox.getMinX();
        this.ymin = clipBox.getMinY();
        this.zmin = clipBox.getMinZ();
        this.xmax = clipBox.getMaxX();
        this.ymax = clipBox.getMaxY();
        this.zmax = clipBox.getMaxZ();
        box_centre = new Vector3D((xmax + xmin) / 2, (ymax + ymin) / 2, (zmax + zmin) / 2);
    }

    public Box3D getClipBox() {
        return new Box3D(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public void setClipBox(Box3D clipBox) {
        this.xmin = clipBox.getMinX();
        this.ymin = clipBox.getMinY();
        this.zmin = clipBox.getMinZ();
        this.xmax = clipBox.getMaxX();
        this.ymax = clipBox.getMaxY();
        this.zmax = clipBox.getMaxZ();
        box_centre = new Vector3D((xmax + xmin) / 2, (ymax + ymin) / 2, (zmax + zmin) / 2);
    }

    public void setClip(double xmin, double xmax, double ymin, double ymax,
                        double zmin, double zmax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.zmin = zmin;
        this.xmax = xmax;
        this.ymax = ymax;
        this.zmax = zmax;
        box_centre = new Vector3D((xmax + xmin) / 2, (ymax + ymin) / 2, (zmax + zmin) / 2);
    }

    /**
     * Computes codes for cohen-sutherland line clipping Algorithm
     */
    final int clipCode(double x, double y, double z) {
        int code = 0;
        if (x < xmin)
            code |= xLT;
        else if (x > xmax)
            code |= xGT;

        if (y < ymin)
            code |= yLT;
        else if (y > ymax)
            code |= yGT;

        if (z < zmin)
            code |= zLT;
        else if (z > zmax)
            code |= zGT;

        return code;
    }


    private int clipCode(Vector3D centre) {
        return clipCode(centre.getX(), centre.getY(), centre.getZ());
    }


    public int getTrimmedLine(Vector3D v1, Vector3D v2, Vector3D vr1,
                              Vector3D vr2) {
        int cc1, cc2;
        double x1t, y1t, z1t, x2t, y2t, z2t;

        x1t = v1.getX();
        x2t = v2.getX();
        y1t = v1.getY();
        y2t = v2.getY();
        z1t = v1.getZ();
        z2t = v2.getZ();

        cc1 = clipCode(x1t, y1t, z1t);
        cc2 = clipCode(x2t, y2t, z2t);
        if ((cc1 | cc2) == 0)
            return VISIBILITY_UNCLIPPED;

        do {
            if ((cc1 & cc2) != 0)
                return VISIBILITY_OFFSCREEN;

            double dx = x2t - x1t;
            double dy = y2t - y1t;
            double dz = z2t - z1t;
            /*
             * double onebydx = x2t - x1t; double onebydy = y2t - y1t; double
             * onebydz = z2t - z1t;
             */

            if (cc1 != 0) { // cohen-sutherland line clipping
                if ((cc1 & xLT) != 0) {
                    y1t += ((xmin - x1t) * dy) / dx;
                    z1t += ((xmin - x1t) * dz) / dx;
                    x1t = xmin;
                } else if ((cc1 & xGT) != 0) {
                    y1t += ((xmax - x1t) * dy) / dx;
                    z1t += ((xmax - x1t) * dz) / dx;
                    x1t = xmax;
                } else if ((cc1 & yLT) != 0) {
                    x1t += ((ymin - y1t) * dx) / dy;
                    z1t += ((ymin - y1t) * dz) / dy;
                    y1t = ymin;
                } else if ((cc1 & yGT) != 0) {
                    x1t += ((ymax - y1t) * dx) / dy;
                    z1t += ((ymax - y1t) * dz) / dy;
                    y1t = ymax;
                } else if ((cc1 & zLT) != 0) {
                    x1t += ((zmin - z1t) * dx) / dz;
                    y1t += ((zmin - z1t) * dy) / dz;
                    z1t = zmin;
                } else // must be zGT
                {
                    x1t += ((zmax - z1t) * dx) / dz;
                    y1t += ((zmax - z1t) * dy) / dz;
                    z1t = zmax;
                }

                cc1 = clipCode(x1t, y1t, z1t);
            } else {
                if ((cc2 & xLT) != 0) {
                    y2t += ((xmin - x2t) * dy) / dx;
                    z2t += ((xmin - x2t) * dz) / dx;
                    x2t = xmin;
                } else if ((cc2 & xGT) != 0) {
                    y2t += ((xmax - x2t) * dy) / dx;
                    z2t += ((xmax - x2t) * dz) / dx;
                    x2t = xmax;
                } else if ((cc2 & yLT) != 0) {
                    x2t += ((ymin - y2t) * dx) / dy;
                    z2t += ((ymin - y2t) * dz) / dy;
                    y2t = ymin;
                } else if ((cc2 & yGT) != 0) {
                    x2t += ((ymax - y2t) * dx) / dy;
                    z2t += ((ymax - y2t) * dz) / dy;
                    y2t = ymax;
                } else if ((cc2 & zLT) != 0) {
                    x2t += ((zmin - z2t) * dx) / dz;
                    y2t += ((zmin - z2t) * dy) / dz;
                    z2t = zmin;
                } else // must be zGT
                {
                    x2t += ((zmax - z2t) * dx) / dz;
                    y2t += ((zmax - z2t) * dy) / dz;
                    z2t = zmax;
                }
                cc2 = clipCode(x2t, y2t, z2t);
            }
        } while ((cc1 | cc2) != 0);

        // return clipped coordinates
        vr1.set(x1t, y1t, z1t);
        vr2.set(x2t, y2t, z2t);
        // System.out.println("trimmed line " + x1t + " " + y1t + " " + z1t +
        // " " + x2t + " " + y2t + " " + z2t + " " + cc1 + "/" + cc2);
        return VISIBILITY_CLIPPED;
    }

    public int getClippedPoly1(ArrayList<Vector3D> poly, ArrayList<Vector3D> poly_return) {
        if (poly.size() < 3) return VISIBILITY_OFFSCREEN;

        Vector3D vr1 = new Vector3D();
        Vector3D vr2 = new Vector3D();
        Vector3D v1 = new Vector3D(poly.get(poly.size() - 1));
        boolean clipped = false;
        for (Vector3D v2 : poly) {
            int i = getTrimmedLine(v1, v2, vr1, vr2);
            switch (i) {
                case VISIBILITY_OFFSCREEN: {
                    break;
                }

                case VISIBILITY_UNCLIPPED: {
                    //poly_return.add(new Vector3D(v1));
                    poly_return.add(new Vector3D(v2));
                    break;
                }
                case VISIBILITY_CLIPPED: {
                    clipped = true;
                    //poly_return.add(vr1);
                    poly_return.add(vr2);
                    break;
                }

            }
            v1.set(v2);
        }


        if (poly_return.size() == 0) return VISIBILITY_OFFSCREEN;
        return (clipped ? VISIBILITY_UNCLIPPED : VISIBILITY_CLIPPED);
    }


    public int getClippedPoly(ArrayList<Vector3D> poly,
                              ArrayList<Vector3D> poly_return) {
        if (poly.size() < 3)
            return VISIBILITY_OFFSCREEN;
        Plane3D[] planes = {new Plane3D(1, 0, 0, -xmin),
                new Plane3D(1, 0, 0, -xmax), new Plane3D(0, 1, 0, -ymin),
                new Plane3D(0, 1, 0, -ymax), new Plane3D(0, 0, 1, -zmin),
                new Plane3D(0, 0, 1, -zmax)};
        ArrayList<Vector3D> polyTemp = new ArrayList<Vector3D>();
        poly_return.addAll(poly);
        for (Plane3D plane : planes) {
            Vector3D v1;
            polyTemp.clear();

            v1 = new Vector3D(poly_return.get(poly_return.size() - 1));
            for (Vector3D v2 : poly_return) {
                if (plane.f(v1) * plane.f(box_centre) >= 0) { //v1 is inside plane
                    polyTemp.add(new Vector3D(v1));
                    if (plane.f(v2) * plane.f(v1) < 0) {
                        Vector3D v = plane.getIntersection(v1, v2);
                        v.edge = true;
                        polyTemp.add(v);
                    }
                } else if (plane.f(v2) * plane.f(box_centre) > 0) { //v2 is inside plane(previously here was >=
                    Vector3D v = plane.getIntersection(v1, v2);
                    v.edge = true;
                    polyTemp.add(v);
                }
                v1.set(v2);
            }
            if (polyTemp.size() < 2) return VISIBILITY_OFFSCREEN;
            poly_return.clear();
            poly_return.addAll(polyTemp);// keep on working with the new polygon

        }
        if (poly_return.size() < 2) return VISIBILITY_OFFSCREEN;
        return VISIBILITY_UNCLIPPED;

    }


    /**
     * returns portion of line determined by given equation which lies in clipped region
     *
     * @param line : line3D
     * @return Clipped Line (returns null if no portion of line lies in clipped region or line3D is invalid)
     */
    public PrimitiveElement3DCurve getClippedLinefromLine3D(Line3D line) {
        double t1, t2;
        if (line.DirVector3D.getZ() != 0) {  //*line cuts xy plane
            t1 = (zmin - line.Pt3D.getZ()) / line.DirVector3D.getZ();
            t2 = (zmax - line.Pt3D.getZ()) / line.DirVector3D.getZ();
        } else if (line.DirVector3D.getY() != 0) { //*line cuts XZ plane
            t1 = (ymin - line.Pt3D.getY()) / line.DirVector3D.getY();
            t2 = (ymax - line.Pt3D.getY()) / line.DirVector3D.getY();
        } else if (line.DirVector3D.getX() != 0) { //*line cuts YZ plane
            t1 = (xmin - line.Pt3D.getX()) / line.DirVector3D.getX();
            t2 = (xmax - line.Pt3D.getX()) / line.DirVector3D.getX();
        } else { ////*line is invalid
            return null;
        }

        /*return new PrimitiveElement3DCurve joining parametric ppoints found above*/
        Vector3D v1 = new Vector3D();
        Vector3D v2 = new Vector3D();
        if (getTrimmedLine(line.getParametricPoint(t1), line.getParametricPoint(t2), v1, v2) == 1)
            return new PrimitiveElement3DCurve(v1, v2);
        else
            return new PrimitiveElement3DCurve(line.getParametricPoint(t1), line.getParametricPoint(t2));
    }

    /**
     * returns portion of primitiveElement3D determined by given equation which lies in clipped region
     *
     * @param plane
     * @return Clipped Polygon (returns null if no portion of primitiveElement3D lies in clipped region or primitiveElement3D is invalid)
     */
    public PrimitiveElement3DPoly getClippedPolygonfromPlane(Plane3D plane) {
        ArrayList<Vector3D> vertices = new ArrayList<Vector3D>();
        if (plane.get_c() != 0) { //Plane cuts xy plane
            vertices.add(new Vector3D(xmin, ymin, (-plane.get_d() - plane.get_a() * xmin - plane.get_b() * ymin) / plane.get_c()));
            vertices.add(new Vector3D(xmin, ymax, (-plane.get_d() - plane.get_a() * xmin - plane.get_b() * ymax) / plane.get_c()));
            vertices.add(new Vector3D(xmax, ymax, (-plane.get_d() - plane.get_a() * xmax - plane.get_b() * ymax) / plane.get_c()));
            vertices.add(new Vector3D(xmax, ymin, (-plane.get_d() - plane.get_a() * xmax - plane.get_b() * ymin) / plane.get_c()));
            PrimitiveElement3DPoly e = new PrimitiveElement3DPoly();
            getClippedPoly(vertices, e.vertices);
            //e.setFillColor(Color.red);
            e.reCalculateNormalandCentre();
            return e;
        } else if (plane.get_b() != 0) { //Plane cuts XZ plane
            vertices.add(new Vector3D(xmin, (-plane.get_d() - plane.get_c() * zmin - plane.get_a() * xmin) / plane.get_b(), zmin));
            vertices.add(new Vector3D(xmax, (-plane.get_d() - plane.get_c() * zmin - plane.get_a() * xmax) / plane.get_b(), zmin));
            vertices.add(new Vector3D(xmax, (-plane.get_d() - plane.get_c() * zmax - plane.get_a() * xmax) / plane.get_b(), zmax));
            vertices.add(new Vector3D(xmin, (-plane.get_d() - plane.get_c() * zmax - plane.get_a() * xmin) / plane.get_b(), zmax));
            PrimitiveElement3DPoly e = new PrimitiveElement3DPoly();
            getClippedPoly(vertices, e.vertices);
            //e.setFillColor(Color.green);
            e.reCalculateNormalandCentre();
            return e;
        } else if (plane.get_a() != 0) { //Plane cuts XZ plane
            vertices.add(new Vector3D((-plane.get_d() - plane.get_b() * ymin - plane.get_c() * zmin) / plane.get_a(), ymin, zmin));
            vertices.add(new Vector3D((-plane.get_d() - plane.get_b() * ymin - plane.get_c() * zmax) / plane.get_a(), ymin, zmax));
            vertices.add(new Vector3D((-plane.get_d() - plane.get_b() * ymax - plane.get_c() * zmax) / plane.get_a(), ymax, zmax));
            vertices.add(new Vector3D((-plane.get_d() - plane.get_b() * ymax - plane.get_c() * zmin) / plane.get_a(), ymax, zmin));
            PrimitiveElement3DPoly e = new PrimitiveElement3DPoly();
            getClippedPoly(vertices, e.vertices);
            //	e.setFillColor(Color.blue);
            e.reCalculateNormalandCentre();
            return e;
        } else return null;
    }

    public PrimitiveElement3D getClippedElement(PrimitiveElement3D primitiveElement3D) {
        if (primitiveElement3D instanceof PrimitiveElement3DPoint) {
            return (clipCode(primitiveElement3D.getCentre()) == 0) ? primitiveElement3D : null;
        } else if (primitiveElement3D instanceof PrimitiveElement3DCurve) {
            int visibility;
            Vector3D v1 = new Vector3D(), v2 = new Vector3D();
            visibility = getTrimmedLine(((PrimitiveElement3DCurve) primitiveElement3D).p1, ((PrimitiveElement3DCurve) primitiveElement3D).p2, v1, v2);
            switch (visibility) {
                case VISIBILITY_OFFSCREEN:
                    return null;
                case VISIBILITY_UNCLIPPED:
                    return primitiveElement3D;
                case VISIBILITY_CLIPPED: {
                    PrimitiveElement3DCurve ec = new PrimitiveElement3DCurve(v1, v2);
                    ec.prefs().copyFrom(primitiveElement3D.prefs());
                    return ec;
                }
            }
        } else if (primitiveElement3D instanceof PrimitiveElement3DString) {
            return (clipCode(primitiveElement3D.getCentre()) == 0) ? primitiveElement3D : null;
        } else if (primitiveElement3D instanceof PrimitiveElement3DPoly) {
            PrimitiveElement3DPoly ep = new PrimitiveElement3DPoly();
            int cc = getClippedPoly(((PrimitiveElement3DPoly) primitiveElement3D).vertices, ep.vertices);
            if (cc == VISIBILITY_OFFSCREEN) return null;
            ep.reCalculateNormalandCentre();
			ep.prefs().copyFrom(primitiveElement3D.prefs());
            ep.drawContours = ((PrimitiveElement3DPoly) primitiveElement3D).drawContours;
            return ep;
        } else if (primitiveElement3D instanceof PrimitiveElement3DArrow) {
            int visibility;
            Vector3D v1 = new Vector3D(), v2 = new Vector3D();
            visibility = getTrimmedLine(((PrimitiveElement3DArrow) primitiveElement3D).p1, ((PrimitiveElement3DArrow) primitiveElement3D).p2, v1, v2);
            switch (visibility) {
                case VISIBILITY_OFFSCREEN:
                    return null;
                case VISIBILITY_UNCLIPPED:
                    return primitiveElement3D;
                case VISIBILITY_CLIPPED: {
                    PrimitiveElement3DArrow ec = new PrimitiveElement3DArrow(v1, v2);
					ec.prefs().copyFrom(primitiveElement3D.prefs());
                    return ec;
                }
            }
        } else if (primitiveElement3D instanceof PrimitiveElement3DCollection) {
            PrimitiveElement3DCollection ec = new PrimitiveElement3DCollection();
            for (PrimitiveElement3D e : ((PrimitiveElement3DCollection) primitiveElement3D).primitiveElement3DS) {
                PrimitiveElement3D e1 = getClippedElement(e);
                if (null != e1) ec.addElement(e1);
            }
            return ec;
        }
        return primitiveElement3D;
    }


}
