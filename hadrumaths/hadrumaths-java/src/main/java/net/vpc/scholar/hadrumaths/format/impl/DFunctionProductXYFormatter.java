/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.ProductFormat;
import net.vpc.scholar.hadrumaths.symbolic.Mul;

/**
 * @author vpc
 */
public class DFunctionProductXYFormatter implements Formatter<Mul> {

    public DFunctionProductXYFormatter() {
    }

    @Override
    public String format(Mul o, FormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Mul o, FormatParamSet format) {
        ProductFormat pp = format.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? " " : (" " + pp.getOp() + " ");
        for (Expr expression : o.getSubExpressions()) {
            if (sb.length() > 0) {
                sb.append(mul);
            }
            sb.append("(");
            FormatFactory.format(sb, expression, format);
            sb.append(")");
        }
    }
}
