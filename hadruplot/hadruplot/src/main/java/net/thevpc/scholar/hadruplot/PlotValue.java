package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.model.value.PlotValueType;

public interface PlotValue {

    PlotValueType getType();

    Object getValue();

    String get(String prop);
}
