/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.Inv;

/**
 * @author vpc
 */
public class InvObjectFormat implements ObjectFormat<Inv> {

    @Override
    public String format(Inv o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Inv o, ObjectFormatParamSet format) {
        sb.append("inv(");
        FormatFactory.format(sb, o.getExpression(), format);
        sb.append(")");
    }
}
