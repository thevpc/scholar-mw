/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.DebugFormat;

/**
 *
 * @author vpc
 */
public class AnyFormatter implements Formatter<Any> {

    @Override
    public String format(Any o, FormatParam... format) {
        FormatParamArray a=new FormatParamArray(format);
        DebugFormat d=a.getParam(DebugFormat.class,false);
        if(d==null) {
            return FormatFactory.format(o.getObject(), format);
        }else{
            return "any("+FormatFactory.format(o.getObject(), format)+")";
        }
    }
}
