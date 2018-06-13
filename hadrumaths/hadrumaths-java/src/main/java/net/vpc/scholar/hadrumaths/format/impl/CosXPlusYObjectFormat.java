/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.CosXPlusY;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

/**
 * @author vpc
 */
public class CosXPlusYObjectFormat implements ObjectFormat<CosXPlusY> {

    @Override
    public String format(CosXPlusY o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();

//        double amp = o.getAmp();
//        double a = o.getA();
//        double b = o.getB();
//        double c = o.getC();
//        Domain domain = o.getDomain();
//        ProductObjectFormatParam pp = (ProductObjectFormatParam) format.getParam(FormatFactory.PRODUCT_STAR);
//        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
//        if (amp == 0) {
//            return "0";
//        } else {
//            StringBuilder sb = new StringBuilder();
//            if (Maths.isInt(amp) && amp == 1 || amp == -1) {
//                if (amp == -1) {
//                    sb.append("-");
//                }
//            } else {
//                sb.append(FormatFactory.format(amp, format));
//                if (a != 0 || b != 0 || c != 0) {
//                    sb.append(mul);
//                }
//            }
//            if (a != 0 || b != 0 || c != 0) {
//                sb.append("cos(");
//                sb.append(FormatFactory.format(new Linear(a, b, c, domain), format.add(FormatFactory.NO_DOMAIN)));
//                sb.append(")");
//            }
//            String s = FormatFactory.format(domain, format);
//            return s.length() > 0 ? (s + mul + sb.toString()) : sb.toString();
//        }
    }

    @Override
    public void format(StringBuilder sb, CosXPlusY o, ObjectFormatParamSet format) {
//        sb.append(format(o, format));
        double amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        Domain domain = o.getDomain();
        ProductObjectFormatParam pp = (ProductObjectFormatParam) format.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        if (amp == 0) {
            sb.append("0");
            return;
        } else {
            if (Maths.isInt(amp) && amp == 1 || amp == -1) {
                if (amp == -1) {
                    sb.append("-");
                }
            } else {
                FormatFactory.format(sb, amp, format);
                if (a != 0 || b != 0 || c != 0) {
                    sb.append(mul);
                }
            }
            if (a != 0 || b != 0 || c != 0) {
                sb.append("cos(");
                FormatFactory.format(sb, new Linear(a, b, c, domain), format.remove(FormatFactory.REQUIRED_PARS).add(FormatFactory.NO_DOMAIN));
                sb.append(")");
            }
            FormatFactory.appendStarredDomain(sb, domain, format);
        }
    }
}
