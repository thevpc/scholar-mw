package net.vpc.scholar.hadruplot;

import java.util.HashMap;
import java.util.Map;

public class DefaultPlotValue implements PlotValue {
    public PlotValueType type;
    public Object value;
    public int priority;
    public Map<String, String> props = new HashMap<>();

    public DefaultPlotValue(int priority,PlotValueType type, Object value) {
        this.priority = priority;
        this.type = type;
        this.value = value;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public PlotValueType getType() {
        return type;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void set(String prop, String val) {
        props.put(prop, val);
    }

    @Override
    public String get(String prop) {
        return null;
    }
}
