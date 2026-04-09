package net.thevpc.scholar.hadrumaths.meshalgo.tri;

import net.thevpc.scholar.hadrumaths.geom.HTriangle;

import java.util.function.Predicate;

public class MeshRefinement {

    public int maxIterations = Integer.MAX_VALUE;
    public int maxTriangles = Integer.MAX_VALUE;
    public double maxWidth = Double.MAX_VALUE;   // max of xwidth/ywidth of bounding box
    public double maxSurface = Double.MAX_VALUE; // max triangle area
    public Predicate<HTriangle> predicate; // max triangle area

    public MeshRefinement setPredicate(Predicate<HTriangle> predicate) {
        this.predicate = predicate;
        return this;
    }

    public MeshRefinement maxTriangles(int v) { this.maxTriangles = v; return this; }
    public MeshRefinement maxWidth(double v)   { this.maxWidth = v;     return this; }
    public MeshRefinement maxSurface(double v) { this.maxSurface = v;   return this; }
    public MeshRefinement maxIterations(int v) { this.maxIterations = v;   return this; }
}

