package net.vpc.scholar.hadrumaths.plot;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import net.vpc.common.strings.StringUtils;

public abstract class BasePlotModel implements PlotModel {

    private PropertyChangeSupport changeSupport;
    private String title = "";
    private String name = "";

    public BasePlotModel() {
        changeSupport = new PropertyChangeSupport(this);
    }

    public PlotModel setTitle(String title) {
        Object old = this.title;
        this.title = title;
        firePropertyChange("title", old, this.title);
        return this;
    }

    public String getTitle() {
        return title;
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
