/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;

/**
 * @author vpc
 */
public class StringObjectFormat extends AbstractObjectFormat<String> {

    @Override
    public void format(StringBuilder sb, String o, ObjectFormatParamSet format) {
        sb.append(o);
    }

}
