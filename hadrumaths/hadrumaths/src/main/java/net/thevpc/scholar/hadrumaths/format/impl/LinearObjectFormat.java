/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.params.DoubleObjectFormatParam;
import net.thevpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.thevpc.scholar.hadrumaths.format.params.XObjectFormatParam;
import net.thevpc.scholar.hadrumaths.format.params.YObjectFormatParam;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;

/**
 * @author vpc
 */
public class LinearObjectFormat implements ObjectFormat<Linear> {

    @Override
    public String format(Linear o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();

    }

    @Override
    public void format(Linear o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        XObjectFormatParam x = format.getParam(FormatFactory.X);
        YObjectFormatParam y = format.getParam(FormatFactory.Y);
        DoubleObjectFormatParam df = format.getParam(DoubleObjectFormatParam.class, false);
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
//            String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        String mul = pp.getOp() == null || pp.getOp().isEmpty() ? " " : (pp.getOp());
        long initialLength = context.length();

        boolean par = format.containsParam(FormatFactory.REQUIRED_PARS) || FormatFactory.requireAppendDomain(o, format);
        if (a == 0 && b == 0 && c == 0) {
            if (df != null) {
                context.append(df.getFormat().format(0));
                return;
            } else {
                context.append("0");
                return;
            }
        } else {
            boolean lastNbr = false;
            boolean par0 = FormatFactory.requireAppendDomain(o, format) && ((a != 0 ? 1 : 0) + (b != 0 ? 1 : 0) + (c != 0 ? 1 : 0) > 1);
            if (par0) {
                context.append("(");
            }
            String v;
            if (a == 1) {
                context.append(x.getName());
                lastNbr = true;
            } else if (a == -1) {
                context.append("-").append(x.getName());
                lastNbr = true;
                par = true;
            } else if (a == 0) {
                // dot nothing
            } else {
                if(a!=0) {
                    v = FormatFactory.toParamString(a, df, false, true, false);
                    if (v.length() > 0) {
                        context.append(v).append(mul).append(x.getName());
                        lastNbr = true;
                        if (v.startsWith("-")) {
                            par = true;
                        }
                    }
                }
            }
            if (b == 1) {
                if (a != 0) {
//                    sb.append(" ");
                    context.append("+");
//                    sb.append(" ");
                }
                context.append(y.getName());
                lastNbr = true;
            } else if (b == -1) {
                if (a != 0) {
//                    sb.append(" ");
                }
                context.append("-");
                if (a != 0) {
//                    sb.append(" ");
                }
                context.append(y.getName());
                lastNbr = true;
                par = true;
            } else if (b == 0) {
                // dot nothing
            } else {
                if (a != 0) {
//                    sb.append(" ");
                }
                if (!(b < 0) && a != 0) {
//                    sb.append(" + ");
                    context.append("+");
                }
                if(b!=0) {
                    v = FormatFactory.toParamString(b, df, context.length() > initialLength && b < 0, true, a != 0);
                    if (v.length() > 0) {
                        context.append(v).append(mul).append(y.getName());
                        lastNbr = true;
                        if (v.startsWith("-")) {
                            par = true;
                        }
                    }
                }
            }
            if (context.length() == initialLength && c == 1) {
                if (o.getDomain().isUnbounded()) {
                    context.append("1");
                    lastNbr = true;
                    return;
                } else {
                    context.format( o.getDomain(), format);
                    if (context.length() - initialLength > 0) {
                        return;
                    }
                    context.append("1");
                    return;
                }
            }
//            if (!(c <= 0)) {
//                sb.append(" + ");
//            }
            if(c!=0) {
                v = FormatFactory.toParamString(c, df, lastNbr, false, false/*a != 0 || b != 0*/);
                if (v.length() > 0) {
                    context.append(v);
                    if (v.startsWith("-")) {
                        par = true;
                    }
                }
            }
            if (par0) {
                context.append(")");
            }
        }
        FormatFactory.appendStarredDomain(context, o, format);
        if (par) {
            context.insert(initialLength, "(");
            context.append(")");
        }
    }
}
