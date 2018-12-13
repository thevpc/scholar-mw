package net.vpc.scholar.hadrumaths.convergence;

import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 16 oct. 2007 23:45:02
 */
public class ConvergenceResult {
    private static final DecimalFormat d = new DecimalFormat("0.00%");
    private boolean finalValue;
    private String label;
    private Object source;
    private double epsilon;
    private double relativeError;
    private Object oldValue;
    private Object value;
    private int varIndex;
    private Object varValue;
    private int stabilityThreshold;
    private ConvergenceResult subResult;
    private ConvergenceConfig config;
    private Map<String, Object> parameters;

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

    public String toString() {
        Dumper h = new Dumper(label, Dumper.Type.SIMPLE);
        h.add("threshold", Double.isNaN(epsilon) ? String.valueOf(epsilon) : d.format(epsilon));
        h.add("err", Double.isNaN(relativeError) ? String.valueOf(relativeError) : d.format(relativeError));
        h.add("ndex", varIndex);
        h.add("value", varValue);
        if (parameters != null && !parameters.isEmpty()) {
            h.add("config", String.valueOf(parameters));
        }
//        h.add("value", String.valueOf(value));
        if (subResult != null) {
            h.add("subResult", String.valueOf(subResult));
        }
        return h.toString();
    }

    public boolean isFinalValue() {
        return finalValue;
    }

    public ConvergenceResult getSubResult() {
        return subResult;
    }
}
