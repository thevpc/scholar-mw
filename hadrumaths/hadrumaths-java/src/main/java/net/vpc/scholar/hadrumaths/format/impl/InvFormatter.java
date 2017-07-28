/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.symbolic.Inv;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class InvFormatter implements Formatter<Inv> {

    @Override
    public String format(Inv o, FormatParam... format) {
        return "inv("+FormatFactory.format(o.getExpression(), format)
                +")";
    }
}
