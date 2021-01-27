/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.params.DomainObjectFormatParam;
import net.thevpc.scholar.hadrumaths.format.params.DoubleObjectFormatParam;
import net.thevpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;

/**
 * @author vpc
 */
public class DomainObjectFormat implements ObjectFormat<Domain> {

    public DomainObjectFormat() {
    }

    @Override
    public String format(Domain o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();

    }

    private static void format(ObjectFormatContext context, Domain o, Axis axis, DoubleObjectFormatParam df, String x, String y, String z, ObjectFormatParamSet format, String emptyValue) {
        ObjectFormatParamSet format2 = format.add(FormatFactory.REQUIRED_FLOAT);
        switch (axis) {
            case X: {
                if (!Double.isNaN(o.xmin()) && !Double.isNaN(o.xmax()) && o.isUnboundedX() && emptyValue!=null) {
                    context.append(emptyValue);
                    return;
                }
                context.format(o.xmin(), format2);
                context.append("->");
                context.format(o.xmax(), format2);
                return;
            }
            case Y: {
                if (!Double.isNaN(o.ymin()) && !Double.isNaN(o.ymax()) && o.isUnboundedY() && emptyValue!=null) {
                    context.append(emptyValue);
                    return;
                }
                context.format( o.ymin(), format2);
                context.append("->");
                context.format( o.ymax(), format2);
                return;
            }
            case Z: {
                if (!Double.isNaN(o.zmin()) && !Double.isNaN(o.zmax()) && o.isUnboundedZ() && emptyValue!=null) {
                    context.append(emptyValue);
                    return;
                }
                context.format( o.zmin(), format2);
                context.append("->");
                context.format( o.zmax(), format2);
                return;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public void format(Domain o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
        String xf = format.getParam(FormatFactory.X).getName();
        String yf = format.getParam(FormatFactory.Y).getName();
        String zf = format.getParam(FormatFactory.Z).getName();
        DomainObjectFormatParam d = format.getParam(FormatFactory.GATE_DOMAIN);
        DoubleObjectFormatParam df = format.getParam(DoubleObjectFormatParam.class, false);
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
        switch (d.getType()) {
            case GATE: {
                if (!o.isNaN() && o.isUnbounded() && d.isIgnoreFull()) {
                    return;
                } else {
                    String emptyValue = null;//"FULL";
                    switch (o.getDimension()) {
                        case 1: {
                            context.append("domain(");
                            format(context, o, Axis.X, df, xf, yf, zf, format, emptyValue);
                            context.append(")");
                            return;
                        }
                        case 2: {
                            context.append("domain(");
                            format(context, o, Axis.X, df, xf, yf, zf, format, emptyValue);
//                            sb.append(", ");
                            context.append(",");
                            format(context, o, Axis.Y, df, xf, yf, zf, format, emptyValue);
                            context.append(")");
                            return;
                        }
                        case 3: {
                            context.append("domain(");
                            format(context, o, Axis.X, df, xf, yf, zf, format, emptyValue);
//                            sb.append(", ");
                            context.append(",");
                            format(context, o, Axis.Y, df, xf, yf, zf, format, emptyValue);
//                            sb.append(", ");
                            context.append(",");
                            format(context, o, Axis.Z, df, xf, yf, zf, format, emptyValue);
                            context.append(")");
                            return;
                        }
                        default: {
                            throw new IllegalArgumentException("Unsupported");
                        }
                    }
                }
            }
            case NONE: {
                return;
            }
        }
    }
}
