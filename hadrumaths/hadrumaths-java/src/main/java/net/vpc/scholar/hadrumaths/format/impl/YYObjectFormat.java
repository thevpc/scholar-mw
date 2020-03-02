/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.YObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.double2double.YY;

/**
 * @author vpc
 */
public class YYObjectFormat implements ObjectFormat<YY> {

    @Override
    public String format(YY o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(YY o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        YObjectFormatParam y = format.getParam(FormatFactory.Y);
        context.append(y.getName());
        FormatFactory.appendStarredDomain(context, o, format);
    }
}
