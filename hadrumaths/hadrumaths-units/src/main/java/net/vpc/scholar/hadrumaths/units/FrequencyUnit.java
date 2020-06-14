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
public enum FrequencyUnit implements MulParamUnit, ConvParamUnit<FrequencyUnit> {
    Hz(1L), KHz(1000L), MHz(1_000_000L), GHz(1_000_000_000L), TH(1_000_000_000_000L);
    public static FrequencyUnit SI_UNIT = Hz;
    double multiplier;

    private FrequencyUnit(long multiplier) {
        this.multiplier = multiplier;
    }

    public double multiplier() {
        return multiplier;
    }

    @Override
    public FrequencyUnit SI() {
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
    public Complex to(Complex value, FrequencyUnit to) {
        if (to == this) {
            return value;
        }
        return value.mul(multiplier()).div(to.multiplier());
    }

    @Override
    public double to(double value, FrequencyUnit to) {
        if (to == this) {
            return value;
        }
        return value * (multiplier()) / (to.multiplier());
    }

    @Override
    public UnitType type() {
        return UnitType.Frequency;
    }
    
}
