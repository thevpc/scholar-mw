package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.extension.PlotModelProvider;
import net.vpc.scholar.hadruplot.libraries.simple.heatmap.PlotNormalizer;

public interface PlotComponentContext {
    PlotNormalizer getNormalizer();

    Integer getPreferredWidth();

    Integer getPreferredHeight();

    LibraryPlotType getPlotType();

    PlotModelProvider getModelProvider();
}
