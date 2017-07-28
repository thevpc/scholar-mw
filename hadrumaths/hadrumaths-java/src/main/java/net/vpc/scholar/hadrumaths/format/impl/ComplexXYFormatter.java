/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
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
    public String format(ComplexValue o, FormatParam... format) {
        Complex v = o.getValue();
        if(v.equals(Complex.ZERO)){
            return "0";
        }else if(v.equals(Complex.ONE)){
            String d = FormatFactory.format(o.getDomain(), format);
            return d.length()==0?"1":d;
        }else if(v.equals(Complex.MINUS_ONE)){
            String d = FormatFactory.format(o.getDomain(), format);
            return "-"+(d.length()==0?"1":d);
        }else{
            String d = FormatFactory.format(o.getDomain(), format);
            return FormatFactory.format(o.getValue(),format)+(d.length()==0?"":(" * "+d));
        }
    }
    
}
