/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.XObjectFormatParam;
import net.vpc.scholar.hadrumaths.format.params.YObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.Linear;
import net.vpc.scholar.hadrumaths.symbolic.Mul;
import net.vpc.scholar.hadrumaths.symbolic.UFunction;

/**
 * @author vpc
 */
public class UFunctionObjectFormat extends AbstractObjectFormat<UFunction> {
    public UFunctionObjectFormat() {
    }

    @Override
    public void format(StringBuilder sb, UFunction o, ObjectFormatParamSet format) {
        double amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        double d = o.getD();
        double e = o.getE();
        FormatFactory.format(sb, new Mul(
                Complex.valueOf(amp), new Linear(a, 0, b, Domain.FULLX)
        ), format);
        sb.append("/(");
        FormatFactory.format(sb, new Linear(c, d, e, Domain.FULLX),
                format
                        .add(new XObjectFormatParam("X*X"))
                        .add(new YObjectFormatParam("X"))
        );
        sb.append(")");
    }
}
