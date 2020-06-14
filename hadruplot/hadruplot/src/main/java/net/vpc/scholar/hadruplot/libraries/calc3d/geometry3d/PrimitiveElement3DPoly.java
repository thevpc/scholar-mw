package net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import net.vpc.scholar.hadruplot.libraries.calc3d.math.AffineTransform3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

import java.awt.*;
import java.util.ArrayList;

/**
 * class to represent a rectangle PrimitiveElement3D, which acts as building block for surface
 *
 * @author mahesh
 */
public class PrimitiveElement3DPoly extends PrimitiveElement3D {
    /**
     * set it true to draw edges only when they are parallel to x or y axis
     */
    public boolean drawContours = false;
    public ArrayList<Vector3D> vertices = new ArrayList<Vector3D>();
    /**
     * Unit vector perpendicular to the plane
     */
    public Vector3D normal;


    //Default Constructor
    public PrimitiveElement3DPoly() {
        this.prefs().setFillColor(Color.white);
        this.prefs().setLineColor(Color.black);
    }

    ;

    /**
     * Create Polygon primitiveElement3DS, with vertices in clockwise order
     *
     * @param pts 4 vertices in clockwise order
     */
    public PrimitiveElement3DPoly(Vector3D... pts) {
        for (Vector3D pt : pts) {
            vertices.add(new Vector3D(pt));
        }
        this.prefs().setFillColor(Color.black);
        this.prefs().setLineColor(Color.BLACK);
        reCalculateNormalandCentre();
    }

    public PrimitiveElement3DPoly(ArrayList<Vector3D> vertices) {
        this.vertices = vertices;
        vertices.size();
        this.prefs().setFillColor(Color.black);
        this.prefs().setLineColor(Color.black);
        reCalculateNormalandCentre();
    }

    /**
     * Makes copy of primitiveElement3D
     *
     * @param element
     */
    public PrimitiveElement3DPoly(PrimitiveElement3DPoly element) {
        for (Vector3D pt : element.vertices) {
            vertices.add(new Vector3D(pt));
        }
        this.prefs().setFillColor(element.prefs().getFillColor());
        this.prefs().setLineColor(element.prefs().getLineColor());
        reCalculateNormalandCentre();
    }

    /**
     * Returns a new {@link PrimitiveElement3DPoly} object with count number of points, where the
     * points are evenly distributed around the unit circle.  The resulting {@link PrimitiveElement3DPoly}
     * will be centered on the origin.
     * <p>
     * The radius parameter is the distance from the center of the polygon to each vertex.
     * <p>
     * The theta parameter is a vertex angle offset used to rotate all the vertices
     * by the given amount.
     *
     * @param count  the number of vertices
     * @param radius the radius from the center to each vertex in meters
     * @param theta  the vertex angle offset in radians
     * @return {@link Polygon}
     * @throws IllegalArgumentException if count is less than 3 or radius is less than or equal to zero
     */
    public static final PrimitiveElement3DPoly createUnitCirclePolygon(int count, double radius, double theta) {
        // check the count
        if (count < 3)
            throw new IllegalArgumentException("invalidVerticesSize: Atleast 3 distinct vertices are required");
        // check the radius
        if (radius <= 0.0) throw new IllegalArgumentException("radius must be positive");
        Vector3D[] verts = new Vector3D[count];
        double angle = 2 * Math.PI / count;
        for (int i = count - 1; i >= 0; i--) {
            verts[i] = new Vector3D(Math.cos(angle * i + theta) * radius, Math.sin(angle * i + theta) * radius, 0);
        }
        return new PrimitiveElement3DPoly(verts);
    }

    public void addPoint(Vector3D pt) {
        vertices.add(new Vector3D(pt));
        //numPts++;
        reCalculateNormalandCentre();
    }

    public void addPoints(Vector3D[] pts) {
        for (Vector3D pt : pts) {
            vertices.add(new Vector3D(pt));
        }
        //numPts=vertices.size();
        reCalculateNormalandCentre();
    }

    /**
     * returns number of vertices of polygon
     */
    public int getVerticesCounr() {
        return vertices.size();
    }

    /**
     * calculate the average of all 4 vertices and stores in centre
     */
    private void calculateCentre() {
        //vertices.trimToSize();
        centre = new Vector3D(0, 0, 0);
        for (Vector3D v : vertices) {
            centre.addEq(v);
        }
        if (vertices.size() > 1) centre.scaleEq(1.0 / (vertices.size()));
    }

    /**
     * Calculate this surface's normal vector.
     */
    private void calculateSurfaceNormal() {
        normal = null;
        if (vertices.size() < 3) return;

        Vector3D v1, v2, v3;
        v1 = vertices.get(0);
        v2 = null;
        v3 = null;
        int i = 0;
        for (i = 1; i < vertices.size(); i++) {
            if (!Vector3D.checkCoincide(v1, vertices.get(i))) {
                v2 = vertices.get(i);
                break;
            }
        }

        //if v2 is null then we do not have different vertex
        if (v2 == null) return;

        for (i = i + 1; i < vertices.size(); i++) {
            if (!Vector3D.checkCoincide(v2, vertices.get(i))) {
                v3 = vertices.get(i);
                break;
            }
        }

        //if v3 is null then we do not have 3 different vertex
        if (v3 == null) return;

        normal = Vector3D.getSurfaceNormal(v1, v2, v3);

        /**
         normal =Vector3D.getSurfaceNormal(
         vertices.get(0),
         vertices.get(1),
         vertices.get(2));
         if ((null==normal)&& (vertices.size()>3))
         normal =Vector3D.getSurfaceNormal(
         vertices.get(1),
         vertices.get(2),
         vertices.get(3) );
         if ((null==normal)&& (vertices.size()>4))
         normal =Vector3D.getSurfaceNormal(
         vertices.get(2),
         vertices.get(3),
         vertices.get(4) );
         if ((null==normal)&& (vertices.size()>5))
         normal =Vector3D.getSurfaceNormal(
         vertices.get(3),
         vertices.get(4),
         vertices.get(0) );
         **/

    }

    /**
     * Updtaes normal and centre
     */
    public void reCalculateNormalandCentre() {
        calculateCentre();
        calculateSurfaceNormal();
    }

    @Override
    public void transform(AffineTransform3D T) {
        for (Vector3D v : vertices) {
            T.transform(v);
        }
        T.transform(centre);
        if (null != normal) T.transform(normal);
    }

    public Plane3D getPlane3D() {
        if (vertices.size() < 3) return null;
        reCalculateNormalandCentre();
        if (normal == null) {
            System.out.println("Normal=null in plane3d class");
            return null;
        }
        return new Plane3D(vertices.get(1), normal);
    }

}
