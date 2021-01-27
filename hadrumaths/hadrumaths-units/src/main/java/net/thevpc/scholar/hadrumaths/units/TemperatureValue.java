/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.units;

/**
 *
 * @author vpc
 */
public class TemperatureValue {

    private final double value;
    private final TemperatureUnit unit;

    public static TemperatureUnit SI_UNIT = TemperatureUnit.Celsius;

    public static TemperatureValue of(double value) {
        return of(value, null);
    }

    public static TemperatureValue of(double value, TemperatureUnit unit) {
        return new TemperatureValue(value, unit);
    }

    public TemperatureValue(double value, TemperatureUnit unit) {
        this.value = value;
        if (unit == null) {
            unit = SI_UNIT;
        }
        this.unit = unit;
    }

    public double value() {
        return value;
    }

    public TemperatureUnit unit() {
        return unit;
    }

    public TemperatureValue toSI() {
        return to(SI_UNIT);
    }

    public TemperatureValue to(TemperatureUnit to) {
        if (unit == to) {
            return this;
        }
        return of(unit.to(value, to), to);
    }

}
