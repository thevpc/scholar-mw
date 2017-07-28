/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;

/**
 *
 * @author vpc
 */
public class DoubleValueFormatter implements Formatter<DoubleValue>{
    public DoubleValueFormatter() {
    }

    @Override
    public String format(DoubleValue o, FormatParam... format) {
        double v = o.getValue();
        if(v==0){
            return FormatFactory.format(0.0);
        }else if(v==1){
            String s=FormatFactory.format(o.getDomain(),format);
            if(s.isEmpty()){
                s=FormatFactory.format(1.0);
            }
            return s;
        }else if(v==-1){
            String s=FormatFactory.format(o.getDomain(),format);
            if(s.isEmpty()){
                s=FormatFactory.format(1.0);
            }
            return "-"+s;
        }else{
            String s = FormatFactory.format(o.getDomain(), format);
            if(s.isEmpty()){
                s=FormatFactory.format(o.getValue());
            }else{
                s=FormatFactory.format(o.getValue())+" * "+s;
            }
            return s;
        }
    }
    
}
