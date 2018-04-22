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
 *
 * @author vpc
 */
public class StringFormatter extends AbstractFormatter<String> {

    @Override
    public void format(StringBuilder sb, String o, FormatParamSet format) {
        sb.append(o);
    }

}
