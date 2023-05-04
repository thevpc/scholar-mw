package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.util.PlotNormalizer;

public interface PlotComponentContext {
    PlotNormalizer getNormalizer();

    Integer getPreferredWidth();

    Integer getPreferredHeight();

    LibraryPlotType getPlotType();

    PlotModelProvider getModelProvider();
}
