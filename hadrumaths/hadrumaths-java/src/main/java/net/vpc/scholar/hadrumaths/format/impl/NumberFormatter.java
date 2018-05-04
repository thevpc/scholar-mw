/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.DoubleFormat;
import net.vpc.scholar.hadrumaths.format.params.RequireParenthesesFormat;

/**
 *
 * @author vpc
 */
public class NumberFormatter implements Formatter<Number>{
    public NumberFormatter() {
    }

    @Override
    public String format(Number o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Number o, FormatParamSet format) {
        DoubleFormat df =  format.getParam(DoubleFormat.class, false);
        String v = df == null ? o.toString() : df.getFormat().format(o.doubleValue());
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        if(!(v.startsWith("-")||v.startsWith("+"))){
            par=false;
        }
        if(par){
            sb.append("(");
        }
        sb.append(v);
        if(par){
            sb.append(")");
        }
    }
}
