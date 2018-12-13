package net.vpc.scholar.hadruwaves.interop;

import net.vpc.scholar.hadrumaths.DoubleArrayList;
import net.vpc.scholar.hadrumaths.DoubleList;

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
    public DoubleList getDoubleList() {
        return DoubleArrayList.column(values);
    }
}
