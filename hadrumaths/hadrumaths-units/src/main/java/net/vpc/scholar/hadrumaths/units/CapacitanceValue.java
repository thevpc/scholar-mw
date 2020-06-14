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
public class CapacitanceValue {

    private final double value;
    private final ConductanceUnit unit;

    public static ConductanceUnit SI_UNIT = ConductanceUnit.S;

    public CapacitanceValue of(double value, ConductanceUnit unit) {
        return new CapacitanceValue(value, unit);
    }

    public CapacitanceValue(double value, ConductanceUnit unit) {
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

    public CapacitanceValue to(ConductanceUnit t) {
        if (t == unit) {
            return this;
        }
        return new CapacitanceValue(t.multiplier() / unit.multiplier() * value, t);
    }

}
