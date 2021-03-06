/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.params.XObjectFormatParam;
import net.thevpc.scholar.hadrumaths.format.params.YObjectFormatParam;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.UFunction;

import static net.thevpc.scholar.hadrumaths.Maths.expr;
import static net.thevpc.scholar.hadrumaths.Maths.mul;

/**
 * @author vpc
 */
public class UFunctionObjectFormat extends AbstractObjectFormat<UFunction> {
    public UFunctionObjectFormat() {
    }

    @Override
    public void format(UFunction o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        double amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        double d = o.getD();
        double e = o.getE();
        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS);

        if (par) {
            context.append("(");
        }
        context.format(mul(
                expr(amp), new Linear(a, 0, b, Domain.FULLX)
        ), format);
        context.append("/(");
        context.format(new Linear(c, d, e, Domain.FULLX),
                format
                        .add(new XObjectFormatParam("X*X"))
                        .add(new YObjectFormatParam("X"))
        );
        context.append(")");
        if (!o.getDomain().isUnbounded2()) {
            context.append(" * ");
            context.format( o.getDomain(), format.remove(FormatFactory.REQUIRED_PARS));
        }
        if (par) {
            context.append(")");
        }
    }
}
