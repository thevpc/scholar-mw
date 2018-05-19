/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.symbolic.Shape2D;

/**
 * @author vpc
 */
public class Shape2DFormatter implements Formatter<Shape2D> {

    @Override
    public String format(Shape2D o, FormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Shape2D o, FormatParamSet format) {
        sb.append("(");
        FormatFactory.format(sb, o.getValue(), format);
        sb.append(" * ");
        sb.append(o.getGeometry());
        sb.append(")");
    }
}
