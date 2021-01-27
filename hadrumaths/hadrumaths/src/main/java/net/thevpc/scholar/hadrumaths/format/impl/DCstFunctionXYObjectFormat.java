/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DefaultDoubleValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;

/**
 * @author vpc
 */
public class DCstFunctionXYObjectFormat implements ObjectFormat<DoubleValue> {
    public DCstFunctionXYObjectFormat() {
    }

    @Override
    public void format(DoubleValue o, ObjectFormatContext context) {
        context.format(new Linear(0, 0, o.toDouble(), o.getDomain()));
    }
}
