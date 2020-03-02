/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Polyhedron;

/**
 * @author vpc
 */
public class PolyhedronObjectFormat extends AbstractObjectFormat<Polyhedron> {

    @Override
    public void format(Polyhedron o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
        context.append("Polyhedron(");
        context.format(o.getMax(), format);
        context.append(",");
        context.format(o.getPolygon(), format);
        context.append(")");
    }

}
