/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.units;

import net.vpc.scholar.hadrumaths.Complex;

/**
 * Henry
 * @author vpc
 */
public enum InductanceUnit implements MulParamUnit, ConvParamUnit<InductanceUnit> {
    H(1L), mH(1E-3), uH(1E-6), nH(1E-9), pH(1E-12);
    public static InductanceUnit SI_UNIT = H;
    private double multiplier;

    private InductanceUnit(double multiplier) {
        this.multiplier = multiplier;
    }

    public double multiplier() {
        return multiplier;
    }

    @Override
    public InductanceUnit SI() {
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
    public Complex to(Complex value, InductanceUnit to) {
        if (to == this) {
            return value;
        }
        return value.mul(multiplier()).div(to.multiplier());
    }

    @Override
    public double to(double value, InductanceUnit to) {
        if (to == this) {
            return value;
        }
        return value * (multiplier()) / (to.multiplier());
    }

    @Override
    public UnitType type() {
        return UnitType.Inductance;
    }
    
}
