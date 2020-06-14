/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.units;

import net.vpc.scholar.hadrumaths.Complex;

/**
 *
 * @author vpc
 */
public enum CurrentUnit implements MulParamUnit, ConvParamUnit<CurrentUnit> {
    A(1L), mA(1E-3), uA(1E-6), nA(1E-9), pA(1E-12);
    public static CurrentUnit SI_UNIT = A;
    private double multiplier;

    private CurrentUnit(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public double multiplier() {
        return multiplier;
    }

    @Override
    public CurrentUnit SI() {
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
    public Complex to(Complex value, CurrentUnit to) {
        if (to == this) {
            return value;
        }
        return value.mul(multiplier()).div(to.multiplier());
    }

    @Override
    public double to(double value, CurrentUnit to) {
        if (to == this) {
            return value;
        }
        return value * (multiplier()) / (to.multiplier());
    }

    @Override
    public UnitType type() {
        return UnitType.Current;
    }
    
}
