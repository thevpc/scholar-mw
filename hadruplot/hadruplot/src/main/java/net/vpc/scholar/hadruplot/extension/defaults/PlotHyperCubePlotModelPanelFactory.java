package net.vpc.scholar.hadruplot.extension.defaults;

import net.vpc.scholar.hadruplot.PlotPanel;
import net.vpc.scholar.hadruplot.PlotWindowManager;
import net.vpc.scholar.hadruplot.component.PlotHyperCubePlotPanel;
import net.vpc.scholar.hadruplot.extension.PlotModelPanelFactory;
import net.vpc.scholar.hadruplot.model.PlotModel;
import net.vpc.scholar.hadruplot.model.PlotHyperCubePlotModel;

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
