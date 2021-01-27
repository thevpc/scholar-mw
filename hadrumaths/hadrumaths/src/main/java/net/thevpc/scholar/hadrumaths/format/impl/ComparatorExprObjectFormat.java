/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond.AbstractComparatorExpr;

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
