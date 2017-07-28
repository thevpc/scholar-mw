package net.vpc.scholar.hadrumaths.plot.surface;

/**
 * Created by vpc on 8/10/14.
 */
public class SurfaceCell {
    private int xIndex;
    private int yIndex;
    private Object xValue;
    private Object yValue;
    private double zValue;
    private double zPercent;

    public SurfaceCell(int xIndex, int yIndex, Object xValue, Object yValue, double zValue, double zPercent) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.xValue = xValue;
        this.yValue = yValue;
        this.zValue = zValue;
        this.zPercent = zPercent;
    }

    public int getxIndex() {
        return xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public Object getxValue() {
        return xValue;
    }

    public Object getyValue() {
        return yValue;
    }

    public double getzValue() {
        return zValue;
    }

    public double getzPercent() {
        return zPercent;
    }
}
