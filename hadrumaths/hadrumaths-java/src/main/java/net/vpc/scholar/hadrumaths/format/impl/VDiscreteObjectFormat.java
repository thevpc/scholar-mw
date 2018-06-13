/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;

/**
 * @author vpc
 */
public class VDiscreteObjectFormat implements ObjectFormat<VDiscrete> {

    @Override
    public String format(VDiscrete o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();

    }

    @Override
    public void format(StringBuilder sb, VDiscrete o, ObjectFormatParamSet format) {
        sb.append("[");
        switch (o.getComponentDimension().rows) {
            case 1: {
                FormatFactory.format(sb, o.getComponent(Axis.X), format);
                break;
            }
            case 2: {
                FormatFactory.format(sb, o.getComponent(Axis.X), format);
                sb.append(", ");
                FormatFactory.format(sb, o.getComponent(Axis.Y), format);
                break;
            }
            case 3: {
                FormatFactory.format(sb, o.getComponent(Axis.X), format);
                sb.append(", ");
                FormatFactory.format(sb, o.getComponent(Axis.Y), format);
                sb.append(", ");
                FormatFactory.format(sb, o.getComponent(Axis.Z), format);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported dim " + o.getDomainDimension());
            }
        }
        sb.append("]");
    }
}
