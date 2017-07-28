/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.symbolic.Sub;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class SubFormatter implements Formatter<Sub> {

    @Override
    public String format(Sub o, FormatParam... format) {
        return FormatFactory.formatArg(o.getFirst(), format)
                +" - "
                +FormatFactory.formatArg(o.getSecond(), format)
                ;
    }
}
