package net.vpc.scholar.hadrumaths.plot;

import java.awt.Color;
import java.util.Arrays;

public class JColorArrayPalette extends JColorPalette{
    private static final long serialVersionUID = -1010101010101001047L;
    private Color[] model;


    public JColorArrayPalette(Color[] model) {
        this(model,model.length);
    }
    public JColorArrayPalette(Color[] model,int size) {
        super(size);
        this.model=model;
    }

    public Color getColorAt(int index) {
        return model[index];
    }

    public Color[] getArray() {
        return model;
    }

    public Color getColor(float ratio){
        ratio=getRatioTransform(ratio);
        int s=getSize();
        int x=(int) (ratio*s);
        return getColorAt(x<0?0:(x>=s)?(s-1):x);
    }

    public Color[] getModel() {
        return Arrays.copyOf(model,getSize());
    }
}
