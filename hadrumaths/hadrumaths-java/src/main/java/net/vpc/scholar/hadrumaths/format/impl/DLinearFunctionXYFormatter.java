/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.DoubleFormat;
import net.vpc.scholar.hadrumaths.format.params.ProductFormat;
import net.vpc.scholar.hadrumaths.format.params.XFormat;
import net.vpc.scholar.hadrumaths.format.params.YFormat;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

/**
 * @author vpc
 */
public class DLinearFunctionXYFormatter implements Formatter<Linear> {

    @Override
    public String format(Linear o, FormatParam... format) {
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        FormatParamArray formatArray = new FormatParamArray(format);
        XFormat x = (XFormat) formatArray.getParam(FormatFactory.X);
        YFormat y = (YFormat) formatArray.getParam(FormatFactory.Y);
        DoubleFormat df = (DoubleFormat) formatArray.getParam(DoubleFormat.class, false);
        ProductFormat pp = (ProductFormat) formatArray.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");

        StringBuilder sb = new StringBuilder();
        boolean par=false;
        if (a == 0 && b == 0 && c == 0) {
            if (df != null) {
                return (df.getFormat().format(0));
            } else {
                return String.valueOf(0);
            }
        } else {
            String v;
            if (a == 1) {
                sb.append(x.getName());
            } else if (a == -1) {
                sb.append("-").append(x.getName());
                par=true;
            } else if (a == 0) {
                // dot nothing
            } else {
                v = FormatFactory.toParamString(a, df, false, true);
                if (v.length() > 0) {
                    sb.append(v).append(mul).append(x.getName());
                    if(v.startsWith("-")){
                        par=true;
                    }
                }
            }
            if (b == 1) {
                sb.append(y.getName());
            } else if (a == -1) {
                sb.append("-").append(y.getName());
                par=true;
            } else if (b == 0) {
                // dot nothing
            } else {
                v = FormatFactory.toParamString(b, df, sb.length() > 0, true);
                if (v.length() > 0) {
                    sb.append(v).append(mul).append(y.getName());
                    if(v.startsWith("-")){
                        par=true;
                    }
                }
            }
            if(sb.length()==0 && c==1){
                if(o.getDomain().equals(Domain.FULL(o.getDomainDimension()))){
                    return "1";
                }else{
                    String f = FormatFactory.format(o.getDomain(), format);
                    if(f.length()>0) {
                        return f;
                    }
                    return "1";
                }
            }
            v = FormatFactory.toParamString(c, df, sb.length() > 0, true);
            if (v.length() > 0) {
                sb.append(v);
                if(v.startsWith("-")){
                    par=true;
                }
            }
        }
        String s = o.getDomain().equals(Domain.FULL(o.getDomainDimension())) ? "" : FormatFactory.format(o.getDomain(), format);
        if(par){
            sb.insert(0,"(");
            sb.append(")");
        }
        return s.length() > 0 ? (s + mul + sb.toString()) : sb.toString();
    }

}
