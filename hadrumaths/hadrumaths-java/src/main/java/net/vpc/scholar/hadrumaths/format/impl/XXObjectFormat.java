/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.XObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.XX;

/**
 * @author vpc
 */
public class XXObjectFormat implements ObjectFormat<XX> {

    @Override
    public String format(XX o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, XX o, ObjectFormatParamSet format) {
        XObjectFormatParam x = format.getParam(FormatFactory.X);
        sb.append(x.getName());
        FormatFactory.appendStarredDomain(sb, o, format);
    }
}
