package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.DoubleFormatter;

import java.text.DecimalFormat;

public class PercentDoubleFormatter implements DoubleFormatter {
    public static DoubleFormatter INSTANCE = new PercentDoubleFormatter();

    DecimalFormat format;
    DecimalFormat simpleFormat;

    public PercentDoubleFormatter() {
        format = new DecimalFormat("00.00%");
        format.setMaximumIntegerDigits(1);
        simpleFormat = new DecimalFormat("00.00%");
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
