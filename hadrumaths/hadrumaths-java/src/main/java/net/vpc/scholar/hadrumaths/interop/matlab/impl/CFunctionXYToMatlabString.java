/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.interop.matlab.impl;

import net.vpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParamArray;
import net.vpc.scholar.hadrumaths.interop.matlab.params.MatlabVectorizeFormat;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * @author vpc
 */
public class CFunctionXYToMatlabString implements ToMatlabString<DoubleToComplex> {

    @Override
    public String toMatlabString(DoubleToComplex o, ToMatlabStringParam... format) {
        DoubleToDouble real = o.getRealDD();
        DoubleToDouble imag = o.getImagDD();
        //DomainXY domain=o.getDomain();
        ToMatlabStringParamArray formatArray = new ToMatlabStringParamArray(format);
        String mul = formatArray.getParam(MatlabVectorizeFormat.class, false) == null ? " * " : " .* ";
        StringBuilder sb = new StringBuilder();
        if (!real.getDomain().isEmpty()) {
            sb.append(MatlabFactory.toMatlabString(real, format));
        }
        if (!imag.getDomain().isEmpty()) {
            String s = MatlabFactory.toMatlabString(imag, format);
            if (!s.startsWith("-") && !real.getDomain().isEmpty()) {
                sb.append("+");
            }
            sb.append(s);
            sb.append(mul).append(" i");
        }
        return sb.toString();
    }
}
