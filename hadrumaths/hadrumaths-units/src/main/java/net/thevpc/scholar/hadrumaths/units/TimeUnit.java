/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.units;

import net.thevpc.scholar.hadrumaths.Complex;

/**
 *
 * @author vpc
 */
public enum TimeUnit implements MulParamUnit, ConvParamUnit<TimeUnit> {
    day(24 * 3600), h(3600), mn(60), s(1L),
    ms(1E-3), us(1E-6), ns(1E-9), ps(1E-12);
    public static TimeUnit SI_UNIT = s;
    private double multiplier;

    private TimeUnit(double multiplier) {
        this.multiplier = multiplier;
    }

    public double multiplier() {
        return multiplier;
    }

    @Override
    public TimeUnit SI() {
        return SI_UNIT;
    }

    @Override
    public double toSI(double value) {
        return to(value, SI());
    }

    @Override
    public Complex toSI(Complex value) {
        return to(value, SI());
    }

    @Override
    public Complex to(Complex value, TimeUnit to) {
        if (to == this) {
            return value;
        }
        return value.mul(multiplier()).div(to.multiplier());
    }

    @Override
    public double to(double value, TimeUnit to) {
        if (to == this) {
            return value;
        }
        return value * (multiplier()) / (to.multiplier());
    }

    @Override
    public UnitType type() {
        return UnitType.Capacitance;
    }

}
