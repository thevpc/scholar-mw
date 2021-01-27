package net.thevpc.scholar.hadruplot;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BasePlotComponent extends JPanel implements PlotComponent {

    private List<PlotPropertyListener> listeners;
    private PlotContainer parentPlotContainer;
    private PlotWindowManager plotWindowManager;
    private String plotTitle;
    private String layoutConstraints = "";

    public BasePlotComponent(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public BasePlotComponent(LayoutManager layout) {
        super(layout);
    }

    public BasePlotComponent(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public BasePlotComponent() {
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
    public PlotContainer getParentPlotContainer() {
        return parentPlotContainer;
    }

    public void setParentPlotContainer(PlotContainer parentPlotContainer) {
        if (this.parentPlotContainer != parentPlotContainer) {
            this.parentPlotContainer = parentPlotContainer;
//            if (!parentPlotContainer.containsPlotComponent(this)) {
//                parentPlotContainer.add(this);
//            }
        }
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

    @Override
    public String getPlotTitle() {
        return plotTitle;
    }

    @Override
    public void setPlotTitle(String plotTitle) {
        this.plotTitle = plotTitle;
    }

    @Override
    public String getLayoutConstraints() {
        return layoutConstraints;
    }

    public PlotComponent setLayoutConstraints(String layoutConstraints) {
        this.layoutConstraints = layoutConstraints;
        return this;
    }

    public JComponent toComponent() {
        return (JComponent) this;
    }

    public void display() {
        getPlotWindowManager().add(this);
    }

    @Override
    public BufferedImage getImage() {
        return Plot.createImage(toComponent());
    }

    @Override
    public void saveImageFile(String file) {
        Plot.saveImageFile(this, file);
    }

}
