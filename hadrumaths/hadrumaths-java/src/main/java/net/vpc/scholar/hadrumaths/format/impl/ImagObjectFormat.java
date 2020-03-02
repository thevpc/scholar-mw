/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.conv.Imag;

/**
 * @author vpc
 */
public class ImagObjectFormat implements ObjectFormat<Imag> {

    @Override
    public String format(Imag o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(Imag o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
        Expr arg = o.getArg();
        context.append("imag(");
        context.format(arg, format);
        context.append(")");

    }
}
