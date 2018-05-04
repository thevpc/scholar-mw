/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.RequireParenthesesFormat;
import net.vpc.scholar.hadrumaths.symbolic.Plus;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

import java.util.List;

/**
 * @author vpc
 */
public class DFunctionAbstractSumXFormat implements Formatter<Plus> {

    @Override
    public String format(Plus o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Plus o, FormatParamSet format) {
        boolean par=format.containsParam(FormatFactory.REQUIRED_PARS);
        List<Expr> segments = o.getSubExpressions();
        int size = segments.size();
        if(par && size<=1){
            par=false;
        }
        format=format.add(FormatFactory.REQUIRED_PARS);
        if(par){
            sb.append("(");
        }
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(" + ");
            }
            FormatFactory.format(sb,segments.get(i), format);
        }
        if(par){
            sb.append("(");
        }
    }
}
