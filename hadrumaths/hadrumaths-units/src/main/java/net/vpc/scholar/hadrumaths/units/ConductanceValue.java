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
public class ConductanceValue {

    private final double value;
    private final ConductanceUnit unit;

    public static ConductanceUnit SI_UNIT = ConductanceUnit.S;

    public ConductanceValue of(double value, ConductanceUnit unit) {
        return new ConductanceValue(value, unit);
    }

    public ConductanceValue(double value, ConductanceUnit unit) {
        this.value = value;
        if (unit == null) {
            unit = SI_UNIT;
        }
        this.unit = unit;
    }

    public double value() {
        return value;
    }

    public ConductanceUnit unit() {
        return unit;
    }

    public ConductanceValue to(ConductanceUnit t) {
        if (t == unit) {
            return this;
        }
        return new ConductanceValue(t.multiplier() / unit.multiplier() * value, t);
    }

}
