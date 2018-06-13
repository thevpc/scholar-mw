/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

/**
 * @author vpc
 */
public class DCstFunctionXYObjectFormat implements ObjectFormat<DoubleValue> {
    public DCstFunctionXYObjectFormat() {
    }

    @Override
    public String format(DoubleValue o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

    @Override
    public void format(StringBuilder sb, DoubleValue o, ObjectFormatParamSet format) {
        FormatFactory.format(sb, new Linear(0, 0, o.getValue(), o.getDomain()), format);
    }
}
