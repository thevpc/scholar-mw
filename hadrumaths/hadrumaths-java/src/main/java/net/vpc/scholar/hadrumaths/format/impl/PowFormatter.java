/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.symbolic.Pow;

/**
 * @author vpc
 */
public class PowFormatter implements Formatter<Pow> {

    @Override
    public String format(Pow o, FormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Pow o, FormatParamSet format) {
        format = format.add(FormatFactory.REQUIRED_PARS);
        sb.append("pow(");
        FormatFactory.format(sb, o.getFirst(), format);
        sb.append(", ");
        FormatFactory.format(sb, o.getSecond(), format);
        sb.append(")");
    }
}
