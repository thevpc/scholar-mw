package net.thevpc.scholar.hadruplot;

public interface PlotLibrary {
    String getName();
    int getSupportLevel(PlotType type);
    PlotComponentPanel createPlotComponentPanel(PlotComponentContext context);
}
