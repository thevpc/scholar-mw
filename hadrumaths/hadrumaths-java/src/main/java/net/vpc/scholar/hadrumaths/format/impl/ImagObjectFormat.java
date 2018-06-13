/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.symbolic.Imag;

/**
 * @author vpc
 */
public class ImagObjectFormat implements ObjectFormat<Imag> {

    @Override
    public String format(Imag o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, Imag o, ObjectFormatParamSet format) {
        Expr arg = o.getArg();
        sb.append("imag(");
        FormatFactory.format(sb, arg, format);
        sb.append(")");

    }
}
