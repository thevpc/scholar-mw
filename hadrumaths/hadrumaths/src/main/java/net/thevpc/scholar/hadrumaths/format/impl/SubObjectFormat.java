/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Minus;

/**
 * @author vpc
 */
public class SubObjectFormat implements ObjectFormat<Minus> {

    @Override
    public String format(Minus o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(Minus o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        format = format.add(FormatFactory.REQUIRED_PARS);
        if (par) {
            context.append("(");
        }
        context.format( o.getChild(0), format);
        context.append("-");
//        sb.append(" - ");
        context.format( o.getChild(1), format);
        if (par) {
            context.append(")");
        }
    }
}
