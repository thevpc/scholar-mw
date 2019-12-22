package net.vpc.scholar.hadruplot;

import java.util.EventObject;

public class PlotPropertyEvent extends EventObject {
    private String property;
    private Object oldValue;
    private Object newValue;

    public PlotPropertyEvent(Object source, String property, Object oldValue, Object newValue) {
        super(source);
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getProperty() {
        return property;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}
