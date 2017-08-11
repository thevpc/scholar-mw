package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadrumaths.ComponentDimension;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * User: taha Date: 2 juil. 2003 Time: 11:58:07
 */
public abstract class ScalarProductOperator implements Dumpable{

    public abstract double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2);

    public double[] evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble[] f2) {
        double[] d=new double[f2.length];
        for (int i = 0; i < d.length; i++) {
            d[i]=evalDD(domain,f1,f2[i]);
        }
        return d;
    }

    public Complex eval(boolean hermitian, Domain domain, Expr f1, Expr f2) {
        if (f1.isDD() && f2.isDD()) {
            return Complex.valueOf(evalDD(domain, f1.toDD(), f2.toDD()));
        }
        if (f1.isDC() && f2.isDC()) {
            return evalDC(hermitian, domain, f1.toDC(), f2.toDC());
        }
        return evalDM(hermitian, domain, f1.toDM(), f2.toDM());
    }

    public Complex eval(boolean hermitian, Expr f1, Expr f2) {
        return eval(hermitian, null, f1, f2);
    }

//    public double process(DFunctionVector2D f1, DFunctionVector2D f2) {
//        return evalDD(f1.fx, f2.fx) + evalDD(f1.fy, f2.fy);
//    }
//
//    public double process(DomainXY domain, DFunctionVector2D f1, DFunctionVector2D f2) {
//        return evalDD(domain, f1.fx, f2.fx) + evalDD(domain, f1.fy, f2.fy);
//    }

    public Complex evalVDC(boolean hermitian, DoubleToVector f1, DoubleToVector f2) {
        return eval(hermitian, f1.getComponent(Axis.X), f2.getComponent(Axis.X)).add(eval(hermitian, f1.getComponent(Axis.Y), f2.getComponent(Axis.Y)));
    }

    public Complex evalDM(boolean hermitian, Domain domain, DoubleToMatrix f1, DoubleToMatrix f2) {
        MutableComplex v = MutableComplex.Zero();
        ComponentDimension dim1 = ComponentDimension.min(f1.getComponentDimension(), f2.getComponentDimension());
        for (int c = 0; c < dim1.columns; c++) {
            for (int r = 0; r < dim1.rows; r++) {
                v.add(eval(hermitian, domain, f1.getComponent(r, c), f2.getComponent(r, c)));
            }
        }
        return v.toComplex();
    }

    public double evalDD(DoubleToDouble f1, DoubleToDouble f2) {
        return evalDD(f1.getDomain(), f1, f2);
    }

    public ScalarProductCache eval(Expr[] g, Expr[] f,boolean hermitian, ProgressMonitor monitor) {
        return Maths.scalarProductCache(hermitian, this, g, f, monitor);
    }

    public ScalarProductCache eval(boolean hermitian, TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return Maths.scalarProductCache(hermitian, this, g.toArray(), f.toArray(), monitor);
    }

    public ScalarProductCache eval(boolean hermitian, TVector<Expr> g, TVector<Expr> f, AxisXY axis, ProgressMonitor monitor) {
        return Maths.scalarProductCache(hermitian, this, g.toArray(), f.toArray(), axis,monitor);
    }

    public ScalarProductCache eval(boolean hermitian, Expr[] g, Expr[] f, AxisXY axis, ProgressMonitor monitor) {
        return Maths.scalarProductCache(hermitian, this, g, f, axis, monitor);
    }

    public Complex evalDC(boolean hermitian, Domain domain, DoubleToComplex f1, DoubleToComplex f2) {
//        if (domain != null) {
//            domain = f1.intersect(f2, domain);
//        } else {
//            domain = f1.intersect(f2);
//        }
//
//        if (domain.isEmpty()) {
//            return Complex.ZERO;
//        }
        DoubleToDouble r1 = f1.getReal();
        DoubleToDouble i1 = f1.getImag();
        DoubleToDouble r2 = f2.getReal();
        DoubleToDouble i2 = f2.getImag();
        return Complex.valueOf(
                evalDD(domain, r1, r2) + evalDD(domain, i1, i2),
                evalDD(domain, r1, i2) - evalDD(domain, i1, r2)
        );
    }

    public Complex eval(boolean hermitian, DoubleToComplex f1, DoubleToComplex f2) {
        return evalDC(hermitian, null, f1, f2);
    }
    public abstract ExpressionRewriter getExpressionRewriter();

}
