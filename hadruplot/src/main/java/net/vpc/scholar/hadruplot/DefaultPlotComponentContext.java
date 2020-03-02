package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.backends.simple.heatmap.PlotNormalizer;

public class DefaultPlotComponentContext implements PlotComponentContext{
    private PlotModelProvider modelProvider;
    private LibraryPlotType plotType;
    private PlotBackendLibraryFilter filter;
    private PlotNormalizer normalizer;
    private Integer preferredWidth;
    private Integer preferredHeight;

    public DefaultPlotComponentContext(LibraryPlotType plotType, PlotModel model) {
        this(plotType,new SimplePlotModelProvider(model,null));
    }

    public DefaultPlotComponentContext(LibraryPlotType plotType, PlotModelProvider modelProvider) {
        this.plotType =plotType;
        this.modelProvider = modelProvider;
    }

    @Override
    public Integer getPreferredWidth() {
        return preferredWidth;
    }

    public DefaultPlotComponentContext setPreferredWidth(Integer preferredWidth) {
        this.preferredWidth = preferredWidth;
        return this;
    }

    @Override
    public Integer getPreferredHeight() {
        return preferredHeight;
    }

    public DefaultPlotComponentContext setPreferredHeight(Integer preferredHeight) {
        this.preferredHeight = preferredHeight;
        return this;
    }

    @Override
    public PlotNormalizer getNormalizer() {
        return normalizer;
    }

    public DefaultPlotComponentContext setNormalizer(PlotNormalizer normalizer) {
        this.normalizer = normalizer;
        return this;
    }

    public LibraryPlotType getPlotType() {
        return plotType;
    }

    @Override
    public PlotModelProvider getModelProvider() {
        return modelProvider;
    }

}
