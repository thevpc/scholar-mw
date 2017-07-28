/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.symbolic.Shape;

/**
 * @author vpc
 */
public class DDxyPolygonFormatter implements Formatter<Shape> {

    @Override
    public String format(Shape o, FormatParam... format) {
        return "("+o.getValue()+"*"+ o.getGeometry().toString()+")";
    }

}
