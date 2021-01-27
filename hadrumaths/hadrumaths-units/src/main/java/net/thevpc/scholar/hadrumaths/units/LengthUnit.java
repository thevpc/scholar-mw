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
public enum LengthUnit implements MulParamUnit, ConvParamUnit<LengthUnit> {
    km(100L), m(1L), dm(1E-1), cm(1E-2), mm(1E-3), um(1E-6), nm(1E-9), pm(1E-12);
    public static LengthUnit SI_UNIT = m;
    double multiplier;

    private LengthUnit(double multiplier) {
        this.multiplier = multiplier;
    }

    public double multiplier() {
        return multiplier;
    }

    @Override
    public LengthUnit SI() {
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
    public Complex to(Complex value, LengthUnit to) {
        if (to == this) {
            return value;
        }
        return value.mul(multiplier()).div(to.multiplier());
    }

    @Override
    public double to(double value, LengthUnit to) {
        if (to == this) {
            return value;
        }
        return value * (multiplier()) / (to.multiplier());
    }

    @Override
    public UnitType type() {
        return UnitType.Length;
    }
    
}
