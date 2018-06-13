/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.Mul;

/**
 * @author vpc
 */
public class DFunctionProductXYObjectFormat implements ObjectFormat<Mul> {

    public DFunctionProductXYObjectFormat() {
    }

    @Override
    public String format(Mul o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Mul o, ObjectFormatParamSet format) {
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
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
