package net.vpc.scholar.hadruplot.libraries.calc3d.geometry3d;

import net.vpc.scholar.hadruplot.libraries.calc3d.math.AffineTransform3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;

import java.awt.*;

//class to represent text
public class PrimitiveElement3DString extends PrimitiveElement3D {
    public int alignment;
    public Color color;
    public String string;
    public boolean tick = false;
    public Vector3D tickPerp;
    public Vector3D p1, p2;

    // tickPerp is to indicate direction perpendicular to the tick, null for no tick
    public PrimitiveElement3DString(String str, Vector3D p1, Vector3D p2, Color color) {
        this.string = str;
        this.color = color;
        this.p1 = p1;
        this.p2 = p2;
        //this.alignment=alignment;
        this.centre = p1.add(p2).scale(0.5);

    }

    @Override
    public void transform(AffineTransform3D T) {
        T.transform(p1);
        T.transform(p2);
        T.transform(centre);
    }

}