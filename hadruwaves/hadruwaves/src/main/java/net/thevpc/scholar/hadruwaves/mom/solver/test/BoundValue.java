/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.solver.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vpc
 */
public class BoundValue<T> {

    private LinkedHashMap<String, Double> paramValues = new LinkedHashMap<>();
    private Object[] values;

    public BoundValue(T value) {
        this.values = new Object[]{value};
    }

    public BoundValue(T value, String param, double paramValue) {
        this.values = new Object[]{value};
        paramValues.put(param, paramValue);
    }

    private BoundValue(Object[] values, Map<String, Double> paramValues) {
        this.values = values;
        this.paramValues = new LinkedHashMap<>(paramValues);
    }

    public BoundValue<T> cross(BoundValue other) {
        Object[] n = new Object[values.length + other.values.length];
        System.arraycopy(values, 0, n, 0, values.length);
        System.arraycopy(other.values, 0, n, values.length, other.values.length);
        Map<String, Double> vals = new HashMap<String, Double>();
        vals.putAll(paramValues);
        vals.putAll(other.paramValues);
        return new BoundValue<T>(n, vals);
    }

    public Map<String, Double> getParamValues() {
        return Collections.unmodifiableMap(paramValues);
    }

    public List<T> getValues() {
        return (List) Arrays.asList(values);
    }

}
