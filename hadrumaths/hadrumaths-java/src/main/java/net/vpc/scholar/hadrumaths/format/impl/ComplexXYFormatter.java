/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.symbolic.ComplexValue;

/**
 *
 * @author vpc
 */
public class ComplexXYFormatter implements Formatter<ComplexValue>{
    public ComplexXYFormatter() {
    }

    @Override
    public String format(ComplexValue o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();
//        Complex v = o.getValue();
//        if(v.equals(Complex.ZERO)){
//            return "0";
//        }else if(v.equals(Complex.ONE)){
//            String d = FormatFactory.format(o.getDomain(), format);
//            return d.length()==0?"1":d;
//        }else if(v.equals(Complex.MINUS_ONE)){
//            String d = FormatFactory.format(o.getDomain(), format);
//            return "-"+(d.length()==0?"1":d);
//        }else{
//            String d = FormatFactory.format(o.getDomain(), format);
//            return FormatFactory.format(o.getValue(),format)+(d.length()==0?"":(" * "+d));
//        }
    }

    @Override
    public void format(StringBuilder sb, ComplexValue o, FormatParamSet format) {
//        sb.append(format(o,format));
        Complex v = o.getValue();
        if(v.equals(Complex.ZERO)){
            sb.append("0");
            return ;
        }else if(v.equals(Complex.ONE)){
            int length = sb.length();
            FormatFactory.format(sb,o.getDomain(), format);
            if(sb.length()==length) {
                sb.append("1");
            }
            return;
        }else if(v.equals(Complex.MINUS_ONE)){
            boolean pars = format.containsParam(FormatFactory.REQUIRED_PARS);
            if(pars){
                sb.append("(");
            }
            sb.append("-");
            int length = sb.length();
            FormatFactory.format(sb,o.getDomain(), format);
            if(sb.length()==length) {
                sb.append("1");
            }
            if(pars){
                sb.append(")");
            }
            return ;
        }else{
            FormatFactory.format(sb,o.getValue(),format);
            FormatFactory.appendStarredDomain(sb,o.getDomain(), format);
            return;
        }
    }
}
