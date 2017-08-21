/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 * @author vpc
 */
public class ComplexFormatter implements Formatter<Complex> {
    public ComplexFormatter() {
    }

    @Override
    public String format(Complex o, FormatParam... format) {
        double real = o.getReal();
        double imag = o.getImag();
        if (Double.isNaN(real) && Double.isNaN(imag)) {
            return "NaN";//FormatFactory.format(imag,format);
        }
        if (Double.isNaN(real)) {
            String s="NaN";
            if(imag==0){
                return s;
            }
            if(imag<0){
                return s+FormatFactory.format(imag,format) + "i";
            }
            return s+"+"+FormatFactory.format(imag,format) + "i";
        }
        if (Double.isNaN(imag)) {
            if(real==0){
                return "NaN*i";
            }
            return FormatFactory.format(real)+"NaN*i";
        }
        String imag_string = FormatFactory.format(imag,format);
        String real_string = FormatFactory.format(real,format);
        String i_string = "i";
        if (imag == 0) {
            return real_string;
        } else if (real == 0) {
            return (imag == 1) ? i_string : (imag == -1) ? ("-"+i_string) : (imag_string + i_string);
        } else {
            return real_string
                    + ((imag == 1) ? ("+"+i_string) : (imag == -1) ? ("-"+i_string) : (imag > 0) ? ("+" + (imag_string + i_string)) : (imag_string + i_string));
        }
    }

}
