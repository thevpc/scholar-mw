/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.symbolic.Div;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 *
 * @author vpc
 */
public class DivFormatter implements Formatter<Div> {

    @Override
    public String format(Div o, FormatParam... format) {
        return FormatFactory.formatArg(o.getFirst(), format)
                +" / "
                +FormatFactory.formatArg(o.getSecond(), format)
                ;
    }

}
