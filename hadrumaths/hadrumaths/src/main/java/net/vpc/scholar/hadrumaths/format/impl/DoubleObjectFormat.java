/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.DoubleObjectFormatParam;

/**
 * @author vpc
 */
public class DoubleObjectFormat implements ObjectFormat<Double> {
    public DoubleObjectFormat() {
    }

    @Override
    public String format(Double o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(Double o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        DoubleObjectFormatParam df = format.getParam(DoubleObjectFormatParam.class, false);
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        boolean _float = format.containsParam(FormatFactory.REQUIRED_FLOAT);
        if (par && !(o.doubleValue() < 0)) {
            par = false;
        }
        if (par) {
            context.append("(");
        }
        if (df != null) {
            context.append(df.getFormat().format(o));
        } else if (!_float && Maths.isInt(o)) {
            context.append(o.intValue());
        } else {
            context.append(o.doubleValue());
        }
        if (par) {
            context.append(")");
        }

    }
}
