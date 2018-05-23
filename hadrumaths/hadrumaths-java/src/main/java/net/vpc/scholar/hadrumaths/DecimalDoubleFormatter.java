package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.DoubleFormatter;

import java.text.DecimalFormat;

class DecimalDoubleFormatter implements DoubleFormatter {
    private final DecimalFormat d;
    private final String subFormat;

    public DecimalDoubleFormatter(String subFormat) {
        this.subFormat = subFormat;
        d = new DecimalFormat(subFormat);
    }

    @Override
    public String formatDouble(double value) {
        return d.format(value);
    }
}
