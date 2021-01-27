package net.thevpc.scholar.hadruplot;

import java.awt.*;

public class ModelSeriesItem {
    private int index;
    private String title;
    private boolean visible;
    private Integer nodeType;
    private Integer lineType;
    private Number xmultiplier;
    private Number ymultiplier;
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

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getLineType() {
        return lineType;
    }

    public void setLineType(Integer lineType) {
        this.lineType = lineType;
    }

    public Number getXmultiplier() {
        return xmultiplier;
    }

    public void setXmultiplier(Number xmultiplier) {
        this.xmultiplier = xmultiplier;
    }

    public Number getYmultiplier() {
        return ymultiplier;
    }

    public void setYmultiplier(Number ymultiplier) {
        this.ymultiplier = ymultiplier;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
