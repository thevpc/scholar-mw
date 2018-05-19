/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.interop.matlab.impl;

import net.vpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.vpc.scholar.hadrumaths.symbolic.DDyIntegralX;

/**
 * @author vpc
 */
public class DFunctionYIntegralXToMatlabString implements ToMatlabString<DDyIntegralX> {

    @Override
    public String toMatlabString(DDyIntegralX o, ToMatlabStringParam... format) {
        return "yintegralx(" + MatlabFactory.toMatlabString(o.getArg(), format) + ")";
    }
}
