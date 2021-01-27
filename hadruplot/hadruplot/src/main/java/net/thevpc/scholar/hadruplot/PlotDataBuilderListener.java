package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.console.PlotData;

public interface PlotDataBuilderListener {
    void onBuild(PlotData data, PlotDataBuilder builder);
}
