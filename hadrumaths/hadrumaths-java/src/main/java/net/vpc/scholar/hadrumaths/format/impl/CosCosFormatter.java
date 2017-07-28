/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.FormatParam;
import net.vpc.scholar.hadrumaths.format.FormatParamArray;
import net.vpc.scholar.hadrumaths.format.Formatter;
import net.vpc.scholar.hadrumaths.format.params.ProductFormat;
import net.vpc.scholar.hadrumaths.format.params.XFormat;
import net.vpc.scholar.hadrumaths.format.params.YFormat;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

/**
 *
 * @author vpc
 */
public class CosCosFormatter implements Formatter<CosXCosY> {

    @Override
    public String format(CosXCosY o, FormatParam... format) {
        double amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        double c = o.getC();
        double d = o.getD();
        Domain domain = o.getDomain();
        FormatParamArray formatArray = new FormatParamArray(format);
        ProductFormat pp = (ProductFormat)formatArray.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp()==null?"":(" "+pp.getOp()+" ");
        if (amp == 0) {
            return "0";
        } else {
            StringBuilder sb = new StringBuilder();
            if(Maths.isInt(amp) && amp==1 || amp==-1){
                if(amp==-1){
                    sb.append("-");
                }
            }else{
                sb.append(FormatFactory.format(amp,format));
                if (a != 0 || b != 0 || c != 0 || d != 0) {
                    sb.append(mul);
                }
            }
            if (a != 0 || b != 0) {
                sb.append("cos(");
                sb.append(FormatFactory.format(new Linear(a, 0, b, domain), formatArray.clone().set(FormatFactory.NO_DOMAIN).toArray()));
                sb.append(")");
                if (c != 0 || d != 0) {
                    sb.append(mul);
                }
            }
            if (c != 0 || d != 0) {
                sb.append("cos(");
                sb.append(FormatFactory.format(new Linear(0, c, d, domain),
                        formatArray.clone().set(new XFormat(((YFormat) formatArray.getParam(FormatFactory.Y)).getName())).set(FormatFactory.NO_DOMAIN).toArray()));
                sb.append(")");
            }
            String s = FormatFactory.format(domain, format);
            return s.length() > 0 ? (s + mul + sb.toString()) : sb.toString();
        }
    }
}
