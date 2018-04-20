/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.FormatParamSet;
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
    public void format(StringBuilder sb, Any o, FormatParamSet format) {
        DebugFormat d=format.getParam(DebugFormat.class,false);
        if(d==null) {
            FormatFactory.format(sb,o.getObject(), format);
        }else{
            sb.append("any(");
            FormatFactory.format(sb,o.getObject(), format);
            sb.append(")");
        }
    }

    @Override
    public String format(Any o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();
    }
}
