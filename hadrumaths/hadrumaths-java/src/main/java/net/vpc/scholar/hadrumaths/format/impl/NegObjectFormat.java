/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.Neg;

/**
 * @author vpc
 */
public class NegObjectFormat implements ObjectFormat<Neg> {

    @Override
    public String format(Neg o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Neg o, ObjectFormatParamSet format) {
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        if (par) {
            sb.append("(");
        }
        sb.append("-");
        FormatFactory.format(sb, o.getExpression(), format.add(FormatFactory.REQUIRED_PARS));
        if (par) {
            sb.append(")");
        }
    }
}
