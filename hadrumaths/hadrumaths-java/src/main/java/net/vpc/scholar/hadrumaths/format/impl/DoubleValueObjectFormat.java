/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;

/**
 * @author vpc
 */
public class DoubleValueObjectFormat implements ObjectFormat<DoubleValue> {
    public DoubleValueObjectFormat() {
    }

    @Override
    public String format(DoubleValue o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();

    }

    @Override
    public void format(StringBuilder sb, DoubleValue o, ObjectFormatParamSet format) {
        double v = o.getValue();
        if (v == 0) {
            FormatFactory.format(sb, 0.0, format);
        } else if (v == 1) {
            if (o.getDomain().isFull()) {
                FormatFactory.format(sb, 1.0, format);
            } else {
                FormatFactory.format(sb, o.getDomain(), format);
            }
        } else if (v == -1) {
            if (o.getDomain().isFull()) {
                FormatFactory.format(sb, -1.0, format);
            } else {
                boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);
                if (par) {
                    sb.append("(");
                }
                sb.append("-");
                FormatFactory.format(sb, o.getDomain(), format.add(FormatFactory.REQUIRED_PARS));
                if (par) {
                    sb.append(")");
                }
            }
        } else {
            FormatFactory.format(sb, o.getValue(), format);
            FormatFactory.appendStarredDomain(sb, o, format);
        }
    }
}