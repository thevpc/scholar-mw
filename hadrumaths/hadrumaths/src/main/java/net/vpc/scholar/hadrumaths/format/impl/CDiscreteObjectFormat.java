/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;

/**
 * @author vpc
 */
public class CDiscreteObjectFormat implements ObjectFormat<CDiscrete> {


    @Override
    public void format(CDiscrete o, ObjectFormatContext context) {
        context.append("CDiscrete(");
        context.format(o.getDomain());
        context.append(",");
        context.append(o.getCountX()).append(":").append(o.getCountY()).append(":").append(o.getCountZ());
        context.append(")");
    }
}
