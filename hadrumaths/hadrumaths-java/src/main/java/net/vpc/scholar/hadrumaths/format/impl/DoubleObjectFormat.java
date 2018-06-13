/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.params.DoubleObjectFormatParam;

/**
 * @author vpc
 */
public class DoubleObjectFormat implements ObjectFormat<Double> {
    public DoubleObjectFormat() {
    }

    @Override
    public String format(Double o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Double o, ObjectFormatParamSet format) {
        DoubleObjectFormatParam df = format.getParam(DoubleObjectFormatParam.class, false);
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        if (par && !(o.doubleValue() < 0)) {
            par = false;
        }
        if (par) {
            sb.append("(");
        }
        if (df != null) {
            sb.append(df.getFormat().format(o));
        } else if (Maths.isInt(o)) {
            sb.append(String.valueOf(o.intValue()));
        } else {
            sb.append(String.valueOf(o.doubleValue()));
        }
        if (par) {
            sb.append(")");
        }

    }
}
