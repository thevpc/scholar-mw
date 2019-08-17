package net.vpc.scholar.hadruplot;

import javax.swing.*;

public class PlotModelPopupFactoryContext {
    private PlotModel model;
    private PlotModelProvider modelProvider;
    private JMenu functionsMenu = new JMenu("Function");
    private JMenu viewMenu = null;
    private JMenu libraries = null;
    private JMenuItem extProperties;

    public PlotModelPopupFactoryContext(PlotModel model, PlotModelProvider modelProvider, JMenu functionsMenu, JMenu viewMenu, JMenu libraries,JMenuItem extProperties) {
        this.model = model;
        this.modelProvider = modelProvider;
        this.functionsMenu = functionsMenu;
        this.viewMenu = viewMenu;
        this.libraries = libraries;
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

    public JMenu getLibrariesMenu() {
        return libraries;
    }

    public JMenuItem getExtProperties() {
        return extProperties;
    }
}
