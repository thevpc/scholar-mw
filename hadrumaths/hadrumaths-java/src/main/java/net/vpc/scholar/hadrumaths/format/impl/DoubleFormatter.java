/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.DoubleFormat;
import net.vpc.scholar.hadrumaths.format.params.RequireParenthesesFormat;

/**
 * @author vpc
 */
public class DoubleFormatter implements Formatter<Double> {
    public DoubleFormatter() {
    }

    @Override
    public String format(Double o, FormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Double o, FormatParamSet format) {
        DoubleFormat df = format.getParam(DoubleFormat.class, false);
        boolean par = format.containsParam(RequireParenthesesFormat.INSTANCE);
        if (par && !(o.doubleValue() < 0)) {
            par = false;
        }
        if (par) {
            sb.append("(");
        }
        if (df != null) {
            sb.append(df.getFormat().format(o));
        }else if (Maths.isInt(o)) {
            sb.append(String.valueOf(o.intValue()));
        }else{
            sb.append(String.valueOf(o.doubleValue()));
        }
        if (par) {
            sb.append(")");
        }

    }
}
