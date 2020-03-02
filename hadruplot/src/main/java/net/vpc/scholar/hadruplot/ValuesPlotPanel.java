/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.actions.LoadPlotAction;

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
public class ValuesPlotPanel extends BasePlotComponent implements PlotModelProvider, PlotPanel {

    private JFrame frame;
    private ValuesPlotModel model;
    private PlotComponentPanel mainComponent;
    private PropertyChangeListener plotListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
//            if ("plotType".equals(evt.getPropertyName())) {
            try {
                setup();
            }catch (Exception ex){
                JOptionPane.showMessageDialog(frame,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
            firePlotPropertyEvent(PlotPropertyListener.COMPONENT_DISPLAY_TYPE, evt.getOldValue(), evt.getNewValue());
//            }
        }
    };
    private File workingFolder;

    public ValuesPlotPanel(ValuesPlotModel model, PlotWindowManager windowManager) {
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
            ValuesPlotModel oldmodel = this.model;
            Map<String, Object> oldPops = null;
            if (oldmodel != null) {
                oldmodel.removePropertyChangeListener(plotListener);
                oldPops = new HashMap<>(this.model.getProperties());
                if (model.getConverter() == null) {
                    model.setConverter(oldmodel.getConverter());
                }
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
        LibraryPlotType type = this.model.getPlotType();
        double[][] z = getZ();
        PlotComponentPanel oldComponent = mainComponent;
        if (type==null || type.getType() == PlotType.AUTO || type.getType() == PlotType.ALL || type.getType() == null) {
            if (model.getZ() == null) {
                type = new LibraryPlotType(PlotType.CURVE,null);
            } else {
                type = new LibraryPlotType(PlotType.HEATMAP,null);
            }
        }
        this.mainComponent = PlotBackendLibraries.createPlotComponentPanel(
                new DefaultPlotComponentContext(type, this)
        );
        JPopupMenu popupMenu = mainComponent.getPopupMenu();
//        if (popupMenu != null) {
//            Plot.buildJPopupMenu(popupMenu, this);
//        }
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

    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame();
            frame.setTitle(getModel().getPreferredTitle());
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
        String baseTitle = super.getPlotTitle();
        if (baseTitle == null) {
            return this.model.getTitle();
        }
        return baseTitle;
    }

}
