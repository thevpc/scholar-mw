/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.ZFormat;
import net.vpc.scholar.hadrumaths.symbolic.ZZ;

/**
 * @author vpc
 */
public class ZZFormatter implements Formatter<ZZ> {

    @Override
    public String format(ZZ o, FormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, ZZ o, FormatParamSet format) {
        ZFormat z = format.getParam(FormatFactory.Z);
        sb.append(z.getName());
        FormatFactory.appendStarredDomain(sb, o, format);
    }
}
