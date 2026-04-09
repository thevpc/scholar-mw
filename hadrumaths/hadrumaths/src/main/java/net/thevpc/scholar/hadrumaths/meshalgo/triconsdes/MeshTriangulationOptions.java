package net.thevpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.geom.GeomUtils;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;
import net.thevpc.scholar.hadrumaths.geom.HTriangle;
import net.thevpc.scholar.hadrumaths.meshalgo.DefaultOption;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MeshTriangulationOptions extends DefaultOption {
    private static final long serialVersionUID = 1L;
    private int maxCount;
    private int maxIterations;
    private double maxArea;
    private double maxEdgeLength;
    private List<HGeometry> locals=new ArrayList<>();

    public MeshTriangulationOptions() {
        Polygon[] p = new Polygon[1];
        p[0] = new Polygon();
    }

    public List<HGeometry> getLocals() {
        return locals;
    }

    public MeshTriangulationOptions setLocals(List<HGeometry> locals) {
        this.locals = locals;
        return this;
    }

    public MeshTriangulationOptions setMaxIterations(int max) {
        this.maxIterations = max;
        return this;
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder b = super.toElement().toObject().get().builder();
        if(maxCount>0){
            b.add("maxCount", NElementHelper.elem(maxCount));
        }
        if(maxIterations>0){
            b.add("maxIterations", NElementHelper.elem(maxIterations));
        }
        if(maxArea >0){
            b.add("maxSurface", NElementHelper.elem(maxArea));
        }
        if(maxEdgeLength>0){
            b.add("maxSurface", NElementHelper.elem(maxEdgeLength));
        }
        return b
                .build();
    }

    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public boolean isMeshAllowed(List<HTriangle> t, int iteration) {
        if(maxCount>0){
            if(t.size()>=maxCount){
                return false;
            }
        }
        if(maxIterations>0){
            if(iteration>=maxIterations){
                return false;
            }
        }
        if(maxArea >0){
            if(GeomUtils.biggestArea(t).area() <= maxArea){
                return false;
            }
        }
        if(maxEdgeLength>0){
            if(GeomUtils.longestEdge(t).longestEdge() <= maxEdgeLength){
                return false;
            }
        }
        return true;
    }

    public MeshTriangulationOptions setMaxCount(int max) {
        this.maxCount = max;
        return this;
    }

    @Override
    public HTriangle selectMeshTriangle(List<HTriangle> t, int iteration) {
        if (isMeshAllowed(t, iteration)) {
            if (isMeshAllowed(t, iteration)) {
                return GeomUtils.biggestArea(t);//TODO buggest??
            } else {
                return enhancedMeshZone != null ? enhancedMeshZone.firstTriangleInZoneValid(t) : null;
            }
        } else {
            return null;
        }
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public double getMaxArea() {
        return maxArea;
    }

    public MeshTriangulationOptions setMaxArea(double maxArea) {
        this.maxArea = maxArea;
        return this;
    }

    public double getMaxEdgeLength() {
        return maxEdgeLength;
    }

    public MeshTriangulationOptions setMaxEdgeLength(double maxEdgeLength) {
        this.maxEdgeLength = maxEdgeLength;
        return this;
    }

    public boolean isBlank() {
        if(maxCount>0){
            return false;
        }
        if(maxIterations>0){
            return false;
        }
        if(maxArea >0){
            return false;
        }
        if(maxEdgeLength>0){
            return false;
        }
        return true;
    }
}
