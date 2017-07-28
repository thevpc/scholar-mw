/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.Imag;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class ImagFormatter implements Formatter<Imag> {

    @Override
    public String format(Imag o, FormatParam... format) {
        StringBuilder sb = new StringBuilder();
        Expr arg = o.getArg();
        sb.append("imag(");
        sb.append(FormatFactory.format(arg, format));
        sb.append(")");
        return sb.toString();
    }
}
