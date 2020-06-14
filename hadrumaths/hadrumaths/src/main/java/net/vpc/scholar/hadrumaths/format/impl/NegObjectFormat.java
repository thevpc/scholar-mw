/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.cond.Neg;

/**
 * @author vpc
 */
public class NegObjectFormat implements ObjectFormat<Neg> {

    @Override
    public String format(Neg o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(Neg o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        if (par) {
            context.append("(");
        }
        context.append("-");
        context.format(o.getChild(0), format.add(FormatFactory.REQUIRED_PARS));
        if (par) {
            context.append(")");
        }
    }
}
