/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.symbolic.AbstractComposedFunction;

/**
 *
 * @author vpc
 */
public class GenericFunctionFormatter implements Formatter<AbstractComposedFunction> {

    @Override
    public String format(AbstractComposedFunction o, FormatParam... format) {
        StringBuilder sb = new StringBuilder();
        Expr[] arguments = o.getArguments();
        if(arguments.length==1){
            sb.append(FormatFactory.format(arguments[0], format));
        }else {
            for (Expr a : arguments) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(FormatFactory.formatArg(a, format));
            }
        }
        sb.insert(0,o.getFunctionName()+"(");
        sb.append(")");
        return sb.toString();
    }
}
