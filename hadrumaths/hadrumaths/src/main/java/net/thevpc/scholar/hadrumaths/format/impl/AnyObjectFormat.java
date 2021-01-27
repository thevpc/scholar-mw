/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.params.DebugObjectFormatParam;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.Any;

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
