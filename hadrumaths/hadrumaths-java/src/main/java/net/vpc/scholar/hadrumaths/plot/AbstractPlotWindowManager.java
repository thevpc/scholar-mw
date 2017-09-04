/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.util.StringUtils;
import net.vpc.scholar.hadrumaths.util.SwingUtils;

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
        SwingUtils.invokeAndWait(new Runnable() {
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
                addPlotComponentImpl(component, StringUtils.splitToArr(path, "/"));
            }
        };
        SwingUtils.invokeAndWait(r);

    }


    @Override
    public void display(PlotComponent plotComponent) {
        add(plotComponent);
    }

    protected String validateTitle(String s) {
        if (StringUtils.isEmpty(s)) {
            s = "Figure";
        }
        return s;
    }

    public abstract PlotContainer getRootContainer();

    public PlotContainer getContainer(String[] path) {
        if (path.length == 0) {
            return getRootContainer();
        }
        return findOrCreateContainer(path, 0, getRootContainer());
    }

    private PlotContainer findOrCreateContainer(String[] path, int index, PlotContainer parent) {
        String name = path[index];
        int childIndex = parent.indexOfPlotComponent(name);
        PlotContainer p=null;
        if(childIndex<0){
            p=parent.add(name);
        }else {
            PlotComponent child = parent.getPlotComponent(name);
            if (child instanceof PlotContainer) {
                p = (PlotContainer) child;
            } else {
                p = parent.add(childIndex, name);
            }
        }
        if (index == path.length - 1) {
            return p;
        }
        return findOrCreateContainer(path,index+1,p);
//
//        if (StringUtils.isInt(name)) {
//
//
//            if (i < 0) {
//                throw new IllegalArgumentException("Invalid index " + i);
//            } else if (i < c) {
//            } else {
//                PlotContainer p = parent.add(String.valueOf(i));
//                if (index == path.length - 1) {
//                    return p;
//                }
//                return findOrCreateContainer(path,index+1,p);
//            }
//        } else {
//            throw new IllegalArgumentException("Not yet supported non numeric path");
//        }
    }

    public void removePlotComponentImpl(PlotComponent component) {
        getRootContainer().remove(component);
    }

    public void addPlotComponentImpl(PlotComponent component, String[] path) {
        getContainer(path).add(component);
    }
}
