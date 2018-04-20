/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.YFormat;
import net.vpc.scholar.hadrumaths.symbolic.YY;

/**
 * @author vpc
 */
public class YYFormatter implements Formatter<YY> {

    @Override
    public String format(YY o, FormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, YY o, FormatParamSet format) {
        YFormat y = format.getParam(FormatFactory.Y);
        sb.append(y.getName());
        FormatFactory.appendStarredDomain(sb, o, format);
    }
}
