/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.DDzIntegralXY;

/**
 * @author vpc
 */
public class DDzIntegralXYFormatter extends AbstractFormatter<DDzIntegralXY> {

    @Override
    public void format(StringBuilder sb, DDzIntegralXY o, FormatParamSet format) {
        sb.append("DDzIntegralXY(");
        FormatFactory.format(sb, o.getArg(), format);
        sb.append(",");
        FormatFactory.format(sb, o.getIntegral().toString(), format);
        sb.append(",");
        FormatFactory.format(sb, o.getX0(), format);
        sb.append("->");
        FormatFactory.format(sb, o.getX1(), format);
        sb.append(",");
        FormatFactory.format(sb, o.getY0(), format);
        sb.append("->");
        FormatFactory.format(sb, o.getY1(), format);
        sb.append(")");
    }

}
