/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;

/**
 * @author vpc
 */
public class ComplexXYObjectFormat implements ObjectFormat<DefaultComplexValue> {
    public ComplexXYObjectFormat() {
    }

//    @Override
//    public String format(DefaultComplexValue o, ObjectFormatParamSet format, ObjectFormatContext context) {
//        StringBuilder sb = new StringBuilder();
//        format(sb, o, format, context);
//        return sb.toString();
////        Complex v = o.getValue();
////        if(v.equals(Complex.ZERO)){
////            return "0";
////        }else if(v.equals(Complex.ONE)){
////            String d = FormatFactory.format(o.getDomain(), format);
////            return d.length()==0?"1":d;
////        }else if(v.equals(Complex.MINUS_ONE)){
////            String d = FormatFactory.format(o.getDomain(), format);
////            return "-"+(d.length()==0?"1":d);
////        }else{
////            String d = FormatFactory.format(o.getDomain(), format);
////            return FormatFactory.format(o.getValue(),format)+(d.length()==0?"":(" * "+d));
////        }
//    }

    @Override
    public void format(DefaultComplexValue o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
//        sb.append(format(o,format));
        Complex v = o.getValue();
        if (v.equals(Complex.ZERO)) {
            context.append("0");
            return;
        } else if (v.equals(Complex.ONE)) {
            long length = context.length();
            context.format(o.getDomain(), format);
            if (context.length() == length) {
                context.append("1");
            }
            return;
        } else if (v.equals(Complex.MINUS_ONE)) {
            boolean pars = format.containsParam(FormatFactory.REQUIRED_PARS);
            if (pars) {
                context.append("(");
            }
            context.append("-");
            long length = context.length();
            context.format(o.getDomain(), format);
            if (context.length() == length) {
                context.append("1");
            }
            if (pars) {
                context.append(")");
            }
            return;
        } else {
            context.format(o.getValue(), format);
            FormatFactory.appendStarredDomain(context, o.getDomain(), format);
            return;
        }
    }
}
