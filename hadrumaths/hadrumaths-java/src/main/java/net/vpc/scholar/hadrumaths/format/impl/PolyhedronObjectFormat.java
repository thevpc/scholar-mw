/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.Polyhedron;

/**
 * @author vpc
 */
public class PolyhedronObjectFormat extends AbstractObjectFormat<Polyhedron> {

    @Override
    public void format(StringBuilder sb, Polyhedron o, ObjectFormatParamSet format) {
        sb.append("Polyhedron(");
        FormatFactory.format(sb, o.getMax(), format);
        sb.append(",");
        FormatFactory.format(sb, o.getPolygon(), format);
        sb.append(")");
    }

}
