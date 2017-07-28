/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.symbolic.Pow;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class PowFormatter implements Formatter<Pow> {

    @Override
    public String format(Pow o, FormatParam... format) {
        return "pow("+FormatFactory.formatArg(o.getFirst(), format)
                +" , "
                +FormatFactory.formatArg(o.getSecond(), format)
                +")";
    }
}
