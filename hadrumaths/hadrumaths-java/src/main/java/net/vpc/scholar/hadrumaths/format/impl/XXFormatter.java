/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.XFormat;
import net.vpc.scholar.hadrumaths.symbolic.XX;

/**
 * @author vpc
 */
public class XXFormatter implements Formatter<XX> {

    @Override
    public String format(XX o, FormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, XX o, FormatParamSet format) {
        XFormat x = format.getParam(FormatFactory.X);
        sb.append(x.getName());
        FormatFactory.appendStarredDomain(sb, o, format);
    }
}
