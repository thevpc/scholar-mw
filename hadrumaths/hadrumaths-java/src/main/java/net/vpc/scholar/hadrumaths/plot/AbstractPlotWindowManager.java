/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.util.StringUtils;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

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

    public PlotWindowContainerFactory getPlotWindowContainerFactory() {
        return plotWindowContainerFactory;
    }

    public void setPlotWindowContainerFactory(PlotWindowContainerFactory plotWindowContainerFactory) {
        this.plotWindowContainerFactory = plotWindowContainerFactory;
    }

    public AbstractPlotWindowManager(String globalTitle) {
        this.globalTitle = globalTitle;
    }

    protected void titleChanged(PlotComponent component) {

    }

    @Override
    public final void remove(final PlotComponent component) {
        if(component==null){
            return;
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JComponent jComponent = component.toComponent();
                    jComponent.removePropertyChangeListener("title", titleChangeListener);
                    jComponent.putClientProperty(PlotWindowManager.class.getName(), null);
                    removePlotComponentImpl(component);
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void removePlotComponentImpl(PlotComponent component);

    @Override
    public final PlotContainer add(String name) {
        PlotContainer container = getPlotWindowContainerFactory().create(name, this);
        add(container);
        return container;
    }

    public final void add(final PlotComponent component) {
        try {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    JComponent jComponent = component.toComponent();
                    jComponent.addPropertyChangeListener("title", titleChangeListener);
                    jComponent.putClientProperty(PlotWindowManager.class.getName(), this);
                    jComponent.putClientProperty(PlotComponent.class.getName(), component);
                    addPlotComponentImpl(component);
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                r.run();
            }else {
                SwingUtilities.invokeAndWait(r);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void addPlotComponentImpl(PlotComponent component);

    @Override
    public void display(PlotComponent plotComponent) {
        add(plotComponent);
    }

    protected String validateTitle(String s){
        if(StringUtils.isEmpty(s)){
            s="Figure";
        }
        return s;
    }

}
