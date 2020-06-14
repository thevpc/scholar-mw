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
public enum ResistanceUnit implements MulParamUnit, ConvParamUnit<ResistanceUnit> {
    OHM(1L), mOHM(1E-3), uOHM(1E-6), nOHM(1E-9), pOHM(1E-12);
    public static ResistanceUnit SI_UNIT = OHM;
    private double multiplier;

    private ResistanceUnit(double multiplier) {
        this.multiplier = multiplier;
    }

    public double multiplier() {
        return multiplier;
    }

    @Override
    public ResistanceUnit SI() {
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
    public Complex to(Complex value, ResistanceUnit to) {
        if (to == this) {
            return value;
        }
        return value.mul(multiplier()).div(to.multiplier());
    }

    @Override
    public double to(double value, ResistanceUnit to) {
        if (to == this) {
            return value;
        }
        return value * (multiplier()) / (to.multiplier());
    }

    @Override
    public UnitType type() {
        return UnitType.Resistance;
    }

}
