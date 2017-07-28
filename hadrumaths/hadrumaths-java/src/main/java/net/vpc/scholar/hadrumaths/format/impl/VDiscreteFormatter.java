/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class VDiscreteFormatter implements Formatter<VDiscrete> {

    @Override
    public String format(VDiscrete o, FormatParam... format) {
        StringBuilder sb=new StringBuilder("[");
        switch (o.getComponentDimension().rows){
            case 1:{
                sb.append(FormatFactory.format(o.getComponent(Axis.X), format));
                break;
            }
            case 2:{
                sb.append(FormatFactory.format(o.getComponent(Axis.X), format));
                sb.append(", ");
                sb.append(FormatFactory.format(o.getComponent(Axis.Y), format));
                break;
            }
            case 3:{
                sb.append(FormatFactory.format(o.getComponent(Axis.X), format));
                sb.append(", ");
                sb.append(FormatFactory.format(o.getComponent(Axis.Y), format));
                sb.append(", ");
                sb.append(FormatFactory.format(o.getComponent(Axis.Z), format));
                break;
            }
            default:{
                throw new IllegalArgumentException("Unsupported dim "+o.getDomainDimension());
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
