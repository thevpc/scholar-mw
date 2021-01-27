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
public class LengthValue {

    private final double value;
    private final LengthUnit unit;

    public static LengthUnit SI_UNIT = LengthUnit.m;

    public LengthValue of(double value, LengthUnit unit) {
        return new LengthValue(value, SI_UNIT);
    }

    private LengthValue(double value, LengthUnit unit) {
        this.value = value;
        if (unit == null) {
            unit = SI_UNIT;
        }
        this.unit = unit;
    }

    public double value() {
        return value;
    }

    public LengthUnit unit() {
        return unit;
    }

    public LengthValue to(LengthUnit t) {
        if (t == unit) {
            return this;
        }
        return new LengthValue(t.multiplier() / unit.multiplier() * value, t);
    }

}
