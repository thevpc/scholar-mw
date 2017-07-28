/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.DoubleFormat;

/**
 *
 * @author vpc
 */
public class DoubleFormatter implements Formatter<Double>{
    public DoubleFormatter() {
    }

    @Override
    public String format(Double o, FormatParam... format) {
        FormatParamArray formatArray=new FormatParamArray(format);
        DoubleFormat df = (DoubleFormat) formatArray.getParam(DoubleFormat.class, false);
        if(df!=null){
            return df.getFormat().format(o);
        }
        if(Maths.isInt(o)){
            return String.valueOf(o.intValue());
        }
        return o.toString();
    }
    
}
