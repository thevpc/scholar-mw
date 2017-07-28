/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

/**
 *
 * @author vpc
 */
public class VDCxyFormatter implements Formatter<DoubleToVector> {

    public VDCxyFormatter() {
    }

    @Override
    public String format(DoubleToVector o, FormatParam... format) {
        StringBuilder b=new StringBuilder();
        for (int i = 0; i < o.getComponentSize(); i++) {
            if(i>0){
                b.append(", ");
            }
            b.append(FormatFactory.formatArg(o.getComponent(Axis.values()[i]), format));
        }
        return "(" + b.toString() + ")";
    }

}
