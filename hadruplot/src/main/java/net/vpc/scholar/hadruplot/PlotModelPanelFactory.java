package net.vpc.scholar.hadruplot;

public interface PlotModelPanelFactory {
    boolean acceptModel(PlotModel model);
    PlotPanel createPanel(PlotModel model, PlotWindowManager windowManager);
}
