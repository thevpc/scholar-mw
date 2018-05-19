/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.ProductFormat;
import net.vpc.scholar.hadrumaths.format.params.XFormat;
import net.vpc.scholar.hadrumaths.symbolic.AxisTransform;

/**
 * @author vpc
 */
public class AxisTransformFormatter implements Formatter<AxisTransform> {

    @Override
    public String format(AxisTransform o, FormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
//        XFormat x = (XFormat) format.getParam(FormatFactory.X);
//        ProductFormat pp = (ProductFormat) format.getParam(FormatFactory.PRODUCT_STAR);
//        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
//        switch (o.getDomainDimension()){
//            case 1:{
//                return "eval"+o.getAxis()[0]+"("+FormatFactory.format(o.getSubExpressions().get(0),FormatParamSet.EMPTY)+")";
//            }
//            case 2:{
//                return "eval"+o.getAxis()[0]+o.getAxis()[1]+"("+FormatFactory.format(o.getSubExpressions().get(0),FormatParamSet.EMPTY)+")";
//            }
//            case 3:{
//                return "eval"+o.getAxis()[0]+o.getAxis()[1]+o.getAxis()[2]+"("+FormatFactory.format(o.getSubExpressions().get(0),FormatParamSet.EMPTY)+")";
//            }
//            default:{
//                throw new IllegalArgumentException("Unsupported dim "+o.getDomainDimension());
//            }
//        }
    }

    @Override
    public void format(StringBuilder sb, AxisTransform o, FormatParamSet format) {
        XFormat x = format.getParam(FormatFactory.X);
        ProductFormat pp = format.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        switch (o.getDomainDimension()) {
            case 1: {
                sb.append("eval");
                sb.append(o.getAxis()[0]);
                sb.append("(");
                FormatFactory.format(sb, o.getSubExpressions().get(0), FormatParamSet.EMPTY);
                sb.append(")");
                break;
            }
            case 2: {
                sb.append("eval");
                sb.append(o.getAxis()[0]);
                sb.append(o.getAxis()[1]);
                sb.append("(");
                FormatFactory.format(sb, o.getSubExpressions().get(0), FormatParamSet.EMPTY);
                sb.append(")");
                break;
            }
            case 3: {
                sb.append("eval");
                sb.append(o.getAxis()[0]);
                sb.append(o.getAxis()[1]);
                sb.append(o.getAxis()[2]);
                sb.append("(");
                FormatFactory.format(sb, o.getSubExpressions().get(0), FormatParamSet.EMPTY);
                sb.append(")");
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported dim " + o.getDomainDimension());
            }
        }
    }
}
