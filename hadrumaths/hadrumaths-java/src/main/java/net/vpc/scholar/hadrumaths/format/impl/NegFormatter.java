/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.symbolic.Neg;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class NegFormatter implements Formatter<Neg> {

    @Override
    public String format(Neg o, FormatParam... format) {
        return "-"+FormatFactory.formatArg(o.getExpression(), format);
    }
}
