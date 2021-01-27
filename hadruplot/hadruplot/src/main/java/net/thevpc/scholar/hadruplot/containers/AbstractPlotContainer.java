/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.containers;

import net.thevpc.scholar.hadruplot.extension.PlotWindowContainerFactory;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import java.awt.image.BufferedImage;

import net.thevpc.common.strings.StringUtils;
import net.thevpc.common.swing.SwingUtilities3;
import net.thevpc.common.util.PlatformUtils;
import net.thevpc.scholar.hadruplot.*;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author vpc
 */
public abstract class AbstractPlotContainer implements PlotContainer {

    private PlotWindowContainerFactory plotWindowContainerFactory = TabbedPlotWindowContainerFactory.INSTANCE;
    private String title = "Plot";
    private String layoutConstraints = "";
    private PlotWindowManager plotWindowManager;
    private PlotContainer parentPlotContainer;
    private List<PlotPropertyListener> listeners;
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

    public AbstractPlotContainer() {
    }

    @Override
    public PlotComponent getPlotComponent(String name) {
        int index = indexOfPlotComponent(name);
        if (index >= 0) {
            return getPlotComponent(index);
        }
        return null;
    }

    @Override
    public int indexOfPlotComponent(String name) {
        int count = getPlotComponentsCount();
        for (int i = 0; i < count; i++) {
            PlotComponent c = getPlotComponent(i);
            if (c != null) {
                if (StringUtils.trim(c.getPlotTitle()).equals(StringUtils.trim(name))) {
                    return i;
                }
            }
        }
        if (name.startsWith("#") && PlatformUtils.isInteger(name.substring(1))) {
            int x = Integer.parseInt(name.substring(1));
            if (x >= 0 && x < count) {
                PlotComponent c = getPlotComponent(x);
                if (c != null) {
                    return x;
                }
            }
        }
        return -1;
    }

    @Override
    public PlotContainer getParentPlotContainer() {
        return parentPlotContainer;
    }

    @Override
    public void setParentPlotContainer(PlotContainer parentPlotContainer) {
        if (this.parentPlotContainer != parentPlotContainer) {
            this.parentPlotContainer = parentPlotContainer;
//            if (!parentPlotContainer.containsPlotComponent(this)) {
//                parentPlotContainer.add(this);
//            }
        }
    }

    protected String validateTitle(String s) {
        if (StringUtils.isBlank(s)) {
            s = "Figure";
        }
        return s;
    }

    @Override
    public boolean containsPlotComponent(PlotComponent plotComponent) {
        return indexOfPlotComponent(plotComponent) >= 0;
    }

    @Override
    public String getLayoutConstraints() {
        return layoutConstraints;
    }

    public PlotContainer setLayoutConstraints(String layoutConstraints) {
        this.layoutConstraints = layoutConstraints;
        return this;
    }

    protected void titleChanged(PlotComponent component) {

    }

    public PlotWindowContainerFactory getPlotWindowContainerFactory() {
        return plotWindowContainerFactory;
    }

    public void setPlotWindowContainerFactory(PlotWindowContainerFactory plotWindowContainerFactory) {
        this.plotWindowContainerFactory = plotWindowContainerFactory;
    }

