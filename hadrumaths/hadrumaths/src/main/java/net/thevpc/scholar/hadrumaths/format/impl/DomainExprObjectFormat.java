/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.params.DomainObjectFormatParam;
import net.thevpc.scholar.hadrumaths.format.params.DoubleObjectFormatParam;
import net.thevpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.thevpc.scholar.hadrumaths.symbolic.DomainExpr;

/**
 * @author vpc
 */
public class DomainExprObjectFormat implements ObjectFormat<DomainExpr> {
    public DomainExprObjectFormat() {
    }

    @Override
    public String format(DomainExpr o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();

    }

    private static void format(ObjectFormatContext sb, DomainExpr o, Axis axis, DoubleObjectFormatParam df, String x, String y, String z, ObjectFormatParamSet format) {
        switch (axis) {
            case X: {
//                if(o.isUnconstrainedX()){
//                    sb.append(emptyValue);
//                    return ;
//                }
                sb.format( o.xmin(), format);
                sb.append("->");
                sb.format( o.xmax(), format);
                return;
            }
            case Y: {
//                if(o.isUnconstrainedY()){
//                    sb.append(emptyValue);
//                }
                sb.format( o.ymin(), format);
                sb.append("->");
                sb.format( o.ymax(), format);
                return;
            }
            case Z: {
//                if(o.isUnconstrainedZ()){
//                    sb.append(emptyValue);
//                }
                sb.format( o.zmin(), format);
                sb.append("->");
                sb.format( o.zmax(), format);
                return;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public void format(DomainExpr o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        String xf = format.getParam(FormatFactory.X).getName();
        String yf = format.getParam(FormatFactory.Y).getName();
        String zf = format.getParam(FormatFactory.Z).getName();
        DomainObjectFormatParam d = format.getParam(FormatFactory.GATE_DOMAIN);
        DoubleObjectFormatParam df = format.getParam(DoubleObjectFormatParam.class, false);
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
        switch (d.getType()) {
            case GATE: {
//                if(o.isFull()){
//                    return;
//                }else {
                switch (o.getDimension()) {
                    case 1: {
                        context.append("domain(");
                        format(context, o, Axis.X, df, xf, yf, zf, format);
                        context.append(")");
                        return;
                    }
                    case 2: {
                        context.append("domain(");
                        format(context, o, Axis.X, df, xf, yf, zf, format);
//                        sb.append(", ");
                        context.append(",");
                        format(context, o, Axis.Y, df, xf, yf, zf, format);
                        context.append(")");
                        return;
                    }
                    case 3: {
                        context.append("domain(");
                        format(context, o, Axis.X, df, xf, yf, zf, format);
//                        sb.append(", ");
                        context.append(",");
                        format(context, o, Axis.Y, df, xf, yf, zf, format);
//                        sb.append(", ");
                        context.append(",");
                        format(context, o, Axis.Z, df, xf, yf, zf, format);
                        context.append(")");
                        return;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported");
                    }
//                    }
                }
            }
            case NONE: {
                return;
            }
        }
        return;
    }
}
