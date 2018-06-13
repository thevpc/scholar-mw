/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

/**
 * @author vpc
 */
public class VDCxyObjectFormat implements ObjectFormat<DoubleToVector> {

    public VDCxyObjectFormat() {
    }

    @Override
    public String format(DoubleToVector o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, DoubleToVector o, ObjectFormatParamSet format) {
        sb.append("(");
        format = format.add(FormatFactory.REQUIRED_PARS);
        for (int i = 0; i < o.getComponentSize(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            FormatFactory.format(sb, o.getComponent(Axis.values()[i]), format);
        }
        sb.append(")");

    }
}
