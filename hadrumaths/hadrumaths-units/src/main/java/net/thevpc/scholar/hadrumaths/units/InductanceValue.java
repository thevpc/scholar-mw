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
public class InductanceValue {

    private final double value;
    private final InductanceUnit unit;

    public static InductanceUnit SI_UNIT = InductanceUnit.H;

    public InductanceValue of(double value, InductanceUnit unit) {
        return new InductanceValue(value, unit);
    }

    public InductanceValue(double value, InductanceUnit unit) {
        this.value = value;
        if (unit == null) {
            unit = SI_UNIT;
        }
        this.unit = unit;
    }

    public double value() {
        return value;
    }

    public InductanceUnit unit() {
        return unit;
    }

    public InductanceValue to(InductanceUnit t) {
        if (t == unit) {
            return this;
        }
        return new InductanceValue(t.multiplier() / unit.multiplier() * value, t);
    }

}
