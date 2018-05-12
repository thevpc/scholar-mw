/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.DebugFormat;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.DDyIntegralX;

/**
 *
 * @author vpc
 */
public class DDyIntegralXFormatter extends AbstractFormatter<DDyIntegralX> {

    @Override
    public void format(StringBuilder sb, DDyIntegralX o, FormatParamSet format) {
        sb.append("DDyIntegralX(");
        FormatFactory.format(sb,o.getArg(),format);
        sb.append(",");
        sb.append(o.getIntegralXY().toString());
        sb.append(",");
        FormatFactory.format(sb,o.getX0(),format);
        sb.append(",");
        FormatFactory.format(sb,o.getX1(),format);
        sb.append(")");
    }

}
