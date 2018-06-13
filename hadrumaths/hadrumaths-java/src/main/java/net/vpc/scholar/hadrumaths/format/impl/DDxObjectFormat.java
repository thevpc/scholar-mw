/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.DDx;

/**
 * @author vpc
 */
public class DDxObjectFormat extends AbstractObjectFormat<DDx> {
    public DDxObjectFormat() {
    }

    @Override
    public void format(StringBuilder sb, DDx o, ObjectFormatParamSet format) {
        sb.append("DDx(");
        FormatFactory.format(sb, o.getArg(), format);
        sb.append(",");
        FormatFactory.format(sb, o.getDefaultY(), format);
        sb.append(",");
        FormatFactory.format(sb, o.getDefaultZ(), format);
        sb.append(")");
    }
}
