package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.CExp;
import net.vpc.scholar.hadrumaths.symbolic.double2double.Linear;

public class CExpObjectFormat extends AbstractObjectFormat<CExp> {
    @Override
    public void format(CExp o, ObjectFormatContext context) {
        ObjectFormatParamSet format = context.getParams();
        Complex amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        Domain domain = o.getDomain();
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
//            String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        String mul = pp.getOp() == null || pp.getOp().isEmpty() ? " " : (pp.getOp());
        context.append(FormatFactory.format(amp, format));
        context.append(mul);
//        sb.append("exp( î").append(mul);
        context.append("exp(î").append(mul);
        context.format(new Linear(a, 0, 0, Domain.FULLX), format);
        context.append(")");
        context.append(mul);
//        sb.append("exp( î").append(mul);
        context.append("exp(î").append(mul);
        context.format(new Linear(0, b, 0, Domain.FULLXY), format);
        context.append(")");
    }
}
