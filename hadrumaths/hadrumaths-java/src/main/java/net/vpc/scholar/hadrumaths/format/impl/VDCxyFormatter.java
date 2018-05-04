/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.RequireParenthesesFormat;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

/**
 *
 * @author vpc
 */
public class VDCxyFormatter implements Formatter<DoubleToVector> {

    public VDCxyFormatter() {
    }

    @Override
    public String format(DoubleToVector o, FormatParamSet format) {
        StringBuilder sb=new StringBuilder();
        format(sb,o,format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, DoubleToVector o, FormatParamSet format) {
        sb.append("(");
        format=format.add(FormatFactory.REQUIRED_PARS);
        for (int i = 0; i < o.getComponentSize(); i++) {
            if(i>0){
                sb.append(", ");
            }
            FormatFactory.format(sb,o.getComponent(Axis.values()[i]), format);
        }
        sb.append(")");

    }
}
