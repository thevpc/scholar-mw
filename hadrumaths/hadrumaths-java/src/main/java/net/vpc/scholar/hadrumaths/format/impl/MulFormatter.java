/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.ProductFormat;
import net.vpc.scholar.hadrumaths.symbolic.Mul;

import java.util.List;

/**
 *
 * @author vpc
 */
public class MulFormatter implements Formatter<Mul> {

    @Override
    public String format(Mul o, FormatParam... format) {
        List<Expr> segments = o.getSubExpressions();
        StringBuilder sb = new StringBuilder();
        FormatParamArray formatArray = new FormatParamArray(format);
        ProductFormat pp = (ProductFormat)formatArray.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp()==null?" ":(" "+pp.getOp()+" ");

        for (int i = 0; i < segments.size(); i++) {
            Expr e = segments.get(i);
            if (i > 0) {
                sb.append(mul);
            }
            sb.append(FormatFactory.formatArg(e, format));
        }
        return sb.toString();
    }
}
