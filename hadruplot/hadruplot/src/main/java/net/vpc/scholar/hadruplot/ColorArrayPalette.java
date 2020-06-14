package net.vpc.scholar.hadruplot;

import java.awt.*;
import java.util.Arrays;

public class ColorArrayPalette extends ColorPalette {
    private static final long serialVersionUID = 1L;
    private Color[] model;


    public ColorArrayPalette(String name, Color[] model) {
        this(name,model, model.length);
    }

    public ColorArrayPalette(String name, Color[] model, int size) {
        super(name,size);
        this.model = model;
    }

    public Color getColorAt(int index) {
        return model[index];
    }

    public Color[] getArray() {
        return model;
    }

    public Color getColorImpl(float ratio) {
        int s = getSize();
        int x = (int) (ratio * s);
        return getColorAt(x < 0 ? 0 : (x >= s) ? (s - 1) : x);
    }

    public Color[] getModel() {
        return Arrays.copyOf(model, getSize());
    }
}
