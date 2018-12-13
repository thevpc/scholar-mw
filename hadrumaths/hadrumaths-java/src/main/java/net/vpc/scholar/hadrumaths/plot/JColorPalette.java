package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Maths;

import java.awt.*;
import java.io.Serializable;

public abstract class JColorPalette implements Cloneable, Serializable {
    private int size;
    private boolean reverse;
    private String name;

    protected JColorPalette(String name, boolean reverse) {
        this.name = name;
        this.reverse = reverse;
        this.size = -1;
    }

    public String getName() {
        return name + (reverse ? "(reversed)" : "");
    }

    protected JColorPalette(String name, int size) {
        this.name = name;
        this.size = size;
        this.reverse = false;
    }

    protected JColorPalette(String name, int size, boolean reverse) {
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
            ratio = Maths.round(ratio / step) * step;
        }
        if (reverse) {
            ratio = 1 - ratio;
        }
        return ratio;
    }

    public JColorPalette derivePaletteSize(int newSize) {
        if (size == newSize) {
            return this;
        }
        JColorPalette p = clone();
        p.setSize(newSize);
        return p;
    }

    protected void setSize(int size) {
        this.size = size;
    }

    public JColorPalette derivePaletteReverse(boolean newReverse) {
        if (newReverse == reverse) {
            return this;
        }
        JColorPalette p = clone();
        p.reverse = newReverse;
        return p;
    }

    protected JColorPalette clone() {
        try {
            return (JColorPalette) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
