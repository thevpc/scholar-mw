/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Polyhedron;

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
