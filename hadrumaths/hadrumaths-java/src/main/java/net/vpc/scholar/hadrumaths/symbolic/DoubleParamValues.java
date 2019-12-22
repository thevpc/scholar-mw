package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.common.util.DoubleFilter;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

public interface DoubleParamValues {
    DoubleParam[] getParams();
    double[][] getParamValues();
}
