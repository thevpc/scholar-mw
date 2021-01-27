package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.scholar.hadrumaths.plot.component.AxisVectorPlotPanel;
import net.thevpc.scholar.hadrumaths.plot.component.ExpressionsPlotPanel;
import net.thevpc.scholar.hadrumaths.plot.model.AxisVectorPlotModel;
import net.thevpc.scholar.hadrumaths.plot.model.ExpressionsPlotModel;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.scholar.hadruplot.extension.PlotModelPanelFactory;
import net.thevpc.scholar.hadruplot.PlotPanel;
import net.thevpc.scholar.hadruplot.PlotWindowManager;

public class ExpressionsPlotModelPanelFactory implements PlotModelPanelFactory {
    @Override
    public boolean acceptModel(PlotModel model) {
        if(model instanceof ExpressionsPlotModel){
            return true;
        }
        if(model instanceof AxisVectorPlotModel){
            return true;
        }
        return false;
    }

    @Override
    public PlotPanel createPanel(PlotModel model, PlotWindowManager windowManager) {
        if (model instanceof ExpressionsPlotModel) {
            return new ExpressionsPlotPanel((ExpressionsPlotModel) model, windowManager);
        }
        if (model instanceof AxisVectorPlotModel) {
            return new AxisVectorPlotPanel((AxisVectorPlotModel) model, windowManager);
        }
        return null;
    }
}
