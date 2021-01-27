/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.thevpc.scholar.hadrumaths.format.params.XObjectFormatParam;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;

/**
 * @author vpc
 */
public class CosCosObjectFormat extends AbstractObjectFormat<CosXCosY> {

    @Override
    public void format(CosXCosY o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
//        sb.append(format(o,format));
        double amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        double d = o.getD();
        Domain domain = o.getDomain();
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
//            String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        String mul = pp.getOp() == null || pp.getOp().isEmpty() ? " " : (pp.getOp());
        if (amp == 0) {
            context.append("0");
            return;
        } else {
            String lastAction = "";
            if (Maths.isInt(amp) && amp == 1 || amp == -1) {
                if (amp == -1) {
                    context.append("-");
                    lastAction = "minus";
                }
            } else {
                context.format(amp, format);
                lastAction = "amp";
                if (a != 0 || b != 0 || c != 0 || d != 0) {
                    context.append(mul);
                    lastAction = mul;
                }
            }
            if (a != 0 || b != 0) {
                context.append("cos(");
                context.format(new Linear(a, 0, b, domain), format.add(FormatFactory.NO_DOMAIN).remove(FormatFactory.REQUIRED_PARS));
                context.append(")");
                lastAction = "cos";
            }
            if (c != 0 || d != 0) {
                if (lastAction.equals("amp") || lastAction.equals("cos")) {
                    context.append(mul);
                }
                context.append("cos(");
                context.format(new Linear(0, c, d, domain),
                        format.add(new XObjectFormatParam(format.getParam(FormatFactory.Y).getName())).add(FormatFactory.NO_DOMAIN).remove(FormatFactory.REQUIRED_PARS));
                context.append(")");
                lastAction = "cos";
            }
            if (lastAction.equals("amp") || lastAction.equals("cos")) {
                if (!FormatFactory.appendStarredDomain(context, domain, format)) {

                }
            } else {
                if (!FormatFactory.appendNonStarredDomain(context, domain, format)) {
                    context.append("1");
                }
            }

        }
    }
}
