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
public enum TemperatureUnit implements ConvParamUnit<TemperatureUnit> {
    Kelvin, Celsius, Fahrenheit;

    public static TemperatureUnit SI_UNIT = Celsius;

    @Override
    public TemperatureUnit SI() {
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
    public Complex to(Complex value, TemperatureUnit to) {
        return Complex.of(to(value.toDouble(), to));
    }

    @Override
    public double to(double value, TemperatureUnit to) {
        if (this == to) {
            return value;
        }
        switch (this) {
            case Celsius: {
                switch (to) {
                    case Fahrenheit: {
                        return (9 / 5 * (value) + 32);
                    }
                    case Kelvin: {
                        return (value + 273);
                    }
                }
                break;
            }
            case Fahrenheit: {
                switch (to) {
                    case Celsius: {
                        return (5 / 9 * (value - 32));
                    }
                    case Kelvin: {
                        return (5 / 9 * (value - 32) + 273);
                    }
                }
                break;
            }
            case Kelvin: {
                switch (to) {
                    case Celsius: {
                        return (value - 273);
                    }
                    case Fahrenheit: {
                        return (9 / 5 * (value - 273) + 32);
                    }
                }
                break;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public UnitType type() {
        return UnitType.Temperature;
    }

}
