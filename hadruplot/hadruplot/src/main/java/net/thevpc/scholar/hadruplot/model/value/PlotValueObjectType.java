package net.thevpc.scholar.hadruplot.model.value;

public class PlotValueObjectType extends AbstractPlotValueType {
    public static final PlotValueType INSTANCE=new PlotValueObjectType();
    public PlotValueObjectType() {
        super("object");
    }

    @Override
    public Object getValue(Object o) {
        return o;
    }
}
