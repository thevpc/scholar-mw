/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.cond.AbstractComparatorExpr;

/**
 * @author vpc
 */
public class ComparatorExprObjectFormat extends AbstractObjectFormat<AbstractComparatorExpr> {

    @Override
    public void format(AbstractComparatorExpr o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
        boolean pars = format.containsParam(FormatFactory.REQUIRED_PARS);
        if (pars) {
            context.append("(");
        }
        context.format(o.getXArgument(), format);
//        sb.append(" ");
        context.append(o.getOperatorName());
//        sb.append(" ");
        context.format(o.getYArgument(), format);
        if (pars) {
            context.append(")");
        }
    }
}
