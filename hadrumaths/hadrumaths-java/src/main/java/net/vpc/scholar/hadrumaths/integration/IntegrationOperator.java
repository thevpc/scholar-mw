package net.vpc.scholar.hadrumaths.integration;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;

/**
 * User: taha Date: 2 juil. 2003 Time: 11:58:07
 */
public abstract class IntegrationOperator implements Dumpable{

    public abstract double evalDD(Domain domain, DoubleToDouble f1);

    public Complex eval(Domain domain, Expr f1) {
        if (f1.isDD()) {
            return Complex.valueOf(evalDD(domain, f1.toDD()));
        }
        if (f1.isDC()) {
            return evalDC(domain, f1.toDC());
        }
        return evalDM(domain, f1.toDM());
    }

    public Complex eval(Expr f1) {
        return eval(null, f1);
    }

    public Complex evalVDC(DoubleToVector f1) {
        return eval(f1.getComponent(Axis.X)).add(eval(f1.getComponent(Axis.Y)));
    }

    public Complex evalDM(Domain domain, DoubleToMatrix f1) {
        MutableComplex v = MutableComplex.Zero();
        ComponentDimension dim1 = f1.getComponentDimension();
        for (int c = 0; c < dim1.columns; c++) {
            for (int r = 0; r < dim1.rows; r++) {
                v.add(eval(domain, f1.getComponent(r, c)));
            }
        }
        return v.toComplex();
    }

    public double evalDD(DoubleToDouble f1) {
        return evalDD(f1.getDomain(), f1);
    }

    public Complex evalDC(Domain domain, DoubleToComplex f1) {
        DoubleToDouble r1 = f1.getRealDD();
        DoubleToDouble i1 = f1.getImagDD();
        return Complex.valueOf(
                evalDD(domain, r1) + evalDD(domain, i1),
                evalDD(domain, r1) - evalDD(domain, i1)
        );
    }

    public Complex eval(DoubleToComplex f1) {
        return evalDC(null, f1);
    }
    public abstract ExpressionRewriter getExpressionRewriter();

}
