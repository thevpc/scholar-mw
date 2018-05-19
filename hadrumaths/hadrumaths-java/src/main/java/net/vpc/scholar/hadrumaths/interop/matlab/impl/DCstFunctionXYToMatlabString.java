/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.interop.matlab.impl;

import net.vpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

/**
 * @author vpc
 */
public class DCstFunctionXYToMatlabString implements ToMatlabString<DoubleValue> {
    public DCstFunctionXYToMatlabString() {
    }

    @Override
    public String toMatlabString(DoubleValue o, ToMatlabStringParam... format) {
        return MatlabFactory.toMatlabString(new Linear(0, 0, o.getValue(), o.getDomain()), format);
    }

}
