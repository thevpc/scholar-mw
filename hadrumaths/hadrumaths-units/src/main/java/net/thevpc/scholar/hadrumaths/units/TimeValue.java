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
public class TimeValue {

    private final double value;
    private final TimeUnit unit;

    public static TimeUnit SI_UNIT = TimeUnit.s;

    public TimeValue of(double value, TimeUnit unit) {
        return new TimeValue(value, unit);
    }

    public TimeValue(double value, TimeUnit unit) {
        this.value = value;
        if (unit == null) {
            unit = SI_UNIT;
        }
        this.unit = unit;
    }

    public double value() {
        return value;
    }

    public TimeUnit unit() {
        return unit;
    }

    public TimeValue to(TimeUnit t) {
        if (t == unit) {
            return this;
        }
        return new TimeValue(t.multiplier() / unit.multiplier() * value, t);
    }

}
