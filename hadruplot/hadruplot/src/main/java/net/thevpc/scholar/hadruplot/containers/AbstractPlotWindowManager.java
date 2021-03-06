/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.containers;

import net.thevpc.scholar.hadruplot.extension.PlotWindowContainerFactory;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.common.swing.SwingUtilities3;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.thevpc.scholar.hadruplot.PlotComponent;
import net.thevpc.scholar.hadruplot.PlotContainer;
import net.thevpc.scholar.hadruplot.PlotPath;
import net.thevpc.scholar.hadruplot.PlotWindowManager;

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
        add(component, PlotPath.ROOT);
    }

    public final void add(final PlotComponent component, final PlotPath path) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                JComponent jComponent = component.toComponent();
                jComponent.addPropertyChangeListener("title", titleChangeListener);
                jComponent.putClientProperty(PlotWindowManager.class.getName(), this);
                jComponent.putClientProperty(PlotComponent.class.getName(), component);
                component.setPlotWindowManager(AbstractPlotWindowManager.this);
                addPlotComponentImpl(component, PlotPath.nonnull(path));
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

    public abstract void addPlotComponentImpl(PlotComponent component, net.thevpc.scholar.hadruplot.PlotPath path);
}
