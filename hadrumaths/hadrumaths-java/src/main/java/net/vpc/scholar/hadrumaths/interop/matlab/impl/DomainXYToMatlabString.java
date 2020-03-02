/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.interop.matlab.impl;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParamArray;
import net.vpc.scholar.hadrumaths.interop.matlab.params.MatlabDomainFormat;
import net.vpc.scholar.hadrumaths.interop.matlab.params.MatlabDoubleFormat;

/**
 * @author vpc
 */
public class DomainXYToMatlabString implements ToMatlabString<Domain> {
    public DomainXYToMatlabString() {
    }

    @Override
    public String toMatlabString(Domain o, ToMatlabStringParam... format) {
        ToMatlabStringParamArray formatArray = new ToMatlabStringParamArray(format);
        MatlabDomainFormat d = formatArray.getParam(MatlabFactory.GATE_DOMAIN);
        MatlabDoubleFormat df = (MatlabDoubleFormat) formatArray.getParam(MatlabDoubleFormat.class, false);
        String xf = formatArray.getParam(MatlabFactory.X).getName();
        String yf = formatArray.getParam(MatlabFactory.Y).getName();
        String zf = formatArray.getParam(MatlabFactory.Z).getName();
        switch (d.getType()) {
            case GATE: {
                if (o.xmin() == Double.NEGATIVE_INFINITY && o.xmax() == Double.NEGATIVE_INFINITY && o.ymin() == Double.NEGATIVE_INFINITY && o.ymax() == Double.NEGATIVE_INFINITY) {
                    return "";
                } else if (o.xmin() == Double.NEGATIVE_INFINITY && o.xmax() == Double.NEGATIVE_INFINITY) {
                    return df == null ?
                            ("gate(" + yf + "," + o.ymin() + "," + o.ymax() + ")")
                            : ("gate(" + yf + "," + df.getFormat().format(o.ymin()) + "," + df.getFormat().format(o.ymax()) + ")")
                            ;

                } else if (o.xmin() == Double.NEGATIVE_INFINITY && o.xmax() == Double.NEGATIVE_INFINITY) {
                    return df == null ?
                            ("gate(" + xf + "," + o.xmin() + "," + o.xmax() + ")")
                            : ("gate(" + xf + "," + df.getFormat().format(o.xmin()) + "," + df.getFormat().format(o.xmax()) + ")")
                            ;
                } else {
                    return df == null ?
                            ("gate(" + xf + "," + yf + "," + o.xmin() + "," + o.xmax() + "," + o.ymin() + "," + o.ymax() + ")")
                            : ("gate(" + xf + "," + yf + "," + df.getFormat().format(o.xmin()) + "," + df.getFormat().format(o.xmax()) + "," + df.getFormat().format(o.ymin()) + "," + df.getFormat().format(o.ymax()) + ")")
                            ;
                }
            }
            case NONE: {
                return "";
            }
        }
        return null;
    }

}
