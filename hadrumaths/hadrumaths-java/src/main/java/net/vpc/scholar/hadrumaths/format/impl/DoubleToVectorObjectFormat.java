/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

/**
 * @author vpc
 */
public class DoubleToVectorObjectFormat implements ObjectFormat<DoubleToVector> {

    public DoubleToVectorObjectFormat() {
    }

    @Override
    public String format(DoubleToVector o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(DoubleToVector o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        context.append("{");
        format = format.add(FormatFactory.REQUIRED_PARS);
        for (int i = 0; i < o.getComponentSize(); i++) {
            if (i > 0) {
//                sb.append(", ");
                context.append(",");
            }
            context.format(o.getComponent(Axis.cartesianValues()[i]), format);
        }
        context.append("}");

    }
}
