package net.vpc.scholar.hadruplot.model.value;

public interface PlotValueType {
    String getName();

    Object getValue(Object o);

    Object toValue(Object value, Class cls);
}
