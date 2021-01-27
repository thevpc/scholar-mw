/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.interop.matlab.impl;

import net.thevpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParamArray;
import net.thevpc.scholar.hadrumaths.interop.matlab.params.MatlabDoubleFormat;
import net.thevpc.scholar.hadrumaths.interop.matlab.params.MatlabVectorizeFormat;
import net.thevpc.scholar.hadrumaths.interop.matlab.params.MatlabXFormat;
import net.thevpc.scholar.hadrumaths.interop.matlab.params.MatlabYFormat;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;

/**
 * @author vpc
 */
public class DLinearFunctionXYToMatlabString implements ToMatlabString<Linear> {

    @Override
    public String toMatlabString(Linear o, ToMatlabStringParam... format) {
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        ToMatlabStringParamArray formatArray = new ToMatlabStringParamArray(format);
        MatlabXFormat x = formatArray.getParam(MatlabFactory.X);
        MatlabYFormat y = formatArray.getParam(MatlabFactory.Y);
        MatlabDoubleFormat df = (MatlabDoubleFormat) formatArray.getParam(MatlabDoubleFormat.class, false);
        String mul = formatArray.getParam(MatlabVectorizeFormat.class, false) == null ? " * " : " .* ";

        StringBuilder sb = new StringBuilder();
        if (a == 0 && b == 0 && c == 0) {
            if (df != null) {
                return (df.getFormat().format(0));
            } else {
                return String.valueOf(0);
            }
        } else {
            String v = MatlabFactory.toParamString(a, df, false, true);
            if (v.length() > 0) {
                sb.append(v).append(mul).append(x.getName());
            }
            v = MatlabFactory.toParamString(b, df, sb.length() > 0, true);
            if (v.length() > 0) {
                sb.append(v).append(mul).append(y.getName());
            }
            v = MatlabFactory.toParamString(c, df, sb.length() > 0, true);
            if (v.length() > 0) {
                sb.append(v);
            }
        }
        String s = MatlabFactory.toMatlabString(o.getDomain(), format);
        return s.length() > 0 ? (s + mul + "(" + sb.toString() + ")") : sb.toString();
    }


}
