/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.DoubleObjectFormatParam;

/**
 * @author vpc
 */
public class NumberObjectFormat implements ObjectFormat<Number> {
    public NumberObjectFormat() {
    }

    @Override
    public String format(Number o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(Number o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        DoubleObjectFormatParam df = format.getParam(DoubleObjectFormatParam.class, false);
        String v = df == null ? o.toString() : df.getFormat().format(o.doubleValue());
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        if (!(v.startsWith("-") || v.startsWith("+"))) {
            par = false;
        }
        if (par) {
            context.append("(");
        }
        context.append(v);
        if (par) {
            context.append(")");
        }
    }
}
