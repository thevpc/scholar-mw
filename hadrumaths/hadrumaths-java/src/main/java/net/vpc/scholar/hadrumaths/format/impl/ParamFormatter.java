/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.symbolic.ParamExpr;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.Formatter;

/**
 * @author vpc
 */
public class ParamFormatter implements Formatter<ParamExpr> {
    public ParamFormatter() {
    }

    @Override
    public String format(ParamExpr o, FormatParam... format) {
        return o.getParamName();
    }

}
