/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.ProductFormat;
import net.vpc.scholar.hadrumaths.format.params.RequireParenthesesFormat;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * @author vpc
 */
public class CFunctionXYFormatter implements Formatter<DoubleToComplex> {

    @Override
    public String format(DoubleToComplex o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();
//        DoubleToDouble real = o.getReal();
//        DoubleToDouble imag = o.getImag();
//        //DomainXY domain=o.getDomain();
//        ProductFormat pp = format.getParam(FormatFactory.PRODUCT_STAR);
//        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
//        StringBuilder sb = new StringBuilder();
//        boolean noReal = real.getDomain().isEmpty() || real.isZero();
//        boolean noImag = imag.getDomain().isEmpty() || imag.isZero();
//        if (noReal && noImag) {
//            return FormatFactory.format(FunctionFactory.DZEROXY, format);
//        }
//        if (!noReal) {
//            sb.append(FormatFactory.format(real, format));
//        }
//        if (!noImag) {
//            String s = FormatFactory.format(imag, format);
//            if (!s.startsWith("-") && !noReal) {
//                sb.append("+");
//            }
//            sb.append(s);
//            sb.append(mul).append(" i");
//        }
//        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, DoubleToComplex o, FormatParamSet format) {
        DoubleToDouble real = o.getRealDD();
        DoubleToDouble imag = o.getImagDD();
        boolean par=format.containsParam(FormatFactory.REQUIRED_PARS);
        //DomainXY domain=o.getDomain();
        ProductFormat pp = format.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        boolean noReal = real.getDomain().isEmpty() || real.isZero();
        boolean noImag = imag.getDomain().isEmpty() || imag.isZero();
        if (noReal && noImag) {
            FormatFactory.format(sb,FunctionFactory.DZEROXY, format);
            return;
        }
        if(par){
            sb.append("(");
        }
        if (!noReal) {
            FormatFactory.format(sb,real, format);
        }
        if (!noImag) {

            String s = FormatFactory.format(imag, format);
            if (!s.startsWith("-") && !noReal) {
                sb.append(" + ");
            }else if(!noReal){
                sb.append(" ");
            }
            sb.append(s);
            sb.append(mul).append(" i");
        }
        if(par){
            sb.append(")");
        }
    }
}
