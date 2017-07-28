package net.vpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.geom.GeomUtils;
import net.vpc.scholar.hadrumaths.meshalgo.DefaultOption;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.awt.*;
import java.util.List;

public class MeshOptionsConsDes extends DefaultOption {
    private static final long serialVersionUID = -1010101010101001058L;
    ConsDesPrecision precision;

    public MeshOptionsConsDes() {
        Polygon[] p = new Polygon[1];
        p[0] = new Polygon();
        precision = new ConsDesSurfacePrecision(400);
    }

    @Override
    public Dumper getDumpStringHelper() {
        Dumper h=super.getDumpStringHelper();
        h.add("precision",precision);
        return h;
    }


    @Override
    public boolean isMeshAllowed(List<Triangle> t, int iteration) {
        return precision.isMeshAllowed(t, iteration) && (enhancedMeshZone==null || enhancedMeshZone.isZoneValide(t));
    }

    @Override
    public Triangle selectMeshTriangle(List<Triangle> t, int iteration) {
        if (isMeshAllowed(t, iteration)) {
            if (precision.isMeshAllowed(t, iteration)) {
                return GeomUtils.biggest(t);//TODO buggest??
            } else {
                return enhancedMeshZone!=null?enhancedMeshZone.firstTriangleInZoneValide(t):null;
            }
        } else {
            return null;
        }
    }

    public MeshOptionsConsDes setPrecision(ConsDesPrecision pr) {
        precision = pr;
        return this;
    }

    public MeshOptionsConsDes setMaxTriangles(int max) {
        return setPrecision(new ConsDesTriangleCountPrecision(max));
    }

    public MeshOptionsConsDes setMaxIterations(int max) {
        return setPrecision(new ConsDesIterationPrecision(max));
    }

    public int getMaxTriangles() {
        if (precision instanceof ConsDesTriangleCountPrecision) {
            return ((ConsDesTriangleCountPrecision) precision).nbre;
        }
        return 0;
    }
}
