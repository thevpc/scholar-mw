package net.thevpc.scholar.hadrumaths.format.params;

import net.thevpc.scholar.hadrumaths.format.ObjectFormatParam;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 30 juil. 2005
 * Time: 13:52:01
 * To change this template use File | Settings | File Templates.
 */
public class DoubleObjectFormatParam implements ObjectFormatParam {
    private final NumberFormat format;

    public DoubleObjectFormatParam(int precision) {
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

    public DoubleObjectFormatParam(NumberFormat format) {
        this.format = format;
    }

    public NumberFormat getFormat() {
        return format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleObjectFormatParam that = (DoubleObjectFormatParam) o;
        return Objects.equals(format, that.format);
    }

    @Override
    public int hashCode() {
        return format.hashCode();
    }
}
