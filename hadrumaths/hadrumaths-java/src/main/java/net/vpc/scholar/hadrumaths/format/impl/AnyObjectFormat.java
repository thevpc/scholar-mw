/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.DebugObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.Any;

/**
 * @author vpc
 */
public class AnyObjectFormat implements ObjectFormat<Any> {

    @Override
    public void format(Any o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        DebugObjectFormatParam d = format.getParam(DebugObjectFormatParam.class, false);
        if (d == null) {
            context.format( o.getReference(), format);
        } else {
            context.append("any(");
            context.format( o.getReference(), format);
            context.append(")");
        }
    }
}
