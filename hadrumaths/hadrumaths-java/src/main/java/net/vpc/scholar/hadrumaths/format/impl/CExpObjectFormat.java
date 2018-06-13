package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.ProductObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.CExp;
import net.vpc.scholar.hadrumaths.symbolic.Linear;

public class CExpObjectFormat extends AbstractObjectFormat<CExp> {
    @Override
    public void format(StringBuilder sb, CExp o, ObjectFormatParamSet format) {
        Complex amp = o.getAmp();
        double a = o.getA();
        double b = o.getB();
        Domain domain = o.getDomain();
        ProductObjectFormatParam pp = format.getParam(FormatFactory.PRODUCT_STAR);
        String mul = pp.getOp() == null ? "" : (" " + pp.getOp() + " ");
        sb.append(FormatFactory.format(amp, format));
        sb.append(mul);
        sb.append("exp( î").append(mul);
        FormatFactory.format(sb, new Linear(a, 0, 0, null), format);
        sb.append(")");
        sb.append(mul);
        sb.append("exp( î").append(mul);
        FormatFactory.format(sb, new Linear(0, b, 0, null), format);
        sb.append(")");
    }
}
