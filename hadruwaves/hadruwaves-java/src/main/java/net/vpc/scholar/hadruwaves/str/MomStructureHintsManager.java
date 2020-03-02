/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.str;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.AxisXY;
import net.vpc.scholar.hadrumaths.HSerializable;
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
public class MomStructureHintsManager extends MWStructureHintsManager implements Cloneable {

    public static final String HINT_DISCARD_FN_BY_SCALAR_PRODUCT = "HINT_DISCARD_FN_BY_SCALAR_PRODUCT";
    public static final String HINT_AMATRIX_SPARSIFY = "HINT_AMATRIX_SPARSIFY";
    public static final String HINT_BMATRIX_SPARSIFY = "HINT_BMATRIX_SPARSIFY";
    public static final String HINT_AXIS_TYPE = "HINT_AXIS_TYPE";
    public static final String HINT_FN_MODE = "HINT_FN_MODE";
    public static final String HINT_REGULAR_ZN_OPERATOR = "HINT_ZN_OPERATOR";
    public static final String HINT_INVARIANCE = "HINT_INVARIANCE";
    public static final String HINT_SYMMETRY = "HINT_SYMMETRY";

    public MomStructureHintsManager() {
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
}