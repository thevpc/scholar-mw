/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Pow;

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
