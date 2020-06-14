/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.project;

/**
 *
 * @author vpc
 */
public abstract class EvalExprResult {
    
    public String expression;
    public String formattedString;
    public Object value;
    public Object unitAwareValue;
    private boolean error;
    private String errorMessage;
    private boolean hasUnit;

    public static EvalExprResult Error(String expression, String ex) {
        return Error(expression, new IllegalArgumentException(ex));
    }

    public static EvalExprResult Error(String expression, Exception ex) {
        return new EvalExprError(expression, "Eval Error of '" + expression + "': " + ex.getMessage());
    }

    public static EvalExprResult Success(String expression, String formattedString, Object value) {
        return new EvalExprSuccess(expression, formattedString, value, value, false);
    }
    public static EvalExprResult Success(String expression, String formattedString, Object value, Object unitAwareValue, boolean hasUnit) {
        return new EvalExprSuccess(expression, formattedString, value, unitAwareValue, hasUnit);
    }

    public EvalExprResult(String expression, String formattedString, Object value, Object unitAwareValue, boolean hasUnit, boolean error, String errorMessage) {
        this.hasUnit = hasUnit;
        this.expression = expression;
        this.formattedString = formattedString;
        this.value = value;
        this.unitAwareValue = unitAwareValue;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public boolean hasUnit() {
        return hasUnit;
    }

    public String getExpression() {
        return expression;
    }

    public String getFormattedString() {
        return formattedString;
    }

    public Object getValueOrError() {
        if (isError()) {
            throw new IllegalArgumentException(getErrorMessage());
        }
        return value;
    }

    public Object getValue() {
        return value;
    }

    public Object getUnitAwareValue() {
        return unitAwareValue;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        if (error) {
            return "[Error] " + errorMessage;
        }
        return formattedString;
    }
    
}
