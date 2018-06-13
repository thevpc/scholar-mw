/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.vpc.scholar.hadrumaths.format.params.XObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

/**
 * @author vpc
 */
public class CosCosObjectFormat extends AbstractObjectFormat<CosXCosY> {

    @Override
    public void format(StringBuilder sb, CosXCosY o, ObjectFormatParamSet format) {
//        sb.append(format(o,format));
        double amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        double d = o.getD();
        Domain domain = o.getDomain();
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        if (amp == 0) {
            sb.append("0");
            return;
        } else {
            String lastAction = "";
            if (Maths.isInt(amp) && amp == 1 || amp == -1) {
                if (amp == -1) {
                    sb.append("-");
                    lastAction = "minus";
                }
            } else {
                FormatFactory.format(sb, amp, format);
                if (a != 0 || b != 0 || c != 0 || d != 0) {
                    sb.append(mul);
                    lastAction = mul;
                }
            }
            if (a != 0 || b != 0) {
                sb.append("cos(");
                FormatFactory.format(sb, new Linear(a, 0, b, domain), format.add(FormatFactory.NO_DOMAIN).remove(FormatFactory.REQUIRED_PARS));
                sb.append(")");
                lastAction = "cos";
            }
            if (c != 0 || d != 0) {
                if (lastAction.equals("amp") || lastAction.equals("cos")) {
                    sb.append(mul);
                }
                sb.append("cos(");
                FormatFactory.format(sb, new Linear(0, c, d, domain),
                        format.add(new XObjectFormatParam(format.getParam(FormatFactory.Y).getName())).add(FormatFactory.NO_DOMAIN).remove(FormatFactory.REQUIRED_PARS));
                sb.append(")");
                lastAction = "cos";
            }
            if (lastAction.equals("amp") || lastAction.equals("cos")) {
                if (!FormatFactory.appendStarredDomain(sb, domain, format)) {

                }
            } else {
                if (!FormatFactory.appendNonStarredDomain(sb, domain, format)) {
                    sb.append("1");
                }
            }

        }
    }
}
