/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.units;

/**
 *
 * @author vpc
 */
public class VoltageValue {

    private final double value;
    private final VoltageUnit unit;

    public static VoltageUnit SI_UNIT = VoltageUnit.V;

    public VoltageValue of(double value, VoltageUnit unit) {
        return new VoltageValue(value, unit);
    }

    public VoltageValue(double value, VoltageUnit unit) {
        this.value = value;
        if (unit == null) {
            unit = SI_UNIT;
        }
        this.unit = unit;
    }

    public double value() {
        return value;
    }

    public VoltageUnit unit() {
        return unit;
    }

    public VoltageValue to(VoltageUnit t) {
        if (t == unit) {
            return this;
        }
        return new VoltageValue(t.multiplier() / unit.multiplier() * value, t);
    }

}
