package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 1/25/15.
 */
public interface IConstantValue extends Expr {
    /**
     * return ComplexValue or null
     *
     * @return
     */
    public Complex getComplexConstant();

}
