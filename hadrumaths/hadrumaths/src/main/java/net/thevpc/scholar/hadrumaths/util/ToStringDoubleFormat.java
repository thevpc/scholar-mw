package net.thevpc.scholar.hadrumaths.util;

import net.thevpc.common.util.DoubleFormat;

public class ToStringDoubleFormat implements DoubleFormat {
    @Override
    public String formatDouble(double value) {
        return String.valueOf(value);
    }
}
