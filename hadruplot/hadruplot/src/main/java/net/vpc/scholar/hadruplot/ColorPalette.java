package net.vpc.scholar.hadruplot;

import java.awt.*;
import java.io.Serializable;

public abstract class ColorPalette implements Cloneable, Serializable {
    private int size;
    private boolean reverse;
    private String name;

    protected ColorPalette(String name, boolean reverse) {
        this.name = name;
        this.reverse = reverse;
        this.size = -1;
    }

    public String getName() {
        return name + (reverse ? "(reversed)" : "");
    }

    protected ColorPalette(String name, int size) {
        this.name = name;
        this.size = size;
        this.reverse = false;
    }

    protected ColorPalette(String name, int size, boolean reverse) {
        this.name = name;
        this.size = size;
        this.reverse = reverse;
    }


    public Color getColor(float ratio){
        ratio = getRatioTransform(ratio);
        return getColorImpl(ratio);
    }

    protected abstract Color getColorImpl(float ratio);

    public int getSize() {
        return size;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    protected float getRatioTransform(float ratio) {
        if(ratio<0){
            ratio=0;
        }
        if(ratio>1){
            ratio=1;
        }
        if (size > 0) {
            float step = 1f / (size - 1);
            ratio = Math.round(ratio / step) * step;
        }
        if (reverse) {
            ratio = 1 - ratio;
        }
        return ratio;
    }

    public ColorPalette derivePaletteSize(int newSize) {
        if (size == newSize) {
            return this;
        }
        ColorPalette p = clone();
        p.setSize(newSize);
        return p;
    }

    protected void setSize(int size) {
        this.size = size;
    }

    public ColorPalette derivePaletteReverse(boolean newReverse) {
        if (newReverse == reverse) {
            return this;
        }
        ColorPalette p = clone();
        p.reverse = newReverse;
        return p;
    }

    protected ColorPalette clone() {
        try {
            return (ColorPalette) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
