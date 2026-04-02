package net.thevpc.scholar.hadruplot.util;

import net.thevpc.nuts.text.NText;
import net.thevpc.nuts.text.NTextFormat;
import net.thevpc.nuts.util.NLiteral;

import java.text.DecimalFormat;

public class SimpleDoubleFormat implements NTextFormat<Number> {
    public static NTextFormat<Number> INSTANCE = new SimpleDoubleFormat();

    private DecimalFormat format;
    private DecimalFormat simpleFormat;

    public SimpleDoubleFormat() {
        format = new DecimalFormat("###0.000E0");
        format.setMaximumIntegerDigits(1);
        simpleFormat = new DecimalFormat("###0.000");
    }

    @Override
    public NText toText(Number object) {
        return NText.of(formatDouble(object.doubleValue()));
    }

    public String formatDouble(double value) {
        if (Double.isNaN(value)) {
            return ("NaN");
        } else if(NLiteral.of(value).asInt().isPresent()){
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
