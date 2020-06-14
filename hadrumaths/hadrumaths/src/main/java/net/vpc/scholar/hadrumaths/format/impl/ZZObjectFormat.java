/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.ZObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.double2double.ZZ;

/**
 * @author vpc
 */
public class ZZObjectFormat implements ObjectFormat<ZZ> {

    @Override
    public String format(ZZ o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(ZZ o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        ZObjectFormatParam z = format.getParam(FormatFactory.Z);
        context.append(z.getName());
        FormatFactory.appendStarredDomain(context, o, format);
    }
}
