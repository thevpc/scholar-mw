package net.thevpc.scholar.hadruplot.model.value;

import net.thevpc.scholar.hadruplot.model.PlotModel;

public class PlotValueModelType extends AbstractPlotValueType {
    public static final PlotValueType INSTANCE = new PlotValueModelType();
    public PlotValueModelType() {
        super("PlotModel");
    }

    public PlotModel toPlotModel(Object o) {
        return (PlotModel) o;
    }

    @Override
    public Object getValue(Object o) {
        return toPlotModel(o);
    }
}
