package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.model.PlotModel;

public interface PlotModelFactory {
    PlotModel createModel(PlotValue data, PlotBuilder builder);
}
