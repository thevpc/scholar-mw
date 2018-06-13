/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.params.DebugObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.Any;

/**
 * @author vpc
 */
public class AnyObjectFormat implements ObjectFormat<Any> {

    @Override
    public void format(StringBuilder sb, Any o, ObjectFormatParamSet format) {
        DebugObjectFormatParam d = format.getParam(DebugObjectFormatParam.class, false);
        if (d == null) {
            FormatFactory.format(sb, o.getObject(), format);
        } else {
            sb.append("any(");
            FormatFactory.format(sb, o.getObject(), format);
            sb.append(")");
        }
    }

    @Override
    public String format(Any o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }
}
