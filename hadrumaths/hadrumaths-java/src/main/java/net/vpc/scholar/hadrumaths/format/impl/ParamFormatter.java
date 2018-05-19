/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.symbolic.ParamExpr;

/**
 * @author vpc
 */
public class ParamFormatter implements Formatter<ParamExpr> {
    public ParamFormatter() {
    }

    @Override
    public String format(ParamExpr o, FormatParamSet format) {
        return o.getParamName();
    }

    @Override
    public void format(StringBuilder sb, ParamExpr o, FormatParamSet format) {
        sb.append(o.getParamName());
    }
}
