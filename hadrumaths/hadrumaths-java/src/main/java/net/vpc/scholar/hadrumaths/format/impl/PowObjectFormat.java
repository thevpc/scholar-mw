/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.Pow;

/**
 * @author vpc
 */
public class PowObjectFormat implements ObjectFormat<Pow> {

    @Override
    public String format(Pow o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Pow o, ObjectFormatParamSet format) {
        format = format.add(FormatFactory.REQUIRED_PARS);
        sb.append("pow(");
        FormatFactory.format(sb, o.getFirst(), format);
        sb.append(", ");
        FormatFactory.format(sb, o.getSecond(), format);
        sb.append(")");
    }
}
