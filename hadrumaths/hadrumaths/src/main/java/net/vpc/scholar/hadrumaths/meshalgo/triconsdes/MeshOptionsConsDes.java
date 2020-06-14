package net.vpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.geom.GeomUtils;
import net.vpc.scholar.hadrumaths.geom.Triangle;
import net.vpc.scholar.hadrumaths.meshalgo.DefaultOption;

import java.awt.*;
import java.util.List;

public class MeshOptionsConsDes extends DefaultOption {
    private static final long serialVersionUID = 1L;
    ConsDesPrecision precision;

    public MeshOptionsConsDes() {
        Polygon[] p = new Polygon[1];
        p[0] = new Polygon();
        precision = new ConsDesSurfacePrecision(400);
    }

    public MeshOptionsConsDes setMaxIterations(int max) {
        return setPrecision(new ConsDesIterationPrecision(max));
    }    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return super.toTsonElement(context).toObject().builder()
                .add("precision", context.elem(precision))
                .build();
    }

    public int getMaxTriangles() {
        if (precision instanceof ConsDesTriangleCountPrecision) {
            return ((ConsDesTriangleCountPrecision) precision).nbre;
        }
        return 0;
    }    @Override
    public boolean isMeshAllowed(List<Triangle> t, int iteration) {
        return precision.isMeshAllowed(t, iteration) && (enhancedMeshZone == null || enhancedMeshZone.isZoneValide(t));
    }

    public MeshOptionsConsDes setMaxTriangles(int max) {
        return setPrecision(new ConsDesTriangleCountPrecision(max));
    }    @Override
    public Triangle selectMeshTriangle(List<Triangle> t, int iteration) {
        if (isMeshAllowed(t, iteration)) {
            if (precision.isMeshAllowed(t, iteration)) {
                return GeomUtils.biggest(t);//TODO buggest??
            } else {
                return enhancedMeshZone != null ? enhancedMeshZone.firstTriangleInZoneValide(t) : null;
            }
        } else {
            return null;
        }
    }

    public MeshOptionsConsDes setPrecision(ConsDesPrecision pr) {
        precision = pr;
        return this;
    }






}
