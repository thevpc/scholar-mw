/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;

import java.util.List;

/**
 * @author vpc
 */
public class PolygonFormatter extends AbstractFormatter<Polygon> {

    @Override
    public void format(StringBuilder sb, Polygon o, FormatParamSet format) {
        sb.append("Polygon([");
        List<Point> points1 = o.getPoints();
        for (int i = 0; i < points1.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("(");
            sb.append(points1.get(i).x);
            sb.append(",");
            sb.append(points1.get(i).x);
            sb.append(")");
        }
        sb.append("]");
        sb.append(", properties").append(o.getProperties());
        sb.append(")");
    }

}
