package net.vpc.scholar.hadruplot;

public interface PlotModelFactory {
    PlotModel createModel(PlotValue data, PlotBuilder builder);
}
