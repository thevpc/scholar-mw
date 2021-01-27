/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;

/**
 * @author vpc
 */
public class VDiscreteObjectFormat implements ObjectFormat<VDiscrete> {

    @Override
    public String format(VDiscrete o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();

    }

    @Override
    public void format(VDiscrete o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        context.append("{");
        switch (o.getComponentDimension().rows) {
            case 1: {
                context.format( o.getComponent(Axis.X), format);
                break;
            }
            case 2: {
                context.format(o.getComponent(Axis.X), format);
//                sb.append(", ");
                context.append(",");
                context.format( o.getComponent(Axis.Y), format);
                break;
            }
            case 3: {
                context.format( o.getComponent(Axis.X), format);
//                sb.append(", ");
                context.append(",");
                context.format( o.getComponent(Axis.Y), format);
//                sb.append(", ");
                context.append(",");
                context.format( o.getComponent(Axis.Z), format);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported dim " + o.getDomain().getDimension());
            }
        }
        context.append("}");
    }
}
