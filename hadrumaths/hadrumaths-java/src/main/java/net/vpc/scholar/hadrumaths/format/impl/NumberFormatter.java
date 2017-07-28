/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.DoubleFormat;

/**
 *
 * @author vpc
 */
public class NumberFormatter implements Formatter<Number>{
    public NumberFormatter() {
    }

    @Override
    public String format(Number o, FormatParam... format) {
        FormatParamArray formatArray=new FormatParamArray(format);
        DoubleFormat df = (DoubleFormat) formatArray.getParam(DoubleFormat.class, false);
        return df==null?o.toString():df.getFormat().format(o.doubleValue());
    }
    
}
