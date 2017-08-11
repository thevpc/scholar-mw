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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vpc
 */
public class ValuesPlotPanel extends BasePlotComponent implements PlotModelProvider, PlotPanel {

    private JFrame frame;
    private ValuesPlotModel model;
    private PlotComponentPanel mainComponent;
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
    private File workingFolder;
    private String layoutConstraints = "";

    public ValuesPlotPanel(ValuesPlotModel model, PlotWindowManager windowManager) {
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

    public ValuesPlotModel getModel() {
        return model;
    }

    public Component getComponent() {
        return this;
    }

    public void setModel(ValuesPlotModel model) {
        if (this.model != model) {
            ValuesPlotModel oldmodel=this.model;
            Map<String, Object> oldPops=null;
            if (oldmodel != null) {
                oldmodel.removePropertyChangeListener(plotListener);
                oldPops = new HashMap<>(this.model.getProperties());
                model.setZDoubleFunction(oldmodel.getZDoubleFunction());
            }
            this.model = model;
            this.model.setProperties(oldPops);
            this.model.addPropertyChangeListener(plotListener);
            setup();
        }
    }

    private double[][] getZ() {
        return model.getZd();
    }

    private void setup() {
        PlotType type = this.model.getPlotType();
        double[][] z = getZ();
        PlotComponentPanel oldComponent = mainComponent;
        if (type == PlotType.AUTO) {
            if (model.getZ() == null) {
                type = PlotType.CURVE;
            } else {
                type = PlotType.HEATMAP;
            }
        }
        switch (type) {
            case CURVE: {
                this.mainComponent = createCourbePanel();
                break;
            }
            case POLAR: {
                this.mainComponent = createPolarPanel();
                break;
            }
            case MESH: {
                this.mainComponent = createSurfaceMeshPanel();
                break;
            }
            case HEATMAP: {
                this.mainComponent = createSurfacePlainPanel();
                break;
            }
            case MATRIX: {
                this.mainComponent = createSurfaceMatrixPanel();
                break;
            }
            case TABLE: {
                this.mainComponent = createTablePanel();
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported type " + type);
            }
        }
        JPopupMenu popupMenu = mainComponent.getPopupMenu();
        if(popupMenu!=null) {
//            getModel().addPropertyChangeListener(plotListener);
            Plot.buildJPopupMenu(popupMenu, this);
        }
        if (mainComponent != oldComponent) {
            if (oldComponent != null) {
                remove(oldComponent.toComponent());
            }
            add(mainComponent.toComponent());
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

    public PlotComponentPanel createSurfacePlainPanel() {
//                heatMapPlot.rotateLeft();
        HeatMapPlot heatMapPlot = new HeatMapPlot(this);
//        heatMapPlot.flipVertically();
        return heatMapPlot;
    }

    public PlotComponentPanel createSurfaceMatrixPanel() {
        return new HeatMapPlot(this);
    }

    public PlotComponentPanel createTablePanel() {
        return new TablePlotComponentPanel(this);
    }

    public PlotComponentPanel createSurfaceMeshPanel() {
        return ChartFactory.createMesh(model, null);
    }

    public PlotComponentPanel createCourbePanel() {
        if (model.getPreferredLibraries().contains(ExternalLibrary.CHARTS_JFREECHART)) {
            return new PlotCanvasCurveJFreeChart(this);
        } else {
            return new PlotCanvasCurveSimple(this);
        }
    }

    public PlotComponentPanel createPolarPanel() {
        if (model.getPreferredLibraries().contains(ExternalLibrary.CHARTS_JFREECHART)) {
            return new PlotCanvasPolarJFreeChart(this);
        } else {
            return createCourbePanel();
        }
    }

    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame();
            frame.setTitle(getModel().getTitle());
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(this);
            frame.pack();
        }
        return frame;
    }

    public JFrame showFrame() {
        JFrame f = getFrame();
        f.setVisible(true);
        return f;
    }

    public File getWorkingFolder() {
        return workingFolder;
    }

    public void setWorkingFolder(File workingFolder) {
        this.workingFolder = workingFolder;
    }

    public static void showOpenFileDialog() {
        LoadPlotAction a = new LoadPlotAction();
        a.actionPerformed(null);
    }

    public PlotComponentPanel getMainComponent() {
        return mainComponent;
    }

    public String getPlotTitle() {
        return getModel().getTitle();
    }

    public JComponent toComponent() {
        return (JComponent) this;
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
