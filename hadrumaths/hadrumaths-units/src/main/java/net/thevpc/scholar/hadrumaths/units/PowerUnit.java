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
public enum PowerUnit implements MulParamUnit, ConvParamUnit<PowerUnit> {
    W(1L), mW(1E-3), uW(1E-6), nW(1E-9), pW(1E-12);
    public static PowerUnit SI_UNIT = W;
    private double multiplier;

    private PowerUnit(double multiplier) {
        this.multiplier = multiplier;
    }

    public double multiplier() {
        return multiplier;
    }

    @Override
    public PowerUnit SI() {
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
    public Complex to(Complex value, PowerUnit to) {
        if (to == this) {
            return value;
        }
        return value.mul(multiplier()).div(to.multiplier());
    }

    @Override
    public double to(double value, PowerUnit to) {
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
