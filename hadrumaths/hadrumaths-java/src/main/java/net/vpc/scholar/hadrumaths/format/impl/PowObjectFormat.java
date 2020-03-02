/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Pow;

/**
 * @author vpc
 */
public class PowObjectFormat implements ObjectFormat<Pow> {

    @Override
    public String format(Pow o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(Pow o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        format = format.add(FormatFactory.REQUIRED_PARS);
        context.append("pow(");
        context.format( o.getFirst(), format);
//        sb.append(", ");
        context.append(",");
        context.format( o.getSecond(), format);
        context.append(")");
    }
}
