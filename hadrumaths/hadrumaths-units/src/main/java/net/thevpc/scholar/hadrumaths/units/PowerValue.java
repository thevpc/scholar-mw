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
public class PowerValue {

    private final double value;
    private final PowerUnit unit;

    public static PowerUnit SI_UNIT = PowerUnit.W;

    public PowerValue of(double value, PowerUnit unit) {
        return new PowerValue(value, unit);
    }

    public PowerValue(double value, PowerUnit unit) {
        this.value = value;
        if (unit == null) {
            unit = SI_UNIT;
        }
        this.unit = unit;
    }

    public double value() {
        return value;
    }

    public PowerUnit unit() {
        return unit;
    }

    public PowerValue to(PowerUnit t) {
        if (t == unit) {
            return this;
        }
        return new PowerValue(t.multiplier() / unit.multiplier() * value, t);
    }

}
