package net.vpc.scholar.hadrumaths.util;

import net.vpc.common.util.DoubleFormat;

public class ToStringDoubleFormat implements DoubleFormat {
    @Override
    public String formatDouble(double value) {
        return String.valueOf(value);
    }
}
