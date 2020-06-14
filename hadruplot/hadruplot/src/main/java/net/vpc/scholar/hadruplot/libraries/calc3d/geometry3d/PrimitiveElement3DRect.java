package net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import net.vpc.scholar.hadruplot.libraries.calc3d.math.AffineTransform3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

import java.awt.*;

/**
 * class to represent a rectangle PrimitiveElement3D, which acts as building block for surface
 *
 * @author mahesh
 */
public class PrimitiveElement3DRect extends PrimitiveElement3D {

    public Vector3D[] vertices = new Vector3D[4];
    /**
     * Unit vector perpendicular to the plane
     */
    public Vector3D normal;


    /**
     * Default Constructor
     * Used to supply this as parameter eg Split
     */
    public PrimitiveElement3DRect() {
    }

    /**
     * Create rectangular primitiveElement3DS, with vertices in clockwise order
     *
     * @param p1 4 vertices in clockwise order
     */
    public PrimitiveElement3DRect(Vector3D p1, Vector3D p2, Vector3D p3, Vector3D p4) {
        vertices[0] = p1;
        vertices[1] = p2;
        vertices[2] = p3;
        vertices[3] = p4;
        calculateCentre();
        calculateSurfaceNormal();
        this.prefs().setLineColor(Color.BLACK);
        this.prefs().setFillColor(Color.white);
    }

    /**
     * Makes copy of primitiveElement3D
     *
     * @param element
     */
    public PrimitiveElement3DRect(PrimitiveElement3DRect element) {
        vertices[0] = new Vector3D(element.vertices[0]);
        vertices[1] = new Vector3D(element.vertices[1]);
        vertices[2] = new Vector3D(element.vertices[2]);
        vertices[3] = new Vector3D(element.vertices[3]);
        prefs().setLineColor(element.prefs().getLineColor());
        prefs().setFillColor(element.prefs().getFillColor());
        this.centre = element.getCentre();
        calculateSurfaceNormal();
    }

    /**
     * Create rectangular primitiveElement3DS, with vertices in clockwise order
     *
     * @param p1 4 vertices in clockwise order
     */
    public PrimitiveElement3DRect(Vector3D p1, Vector3D p2, Vector3D p3, Vector3D p4, Color surfacecolor, Color curvecolor) {
        this(p1, p2, p3, p4);
        this.prefs().setFillColor(surfacecolor);
        this.prefs().setLineColor(curvecolor);
    }

    /**
     * calculate the average of all 4 vertices and stores in centre
     */
    private void calculateCentre() {
        centre = new Vector3D();
        for (Vector3D v : vertices) {
            centre.addEq(v);
        }
        centre.scaleEq(0.25);
    }

    /**
     * Calculate this surface's normal vector.
     */
    private void calculateSurfaceNormal() {
        normal = Vector3D.getSurfaceNormal(
                vertices[0],
                vertices[1],
                vertices[2]);
    }

    @Override
    public void transform(AffineTransform3D T) {
        for (Vector3D v : vertices) {
            T.transform(v);
        }
        T.transform(centre);
        T.transform(normal);
    }

}
