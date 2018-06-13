/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.params.ZObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.ZZ;

/**
 * @author vpc
 */
public class ZZObjectFormat implements ObjectFormat<ZZ> {

    @Override
    public String format(ZZ o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, ZZ o, ObjectFormatParamSet format) {
        ZObjectFormatParam z = format.getParam(FormatFactory.Z);
        sb.append(z.getName());
        FormatFactory.appendStarredDomain(sb, o, format);
    }
}
