/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot;

import net.vpc.common.strings.StringUtils;
import net.vpc.common.swings.SwingUtilities3;
import net.vpc.scholar.hadruplot.containers.TabbedPlotWindowContainerFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author vpc
 */
public abstract class AbstractPlotWindowManager implements PlotWindowManager {

    private PlotWindowContainerFactory plotWindowContainerFactory = new TabbedPlotWindowContainerFactory();
    private String globalTitle = "Plot";
    private PropertyChangeListener titleChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object source = evt.getSource();
            PlotComponent cc = null;
            if (source instanceof PlotComponent) {
                cc = (PlotComponent) source;
            } else {
                cc = (PlotComponent) ((JComponent) source).getClientProperty(PlotComponent.class.getName());
            }
            if (cc != null) {
                titleChanged(cc);
            }
        }
    };

    public AbstractPlotWindowManager() {
    }

    public AbstractPlotWindowManager(String globalTitle) {
        this.globalTitle = globalTitle;
    }

    public PlotWindowContainerFactory getPlotWindowContainerFactory() {
        return plotWindowContainerFactory;
    }

    public void setPlotWindowContainerFactory(PlotWindowContainerFactory plotWindowContainerFactory) {
        this.plotWindowContainerFactory = plotWindowContainerFactory;
    }

    protected void titleChanged(PlotComponent component) {

    }

    @Override
    public final void remove(final PlotComponent component) {
        if (component == null) {
            return;
        }
        SwingUtilities3.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JComponent jComponent = component.toComponent();
                jComponent.removePropertyChangeListener("title", titleChangeListener);
                jComponent.putClientProperty(PlotWindowManager.class.getName(), null);
                removePlotComponentImpl(component);
            }
        });
    }

    @Override
    public final PlotContainer add(String name) {
        PlotContainer container = getPlotWindowContainerFactory().create();
        container.setPlotTitle(name);
        container.setPlotWindowManager(this);
        add(container);
        return container;
    }

    public final void add(final PlotComponent component) {
        add(component, "/");
    }

    public final void add(final PlotComponent component, final String path) {
        if (path == null || !path.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path " + path);
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                JComponent jComponent = component.toComponent();
                jComponent.addPropertyChangeListener("title", titleChangeListener);
                jComponent.putClientProperty(PlotWindowManager.class.getName(), this);
                jComponent.putClientProperty(PlotComponent.class.getName(), component);
                component.setPlotWindowManager(AbstractPlotWindowManager.this);
                addPlotComponentImpl(component, StringUtils.split(path, "/"));
            }
        };
        SwingUtilities3.invokeAndWait(r);

    }

    @Override
    public void display(PlotComponent plotComponent) {
        add(plotComponent);
    }

    protected String validateTitle(String s) {
        if (StringUtils.isBlank(s)) {
            s = "Figure";
        }
        return s;
    }

    public abstract void removePlotComponentImpl(PlotComponent component);

    public abstract void addPlotComponentImpl(PlotComponent component, String[] path);
}
