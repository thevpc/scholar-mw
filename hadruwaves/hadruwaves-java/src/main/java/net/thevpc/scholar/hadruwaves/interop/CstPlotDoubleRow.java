package net.thevpc.scholar.hadruwaves.interop;

import net.thevpc.scholar.hadrumaths.ArrayDoubleVector;
import net.thevpc.scholar.hadrumaths.DoubleVector;

import java.util.HashMap;
import java.util.Map;

public class CstPlotDoubleRow {
    private String[] names;
    private double[] values;
    private Map<String, Double> valuesMap;

    public CstPlotDoubleRow(String[] names, double[] values) {
        this.names = names;
        this.values = values;
        this.valuesMap = new HashMap<>(this.names.length);
        for (int i = 0; i < names.length; i++) {
            this.valuesMap.put(names[i], values[i]);
        }
    }

    public double get(int i) {
        return values[i];
    }

    public double get(String name) {
        return valuesMap.get(name);
    }

    public double[] getValues() {
        return values;
    }

    public DoubleVector getDoubleList() {
        return ArrayDoubleVector.column(values);
    }
}
