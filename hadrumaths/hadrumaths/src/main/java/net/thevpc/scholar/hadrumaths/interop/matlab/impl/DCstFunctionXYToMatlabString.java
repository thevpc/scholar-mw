/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.interop.matlab.impl;

import net.thevpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DefaultDoubleValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;

/**
 * @author vpc
 */
public class DCstFunctionXYToMatlabString implements ToMatlabString<DoubleValue> {
    public DCstFunctionXYToMatlabString() {
    }

    @Override
    public String toMatlabString(DoubleValue o, ToMatlabStringParam... format) {
        return MatlabFactory.toMatlabString(new Linear(0, 0, o.toDouble(), o.getDomain()), format);
    }

}
