package net.vpc.scholar.hadrumaths.util;

import net.vpc.common.util.DoubleFormatter;

import java.text.DecimalFormat;

public class SimpleDoubleFormatter implements DoubleFormatter {
    public static DoubleFormatter INSTANCE = new SimpleDoubleFormatter();

    private DecimalFormat format;
    private DecimalFormat simpleFormat;

    public SimpleDoubleFormatter() {
        format = new DecimalFormat("###0.000E0");
        format.setMaximumIntegerDigits(1);
        simpleFormat = new DecimalFormat("###0.000");
    }

    @Override
    public String formatDouble(double value) {
        if (Double.isNaN(value)) {
            return ("NaN");
        } else {
            DecimalFormat f = format;
            if ((value >= 1E-3 && value <= 1E4) || (value <= -1E-3 && value >= -1E4)) {
                f = simpleFormat;
            }
            String v = f == null ? String.valueOf(value) : f.format(value);
            if (v.endsWith("E0")) {
                v = v.substring(0, v.length() - 2);
            }
            return v;
        }
    }
}
