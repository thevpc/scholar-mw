/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.Real;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class RealFormatter implements Formatter<Real> {

    @Override
    public String format(Real o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();

    }

    @Override
    public void format(StringBuilder sb, Real o, FormatParamSet format) {
        Expr arg = o.getArg();
        sb.append("real(");
        FormatFactory.format(sb,arg, format);
        sb.append(")");
    }
}
