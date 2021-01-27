/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.units;

import net.thevpc.scholar.hadrumaths.Complex;

/**
 * Siemens
 * @author vpc
 */
public enum ConductanceUnit implements MulParamUnit, ConvParamUnit<ConductanceUnit> {
    S(1L), mS(1E-3), uS(1E-6), nS(1E-9), pS(1E-12);
    public static ConductanceUnit SI_UNIT = S;
    private double multiplier;

    private ConductanceUnit(double multiplier) {
        this.multiplier = multiplier;
    }

    public double multiplier() {
        return multiplier;
    }

    @Override
    public ConductanceUnit SI() {
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
    public Complex to(Complex value, ConductanceUnit to) {
        if (to == this) {
            return value;
        }
        return value.mul(multiplier()).div(to.multiplier());
    }

    @Override
    public double to(double value, ConductanceUnit to) {
        if (to == this) {
            return value;
        }
        return value * (multiplier()) / (to.multiplier());
    }

    @Override
    public UnitType type() {
        return UnitType.Conductance;
    }
    
}
