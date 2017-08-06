/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.symbolic.AxisTransform;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.ProductFormat;
import net.vpc.scholar.hadrumaths.format.params.XFormat;

/**
 * @author vpc
 */
public class AxisTransformFormatter implements Formatter<AxisTransform> {

    @Override
    public String format(AxisTransform o, FormatParam... format) {
        FormatParamArray formatArray = new FormatParamArray(format);
        XFormat x = (XFormat) formatArray.getParam(FormatFactory.X);
        ProductFormat pp = (ProductFormat) formatArray.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        switch (o.getDomainDimension()){
            case 1:{
                return "eval"+o.getAxis()[0]+"("+FormatFactory.format(o.getSubExpressions().get(0))+")";
            }
            case 2:{
                return "eval"+o.getAxis()[0]+o.getAxis()[1]+"("+FormatFactory.format(o.getSubExpressions().get(0))+")";
            }
            case 3:{
                return "eval"+o.getAxis()[0]+o.getAxis()[1]+o.getAxis()[2]+"("+FormatFactory.format(o.getSubExpressions().get(0))+")";
            }
            default:{
                throw new IllegalArgumentException("Unsupported dim "+o.getDomainDimension());
            }
        }
    }

}
