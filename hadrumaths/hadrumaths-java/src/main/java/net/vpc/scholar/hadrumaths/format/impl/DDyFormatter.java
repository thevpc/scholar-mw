/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.DDx;
import net.vpc.scholar.hadrumaths.symbolic.DDy;

/**
 * @author vpc
 */
public class DDyFormatter extends AbstractFormatter<DDy> {
    public DDyFormatter() {
    }

    @Override
    public void format(StringBuilder sb, DDy o, FormatParamSet format) {
        sb.append("DDy(");
        FormatFactory.format(sb,o.getArg(),format);
        sb.append(",");
        FormatFactory.format(sb,o.getDefaultX(),format);
        sb.append(",");
        FormatFactory.format(sb,o.getDefaultZ(),format);
        sb.append(")");
    }
}
