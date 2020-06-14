/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.geom.DefaultPolygon;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;

import java.util.List;

/**
 * @author vpc
 */
public class PolygonObjectFormat extends AbstractObjectFormat<Polygon> {

    @Override
    public void format(Polygon o, ObjectFormatContext context) {
        context.append("Polygon([");
        List<Point> points1 = o.getPoints();
        for (int i = 0; i < points1.size(); i++) {
            if (i > 0) {
                context.append(",");
            }
            context.append("(");
            context.append(points1.get(i).x);
            context.append(",");
            context.append(points1.get(i).x);
            context.append(")");
        }
        context.append("]");
        context.append(", properties").append(o.getProperties());
        context.append(")");
    }

}
