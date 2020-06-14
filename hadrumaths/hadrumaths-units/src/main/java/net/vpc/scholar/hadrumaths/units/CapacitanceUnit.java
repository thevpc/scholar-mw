/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.units;

import net.vpc.scholar.hadrumaths.Complex;

/**
 * Farade
 *
 * @author vpc
 */
public enum CapacitanceUnit implements MulParamUnit, ConvParamUnit<CapacitanceUnit> {
    F(1L), mF(1E-3), uF(1E-6), nF(1E-9), pF(1E-12);
    public static CapacitanceUnit SI_UNIT = F;
    private double multiplier;

    private CapacitanceUnit(double multiplier) {
        this.multiplier = multiplier;
    }

    public double multiplier() {
        return multiplier;
    }

    @Override
    public CapacitanceUnit SI() {
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
    public Complex to(Complex value, CapacitanceUnit to) {
        if (to == this) {
            return value;
        }
        return value.mul(multiplier()).div(to.multiplier());
    }

    @Override
    public double to(double value, CapacitanceUnit to) {
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
