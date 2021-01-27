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
public class CurrentValue {

    private final double value;
    private final CurrentUnit unit;

    public static CurrentUnit SI_UNIT = CurrentUnit.A;

    public CurrentValue of(double value, CurrentUnit unit) {
        return new CurrentValue(value, unit);
    }

    public CurrentValue(double value, CurrentUnit unit) {
        this.value = value;
        if (unit == null) {
            unit = SI_UNIT;
        }
        this.unit = unit;
    }

    public double value() {
        return value;
    }

    public CurrentUnit unit() {
        return unit;
    }

    public CurrentValue to(CurrentUnit t) {
        if (t == unit) {
            return this;
        }
        return new CurrentValue(t.multiplier() / unit.multiplier() * value, t);
    }

}
