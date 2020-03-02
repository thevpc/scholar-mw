/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Shape2D;

/**
 * @author vpc
 */
public class Shape2DObjectFormat implements ObjectFormat<Shape2D> {

    @Override
    public String format(Shape2D o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(Shape2D o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
        context.append("(");
        context.format(o.getValue(), format);
//        sb.append(" * ");
        context.append("*");
        context.append(o.getGeometry());
        context.append(")");
    }
}
