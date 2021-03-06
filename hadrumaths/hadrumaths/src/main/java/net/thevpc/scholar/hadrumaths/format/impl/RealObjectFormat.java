/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.conv.Real;

/**
 * @author vpc
 */
public class RealObjectFormat implements ObjectFormat<Real> {

    @Override
    public String format(Real o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();

    }

    @Override
    public void format(Real o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        Expr arg = o.getArg();
        context.append("real(");
        context.format(arg, format);
        context.append(")");
    }
}
