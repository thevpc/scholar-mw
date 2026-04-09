/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.conv.Real;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG;

/**
 * @author vpc
 */
public class RWGFormat implements ObjectFormat<RWG> {

    @Override
    public String format(RWG o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();

    }

    @Override
    public void format(RWG o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        context.append("rwg(");
        context.append(o.getTriangle1().toElement());
        context.append(",");
        context.append(o.getTriangle2().toElement());
        context.append(")");
    }
}
