/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.geom.HPolygon;

import java.util.List;

/**
 * @author vpc
 */
public class PolygonObjectFormat extends AbstractObjectFormat<HPolygon> {

    @Override
    public void format(HPolygon o, ObjectFormatContext context) {
        context.append("Polygon([");
        List<HPoint> points1 = o.getPoints();
        for (int i = 0; i < points1.size(); i++) {
            if (i > 0) {
                context.append(",");
            }
            context.append("(");
            context.append(points1.get(i).x);
            context.append(",");
            context.append(points1.get(i).y);
            context.append(")");
        }
        context.append("]");
        context.append(", properties").append(o.getProperties());
        context.append(")");
    }

}
