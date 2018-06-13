/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.symbolic.ParamExpr;

/**
 * @author vpc
 */
public class ParamObjectFormat implements ObjectFormat<ParamExpr> {
    public ParamObjectFormat() {
    }

    @Override
    public String format(ParamExpr o, ObjectFormatParamSet format) {
        return o.getParamName();
    }

    @Override
    public void format(StringBuilder sb, ParamExpr o, ObjectFormatParamSet format) {
        sb.append(o.getParamName());
    }
}
