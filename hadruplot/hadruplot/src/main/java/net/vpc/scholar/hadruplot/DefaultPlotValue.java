package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.model.value.PlotValueType;
import java.util.HashMap;
import java.util.Map;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;

public class DefaultPlotValue implements PlotValue {
    public PlotValueType type;
    public Object value;
    public Map<String, String> props = new HashMap<>();

    public DefaultPlotValue(String type, Object value) {
        this.type=PlotConfigManager.getPlotValueTypeFactory().getType(type);
        this.value=value;
    }
    
    public PlotValueType getType() {
        return type;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void set(String prop, String val) {
        props.put(prop, val);
    }

    @Override
    public String get(String prop) {
        return props.get(prop);
    }
}
