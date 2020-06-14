/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot.console.params;

/**
 * @author vpc
 */
public class CoupleValue<T> {

    private T referenceValue;
    private T modelValue;

    public CoupleValue(T referenceValue, T modelValue) {
        this.referenceValue = referenceValue;
        this.modelValue = modelValue;
    }

    public T getModelValue() {
        return modelValue;
    }

    public T getReferenceValue() {
        return referenceValue;
    }

    @Override
    public String toString() {
        return "(" + referenceValue + "," + modelValue + ")";
    }

}
