/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.XFormat;
import net.vpc.scholar.hadrumaths.format.params.YFormat;
import net.vpc.scholar.hadrumaths.symbolic.DDx;
import net.vpc.scholar.hadrumaths.symbolic.Linear;
import net.vpc.scholar.hadrumaths.symbolic.Mul;
import net.vpc.scholar.hadrumaths.symbolic.UFunction;

/**
 * @author vpc
 */
public class UFunctionFormatter extends AbstractFormatter<UFunction> {
    public UFunctionFormatter() {
    }

    @Override
    public void format(StringBuilder sb, UFunction o, FormatParamSet format) {
        double amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        double d = o.getD();
        double e = o.getE();
        FormatFactory.format(sb,new Mul(
                Complex.valueOf(amp),new Linear(a,0,b, Domain.FULLX)
        ),format);
        sb.append("/(");
        FormatFactory.format(sb,new Linear(c,d,e, Domain.FULLX),
                format
                        .add(new XFormat("X*X"))
                        .add(new YFormat("X"))
        );
        sb.append(")");
    }
}
