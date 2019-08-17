package net.vpc.scholar.hadruplot;

public abstract class PlotValueCustomType<T> extends AbstractPlotValueType {
    public PlotValueCustomType(String name) {
        super(name);
    }
    public abstract T toCustomType(Object o);

    @Override
    public Object getValue(Object o) {
        return toCustomType(o);
    }
}
