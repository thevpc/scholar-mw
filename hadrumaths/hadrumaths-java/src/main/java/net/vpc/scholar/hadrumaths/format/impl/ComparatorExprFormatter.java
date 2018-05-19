/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.ComparatorExpr;

/**
 * @author vpc
 */
public class ComparatorExprFormatter extends AbstractFormatter<ComparatorExpr> {

    @Override
    public void format(StringBuilder sb, ComparatorExpr o, FormatParamSet format) {
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
