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
import net.vpc.scholar.hadrumaths.format.params.XFormat;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.XX;

/**
 * @author vpc
 */
public class XXFormatter implements Formatter<XX> {

    @Override
    public String format(XX o, FormatParam... format) {
        FormatParamArray formatArray = new FormatParamArray(format);
        XFormat x = (XFormat) formatArray.getParam(FormatFactory.X);
        ProductFormat pp = (ProductFormat) formatArray.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");

        String s = o.getDomain().equals(Domain.FULL(o.getDomainDimension())) ? "" : FormatFactory.format(o.getDomain(), format);
        if(s.isEmpty()){
            return x.getName();
        }else{
            return x.getName()+mul+s;
        }
    }

}
