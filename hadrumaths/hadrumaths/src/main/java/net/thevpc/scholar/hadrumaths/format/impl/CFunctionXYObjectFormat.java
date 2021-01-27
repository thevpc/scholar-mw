/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * @author vpc
 */
public class CFunctionXYObjectFormat implements ObjectFormat<DoubleToComplex> {


    @Override
    public void format(DoubleToComplex o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        DoubleToDouble real = o.getRealDD();
        DoubleToDouble imag = o.getImagDD();
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        //DomainXY domain=o.getDomain();
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
//            String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        String mul = pp.getOp() == null || pp.getOp().isEmpty() ? " " : (pp.getOp());
        boolean noReal = false;//real.getDomain().isEmpty() || real.isZero();
        boolean noImag = false;//imag.getDomain().isEmpty() || imag.isZero();
        if (noReal && noImag) {
            context.format( Maths.DZEROXY, format);
            return;
        }
        if (par) {
            context.append("(");
        }
        if (!noReal) {
            context.format( real, format);
        }
        if (!noImag) {

            String s = FormatFactory.format(imag, format);
            if (!s.startsWith("-") && !noReal) {
//                sb.append(" + ");
                context.append("+");
            } else if (!noReal) {
//                sb.append(" ");
                context.append("");
            }
            context.append(s);
//            sb.append(mul).append(" i");
            context.append(mul).append("i");
        }
        if (par) {
            context.append(")");
        }
    }
}
