package net.vpc.scholar.hadruplot.extension;

import net.vpc.scholar.hadruplot.PlotBuilder;
import net.vpc.scholar.hadruplot.PlotValueAndPriority;

public interface PlotValueFactory {
    boolean isColumn(Object obj);

    PlotValueAndPriority createPlotValue(Object object, PlotBuilder builder);
}
