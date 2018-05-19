/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.Polyhedron;

/**
 * @author vpc
 */
public class PolyhedronFormatter extends AbstractFormatter<Polyhedron> {

    @Override
    public void format(StringBuilder sb, Polyhedron o, FormatParamSet format) {
        sb.append("Polyhedron(");
        FormatFactory.format(sb, o.getMax(), format);
        sb.append(",");
        FormatFactory.format(sb, o.getPolygon(), format);
        sb.append(")");
    }

}
