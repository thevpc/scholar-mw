package net.vpc.scholar.hadrumaths.convergence;

import java.util.Map;

/**
 * @author taha.bensalah@gmail.com on 7/21/16.
 */
public class ConvergenceParamContext {
    private final Object source;
    private final Object value;
    private final int index;
    private final Map<String, Object> config;

    public ConvergenceParamContext(Object source, Object value, int index, Map<String, Object> config) {
        this.source = source;
        this.value = value;
        this.index = index;
        this.config = config;
    }

    public Object getSource() {
        return source;
    }

    public Object getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }

    public Map<String, Object> getConfig() {
        return config;
    }
}
