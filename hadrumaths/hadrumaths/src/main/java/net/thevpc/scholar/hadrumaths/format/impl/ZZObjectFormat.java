/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.params.ZObjectFormatParam;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.ZZ;

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
