package net.vpc.scholar.hadruplot;

public class PlotHyperCubePlotModelPanelFactory implements PlotModelPanelFactory {
    @Override
    public boolean acceptModel(PlotModel model) {
        return model instanceof PlotHyperCubePlotModel;
    }

    @Override
    public PlotPanel createPanel(PlotModel model, PlotWindowManager windowManager) {
        if (model instanceof PlotHyperCubePlotModel) {
            return new PlotHyperCubePlotPanel((PlotHyperCubePlotModel) model, windowManager);
        }
        return null;
    }
}
