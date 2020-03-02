package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.geom.DefaultGeometryList;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by vpc on 2/23/17.
 */
public class PolygonListMeshInfo {

    Domain from;
    java.util.List<MeshZone> meshZones;
    GeometryList polygons;
    MeshAlgo meshalgo;
    GpPattern pattern;

    public PolygonListMeshInfo(GeometryList polygons, Domain globalDomain, MeshAlgo meshalgo, GpPattern pattern) {
        this.from = polygons.getBounds() == null ? globalDomain : polygons.getBounds();
        this.pattern = pattern;
        this.polygons = new DefaultGeometryList(polygons);
        this.meshalgo = meshalgo;
        rebuild();
    }

    public void rebuild(){
        java.util.List<MeshZone> zones = new ArrayList<MeshZone>();
        for (Geometry polygon : this.polygons) {
            zones.addAll(meshalgo.meshPolygon(polygon));
        }
        zones = pattern.transform(zones, from);
        this.meshZones = zones;
    }

    public Domain to(Rectangle currBounds) {
        Domain to = null;
        int precision = 100;
        if (currBounds.width < currBounds.height) {
            precision = currBounds.width - 3;
        } else {
            precision = currBounds.height - 3;
        }
        if (this.from.xwidth() <= this.from.ywidth()) {
            to = Domain.ofBounds(0, precision * this.from.xwidth() / this.from.ywidth(), 0, precision);
        } else {
            to = Domain.ofBounds(0, precision, 0, precision * this.from.ywidth() / this.from.xwidth());
        }
        return to;
    }
}
