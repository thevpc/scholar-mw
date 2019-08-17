package net.vpc.scholar.hadruplot;

public interface PlotValueFactory {
    boolean isColumn(Object obj);

    PlotValue createPlotValue(Object object, PlotBuilder builder);
}
