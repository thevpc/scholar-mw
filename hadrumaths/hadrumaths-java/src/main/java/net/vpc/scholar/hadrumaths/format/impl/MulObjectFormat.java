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

import java.util.List;

/**
 * @author vpc
 */
public class MulObjectFormat implements ObjectFormat<Mul> {

    @Override
    public String format(Mul o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Mul o, ObjectFormatParamSet format) {
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        format = format.add(FormatFactory.REQUIRED_PARS);
        List<Expr> segments = o.getSubExpressions();
        int size = segments.size();
        if (size > 1 && par) {
            sb.append("(");
        }
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
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
