/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DDzIntegralXY;

/**
 * @author vpc
 */
public class DDzIntegralXYObjectFormat extends AbstractObjectFormat<DDzIntegralXY> {

    @Override
    public void format(DDzIntegralXY o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
        context.append("DDzIntegralXY(");
        context.format(o.getArg(), format);
        context.append(",");
        context.format(o.getIntegral().toString(), format);
        context.append(",");
        context.format(o.getX0(), format);
        context.append("->");
        context.format(o.getX1(), format);
        context.append(",");
        context.format(o.getY0(), format);
        context.append("->");
        context.format(o.getY1(), format);
        context.append(")");
    }

}
