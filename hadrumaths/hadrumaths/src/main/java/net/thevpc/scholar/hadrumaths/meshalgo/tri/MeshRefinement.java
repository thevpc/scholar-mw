package net.thevpc.scholar.hadrumaths.meshalgo.tri;

import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.SubMesh;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MeshRefinement {

    public int maxIterations = Integer.MAX_VALUE;
    public int maxTriangles = Integer.MAX_VALUE;
    public double maxWidth = Double.MAX_VALUE;   // max edge length
    public double maxSurface = Double.MAX_VALUE; // max triangle area
    public boolean adaptive;
    public List<SubMesh> subMeshes = new ArrayList<>();
    public Predicate<HTriangle> predicate;

    public MeshRefinement setPredicate(Predicate<HTriangle> predicate) {
        this.predicate = predicate;
        return this;
    }

    public MeshRefinement maxTriangles(int v) { this.maxTriangles = v; return this; }
    public MeshRefinement maxWidth(double v)   { this.maxWidth = v;     return this; }
    public MeshRefinement maxSurface(double v) { this.maxSurface = v;   return this; }
    public MeshRefinement maxIterations(int v) { this.maxIterations = v;   return this; }
    public MeshRefinement adaptive(boolean v)  { this.adaptive = v; return this; }
    public MeshRefinement subMeshes(List<SubMesh> v) {
        this.subMeshes = v != null ? v : new ArrayList<>();
        return this;
    }
}

