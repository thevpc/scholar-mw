/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.RequireParenthesesFormat;
import net.vpc.scholar.hadrumaths.symbolic.Div;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class DivFormatter implements Formatter<Div> {

    @Override
    public String format(Div o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Div o, FormatParamSet format) {
        boolean par=format.containsParam(RequireParenthesesFormat.INSTANCE);
        format=format.add(RequireParenthesesFormat.INSTANCE);
        if(par){
            sb.append("(");
        }
        FormatFactory.format(sb,o.getFirst(), format);
        sb.append(" / ");
        FormatFactory.format(sb,o.getSecond(), format);
        if(par){
            sb.append(")");
        }
    }
}
