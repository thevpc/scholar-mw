/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.interop.matlab.impl;

import net.vpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.vpc.scholar.hadrumaths.symbolic.DDy;

/**
 * @author vpc
 */
public class DFunctionYFromXYToMatlabString implements ToMatlabString<DDy> {
    public DFunctionYFromXYToMatlabString() {
    }

    @Override
    public String toMatlabString(DDy o, ToMatlabStringParam... format) {
        return MatlabFactory.toMatlabString(o.getArg(), format);
    }

}
