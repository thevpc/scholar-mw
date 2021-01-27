/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.conv.Imag;

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
