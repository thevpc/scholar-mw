package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.plot.ComplexAsDouble;

public class HadrumathsUtils {
    public static ComplexAsDouble notNullValue(ComplexAsDouble v) {
        if (v == null) {
            return ComplexAsDouble.ABS;
        }
        return v;
    }
}
