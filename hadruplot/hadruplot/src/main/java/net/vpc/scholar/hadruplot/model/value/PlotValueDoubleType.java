package net.vpc.scholar.hadruplot.model.value;

public class PlotValueDoubleType extends AbstractPlotValueType {
    public static final PlotValueType INSTANCE=new PlotValueDoubleType();
    public PlotValueDoubleType() {
        super("number");
    }
    public double toDouble(Object value){
        return ((Number)value).doubleValue();
    }

    @Override
    public Object getValue(Object o) {
        return toDouble(o);
    }
}
