package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.backends.simple.heatmap.PlotNormalizer;

public interface PlotComponentContext {
    PlotNormalizer getNormalizer();

    Integer getPreferredWidth();

    Integer getPreferredHeight();

    PlotType getPlotType();

    PlotModelProvider getModelProvider();
}
