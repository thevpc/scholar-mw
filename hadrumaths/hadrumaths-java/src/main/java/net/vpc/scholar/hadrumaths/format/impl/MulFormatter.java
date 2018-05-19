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

import java.util.List;

/**
 * @author vpc
 */
public class MulFormatter implements Formatter<Mul> {

    @Override
    public String format(Mul o, FormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Mul o, FormatParamSet format) {
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        format = format.add(FormatFactory.REQUIRED_PARS);
        List<Expr> segments = o.getSubExpressions();
        int size = segments.size();
        if (size > 1 && par) {
            sb.append("(");
        }
        ProductFormat pp = format.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? " " : (" " + pp.getOp() + " ");
        if (size > 1) {
            format = format.add(FormatFactory.REQUIRED_PARS);
        }
        for (int i = 0; i < size; i++) {
            Expr e = segments.get(i);
            StringBuilder sb2 = new StringBuilder();
            FormatFactory.format(sb2, e, format);
            if (sb2.length() > 0) {
                if (i > 0) {
                    sb.append(mul);
                }
                sb.append(sb2);
            }
        }
        if (size > 1 && par) {
            sb.append(")");
        }
    }
}
