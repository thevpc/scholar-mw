package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadruplot.PlotModel;
import net.vpc.scholar.hadruplot.PlotModelPanelFactory;
import net.vpc.scholar.hadruplot.PlotPanel;
import net.vpc.scholar.hadruplot.PlotWindowManager;

public class ExpressionsPlotModelPanelFactory implements PlotModelPanelFactory {
    @Override
    public boolean acceptModel(PlotModel model) {
        return model instanceof ExpressionsPlotModel;
    }

    @Override
    public PlotPanel createPanel(PlotModel model, PlotWindowManager windowManager) {
        if (model instanceof ExpressionsPlotModel) {
            return new ExpressionsPlotPanel((ExpressionsPlotModel) model, windowManager);
        }
        return null;
    }
}
