/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DefaultDoubleValue;

/**
 * @author vpc
 */
public class DoubleValueObjectFormat implements ObjectFormat<DoubleValue> {
    public DoubleValueObjectFormat() {
    }

    @Override
    public String format(DoubleValue o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();

    }

    @Override
    public void format(DoubleValue o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
        double v = o.toDouble();
        if (v == 0) {
            context.format(0.0, format);
        } else if (v == 1) {
            if (o.getDomain().isUnbounded()) {
                context.format(1.0, format);
            } else {
                context.format(o.getDomain(), format);
            }
        } else if (v == -1) {
            if (o.getDomain().isUnbounded()) {
                context.format(-1.0, format);
            } else {
                boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
                if (par) {
                    context.append("(");
                }
                context.append("-");
                context.format(o.getDomain(), format.add(FormatFactory.REQUIRED_PARS));
                if (par) {
                    context.append(")");
                }
            }
        } else {
            context.format(o.toDouble(), format);
            FormatFactory.appendStarredDomain(context, o, format);
        }
    }
}
