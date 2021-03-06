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
public class ResistanceValue {

    private final double value;
    private final ResistanceUnit unit;

    public static ResistanceUnit SI_UNIT = ResistanceUnit.OHM;

    public ResistanceValue of(double value, ResistanceUnit unit) {
        return new ResistanceValue(value, SI_UNIT);
    }

    public ResistanceValue(double value, ResistanceUnit unit) {
        this.value = value;
        if (unit == null) {
            unit = SI_UNIT;
        }
        this.unit = unit;
    }

    public double value() {
        return value;
    }

    public ResistanceUnit unit() {
        return unit;
    }

    public ResistanceValue to(ResistanceUnit t) {
        if (t == unit) {
            return this;
        }
        return new ResistanceValue(t.multiplier() / unit.multiplier() * value, t);
    }

}
