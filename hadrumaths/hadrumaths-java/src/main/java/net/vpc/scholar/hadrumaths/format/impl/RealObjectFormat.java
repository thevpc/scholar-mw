/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.Real;

/**
 * @author vpc
 */
public class RealObjectFormat implements ObjectFormat<Real> {

    @Override
    public String format(Real o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();

    }

    @Override
    public void format(StringBuilder sb, Real o, ObjectFormatParamSet format) {
        Expr arg = o.getArg();
        sb.append("real(");
        FormatFactory.format(sb, arg, format);
        sb.append(")");
    }
}
