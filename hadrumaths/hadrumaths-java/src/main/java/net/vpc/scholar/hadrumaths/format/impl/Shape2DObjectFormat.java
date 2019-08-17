/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.Shape2D;

/**
 * @author vpc
 */
public class Shape2DObjectFormat implements ObjectFormat<Shape2D> {

    @Override
    public String format(Shape2D o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Shape2D o, ObjectFormatParamSet format) {
        sb.append("(");
        FormatFactory.format(sb, o.getValue(), format);
        sb.append(" * ");
        sb.append(o.getGeometry());
        sb.append(")");
    }
}
