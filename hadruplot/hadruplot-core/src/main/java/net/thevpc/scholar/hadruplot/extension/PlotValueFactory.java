package net.thevpc.scholar.hadruplot.extension;

import net.thevpc.scholar.hadruplot.PlotBuilder;
import net.thevpc.scholar.hadruplot.PlotValueAndPriority;

public interface PlotValueFactory {
    boolean isColumn(Object obj);

    PlotValueAndPriority createPlotValue(Object object, PlotBuilder builder);
}
