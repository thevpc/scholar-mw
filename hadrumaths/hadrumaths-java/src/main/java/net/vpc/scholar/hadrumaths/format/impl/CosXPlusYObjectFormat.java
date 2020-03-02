/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.double2double.CosXPlusY;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Linear;

/**
 * @author vpc
 */
public class CosXPlusYObjectFormat implements ObjectFormat<CosXPlusY> {

    @Override
    public void format(CosXPlusY o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
//        sb.append(format(o, format));
        double amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        Domain domain = o.getDomain();
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
//            String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        String mul = pp.getOp() == null || pp.getOp().isEmpty() ? " " : (pp.getOp());
        if (amp == 0) {
            context.append("0");
            return;
        } else {
            if (Maths.isInt(amp) && amp == 1 || amp == -1) {
                if (amp == -1) {
                    context.append("-");
                }
            } else {
                context.format(amp, format);
                if (a != 0 || b != 0 || c != 0) {
                    context.append(mul);
                }
            }
            if (a != 0 || b != 0 || c != 0) {
                context.append("cos(");
                context.format(new Linear(a, b, c, domain), format.remove(FormatFactory.REQUIRED_PARS).add(FormatFactory.NO_DOMAIN));
                context.append(")");
            }
            FormatFactory.appendStarredDomain(context, domain, format);
        }
    }
}
