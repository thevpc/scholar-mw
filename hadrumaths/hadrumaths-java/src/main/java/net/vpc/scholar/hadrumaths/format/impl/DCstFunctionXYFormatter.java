/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

/**
 *
 * @author vpc
 */
public class DCstFunctionXYFormatter implements Formatter<DoubleValue>{
    public DCstFunctionXYFormatter() {
    }

    @Override
    public String format(DoubleValue o, FormatParam... format) {
        return FormatFactory.format(new Linear(0, 0, o.getValue(), o.getDomain()),format);
    }
    
}
