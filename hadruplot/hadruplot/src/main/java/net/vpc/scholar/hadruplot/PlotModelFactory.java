package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.model.PlotModel;

public interface PlotModelFactory {
    PlotModel createModel(PlotValue data, PlotBuilder builder);
}
