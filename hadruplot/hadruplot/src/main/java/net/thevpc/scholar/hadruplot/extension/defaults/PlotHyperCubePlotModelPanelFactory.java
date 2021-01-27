package net.thevpc.scholar.hadruplot.extension.defaults;

import net.thevpc.scholar.hadruplot.PlotPanel;
import net.thevpc.scholar.hadruplot.PlotWindowManager;
import net.thevpc.scholar.hadruplot.component.PlotHyperCubePlotPanel;
import net.thevpc.scholar.hadruplot.extension.PlotModelPanelFactory;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.scholar.hadruplot.model.PlotHyperCubePlotModel;

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
