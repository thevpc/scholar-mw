/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.params.XObjectFormatParam;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.XX;

/**
 * @author vpc
 */
public class XXObjectFormat implements ObjectFormat<XX> {

    @Override
    public String format(XX o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(XX o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        XObjectFormatParam x = format.getParam(FormatFactory.X);
        context.append(x.getName());
        FormatFactory.appendStarredDomain(context, o, format);
    }
}
