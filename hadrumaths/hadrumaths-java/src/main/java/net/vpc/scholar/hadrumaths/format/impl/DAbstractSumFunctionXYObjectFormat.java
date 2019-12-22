/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.DDxyAbstractSum;

import java.util.List;

/**
 * @author vpc
 */
public class DAbstractSumFunctionXYObjectFormat implements ObjectFormat<DDxyAbstractSum> {

    @Override
    public String format(DDxyAbstractSum o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, DDxyAbstractSum o, ObjectFormatParamSet format) {
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
        List<Expr> segments = o.getSubExpressions();
        int size = segments.size();
        if (par && size <= 1) {
            par = false;
        }
        format = format.add(FormatFactory.REQUIRED_PARS);
        if (par) {
            sb.append("(");
        }
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(" + ");
            }
            FormatFactory.format(sb, segments.get(i), format);
        }
        if (par) {
            sb.append("(");
        }
    }
}