/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.interop.matlab.impl;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParamArray;
import net.thevpc.scholar.hadrumaths.interop.matlab.params.MatlabVectorizeFormat;
import net.thevpc.scholar.hadrumaths.interop.matlab.params.MatlabXFormat;
import net.thevpc.scholar.hadrumaths.interop.matlab.params.MatlabYFormat;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;

/**
 * @author vpc
 */
public class DCosCosFunctionXYToMatlabString implements ToMatlabString<CosXCosY> {

    @Override
    public String toMatlabString(CosXCosY o, ToMatlabStringParam... format) {
        double amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        double d = o.getD();
        Domain domain = o.getDomain();
        ToMatlabStringParamArray formatArray = new ToMatlabStringParamArray(format);
        String mul = formatArray.getParam(MatlabVectorizeFormat.class, false) == null ? " * " : " .* ";
        if (amp == 0) {
            return "0";
        } else {
            StringBuilder sb = new StringBuilder(String.valueOf(amp));
            if (a != 0 || b != 0 || c != 0 || d != 0) {
                sb.append(mul);
            }
            if (a != 0 || b != 0) {
                sb.append("cos(");
                sb.append(MatlabFactory.toMatlabString(new Linear(a, 0, b, domain), formatArray.clone().set(MatlabFactory.NO_DOMAIN).toArray()));
                sb.append(")");
                if (c != 0 || d != 0) {
                    sb.append(mul);
                }
            }
            if (c != 0 || d != 0) {
                sb.append("cos(");
                sb.append(MatlabFactory.toMatlabString(new Linear(0, c, d, domain),
                        formatArray.clone().set(new MatlabXFormat(formatArray.getParam(MatlabFactory.Y).getName())).set(MatlabFactory.NO_DOMAIN).toArray()));
                sb.append(")");
            }
            String s = MatlabFactory.toMatlabString(domain, format);
            return s.length() > 0 ? (s + mul + sb.toString()) : sb.toString();
        }
    }
}