    @Override
    public void remove(final PlotComponent component) {
        SwingUtilities3.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                if (component.getParentPlotContainer() == AbstractPlotContainer.this) {
                    component.setParentPlotContainer(null);
                }
                JComponent jComponent = component.toComponent();
                jComponent.removePropertyChangeListener("title", titleChangeListener);
                jComponent.putClientProperty(PlotWindowManager.class.getName(), null);
                removePlotComponentImpl(component);
            }
        });
    }

    public abstract void removePlotComponentImpl(PlotComponent component);

    @Override
    public PlotContainer add(String name) {
        PlotContainer container = getPlotWindowContainerFactory().create();
        container.setPlotTitle(name);
        container.setPlotWindowManager(plotWindowManager);
        add(container);
        return container;
    }

    public void add(final PlotComponent component) {
        prepare(component);
        SwingUtilities3.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                addComponentImpl(component, -1);
            }
        });

    }

    public abstract void addComponentImpl(PlotComponent component, int index);

    @Override
    public String getPlotTitle() {
        return title;
    }

    public void setPlotTitle(String title) {
        String old = this.title;
        this.title = title;
        firePlotPropertyEvent("title", old, title);
    }

    public void display() {
        if (getParentPlotContainer() == null) {
            plotWindowManager.add(this);
        } else {
//            c.getParent().setVisible(true);
        }
    }

    @Override
    public void display(PlotComponent component) {
        add(component);
    }

    @Override
    public void addPlotPropertyListener(PlotPropertyListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    @Override
    public void removePlotPropertyListener(PlotPropertyListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    protected void firePlotPropertyEvent(String propertyName, Object oldValue, Object newValue) {
        if (!Objects.equals(oldValue, newValue)) {
            if (listeners != null) {
                PlotPropertyEvent event = null;
                for (PlotPropertyListener listener : listeners) {
                    if (event == null) {
                        event = new PlotPropertyEvent(this, propertyName, oldValue, newValue);
                    }
                    listener.onPropertyChange(event);
                }
            }
        }
    }

    public PlotComponent toPlotComponent(JComponent component) {
        Object a = component.getClientProperty(PlotComponent.class.getName());
        if (a != null) {
            return (PlotComponent) a;
        }
        return null;
    }

    public JComponent toComponent(PlotComponent component) {
        JComponent comp = component.toComponent();
        Object a = comp.getClientProperty(PlotComponent.class.getName());
        if (a != null) {
            comp.putClientProperty(PlotComponent.class.getName(), component);
        }
        return comp;
    }

    @Override
    public PlotWindowManager getPlotWindowManager() {
        return plotWindowManager;
    }

    @Override
    public void setPlotWindowManager(PlotWindowManager plotWindowManager) {
        this.plotWindowManager = plotWindowManager;
    }

    @Override
    public void clear() {
        while (true) {
            int t1 = getPlotComponentsCount();
            if (t1 == 0) {
                return;
            }
            remove(getPlotComponent(0));
            int t2 = getPlotComponentsCount();
            if (t2 >= t1) {
                //some components are invalid!
                return;
            }
        }
    }

    private void prepare(PlotComponent component) {
        if (component.getParentPlotContainer() != AbstractPlotContainer.this) {
            component.setParentPlotContainer(AbstractPlotContainer.this);
        }
        JComponent jComponent = component.toComponent();
        jComponent.addPropertyChangeListener("title", titleChangeListener);
        jComponent.putClientProperty(PlotWindowManager.class.getName(), this);
        jComponent.putClientProperty(PlotComponent.class.getName(), component);
    }

    @Override
    public PlotContainer add(final int index, String containerName) {
        final PlotContainer container = getPlotWindowContainerFactory().create();
        container.setPlotTitle(containerName);
        container.setPlotWindowManager(plotWindowManager);
        PlotComponent oldComponent = null;
        if (index >= 0) {
            oldComponent = getPlotComponent(index);
        }
        prepare(container);
        SwingUtilities3.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                addComponentImpl(container, index);
            }
        });
        if (oldComponent != null) {
            container.add(oldComponent);
        }
        return container;
    }

    @Override
    public void add(PlotComponent component, String path) {
        if (path == null || path.isEmpty()) {
            path = "/";
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
//        if (path == null || !path.startsWith("/")) {
//            throw new IllegalArgumentException("Invalid path " + path);
//        }
        List<String> pathList = new ArrayList<>(Arrays.asList(StringUtils.split(path, "/")));
        if (pathList.size() == 0) {
            add(component);
        } else {
            String name = pathList.get(0);
            int childIndex = indexOfPlotComponent(name);
            PlotContainer pp = null;
            if (childIndex < 0) {
                pp = add(name);
            } else {
                PlotComponent child = getPlotComponent(name);
                if (child instanceof PlotContainer) {
                    pp = (PlotContainer) child;
                } else {
                    pp = add(childIndex, name);
                }
            }
            pathList.remove(0);
            pp.add(component, StringUtils.join("/", pathList));
        }
    }

    @Override
    public BufferedImage getImage() {
        return Plot.createImage(toComponent());
    }

    @Override
    public void saveImageFile(String file) {
        Plot.saveImageFile(this, file);
    }

    @Override
    public PlotModel getModel() {
        return null;
    }
}
