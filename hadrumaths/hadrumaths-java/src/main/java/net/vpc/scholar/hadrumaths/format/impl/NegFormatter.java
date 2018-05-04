/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.RequireParenthesesFormat;
import net.vpc.scholar.hadrumaths.symbolic.Neg;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class NegFormatter implements Formatter<Neg> {

    @Override
    public String format(Neg o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Neg o, FormatParamSet format) {
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        if(par){
            sb.append("(");
        }
        sb.append("-");
        FormatFactory.format(sb,o.getExpression(), format.add(FormatFactory.REQUIRED_PARS));
        if(par){
            sb.append(")");
        }
    }
}
