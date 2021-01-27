/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;

import java.util.List;

/**
 * @author vpc
 */
public class MulObjectFormat implements ObjectFormat<Mul> {

    @Override
    public String format(Mul o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(Mul o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        format = format.add(FormatFactory.REQUIRED_PARS);
        List<Expr> segments = o.getChildren();
        int size = segments.size();
        if (size > 1 && par) {
            context.append("(");
        }
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
//        String mul = pp.getOp() == null ? " " : (" " + pp.getOp() + " ");
        String mul = pp.getOp() == null || pp.getOp().isEmpty() ? " " : (pp.getOp());
        if (size > 1) {
            format = format.add(FormatFactory.REQUIRED_PARS);
        }
        int count = 0;
        for (int i = 0; i < size; i++) {
            Expr e = segments.get(i);
            long old = context.length();
            context.format(e, format);
            long curr = context.length();
            if (curr > old) {
                if (count > 0) {
                    context.insert(old, mul);
                }
                count++;
            }
        }
        if (size > 1 && par) {
            context.append(")");
        }
    }
}
