package net.vpc.scholar.hadruplot;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public abstract class BasePlotModel implements PlotModel {

    private PropertyChangeSupport changeSupport;
    private String libraries;
    private String title = "";
    private String name = "";
    private String colorPalette = "";

    public BasePlotModel() {
        changeSupport = new PropertyChangeSupport(this);
    }

    @Override
    public PlotModel setTitle(String title) {
        Object old = this.title;
        this.title = title;
        firePropertyChange("title", old, this.title);
        return this;
    }

    public String getLibraries() {
        return libraries;
    }

    public PlotModel setLibraries(String libraries) {
        String old=this.libraries;
        this.libraries = libraries;
        firePropertyChange("libraries", old, this.libraries);
        return this;
    }

    public String getColorPalette() {
        return colorPalette;
    }

    public PlotModel setColorPalette(String colorPalette) {
        String old=this.colorPalette;
        this.colorPalette = colorPalette;
        firePropertyChange("colorPalette", old, this.colorPalette);
        return this;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean accept(PlotModelType t) {
        return getModelTypes().contains(t);
    }

    @Override
    public Set<PlotModelType> getModelTypes() {
        return EnumSet.of(PlotModelType.CUSTOM);
    }

    @Override
    public PlotModel1D toPlotModel1D() {
        if (getModelTypes().contains(PlotModelType.NUMBER_1) && this instanceof PlotModel1D) {
            return (PlotModel1D) this;
        }
        throw new UnsupportedOperationException("Not supported toPlotModel1D.");
    }

    @Override
    public PlotModel2D toPlotModel2D() {
        if (getModelTypes().contains(PlotModelType.NUMBER_2) && this instanceof PlotModel2D) {
            return (PlotModel2D) this;
        }
        throw new UnsupportedOperationException("Not supported toPlotModel2D.");
    }

    @Override
    public PlotModel3D toPlotModel3D() {
        if (getModelTypes().contains(PlotModelType.NUMBER_3) && this instanceof PlotModel3D) {
            return (PlotModel3D) this;
        }
        throw new UnsupportedOperationException("Not supported toPlotModel3D.");
    }

    @Override
    public String getPreferredTitle() {
        String s = getTitle();
        if (s != null) {
            return s;
        }
        s = getName();
        if (s != null) {
            return s;
        }
        return "NoName";
    }

    public PlotModel setName(String name) {
        Object old = this.name;
        this.name = name;
        firePropertyChange("name", old, this.name);
        return this;
    }

    public String getName() {
        return name;
    }

    protected void firePropertyChange(String name, Object oldValue, Object newValue) {
        if (!Objects.equals(oldValue, newValue)) {
//            changeSupport.firePropertyChange(DATA_PROPERTY, Boolean.FALSE, Boolean.TRUE);
            changeSupport.firePropertyChange(name, oldValue, newValue);
        }
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(property, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(property, listener);
    }

}
