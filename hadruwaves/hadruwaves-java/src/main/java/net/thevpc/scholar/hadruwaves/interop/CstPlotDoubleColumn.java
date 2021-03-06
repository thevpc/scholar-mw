package net.thevpc.scholar.hadruwaves.interop;

import net.thevpc.scholar.hadrumaths.ArrayDoubleVector;
import net.thevpc.scholar.hadrumaths.DoubleVector;

public class CstPlotDoubleColumn {
    private String title;
    private double[] values;

    public CstPlotDoubleColumn(String title, double[] values) {
        this.title = title;
        this.values = values;
    }

    public String getTitle() {
        return title;
    }

    public int length() {
        return values.length;
    }
    
    public double get(int i) {
        return values[i];
    }

    public double[] getValues() {
        return values;
    }
    public DoubleVector getDoubleList() {
        return ArrayDoubleVector.column(values);
    }
}
