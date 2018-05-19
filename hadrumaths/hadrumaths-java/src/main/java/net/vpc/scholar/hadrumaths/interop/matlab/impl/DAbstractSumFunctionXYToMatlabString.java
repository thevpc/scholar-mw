/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.interop.matlab.impl;

import net.vpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.vpc.scholar.hadrumaths.symbolic.DDxyAbstractSum;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * @author vpc
 */
public class DAbstractSumFunctionXYToMatlabString implements ToMatlabString<DDxyAbstractSum> {

    @Override
    public String toMatlabString(DDxyAbstractSum o, ToMatlabStringParam... format) {
        DoubleToDouble[] segments = o.getSegments();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segments.length; i++) {
            if (i > 0) {
                sb.append(" + ");
            }
            sb.append("(");
            sb.append(MatlabFactory.toMatlabString(segments[i], format));
            sb.append(")");
        }
        return sb.toString();
    }


}
