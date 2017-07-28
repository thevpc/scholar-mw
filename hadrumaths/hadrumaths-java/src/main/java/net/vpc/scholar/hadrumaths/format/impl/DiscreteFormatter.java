/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.symbolic.Discrete;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class DiscreteFormatter implements Formatter<Discrete> {

    @Override
    public String format(Discrete o, FormatParam... format) {
        return "Discrete("+FormatFactory.format(o.getDomain(),format)+","+o.getCountX()+":"+o.getCountY()+":"+o.getCountZ()+")";
    }
}
