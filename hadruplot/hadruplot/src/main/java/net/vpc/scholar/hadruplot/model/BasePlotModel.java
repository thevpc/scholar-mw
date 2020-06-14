package net.vpc.scholar.hadruplot.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToDoubleFunction;
import net.vpc.scholar.hadruplot.LibraryPlotType;
import net.vpc.scholar.hadruplot.PlotModelType;
import net.vpc.scholar.hadruplot.PlotType;

public abstract class BasePlotModel implements PlotModel {

    private PropertyChangeSupport changeSupport;
    private String title = "";
    private String name = "";
    private String colorPalette = "";
    private ToDoubleFunction<Object> converter;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private LibraryPlotType plotType = new LibraryPlotType(PlotType.CURVE, "default");

    public BasePlotModel() {
        changeSupport = new PropertyChangeSupport(this);
    }

    @Override
    public PlotModel setConverter(ToDoubleFunction<Object> converter) {
        Object old = this.converter;
        this.converter = converter;
        firePropertyChange("converter", old, this.converter);
        return this;
    }

    @Override
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    @Override
    public PlotModel setProperties(Map<String, Object> other) {
        if (other != null) {
            for (Map.Entry<String, Object> e : other.entrySet()) {
                setProperty(e.getKey(), e.getValue());
            }
        }
        return this;
    }

    @Override
    public PlotModel removeProperty(String key) {
        return setProperty(key, null);
    }

    @Override
    public Object getProperty(String key) {
        return getProperty(key, null);
    }

    @Override
    public Object getProperty(String key, Object defaultValue) {
        if (properties.containsKey(key)) {
            return properties.get(key);
        }
        return defaultValue;
    }

    @Override
    public PlotModel setProperty(String key, Object value) {
        if (value == null) {
            Object old = properties.remove(key);
            if (old != null) {
                firePropertyChange("property." + key, old, null);
            }
        } else {
            Object old = properties.put(key, value);
            firePropertyChange("property." + key, old, value);
        }
        return this;
    }

    @Override
    public PlotModel setProperty(int index, String key, Object value) {
        String s = "[" + index + "]." + key;
        return setProperty(s, value);
    }

    @Override
    public Object getProperty(int index, String key) {
        return getProperty(index, key, null);
    }

    @Override
    public Object getProperty(int index, String key, Object defaultValue) {
        String s = "[" + index + "]." + key;
        return getProperty(s, defaultValue);
    }

    @Override
    public ToDoubleFunction<Object> getConverter() {
        return converter;
    }

    @Override
    public PlotModel setTitle(String title) {
        Object old = this.title;
        this.title = title;
        firePropertyChange("title", old, this.title);
        return this;
    }

    @Override
    public String getColorPalette() {
        return colorPalette;
    }

    @Override
    public PlotModel setColorPalette(String colorPalette) {
        String old = this.colorPalette;
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

    @Override
    public PlotModel setName(String name) {
        Object old = this.name;
        this.name = name;
        firePropertyChange("name", old, this.name);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    protected void firePropertyChange(String name, Object oldValue, Object newValue) {
        if (!Objects.equals(oldValue, newValue)) {
//            changeSupport.firePropertyChange(DATA_PROPERTY, Boolean.FALSE, Boolean.TRUE);
            changeSupport.firePropertyChange(name, oldValue, newValue);
        }
    }

    @Override
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(property, listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(property, listener);
    }

    public LibraryPlotType getPlotType() {
        return plotType;
    }

    @Override
    public PlotModel setPlotType(LibraryPlotType plotType) {
        Object old = this.plotType;
        this.plotType = plotType;
        firePropertyChange("plotType", old, this.plotType);
        return this;
    }

}
