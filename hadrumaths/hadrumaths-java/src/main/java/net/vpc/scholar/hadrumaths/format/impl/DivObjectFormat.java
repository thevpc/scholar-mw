/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.symbolic.Div;

/**
 * @author vpc
 */
public class DivObjectFormat implements ObjectFormat<Div> {

    @Override
    public String format(Div o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Div o, ObjectFormatParamSet format) {
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        format = format.add(FormatFactory.REQUIRED_PARS);
        if (par) {
            sb.append("(");
        }
        FormatFactory.format(sb, o.getFirst(), format);
        sb.append(" / ");
        FormatFactory.format(sb, o.getSecond(), format);
        if (par) {
            sb.append(")");
        }
    }
}
