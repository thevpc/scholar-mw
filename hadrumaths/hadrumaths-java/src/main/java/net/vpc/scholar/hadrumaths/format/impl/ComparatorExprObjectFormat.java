/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.ComparatorExpr;

/**
 * @author vpc
 */
public class ComparatorExprObjectFormat extends AbstractObjectFormat<ComparatorExpr> {

    @Override
    public void format(StringBuilder sb, ComparatorExpr o, ObjectFormatParamSet format) {
        boolean pars = format.containsParam(FormatFactory.REQUIRED_PARS);
        if (pars) {
            sb.append("(");
        }
        FormatFactory.format(sb, o.getXArgument(), format);
        sb.append(" ");
        sb.append(o.getFunctionName());
        sb.append(" ");
        FormatFactory.format(sb, o.getYArgument(), format);
        if (pars) {
            sb.append(")");
        }
    }
}
