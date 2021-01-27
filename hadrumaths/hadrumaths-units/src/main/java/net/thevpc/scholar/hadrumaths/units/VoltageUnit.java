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
public enum VoltageUnit implements MulParamUnit, ConvParamUnit<VoltageUnit> {
    V(1L), mV(1E-3), uV(1E-6), nV(1E-9), pV(1E-12);
    public static VoltageUnit SI_UNIT = V;
    private double multiplier;

    private VoltageUnit(double multiplier) {
        this.multiplier = multiplier;
    }

    public double multiplier() {
        return multiplier;
    }

    @Override
    public VoltageUnit SI() {
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
    public Complex to(Complex value, VoltageUnit to) {
        if (to == this) {
            return value;
        }
        return value.mul(multiplier()).div(to.multiplier());
    }

    @Override
    public double to(double value, VoltageUnit to) {
        if (to == this) {
            return value;
        }
        return value * (multiplier()) / (to.multiplier());
    }

    @Override
    public UnitType type() {
        return UnitType.Voltage;
    }
    
}
