/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.units;

/**
 *
 * @author vpc
 */
public class FrequencyValue {

    private final double value;
    private final FrequencyUnit unit;

    public static FrequencyUnit SI_UNIT = FrequencyUnit.Hz;

    public FrequencyValue of(double value, FrequencyUnit unit) {
        return new FrequencyValue(value, unit);
    }

    private FrequencyValue(double value, FrequencyUnit unit) {
        this.value = value;
        if (unit == null) {
            unit = SI_UNIT;
        }
        this.unit = unit;
    }

    public double value() {
        return value;
    }

    public FrequencyUnit unit() {
        return unit;
    }

    public FrequencyValue to(FrequencyUnit t) {
        if (t == unit) {
            return this;
        }
        return new FrequencyValue(t.multiplier() / unit.multiplier() * value, t);
    }

}
