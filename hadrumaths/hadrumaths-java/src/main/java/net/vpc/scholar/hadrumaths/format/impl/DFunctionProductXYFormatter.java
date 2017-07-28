/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.Mul;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.ProductFormat;

/**
 * @author vpc
 */
public class DFunctionProductXYFormatter implements Formatter<Mul> {

    public DFunctionProductXYFormatter() {
    }

    @Override
    public String format(Mul o, FormatParam... format) {
        FormatParamArray formatArray = new FormatParamArray(format);
        ProductFormat pp = (ProductFormat) formatArray.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? " " : (" " + pp.getOp() + " ");
        StringBuilder sb = new StringBuilder();
        for (Expr expression : o.getSubExpressions()) {
            if (sb.length() > 0) {
                sb.append(mul);
            }
            sb.append("(").append(FormatFactory.format(expression, format)).append(")");
        }
        return sb.toString();
    }
}
