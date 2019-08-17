package net.vpc.scholar.hadruplot;

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
