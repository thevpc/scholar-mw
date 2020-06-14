package net.vpc.scholar.hadruplot.model.value;

import java.awt.Component;

public class PlotValueComponentType extends AbstractPlotValueType {
    public static final PlotValueType INSTANCE = new PlotValueComponentType();
    public PlotValueComponentType() {
        super("component");
    }

    public Component toComponent(Object o) {
        return (Component) o;
    }

    @Override
    public Object getValue(Object o) {
        return toComponent(o);
    }
}
