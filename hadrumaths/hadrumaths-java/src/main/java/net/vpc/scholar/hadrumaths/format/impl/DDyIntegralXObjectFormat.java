/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DDyIntegralX;

/**
 * @author vpc
 */
public class DDyIntegralXObjectFormat extends AbstractObjectFormat<DDyIntegralX> {

    @Override
    public void format(DDyIntegralX o, ObjectFormatContext context) {
        context.append("DDyIntegralX(");
        context.format(o.getArg());
        context.append(",");
        context.append(o.getIntegralXY().toString());
        context.append(",");
        context.format(o.getX0());
        context.append(",");
        context.format(o.getX1());
        context.append(")");
    }

}
