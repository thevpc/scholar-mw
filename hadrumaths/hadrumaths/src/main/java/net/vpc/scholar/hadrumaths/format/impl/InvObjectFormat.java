/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Inv;

/**
 * @author vpc
 */
public class InvObjectFormat implements ObjectFormat<Inv> {

    @Override
    public String format(Inv o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(Inv o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        context.append("inv(");
        context.format( o.getChild(0), format);
        context.append(")");
    }
}
