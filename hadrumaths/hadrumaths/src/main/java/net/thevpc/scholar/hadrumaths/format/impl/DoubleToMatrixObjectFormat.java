/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.ComponentDimension;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToMatrix;

/**
 * @author vpc
 */
public class DoubleToMatrixObjectFormat implements ObjectFormat<DoubleToMatrix> {

    public DoubleToMatrixObjectFormat() {
    }

    @Override
    public String format(DoubleToMatrix o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    @Override
    public void format(DoubleToMatrix o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
        context.append("[");
        format = format.add(FormatFactory.REQUIRED_PARS);
        ComponentDimension d = o.getComponentDimension();
        for (int i = 0; i < d.rows; i++) {
            if (i > 0) {
                context.append(";");
            }
            for (int j = 0; j < d.columns; j++) {
                if (j > 0) {
                    context.append(",");
                }
                context.format(o.getComponent(i, j), format);
            }
        }
        context.append("]");

    }
}
