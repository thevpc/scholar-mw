/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;

/**
 * @author vpc
 */
public class DFunctionProductXYObjectFormat implements ObjectFormat<Mul> {

    public DFunctionProductXYObjectFormat() {
    }

    @Override
    public void format(Mul o, ObjectFormatContext context) {
        ProductObjectFormatParam pp = context.getParam(FormatFactory.PRODUCT_STAR);
//            String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        String mul = pp.getOp() == null || pp.getOp().isEmpty() ? " " : (pp.getOp());
        for (Expr expression : o.getChildren()) {
            if (context.length() > 0) {
                context.append(mul);
            }
            context.append("(");
            context.format(expression);
            context.append(")");
        }
    }
}
