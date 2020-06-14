package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.model.value.PlotValueType;

public interface PlotValue {

    PlotValueType getType();

    Object getValue();

    String get(String prop);
}
