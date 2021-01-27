package net.thevpc.scholar.hadruplot.util;

import net.thevpc.common.util.DoubleFormat;
import net.thevpc.common.util.PlatformUtils;

import java.text.DecimalFormat;

public class SimpleDoubleFormat implements DoubleFormat {
    public static DoubleFormat INSTANCE = new SimpleDoubleFormat();

    private DecimalFormat format;
    private DecimalFormat simpleFormat;

    public SimpleDoubleFormat() {
        format = new DecimalFormat("###0.000E0");
        format.setMaximumIntegerDigits(1);
        simpleFormat = new DecimalFormat("###0.000");
    }

    @Override
    public String formatDouble(double value) {
        if (Double.isNaN(value)) {
            return ("NaN");
        } else if(PlatformUtils.isInt(value)){
            return String.valueOf((int)value);
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
