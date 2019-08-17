package net.vpc.scholar.hadrumaths.integration;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToMatrix;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;

//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;

/**
 * User: taha Date: 2 juil. 2003 Time: 11:58:07
 */
public abstract class AbstractIntegrationOperator implements IntegrationOperator {

    public abstract double evalDD(Domain domain, DoubleToDouble f1);

    @Override
    public double[] evalDD(Domain domain, DoubleToDouble[] f2) {
        double[] d = new double[f2.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalDD(domain, f2[i]);
        }
        return d;
    }

    @Override
    public Complex[] evalDC(Domain domain, DoubleToComplex[] f2) {
        Complex[] d = new Complex[f2.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalDC(domain, f2[i]);
        }
        return d;
    }

    @Override
    public Complex[] evalVDC(Domain domain, DoubleToVector[] f1) {
        Complex[] d = new Complex[f1.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalVDC(domain, f1[i]);
        }
        return d;
    }

    @Override
    public double[] evalVDD(Domain domain, DoubleToVector[] f2) {
        double[] d = new double[f2.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = evalVDD(domain, f2[i]);
        }
        return d;
    }

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

//    public double process(DFunctionVector2D f1, DFunctionVector2D f2) {
//        return evalDD(f1.fx, f2.fx) + evalDD(f1.fy, f2.fy);
//    }
//
//    public double process(DomainXY domain, DFunctionVector2D f1, DFunctionVector2D f2) {
//        return evalDD(domain, f1.fx, f2.fx) + evalDD(domain, f1.fy, f2.fy);
//    }

    @Override
    public Complex evalVDC(Domain domain, DoubleToVector f1) {
        return eval(domain, f1.getComponent(Axis.X)).add(eval(domain, f1.getComponent(Axis.Y)));
    }

    @Override
    public double evalVDD(Domain domain, DoubleToVector f1) {
        return evalDD(domain, f1.getComponent(Axis.X).toDD())
                + (evalDD(domain, f1.getComponent(Axis.Y).toDD())
        );
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

    @Override
    public Complex[] eval(Expr[] g, ProgressMonitor monitor) {
        ProgressMonitor m = ProgressMonitorFactory.nonnull(monitor);
        m.start(null);
        Complex[] r = new Complex[g.length];
        for (int i = 0; i < g.length; i++) {
            m.setProgress(i, g.length, null);
            r[i] = eval(g[i]);
        }
        m.terminate(null);
        return r;
    }

    @Override
    public Complex[] eval(TVector<Expr> g, ProgressMonitor monitor) {
        return eval(g.toArray(), monitor);
    }

//    public TMatrix<Complex> eval(boolean hermitian, TVector<Expr> g, TVector<Expr> f, AxisXY axis, ProgressMonitor monitor) {
//        return Maths.scalarProductCache(hermitian, this, g.toArray(), f.toArray(), axis,monitor);
//    }
//
//    public Complex[] eval(boolean hermitian, Expr[] g, Expr[] f, AxisXY axis, ProgressMonitor monitor) {
//        ProgressMonitor m = ProgressMonitorFactory.nonnull(monitor);
//        m.start(null);
//        Complex[] r=new Complex[g.length];
//        for (int i = 0; i < g.length; i++) {
//            m.setProgress(i,g.length,null);
//            r[i]=eval(hermitian,g[i].toDV().getComponent(axis));
//        }
//        m.terminate(null);
//        return r;
//    }

    public Complex evalDC(Domain domain, DoubleToComplex f1) {
        DoubleToDouble r1 = f1.getRealDD();
        DoubleToDouble i1 = f1.getImagDD();
        return Complex.valueOf(evalDD(domain, r1), evalDD(domain, i1));
    }

    @Override
    public Complex evalDC(DoubleToComplex f1) {
        return evalDC(null, f1);
    }

    public abstract ExpressionRewriter getExpressionRewriter();


    public Complex evalVDC(DoubleToVector f1) {
        return eval(f1.getComponent(Axis.X)).add(eval(f1.getComponent(Axis.Y)));
    }

    public Complex eval(DoubleToComplex f1) {
        return evalDC(null, f1);
    }

}
