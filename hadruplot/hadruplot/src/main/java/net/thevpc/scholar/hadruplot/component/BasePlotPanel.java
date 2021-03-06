package net.thevpc.scholar.hadruplot.component;

import net.thevpc.scholar.hadruplot.model.PlotModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.thevpc.scholar.hadruplot.BasePlotComponent;
import net.thevpc.scholar.hadruplot.PlotPanel;

public abstract class BasePlotPanel extends BasePlotComponent implements PlotPanel {
    private PlotModel plotModel;
    PropertyChangeListener modelChanged = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            modelChanged(evt);
        }
    };

    public BasePlotPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public BasePlotPanel(LayoutManager layout) {
        super(layout);
    }

    public BasePlotPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public BasePlotPanel() {
    }

    @Override
    public PlotModel getModel() {
        return plotModel;
    }

    @Override
    public void setModel(PlotModel model) {
        PlotModel old = this.plotModel;
        if (old != null) {
            old.removePropertyChangeListener(modelChanged);
        }
        this.plotModel = model;
        modelChanged();
        if (this.plotModel != null) {
            this.plotModel.addPropertyChangeListener(modelChanged);
        }
    }

    @Override
    public String getPlotTitle() {
        String s = super.getPlotTitle();
        if(s!=null){
            return s;
        }
        if(plotModel!=null){
            return plotModel.getTitle();
        }
        return null;
    }
    

    protected void modelChanged() {

    }

    @Override
    public boolean accept(PlotModel model) {
        return true;
    }

    protected void modelChanged(PropertyChangeEvent evt) {

    }
}
