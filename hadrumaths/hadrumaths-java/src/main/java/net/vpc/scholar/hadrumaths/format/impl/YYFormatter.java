/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.ProductFormat;
import net.vpc.scholar.hadrumaths.format.params.YFormat;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.YY;

/**
 * @author vpc
 */
public class YYFormatter implements Formatter<YY> {

    @Override
    public String format(YY o, FormatParam... format) {
        FormatParamArray formatArray = new FormatParamArray(format);
        YFormat y = (YFormat) formatArray.getParam(FormatFactory.Y);
        ProductFormat pp = (ProductFormat) formatArray.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");

        String s = o.getDomain().equals(Domain.FULL(o.getDomainDimension())) ? "" : FormatFactory.format(o.getDomain(), format);
        if(s.isEmpty()){
            return y.getName();
        }else{
            return y.getName()+mul+s;
        }
    }

}
