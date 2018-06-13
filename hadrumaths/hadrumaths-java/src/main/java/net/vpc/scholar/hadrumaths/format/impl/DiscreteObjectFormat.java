/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.symbolic.Discrete;

/**
 * @author vpc
 */
public class DiscreteObjectFormat implements ObjectFormat<Discrete> {

    @Override
    public String format(Discrete o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Discrete o, ObjectFormatParamSet format) {
        sb.append("Discrete(");
        FormatFactory.format(sb, o.getDomain(), format);
        sb.append(",");
        sb.append(o.getCountX()).append(":").append(o.getCountY()).append(":").append(o.getCountZ());
        sb.append(")");
    }
}
