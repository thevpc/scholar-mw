package net.vpc.scholar.hadrumaths.convergence;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.HSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 16 oct. 2007 23:45:02
 */
public class ConvergenceResult implements HSerializable {
    private final boolean finalValue;
    private final String label;
    private final Object source;
    private final double epsilon;
    private final double relativeError;
    private final Object oldValue;
    private final Object value;
    private final int varIndex;
    private final Object varValue;
    private final int stabilityThreshold;
    private final ConvergenceResult subResult;
    private ConvergenceConfig config;
    private final Map<String, Object> parameters;

    public ConvergenceResult(
            String label, Object source, Object value, int varIndex, Object varValue, Object oldValue, double relativeError, Map<String, Object> parameters, double epsilon, int stabilityThreshold, boolean finalValue, ConvergenceResult subResult) {
        this.parameters = parameters == null ? new HashMap<String, Object>() : new HashMap<String, Object>(parameters);
        this.relativeError = relativeError;
        this.source = source;
        this.value = value;
        this.varIndex = varIndex;
        this.varValue = varValue;
        this.epsilon = epsilon;
        this.subResult = subResult;
        this.label = label;
        this.finalValue = finalValue;
        this.stabilityThreshold = stabilityThreshold;
        this.oldValue = oldValue;
    }

    public ConvergenceConfig getConfig() {
        return config;
    }

    public ConvergenceResult setConfig(ConvergenceConfig config) {
        this.config = config;
        return this;
    }

    public int getStabilityThreshold() {
        return stabilityThreshold;
    }

    public Object getSource() {
        return source;
    }

    public String getLabel() {
        return label;
    }

    public int getVarIndex() {
        return varIndex;
    }

    public <T> T getVarValue() {
        return (T) varValue;
    }

    public int varIndex() {
        return varIndex;
    }

    public <T> T varValue() {
        return (T) varValue;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public double relativeError() {
        return getRelativeError();
    }

    public double getRelativeError() {
        return relativeError;
    }

    public <T> T value() {
        return (T) value;
    }

    public <T> T getValue() {
        return (T) value;
    }

    public double getEpsilon() {
        return epsilon;
    }

    /**
     * @return true if finalValue and relativeError &le; epsilon
     */
    public boolean isConvergent() {
        return isFinalValue() && (relativeError <= epsilon);
    }

    public boolean isFinalValue() {
        return finalValue;
    }

    public String toString() {
        return dump();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder obj = Tson.obj(getClass().getSimpleName());
        obj.add("name", Tson.elem(label));
        obj.add("threshold", Tson.elem(epsilon));
        obj.add("err", Tson.elem(relativeError));
        obj.add("ndex", Tson.elem(varIndex));
        obj.add("value", context.elem(varValue));
        if (parameters != null && !parameters.isEmpty()) {
            obj.add("config", context.elem(parameters));
        }
//        h.add("value", String.valueOf(value));
        if (subResult != null) {
            obj.add("subResult", context.elem(subResult));
        }
        return null;
    }

    public ConvergenceResult getSubResult() {
        return subResult;
    }
}
