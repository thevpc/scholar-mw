/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.DoubleObjectFormatParam;
import net.vpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.vpc.scholar.hadrumaths.format.params.XObjectFormatParam;
import net.vpc.scholar.hadrumaths.format.params.YObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

/**
 * @author vpc
 */
public class LinearObjectFormat implements ObjectFormat<Linear> {

    @Override
    public String format(Linear o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();

    }

    @Override
    public void format(StringBuilder sb, Linear o, ObjectFormatParamSet format) {
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        XObjectFormatParam x = format.getParam(FormatFactory.X);
        YObjectFormatParam y = format.getParam(FormatFactory.Y);
        DoubleObjectFormatParam df = format.getParam(DoubleObjectFormatParam.class, false);
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        int initialLength = sb.length();

        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS) || FormatFactory.requireAppendDomain(o, format);
        if (a == 0 && b == 0 && c == 0) {
            if (df != null) {
                sb.append(df.getFormat().format(0));
                return;
            } else {
                sb.append("0");
                return;
            }
        } else {
            boolean lastNbr = false;
            boolean par0 = FormatFactory.requireAppendDomain(o, format) && ((a != 0 ? 1 : 0) + (b != 0 ? 1 : 0) + (c != 0 ? 1 : 0) > 1);
            if (par0) {
                sb.append("(");
            }
            String v;
            if (a == 1) {
                sb.append(x.getName());
                lastNbr = true;
            } else if (a == -1) {
                sb.append("-").append(x.getName());
                lastNbr = true;
                par = true;
            } else if (a == 0) {
                // dot nothing
            } else {
                v = FormatFactory.toParamString(a, df, false, true, false);
                if (v.length() > 0) {
                    sb.append(v).append(mul).append(x.getName());
                    lastNbr = true;
                    if (v.startsWith("-")) {
                        par = true;
                    }
                }
            }
            if (b == 1) {
                if (a != 0) {
                    sb.append(" ");
                    sb.append("+");
                    sb.append(" ");
                }
                sb.append(y.getName());
                lastNbr = true;
            } else if (b == -1) {
                if (a != 0) {
                    sb.append(" ");
                }
                sb.append("-");
                if (a != 0) {
                    sb.append(" ");
                }
                sb.append(y.getName());
                lastNbr = true;
                par = true;
            } else if (b == 0) {
                // dot nothing
            } else {
                if (a != 0) {
                    sb.append(" ");
                }
                if (!(b < 0) && a != 0) {
                    sb.append(" + ");
                }
                v = FormatFactory.toParamString(b, df, sb.length() > initialLength && b < 0, true, a != 0);
                if (v.length() > 0) {
                    sb.append(v).append(mul).append(y.getName());
                    lastNbr = true;
                    if (v.startsWith("-")) {
                        par = true;
                    }
                }
            }
            if (sb.length() == initialLength && c == 1) {
                if (o.getDomain().isFull()) {
                    sb.append("1");
                    lastNbr = true;
                    return;
                } else {
                    FormatFactory.format(sb, o.getDomain(), format);
                    if (sb.length() - initialLength > 0) {
                        return;
                    }
                    sb.append("1");
                    return;
                }
            }
//            if (!(c <= 0)) {
//                sb.append(" + ");
//            }
            v = FormatFactory.toParamString(c, df, lastNbr, true, a != 0 || b != 0);
            if (v.length() > 0) {
                sb.append(v);
                if (v.startsWith("-")) {
                    par = true;
                }
            }
            if (par0) {
                sb.append(")");
            }
        }
        FormatFactory.appendStarredDomain(sb, o, format);
        if (par) {
            sb.insert(initialLength, "(");
            sb.append(")");
        }
    }
}
