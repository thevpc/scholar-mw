package net.vpc.scholar.hadrumaths.plot;

import java.awt.Color;
import java.io.Serializable;

public abstract class JColorPalette implements Cloneable, Serializable {
    private int size;
    private boolean reverse;

    protected JColorPalette(boolean reverse) {
        this.reverse = reverse;
        this.size=-1;
    }

    protected JColorPalette(int size) {
        this.size = size;
        this.reverse = false;
    }

    protected JColorPalette(int size, boolean reverse) {
        this.size = size;
        this.reverse = reverse;
    }

    public abstract Color getColor(float ratio);

    public int getSize(){
        return size;
    }

    public boolean isReverse() {
        return reverse;
    }

    protected float getRatioTransform(float ratio){
        if(size>0){
            float step=1f/(size-1);
            ratio=Math.round(ratio/step)*step;
        }
        if(reverse){
            ratio=1-ratio;
        }
        return ratio;
    }

    public JColorPalette derivePaletteSize(int newSize){
        if(size==newSize){
            return this;
        }
        JColorPalette p=clone();
        p.setSize(newSize);
        return p;
    }

    protected void setSize(int size) {
        this.size = size;
    }

    public JColorPalette derivePaletteReverse(boolean newReverse){
        if(newReverse==reverse){
            return this;
        }
        JColorPalette p=clone();
        p.reverse=newReverse;
        return p;
    }

    protected JColorPalette clone(){
        try {
            return (JColorPalette)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
