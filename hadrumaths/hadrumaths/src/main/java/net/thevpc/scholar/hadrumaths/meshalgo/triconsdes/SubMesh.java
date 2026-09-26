package net.thevpc.scholar.hadrumaths.meshalgo.triconsdes;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.io.Serializable;
import java.util.Objects;

public class SubMesh implements Serializable {
    private static final long serialVersionUID = 1L;
    private HGeometry geometry;
    private double maxEdgeLength;
    private double maxArea;
    private int maxCount;

    public SubMesh() {
    }

    public SubMesh(HGeometry geometry, double maxEdgeLength) {
        this.geometry = geometry;
        this.maxEdgeLength = maxEdgeLength;
    }

    public SubMesh(HGeometry geometry, double maxEdgeLength, double maxArea) {
        this.geometry = geometry;
        this.maxEdgeLength = maxEdgeLength;
        this.maxArea = maxArea;
    }

    public HGeometry getGeometry() {
        return geometry;
    }

    public SubMesh setGeometry(HGeometry geometry) {
        this.geometry = geometry;
        return this;
    }

    public double getMaxEdgeLength() {
        return maxEdgeLength;
    }

    public SubMesh setMaxEdgeLength(double maxEdgeLength) {
        this.maxEdgeLength = maxEdgeLength;
        return this;
    }

    public double getMaxArea() {
        return maxArea;
    }

    public SubMesh setMaxArea(double maxArea) {
        this.maxArea = maxArea;
        return this;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public SubMesh setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public NElement toElement() {
        NObjectElementBuilder b = NElement.ofObjectBuilder("SubMesh");
        if (geometry != null) {
            b.add("geometry", NElementHelper.elem(geometry));
        }
        if (maxEdgeLength > 0) {
            b.add("maxEdgeLength", NElementHelper.elem(maxEdgeLength));
        }
        if (maxArea > 0) {
            b.add("maxArea", NElementHelper.elem(maxArea));
        }
        if (maxCount > 0) {
            b.add("maxCount", NElementHelper.elem(maxCount));
        }
        return b.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubMesh subMesh = (SubMesh) o;
        return Double.compare(subMesh.maxEdgeLength, maxEdgeLength) == 0 &&
                Double.compare(subMesh.maxArea, maxArea) == 0 &&
                maxCount == subMesh.maxCount &&
                Objects.equals(geometry, subMesh.geometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geometry, maxEdgeLength, maxArea, maxCount);
    }

    @Override
    public String toString() {
        return toElement().toString();
    }
}
