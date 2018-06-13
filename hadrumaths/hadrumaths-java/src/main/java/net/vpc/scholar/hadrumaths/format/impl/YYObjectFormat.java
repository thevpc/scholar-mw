/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.params.YObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.YY;

/**
 * @author vpc
 */
public class YYObjectFormat implements ObjectFormat<YY> {

    @Override
    public String format(YY o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, YY o, ObjectFormatParamSet format) {
        YObjectFormatParam y = format.getParam(FormatFactory.Y);
        sb.append(y.getName());
        FormatFactory.appendStarredDomain(sb, o, format);
    }
}
