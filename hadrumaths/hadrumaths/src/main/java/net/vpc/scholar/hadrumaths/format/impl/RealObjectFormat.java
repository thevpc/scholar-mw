/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.conv.Real;

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
