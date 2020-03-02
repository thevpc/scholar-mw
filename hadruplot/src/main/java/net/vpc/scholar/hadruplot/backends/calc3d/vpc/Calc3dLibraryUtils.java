package net.vpc.scholar.hadruplot.backends.calc3d.vpc;

import net.vpc.scholar.hadruplot.backends.calc3d.core.Globalsettings;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;

public class Calc3dLibraryUtils {
    public static boolean isFinite(Vector3D v){
        if(!Double.isFinite(v.getX())){
            return false;
        }
        if(!Double.isFinite(v.getY())){
            return false;
        }
        if(!Double.isFinite(v.getZ())){
            return false;
        }
        return true;
    }

    public static Vector3D[] toVectors(Point3D[] r) {
        Vector3D[] a=new Vector3D[r.length];
        for (int i = 0; i < r.length; i++) {
            a[i]=r[i].toVector();
        }
        return a;
    }

    public static Vector3D[][] inverseMap(Element3D e, Vector3D[][] r) {
        Vector3D[][] a=new Vector3D[r.length][];
        for (int i = 0; i < a.length; i++) {
            a[i]= inverseMap(e,r[i]);
        }
        return a;
    }

    public static Vector3D[] inverseMap(Element3D e, Vector3D[] r) {
        Vector3D[] a=new Vector3D[r.length];
        for (int i = 0; i < a.length; i++) {
            a[i]=inverseMap(e,r[i]);
        }
        return a;
    }

    public static Vector3D inverseMap(Element3D e, Vector3D r) {
        Globalsettings settings = e.getSceneManager().getSettings();
        return new Vector3D(
                settings.inverseMapX(r.getX()),
                settings.inverseMapY(r.getY()),
                settings.inverseMapZ(r.getZ())
        );
    }
}
