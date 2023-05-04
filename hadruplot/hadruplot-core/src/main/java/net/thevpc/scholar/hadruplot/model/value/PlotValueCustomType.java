package net.thevpc.scholar.hadruplot.model.value;

import net.thevpc.scholar.hadruplot.model.value.AbstractPlotValueType;

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
