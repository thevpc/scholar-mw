/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.DDx;
import net.vpc.scholar.hadrumaths.symbolic.DDz;

/**
 * @author vpc
 */
public class DDzFormatter extends AbstractFormatter<DDz> {
    public DDzFormatter() {
    }

    @Override
    public void format(StringBuilder sb, DDz o, FormatParamSet format) {
        sb.append("DDz(");
        FormatFactory.format(sb,o.getArg(),format);
        sb.append(",");
        FormatFactory.format(sb,o.getDefaultX(),format);
        sb.append(",");
        FormatFactory.format(sb,o.getDefaultY(),format);
        sb.append(")");
    }
}
