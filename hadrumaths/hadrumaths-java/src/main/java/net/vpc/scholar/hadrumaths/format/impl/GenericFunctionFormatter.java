/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.RequireParenthesesFormat;
import net.vpc.scholar.hadrumaths.symbolic.AbstractComposedFunction;

/**
 *
 * @author vpc
 */
public class GenericFunctionFormatter implements Formatter<AbstractComposedFunction> {

    @Override
    public String format(AbstractComposedFunction o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, AbstractComposedFunction o, FormatParamSet format) {
        Expr[] arguments = o.getArguments();
        sb.append(o.getFunctionName()).append("(");

        if(arguments.length==1){
            format=format.remove(RequireParenthesesFormat.INSTANCE);
            FormatFactory.format(sb,arguments[0], format);
        }else {
            format=format.add(RequireParenthesesFormat.INSTANCE);
            for (int i = 0; i < arguments.length; i++) {
                Expr a = arguments[i];
                if (i > 0) {
                    sb.append(", ");
                }
                FormatFactory.format(sb,a, format);
            }
        }
        sb.append(")");
    }
}
