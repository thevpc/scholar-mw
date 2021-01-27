package net.thevpc.scholar.hadrumaths;

public enum PlaneAxis {
    XY(Axis.Z),
    YZ(Axis.X),
    XZ(Axis.Y);
    private final Axis normalAxis;

    PlaneAxis(Axis normalAxis) {
        this.normalAxis = normalAxis;
    }

    public Axis getNormalAxis() {
        return normalAxis;
    }
}
