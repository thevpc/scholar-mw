/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.symbolic.AbstractComposedFunction;

/**
 * @author vpc
 */
public class GenericFunctionObjectFormat implements ObjectFormat<AbstractComposedFunction> {

    @Override
    public String format(AbstractComposedFunction o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, AbstractComposedFunction o, ObjectFormatParamSet format) {
        Expr[] arguments = o.getArguments();
        sb.append(o.getFunctionName()).append("(");

        if (arguments.length == 1) {
            format = format.remove(FormatFactory.REQUIRED_PARS);
            FormatFactory.format(sb, arguments[0], format);
        } else {
            format = format.add(FormatFactory.REQUIRED_PARS);
            for (int i = 0; i < arguments.length; i++) {
                Expr a = arguments[i];
                if (i > 0) {
                    sb.append(", ");
                }
                FormatFactory.format(sb, a, format);
            }
        }
        sb.append(")");
    }
}
