package net.vpc.scholar.hadrumaths;

import java.awt.*;

class ModelSeriesItem {
    private int index;
    private String title;
    private boolean visible;
    private int nodeType;
    private int lineType;
    private double xmultiplier;
    private double ymultiplier;
    private Color color;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public int getLineType() {
        return lineType;
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
    }

    public double getXmultiplier() {
        return xmultiplier;
    }

    public void setXmultiplier(double xmultiplier) {
        this.xmultiplier = xmultiplier;
    }

    public double getYmultiplier() {
        return ymultiplier;
    }

    public void setYmultiplier(double ymultiplier) {
        this.ymultiplier = ymultiplier;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
