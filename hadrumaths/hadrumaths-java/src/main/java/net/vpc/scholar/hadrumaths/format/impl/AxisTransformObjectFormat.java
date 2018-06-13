/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.vpc.scholar.hadrumaths.format.params.XObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.AxisTransform;

/**
 * @author vpc
 */
public class AxisTransformObjectFormat implements ObjectFormat<AxisTransform> {

    @Override
    public String format(AxisTransform o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
//        XObjectFormatParam x = (XObjectFormatParam) format.getParam(FormatFactory.X);
//        ProductObjectFormatParam pp = (ProductObjectFormatParam) format.getParam(FormatFactory.PRODUCT_STAR);
//        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
//        switch (o.getDomainDimension()){
//            case 1:{
//                return "eval"+o.getAxis()[0]+"("+FormatFactory.format(o.getSubExpressions().get(0),ObjectFormatParamSet.EMPTY)+")";
//            }
//            case 2:{
//                return "eval"+o.getAxis()[0]+o.getAxis()[1]+"("+FormatFactory.format(o.getSubExpressions().get(0),ObjectFormatParamSet.EMPTY)+")";
//            }
//            case 3:{
//                return "eval"+o.getAxis()[0]+o.getAxis()[1]+o.getAxis()[2]+"("+FormatFactory.format(o.getSubExpressions().get(0),ObjectFormatParamSet.EMPTY)+")";
//            }
//            default:{
//                throw new IllegalArgumentException("Unsupported dim "+o.getDomainDimension());
//            }
//        }
    }

    @Override
    public void format(StringBuilder sb, AxisTransform o, ObjectFormatParamSet format) {
        XObjectFormatParam x = format.getParam(FormatFactory.X);
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        switch (o.getDomainDimension()) {
            case 1: {
                sb.append("eval");
                sb.append(o.getAxis()[0]);
                sb.append("(");
                FormatFactory.format(sb, o.getSubExpressions().get(0), ObjectFormatParamSet.EMPTY);
                sb.append(")");
                break;
            }
            case 2: {
                sb.append("eval");
                sb.append(o.getAxis()[0]);
                sb.append(o.getAxis()[1]);
                sb.append("(");
                FormatFactory.format(sb, o.getSubExpressions().get(0), ObjectFormatParamSet.EMPTY);
                sb.append(")");
                break;
            }
            case 3: {
                sb.append("eval");
                sb.append(o.getAxis()[0]);
                sb.append(o.getAxis()[1]);
                sb.append(o.getAxis()[2]);
                sb.append("(");
                FormatFactory.format(sb, o.getSubExpressions().get(0), ObjectFormatParamSet.EMPTY);
                sb.append(")");
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported dim " + o.getDomainDimension());
            }
        }
    }
}
