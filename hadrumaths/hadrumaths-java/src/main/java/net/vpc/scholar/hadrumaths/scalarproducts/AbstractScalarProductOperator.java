package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToMatrix;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;

/**
 * User: taha Date: 2 juil. 2003 Time: 11:58:07
 */
public abstract class AbstractScalarProductOperator implements ScalarProductOperator {
    private boolean hermitian;

    public AbstractScalarProductOperator(boolean hermitian) {
        this.hermitian = hermitian;
    }

    public abstract double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2);

    public double[] evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble[] f2) {
        double[] d = new double[f2.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalDD(domain, f1, f2[i]);
        }
        return d;
    }

    @Override
    public double[] evalDD(Domain domain, DoubleToDouble[] f1, DoubleToDouble f2) {
        return evalDD(domain, f2, f1);
    }

    @Override
    public Complex[] evalDC(Domain domain, DoubleToComplex f1, DoubleToComplex[] f2) {
        Complex[] d = new Complex[f2.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalDC(domain, f1, f2[i]);
        }
        return d;
    }

    @Override
    public Complex[] evalDC(Domain domain, DoubleToComplex[] f1, DoubleToComplex f2) {
        Complex[] d = new Complex[f1.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalDC(domain, f1[i], f2);
        }
        return d;
    }

    @Override
    public Complex[] evalVDC(Domain domain, DoubleToVector[] f1, DoubleToVector f2) {
        Complex[] d = new Complex[f1.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalVDC(domain, f1[i], f2);
        }
        return d;
    }

    @Override
    public Complex[] evalVDC(Domain domain, DoubleToVector f1, DoubleToVector[] f2) {
        Complex[] d = new Complex[f2.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalVDC(domain, f1, f2[i]);
        }
        return d;
    }

    @Override
    public double[] evalVDD(Domain domain, DoubleToVector f1, DoubleToVector[] f2) {
        double[] d = new double[f2.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalVDD(domain, f1, f2[i]);
        }
        return d;
    }

    @Override
    public double[] evalVDD(Domain domain, DoubleToVector[] f1, DoubleToVector f2) {
        double[] d = new double[f1.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalVDD(domain, f1[i], f2);
        }
        return d;
    }

    public Complex eval(Domain domain, Expr f1, Expr f2) {
        if (f1.isDD() && f2.isDD()) {
            return Complex.valueOf(evalDD(domain, f1.toDD(), f2.toDD()));
        }
        if (f1.isDC() && f2.isDC()) {
            return evalDC(domain, f1.toDC(), f2.toDC());
        }
        return evalDM(domain, f1.toDM(), f2.toDM());
    }

    public Complex eval(Expr f1, Expr f2) {
        return eval(null, f1, f2);
    }

//    public double process(DFunctionVector2D f1, DFunctionVector2D f2) {
//        return evalDD(f1.fx, f2.fx) + evalDD(f1.fy, f2.fy);
//    }
//
//    public double process(DomainXY domain, DFunctionVector2D f1, DFunctionVector2D f2) {
//        return evalDD(domain, f1.fx, f2.fx) + evalDD(domain, f1.fy, f2.fy);
//    }

    public Complex evalVDC(Domain domain, DoubleToVector f1, DoubleToVector f2) {
        return eval(domain, f1.getComponent(Axis.X), f2.getComponent(Axis.X)).add(eval(domain, f1.getComponent(Axis.Y), f2.getComponent(Axis.Y)));
    }

    @Override
    public double evalVDD(Domain domain, DoubleToVector f1, DoubleToVector f2) {
        return evalDD(domain, f1.getComponent(Axis.X).toDD(), f2.getComponent(Axis.X).toDD())
                + (evalDD(domain, f1.getComponent(Axis.Y).toDD(), f2.getComponent(Axis.Y).toDD())
        );
    }

    public Complex evalDM(Domain domain, DoubleToMatrix f1, DoubleToMatrix f2) {
        MutableComplex v = MutableComplex.Zero();
        ComponentDimension dim1 = ComponentDimension.min(f1.getComponentDimension(), f2.getComponentDimension());
        for (int c = 0; c < dim1.columns; c++) {
            for (int r = 0; r < dim1.rows; r++) {
                v.add(eval(domain, f1.getComponent(r, c), f2.getComponent(r, c)));
            }
        }
        return v.toComplex();
    }

    public double evalDD(DoubleToDouble f1, DoubleToDouble f2) {
        return evalDD(f1.getDomain(), f1, f2);
    }

    public TMatrix<Complex> eval(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return MathsBase.scalarProductCache(this, g, f, monitor);
    }

    public TMatrix<Complex> eval(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor) {
        return MathsBase.scalarProductCache(this, g.toArray(), f.toArray(), monitor);
    }

    public TMatrix<Complex> eval(TVector<Expr> g, TVector<Expr> f, AxisXY axis, ProgressMonitor monitor) {
        return MathsBase.scalarProductCache(this, g.toArray(), f.toArray(), axis, monitor);
    }

    public TMatrix<Complex> eval(Expr[] g, Expr[] f, AxisXY axis, ProgressMonitor monitor) {
        return MathsBase.scalarProductCache(this, g, f, axis, monitor);
    }

    public Complex evalDC(Domain domain, DoubleToComplex f1, DoubleToComplex f2) {
        DoubleToDouble r1 = f1.getRealDD();
        DoubleToDouble i1 = f1.getImagDD();
        DoubleToDouble r2 = f2.getRealDD();
        DoubleToDouble i2 = f2.getImagDD();
        if (this.hermitian) {
            return Complex.valueOf(
                    evalDD(domain, r1, r2) + evalDD(domain, i1, i2),
                    evalDD(domain, i1, r2) - evalDD(domain, r1, i2)
            );
        } else {
            return Complex.valueOf(
                    evalDD(domain, r1, r2) - evalDD(domain, i1, i2),
                    evalDD(domain, i1, r2) + evalDD(domain, r1, i2)
            );
        }
    }

    public Complex evalDC(DoubleToComplex f1, DoubleToComplex f2) {
        return evalDC(null, f1, f2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractScalarProductOperator that = (AbstractScalarProductOperator) o;

        return hermitian == that.hermitian;
    }

    @Override
    public int hashCode() {
        return (hermitian ? 1 : 0);
    }

    public boolean isHermitian() {
        return hermitian;
    }
}
