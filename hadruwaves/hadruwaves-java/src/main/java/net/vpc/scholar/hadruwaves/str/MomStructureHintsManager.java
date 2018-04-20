/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.str;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.AxisXY;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruwaves.mom.HintAxisType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vpc
 */
public class MomStructureHintsManager implements Cloneable, Dumpable {

    private static final String HINT_DISCARD_FN_BY_SCALAR_PRODUCT = "HINT_DISCARD_FN_BY_SCALAR_PRODUCT";
    private static final String HINT_AMATRIX_SPARSIFY = "HINT_AMATRIX_SPARSIFY";
    private static final String HINT_BMATRIX_SPARSIFY = "HINT_BMATRIX_SPARSIFY";
    private static final String HINT_AXIS_TYPE = "HINT_AXIS_TYPE";
    private static final String HINT_FN_MODE = "HINT_FN_MODE";
    private static final String HINT_REGULAR_ZN_OPERATOR = "HINT_ZN_OPERATOR";
    private static final String HINT_INVARIANCE = "HINT_INVARIANCE";
    private static final String HINT_SYMMETRY = "HINT_SYMMETRY";

    private HashMap<String, Object> parameters = new HashMap<String, Object>();
    private PropertyChangeSupport changeSupport;

    public MomStructureHintsManager() {
        this.changeSupport = new PropertyChangeSupport(this);
    }

    public void addChangeListener(PropertyChangeListener changeListener) {
        this.changeSupport.addPropertyChangeListener(changeListener);
    }

    public void removeChangeListener(PropertyChangeListener changeListener) {
        this.changeSupport.addPropertyChangeListener(changeListener);
    }

    public Number getHintAMatrixSparsify() {
        Number ceil = getHintNumber(MomStructureHintsManager.HINT_AMATRIX_SPARSIFY, null);
        if (ceil != null && !Double.isNaN(ceil.doubleValue()) && ceil.doubleValue() > 0) {
            return ceil;
        }
        return null;
    }

    public void setHintAMatrixSparsify(Number value) {
        setHintNotNull(HINT_AMATRIX_SPARSIFY, value);
    }

    public Number getHintBMatrixSparsify() {
        Number ceil = getHintNumber(MomStructureHintsManager.HINT_BMATRIX_SPARSIFY, null);
        if (ceil != null && !Double.isNaN(ceil.doubleValue()) && ceil.doubleValue() > 0) {
            return ceil;
        }
        return null;
    }

    public void setHintBMatrixSparsify(Number value) {
        setHintNotNull(HINT_BMATRIX_SPARSIFY, value);
    }

    public Number getHintNumber(String name, Number defaultValue) {
        return (Number) getHint(name, defaultValue);
    }

    public Boolean getHintBoolean(String name, Boolean defaultValue) {
        return (Boolean) getHint(name, defaultValue);
    }

    public HintAxisType getHintAxisType() {
        return (HintAxisType) getHint(HINT_AXIS_TYPE, HintAxisType.XY);
    }

    public void setHintAxisType(HintAxisType value) {
        setHint(HINT_AXIS_TYPE, value == null ? HintAxisType.XY : value);
    }

    public Float getHintDiscardFnByScalarProduct() {
        return (Float) getHint(HINT_DISCARD_FN_BY_SCALAR_PRODUCT);
    }

    /**
     * if(for any p : sp(Fn,Gp)<max{q}(sp(Fn,Gq))*minimumPercent) then discard
     * Fn
     *
     * @param minimumPercent
     */
    public void setHintDiscardFnByScalarProduct(Float minimumPercent) {
        setHintNotNull(HINT_DISCARD_FN_BY_SCALAR_PRODUCT, minimumPercent);
    }

    public void setHintFnMode(ModeType... value) {
        if (value != null && value.length == 0) {
            value = null;
        }
        setHintNotNull(HINT_FN_MODE, value);
    }

    public boolean isHintRegularZnOperator() {
        return isHint(HINT_REGULAR_ZN_OPERATOR);
    }

    public void setHintRegularZnOperator(Boolean value) {
        setHintNotNull(HINT_REGULAR_ZN_OPERATOR, value);
    }

    /**
     * @param value
     */
    public void setHintAMatrixSparcify(Float value) {
        if (value != null && (value.isNaN() || value.floatValue() <= 0)) {
            value = null;
        }
        setHintNotNull(HINT_AMATRIX_SPARSIFY, value);
    }

    public void setHintBMatrixSparcify(Float value) {
        if (value != null && (value.isNaN() || value.doubleValue() <= 0)) {
            value = null;
        }
        setHintNotNull(HINT_BMATRIX_SPARSIFY, value);
    }

    public ModeType[] getHintFnModeTypes() {
        return (ModeType[]) getHint(HINT_FN_MODE, null);
    }

    public Axis getHintInvariance() {
        return (Axis) getHint(HINT_INVARIANCE);
    }

    public void setHintInvariance(Axis axis) {
        setHintNotNull(HINT_INVARIANCE, axis);
    }

    public AxisXY getHintSymmetry() {
        return (AxisXY) getHint(HINT_SYMMETRY);
    }

    public void setHintSymmetry(AxisXY axis) {
        setHintNotNull(HINT_SYMMETRY, axis);
    }


//----------------------------------------------

    public void setHint(String name) {
        setHint(name, Boolean.TRUE);
    }

    public void setHintNotNull(String name, Object value) {
        if (value == null) {
            removeHint(name);
        } else {
            setHint(name, value);
        }
    }

    public void setHint(String name, Object value) {
        Object old = parameters.put(name, value);
        changeSupport.firePropertyChange(name, old, value);
    }

    public void removeHint(String name) {
        Object old = parameters.remove(name);
        changeSupport.firePropertyChange(name, old, null);
    }

    public Object getHint(String name) {
        return parameters.get(name);
    }

    public Object getHint(String name, Object defaultValue) {
        if (parameters.containsKey(name)) {
            return parameters.get(name);
        }
        return defaultValue;
    }


    public boolean containsHint(String name) {
        return parameters.containsKey(name);
    }

    public boolean isHint(String name, boolean defaultValue) {
        if (parameters.containsKey(name)) {
            return Boolean.TRUE.equals(parameters.get(name));
        } else {
            return defaultValue;
        }
    }

    public boolean isHint(String name) {
        return Boolean.TRUE.equals(parameters.get(name));
    }

    @Override
    public MomStructureHintsManager clone() {
        try {
            final MomStructureHintsManager copy = (MomStructureHintsManager) super.clone();
            copy.parameters = (HashMap<String, Object>) this.parameters.clone();
            return copy;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MomStructureHintsManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public Dumper getDumpStringHelper() {
        Dumper helper = new Dumper(getClass().getSimpleName());
        helper.add(parameters);
        return helper;
    }

    public String dump() {
        return getDumpStringHelper().toString();
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    public void setAll(MomStructureHintsManager all) {
        if (all != null) {
            for (Map.Entry<String, Object> e : all.parameters.entrySet()) {
                setHint(e.getKey(), e.getValue());
            }
        }
    }
}
