/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.Param;

/**
 * @author vpc
 */
public class ParamObjectFormat implements ObjectFormat<Param> {
    public ParamObjectFormat() {
    }

    @Override
    public void format(Param o, ObjectFormatContext context) {
        context.append(o.getName());
    }

    @Override
    public String format(Param o, ObjectFormatParamSet format, ObjectFormatContext context) {
        return o.getName();
    }
}
