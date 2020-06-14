/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;

import java.util.List;

/**
 * @author vpc
 */
public class PlusObjectFormat implements ObjectFormat<Plus> {

    @Override
    public String format(Plus o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(Plus o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        format = format.add(FormatFactory.REQUIRED_PARS);
        List<Expr> segments = o.getChildren();
        int size = segments.size();
        if (size > 1 && par) {
            context.append("(");
        }
        for (int i = 0; i < size; i++) {
            Expr e = segments.get(i);
            if (i > 0) {
//                sb.append(" + ");
                context.append("+");
            }
            context.format(e, format.add(FormatFactory.GATE_DOMAIN));
        }

        if (size > 1 && par) {
            context.append(")");
        }
    }

}
