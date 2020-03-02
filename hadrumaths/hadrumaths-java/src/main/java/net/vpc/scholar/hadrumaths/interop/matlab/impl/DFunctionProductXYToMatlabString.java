/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.interop.matlab.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParamArray;
import net.vpc.scholar.hadrumaths.interop.matlab.params.MatlabVectorizeFormat;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;

/**
 * @author vpc
 */
public class DFunctionProductXYToMatlabString implements ToMatlabString<Mul> {

    public DFunctionProductXYToMatlabString() {
    }

    @Override
    public String toMatlabString(Mul o, ToMatlabStringParam... format) {
        ToMatlabStringParamArray formatArray = new ToMatlabStringParamArray(format);
        String mul = formatArray.getParam(MatlabVectorizeFormat.class, false) == null ? " * " : " .* ";
        StringBuilder sb = new StringBuilder();
        for (Expr expression : o.getChildren()) {
            if (sb.length() > 0) {
                sb.append(mul);
            }
            sb.append("(").append(MatlabFactory.toMatlabString(expression, format)).append(")");
        }
        return sb.toString();

    }
}
