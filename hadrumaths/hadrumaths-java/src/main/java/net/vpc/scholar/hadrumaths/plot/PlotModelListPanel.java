/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.ExternalLibrary;
import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.plot.surface.HeatMapPlot;
import net.vpc.scholar.hadrumaths.plot.table.TablePlotComponentPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vpc
 */
public class PlotModelListPanel extends BasePlotComponent implements PlotModelProvider, PlotPanel {
    private PlotModelList model;
    private JComponent mainComponent;
    private PropertyChangeListener plotListener =  new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
//            if ("plotType".equals(evt.getPropertyName())) {
                setup();
                firePlotPropertyEvent(PlotPropertyListener.COMPONENT_DISPLAY_TYPE, evt.getOldValue(), evt.getNewValue());
//            }
        }
    };
    private PlotWindowManager windowManager;
    private String layoutConstraints = "";

    public PlotModelListPanel(PlotModelList model, PlotWindowManager windowManager) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(400, 400));
        if (model != null) {
            setModel(model);
        }
        this.windowManager = windowManager;
    }

    @Override
    public boolean accept(PlotModel model) {
        return model instanceof ValuesPlotModel;
    }

    @Override
    public void setModel(PlotModel model) {
        this.setModel((ValuesPlotModel) model);
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

    public void setModel(PlotModelList model) {
        if (this.model != model) {
            PlotModelList oldmodel=this.model;
            Map<String, Object> oldPops=null;
            if (oldmodel != null) {
//                oldmodel.removePropertyChangeListener(plotListener);
//                oldPops = new HashMap<>(this.model.getProperties());
//                model.setZDoubleFunction(oldmodel.getZDoubleFunction());
            }
            this.model = model;
//            this.model.setProperties(oldPops);
//            this.model.addPropertyChangeListener(plotListener);
            setup();
        }
    }


    private void setup() {
        JComponent oldComponent=this.mainComponent;
        this.mainComponent = createPanel();
        JPopupMenu popupMenu = mainComponent.getComponentPopupMenu();
        if(popupMenu!=null) {
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
        if(model.size()<=8){
            return createGrid();
        }
        return createTabbedPane();
    }

    private JComponent createTabbedPane() {
        JTabbedPane panel=new JTabbedPane();
        for (PlotModel plotModel : model) {
            panel.addTab(plotModel.getTitle(),Plot.create(plotModel,windowManager).toComponent());
        }
        return panel;
    }
    private JComponent createGrid() {
        int size = model.size();
        if(size<=1){
            return Plot.create(model,windowManager).toComponent();
        }
        if(size<=2){
            JPanel panel=new JPanel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
            for (PlotModel plotModel : model) {
                panel.add(Plot.create(plotModel,windowManager).toComponent());
            }
            return panel;
        }
        if(size<=8){
            JPanel panel=new JPanel(new GridLayout(0,2));
            for (PlotModel plotModel : model) {
                panel.add(Plot.create(plotModel,windowManager).toComponent());
            }
            return panel;
        }
        JPanel panel=new JPanel(new GridLayout(0,4));
        for (PlotModel plotModel : model) {
            panel.add(Plot.create(plotModel,windowManager).toComponent());
        }
        return panel;
    }

    public String getPlotTitle() {
        String baseTitle = super.getPlotTitle();
        if(baseTitle ==null){
            return getModel().getTitle();
        }
        return baseTitle;
    }

    public JComponent toComponent() {
        return this;
    }

    public void display() {
        windowManager.add(this);
    }


    @Override
    public String getLayoutConstraints() {
        return layoutConstraints;
    }

    public void setLayoutConstraints(String layoutConstraints) {
        this.layoutConstraints = layoutConstraints;
    }
}