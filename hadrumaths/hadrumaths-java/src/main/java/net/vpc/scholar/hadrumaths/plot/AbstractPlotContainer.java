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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author vpc
 */
public abstract class AbstractPlotContainer implements PlotContainer {
    private PlotWindowContainerFactory plotWindowContainerFactory;
    private String title ="Plot";
    private String layoutConstraints ="";
    private PlotWindowManager plotWindowManager;
    private List<PlotPropertyListener> listeners;
    private PropertyChangeListener titleChangeListener =new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object source = evt.getSource();
            PlotComponent cc=null;
            if(source instanceof PlotComponent){
                cc=(PlotComponent) source;
            }else{
                cc=(PlotComponent) ((JComponent) source).getClientProperty(PlotComponent.class.getName());
            }
            if(cc!=null){
                titleChanged(cc);
            }
        }
    };
    protected String validateTitle(String s){
        if(StringUtils.isEmpty(s)){
            s="Figure";
        }
        return s;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLayoutConstraints(String layoutConstraints) {
        this.layoutConstraints = layoutConstraints;
    }

    @Override
    public String getLayoutConstraints() {
        return layoutConstraints;
    }


    protected void titleChanged(PlotComponent component){

    }
    public AbstractPlotContainer(PlotWindowManager plotWindowManager, String title) {
        this.plotWindowManager=plotWindowManager;
        this.title = title;
    }

    public PlotWindowContainerFactory getPlotWindowContainerFactory() {
        return plotWindowContainerFactory;
    }

    public void setPlotWindowContainerFactory(PlotWindowContainerFactory plotWindowContainerFactory) {
        this.plotWindowContainerFactory = plotWindowContainerFactory;
    }

    @Override
    public final void remove(final PlotComponent component) {
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

    public abstract void removePlotComponentImpl(PlotComponent component) ;

    @Override
    public final PlotContainer add(String name) {
        PlotContainer container = getPlotWindowContainerFactory().create(name, plotWindowManager);
        add(container);
        return container;
    }

    public final void add(final PlotComponent component) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JComponent jComponent = component.toComponent();
                    jComponent.addPropertyChangeListener("title", titleChangeListener);
                    jComponent.putClientProperty(PlotWindowManager.class.getName(), this);
                    jComponent.putClientProperty(PlotComponent.class.getName(), component);
                    addPlotComponentImpl(component);
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void addPlotComponentImpl(PlotComponent component) ;

    @Override
    public String getPlotTitle() {
        return title;
    }

    public void display() {
        plotWindowManager.add(this);
    }

    @Override
    public void display(PlotComponent component) {
        add(component);
    }

    @Override
    public void addPlotPropertyListener(PlotPropertyListener listener) {
        if(listeners==null){
            listeners=new ArrayList<>();
        }
        listeners.add(listener);
    }
    @Override
    public void removePlotPropertyListener(PlotPropertyListener listener) {
        if(listeners!=null){
            listeners.remove(listener);
        }
    }

    protected void firePlotPropertyEvent(String propertyName,Object oldValue,Object newValue){
        if(!Objects.equals(oldValue,newValue)){
            if(listeners!=null){
                PlotPropertyEvent event=null;
                for (PlotPropertyListener listener : listeners) {
                    if (event == null){
                        event = new PlotPropertyEvent(this, propertyName, oldValue, newValue);
                    }
                    listener.onPropertyChange(event);
                }
            }
        }
    }
}
