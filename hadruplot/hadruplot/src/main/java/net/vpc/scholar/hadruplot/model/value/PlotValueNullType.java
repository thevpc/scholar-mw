package net.vpc.scholar.hadruplot.model.value;

import net.vpc.scholar.hadruplot.model.value.AbstractPlotValueType;

public class PlotValueNullType extends AbstractPlotValueType {
    public static final PlotValueType INSTANCE = new PlotValueNullType();

    public PlotValueNullType() {
        super("null");
    }

    @Override
    public Object getValue(Object o) {
        return o;
    }
}
