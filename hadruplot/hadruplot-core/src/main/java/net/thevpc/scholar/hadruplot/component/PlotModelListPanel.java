/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.component;

import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.scholar.hadruplot.model.PlotModelList;
import net.thevpc.common.swing.tab.JDraggableTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import net.thevpc.scholar.hadruplot.BasePlotComponent;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.PlotPanel;
import net.thevpc.scholar.hadruplot.PlotPropertyListener;
import net.thevpc.scholar.hadruplot.PlotWindowManager;

/**
 * @author vpc
 */
public class PlotModelListPanel extends BasePlotComponent implements PlotModelProvider, PlotPanel {
    private PlotModelList model;
    private JComponent mainComponent;
    private PropertyChangeListener plotListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
//            if ("plotType".equals(evt.getPropertyName())) {
            setup();
            firePlotPropertyEvent(PlotPropertyListener.COMPONENT_DISPLAY_TYPE, evt.getOldValue(), evt.getNewValue());
//            }
        }
    };

    public PlotModelListPanel(PlotModelList model, PlotWindowManager windowManager) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(400, 400));
        if (model != null) {
            setModel(model);
        }
        setPlotWindowManager(windowManager);
    }

    @Override
    public boolean accept(PlotModel model) {
        return model instanceof ValuesPlotModel;
    }

    @Override
    public void setModel(PlotModel model) {
        if (model == null) {
            throw new NullPointerException();
        }

        if (!(model instanceof PlotModelList)) {
            PlotModelList r = new PlotModelList(model.getTitle());
            r.add(model);
            model = r;
        }
        if (this.model != model) {
            PlotModelList oldmodel = this.model;
            Map<String, Object> oldPops = null;
            if (oldmodel != null) {
//                oldmodel.removePropertyChangeListener(plotListener);
//                oldPops = new HashMap<>(this.model.getProperties());
//                model.setZDoubleFunction(oldmodel.getZDoubleFunction());
            }
            this.model = (PlotModelList) model;
//            this.model.setProperties(oldPops);
//            this.model.addPropertyChangeListener(plotListener);
            setup();
        }
    }
    //    public ValuesPlotPanel(PlotWindowManager windowManager) {
//        this(null, null,null,windowManager);
//    }

    public PlotModelList getModel() {
        return model;
    }

    public Component getComponent() {
        return this;
    }


    private void setup() {
        JComponent oldComponent = this.mainComponent;
        this.mainComponent = createPanel();
        JPopupMenu popupMenu = mainComponent.getComponentPopupMenu();
        if (popupMenu != null) {
//            getModel().addPropertyChangeListener(plotListener);
            Plot.buildJPopupMenu(popupMenu, this);
        }
        if (mainComponent != oldComponent) {
            if (oldComponent != null) {
                remove(oldComponent);
            }
            add(mainComponent);
        }
        invalidate();
        revalidate();
        repaint();
        //        Container parent = getParent();
//        if (parent != null) {
//            parent.invalidate();
//            parent.repaint();
//        }
    }

    private JComponent createPanel() {
        if (model.size() <= 8) {
            return createGrid();
        }
        return createTabbedPane();
    }

    private JComponent createTabbedPane() {
        JDraggableTabbedPane panel = new JDraggableTabbedPane();
        for (PlotModel plotModel : model) {
            panel.addTab(plotModel.getPreferredTitle(), Plot.create(plotModel, getPlotWindowManager()).toComponent());
        }
        return panel;
    }

    private JComponent createGrid() {
        int size = model.size();
        if (size <= 1) {
            for (PlotModel plotModel : model) {
                return Plot.create(plotModel, getPlotWindowManager()).toComponent();
            }
        }
        if (size <= 2) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            for (PlotModel plotModel : model) {
                panel.add(Plot.create(plotModel, getPlotWindowManager()).toComponent());
            }
            return panel;
        }
        if (size <= 8) {
            JPanel panel = new JPanel(new GridLayout(0, 2));
            for (PlotModel plotModel : model) {
                panel.add(Plot.create(plotModel, getPlotWindowManager()).toComponent());
            }
            return panel;
        }
        JPanel panel = new JPanel(new GridLayout(0, 4));
        for (PlotModel plotModel : model) {
            panel.add(Plot.create(plotModel, getPlotWindowManager()).toComponent());
        }
        return panel;
    }

    public String getPlotTitle() {
        String baseTitle = super.getPlotTitle();
        if (baseTitle == null) {
            return getModel().getTitle();
        }
        return baseTitle;
    }

}
