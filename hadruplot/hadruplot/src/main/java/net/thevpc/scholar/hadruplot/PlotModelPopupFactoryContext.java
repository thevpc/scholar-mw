package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import javax.swing.*;

public class PlotModelPopupFactoryContext {
    private PlotModel model;
    private PlotModelProvider modelProvider;
    private JMenu functionsMenu = new JMenu("Function");
    private JMenu viewMenu = null;
    private JMenuItem extProperties;

    public PlotModelPopupFactoryContext(PlotModel model, PlotModelProvider modelProvider, JMenu functionsMenu, JMenu viewMenu, JMenuItem extProperties) {
        this.model = model;
        this.modelProvider = modelProvider;
        this.functionsMenu = functionsMenu;
        this.viewMenu = viewMenu;
        this.extProperties = extProperties;
    }

    public PlotModel getModel() {
        return model;
    }

    public PlotModelProvider getModelProvider() {
        return modelProvider;
    }

    public JMenu getFunctionsMenu() {
        return functionsMenu;
    }

    public JMenu getViewMenu() {
        return viewMenu;
    }

    public JMenuItem getExtProperties() {
        return extProperties;
    }
}
