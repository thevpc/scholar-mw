package net.vpc.scholar.hadrumaths.interop.matlab.params;

import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 30 juil. 2005
 * Time: 13:52:01
 * To change this template use File | Settings | File Templates.
 */
public class MatlabDoubleFormat implements ToMatlabStringParam {
    private final NumberFormat format;

    public MatlabDoubleFormat(int precision) {
        StringBuilder sb = new StringBuilder("0.");
        if (precision <= 0) {
            sb.append("#");
        } else {
            for (int i = 0; i < precision; i++) {
                sb.append('#');
            }
        }
        DecimalFormat f = new DecimalFormat(sb.toString());
        DecimalFormatSymbols symbols = f.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        f.setDecimalFormatSymbols(symbols);
        this.format = f;
    }

    public MatlabDoubleFormat(NumberFormat format) {
        this.format = format;
    }

    public NumberFormat getFormat() {
        return format;
    }
}
