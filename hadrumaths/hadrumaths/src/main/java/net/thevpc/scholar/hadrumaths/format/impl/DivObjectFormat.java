/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Div;

/**
 * @author vpc
 */
public class DivObjectFormat implements ObjectFormat<Div> {


    @Override
    public void format(Div o, ObjectFormatContext context) {
        boolean par = context.containsParam(FormatFactory.REQUIRED_PARS);
        ObjectFormatParamSet format2 = context.getParams().add(FormatFactory.REQUIRED_PARS);
        if (par) {
            context.append("(");
        }
        context.format(o.getFirst(), format2);
//        context.append(" / ");
        context.append("/");
        context.format(o.getSecond(), format2);
        if (par) {
            context.append(")");
        }
    }
}
