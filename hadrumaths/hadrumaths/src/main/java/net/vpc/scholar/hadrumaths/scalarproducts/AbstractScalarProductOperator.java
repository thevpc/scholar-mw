package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;

//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;

/**
 * User: taha Date: 2 juil. 2003 Time: 11:58:07
 */
public abstract class AbstractScalarProductOperator implements ScalarProductOperator {
    private final boolean hermitian;

    public AbstractScalarProductOperator(boolean hermitian) {
        this.hermitian = hermitian;
    }

    public Complex evalDM(Domain domain, DoubleToMatrix f1, DoubleToMatrix f2, ProgressMonitor monitor) {
        MutableComplex v = MutableComplex.Zero();
        ComponentDimension dim1 = ComponentDimension.min(f1.getComponentDimension(), f2.getComponentDimension());
        if (ProgressMonitors.isSilent(monitor)) {
            for (int c = 0; c < dim1.columns; c++) {
                for (int r = 0; r < dim1.rows; r++) {
                    v.add(eval(domain, f1.getComponent(r, c), f2.getComponent(r, c)).toComplex());
                }
            }
            return v.toComplex();
        }
        ProgressMonitor m = monitor.incremental(dim1.columns);
        for (int c = 0; c < dim1.columns; c++) {
            for (int r = 0; r < dim1.rows; r++) {
                v.add(eval(domain, f1.getComponent(r, c), f2.getComponent(r, c)).toComplex());
            }
        }
        return v.toComplex();
    }

    public double evalDD(DoubleToDouble f1, DoubleToDouble f2) {
        return evalDD(f1.getDomain(), f1, f2);
    }

    public abstract double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2);

    public double[] evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble[] f2) {
        return evalDD(domain, f1, f2, null);
    }

    public double[] evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble[] f2, ProgressMonitor monitor) {
        if (ProgressMonitors.isSilent(monitor)) {
            double[] d = new double[f2.length];
            for (int i = 0; i < d.length; i++) {
                d[i] = evalDD(domain, f1, f2[i]);
            }
            return d;
        }
        ProgressMonitor m = monitor.incremental(f2.length);
        m.start();
        double[] d = new double[f2.length];
        for (int i = 0; i < d.length; i++) {
            m.inc();
            d[i] = evalDD(domain, f1, f2[i]);
        }
        m.terminate();
        return d;
    }

    @Override
    public double[] evalDD(Domain domain, DoubleToDouble[] f1, DoubleToDouble f2) {
        return evalDD(domain, f1, f2, null);
    }

    @Override
    public double[] evalDD(Domain domain, DoubleToDouble[] f1, DoubleToDouble f2, ProgressMonitor monitor) {
        return evalDD(domain, f2, f1, monitor);
    }

    @Override
    public Complex[] evalDC(Domain domain, DoubleToComplex f1, DoubleToComplex[] f2) {
        return evalDC(domain, f1, f2, null);
    }

    @Override
    public Complex[] evalDC(Domain domain, DoubleToComplex f1, DoubleToComplex[] f2, ProgressMonitor monitor) {
        if (ProgressMonitors.isSilent(monitor)) {
            Complex[] d = new Complex[f2.length];
            for (int i = 0; i < d.length; i++) {
                d[i] = evalDC(domain, f1, f2[i]);
            }
            return d;
        } else {
            ProgressMonitor m = monitor.incremental(f2.length);
            m.start();
            Complex[] d = new Complex[f2.length];
            for (int i = 0; i < d.length; i++) {
                m.inc();
                d[i] = evalDC(domain, f1, f2[i]);
            }
            m.terminate();
            return d;
        }
    }

    @Override
    public Complex[] evalDC(Domain domain, DoubleToComplex[] f1, DoubleToComplex f2) {
        return evalDC(domain, f1, f2, null);
    }

    @Override
    public Complex[] evalDC(Domain domain, DoubleToComplex[] f1, DoubleToComplex f2, ProgressMonitor monitor) {
        if (ProgressMonitors.isSilent(monitor)) {
            Complex[] d = new Complex[f1.length];
            for (int i = 0; i < d.length; i++) {
                d[i] = evalDC(domain, f1[i], f2);
            }
            return d;
        } else {
            ProgressMonitor m = monitor.incremental(f1.length);
            m.start();
            Complex[] d = new Complex[f1.length];
            for (int i = 0; i < d.length; i++) {
                m.inc();
                d[i] = evalDC(domain, f1[i], f2);
            }
            m.terminate();
            return d;
        }
    }

    public Complex evalDC(Domain domain, DoubleToComplex f1, DoubleToComplex f2) {
        DoubleToDouble r1 = f1.getRealDD();
        DoubleToDouble i1 = f1.getImagDD();
        DoubleToDouble r2 = f2.getRealDD();
        DoubleToDouble i2 = f2.getImagDD();
        if (this.hermitian) {
            return Complex.of(
                    evalDD(domain, r1, r2) + evalDD(domain, i1, i2),
                    evalDD(domain, i1, r2) - evalDD(domain, r1, i2)
            );
        } else {
            return Complex.of(
                    evalDD(domain, r1, r2) - evalDD(domain, i1, i2),
                    evalDD(domain, i1, r2) + evalDD(domain, r1, i2)
            );
        }
    }

    public Complex evalDC(DoubleToComplex f1, DoubleToComplex f2) {
        return evalDC(null, f1, f2);
    }

    public Complex evalVDC(Domain domain, DoubleToVector f1, DoubleToVector f2) {
        return eval(domain, f1.getComponent(Axis.X), f2.getComponent(Axis.X)).plus(eval(domain, f1.getComponent(Axis.Y), f2.getComponent(Axis.Y))).toComplex();
    }

    @Override
    public Complex[] evalVDC(Domain domain, DoubleToVector[] f1, DoubleToVector f2) {
        return evalVDC(domain, f1, f2, null);
    }

    @Override
    public Complex[] evalVDC(Domain domain, DoubleToVector[] f1, DoubleToVector f2, ProgressMonitor monitor) {
        return evalVDC(domain, f1, f2, null);
    }

    @Override
    public Complex[] evalVDC(Domain domain, DoubleToVector f1, DoubleToVector[] f2) {
        return evalVDC(domain, f1, f2, null);
    }

    @Override
    public Complex[] evalVDC(Domain domain, DoubleToVector f1, DoubleToVector[] f2, ProgressMonitor monitor) {
        if (ProgressMonitors.isSilent(monitor)) {
            Complex[] d = new Complex[f2.length];
            for (int i = 0; i < d.length; i++) {
                d[i] = evalVDC(domain, f1, f2[i]);
            }
            return d;
        }
        ProgressMonitor m = monitor.incremental(f2.length);
        Complex[] d = new Complex[f2.length];
        for (int i = 0; i < d.length; i++) {
            m.inc("eval dc scalar product {0}", i);
            d[i] = evalVDC(domain, f1, f2[i]);
        }
        m.terminate();
        return d;
    }

    @Override
    public double evalVDD(Domain domain, DoubleToVector f1, DoubleToVector f2) {
        return evalDD(domain, f1.getComponent(Axis.X).toDD(), f2.getComponent(Axis.X).toDD())
                + (evalDD(domain, f1.getComponent(Axis.Y).toDD(), f2.getComponent(Axis.Y).toDD())
        );
    }

//    public double process(DFunctionVector2D f1, DFunctionVector2D f2) {
//        return evalDD(f1.fx, f2.fx) + evalDD(f1.fy, f2.fy);
//    }
//
//    public double process(DomainXY domain, DFunctionVector2D f1, DFunctionVector2D f2) {
//        return evalDD(domain, f1.fx, f2.fx) + evalDD(domain, f1.fy, f2.fy);
//    }

    public Complex evalDM(Domain domain, DoubleToMatrix f1, DoubleToMatrix f2) {
        return evalDM(domain, f1, f2, null);
    }

    @Override
    public double[] evalVDD(Domain domain, DoubleToVector f1, DoubleToVector[] f2) {
        return evalVDD(domain, f1, f2, null);
    }

    @Override
    public double[] evalVDD(Domain domain, DoubleToVector f1, DoubleToVector[] f2, ProgressMonitor monitor) {
        if (ProgressMonitors.isSilent(monitor)) {
            double[] d = new double[f2.length];
            for (int i = 0; i < d.length; i++) {
                d[i] = evalVDD(domain, f1, f2[i]);
            }
            return d;
        }
        ProgressMonitor m = monitor.incremental(f2.length);
        double[] d = new double[f2.length];
        for (int i = 0; i < d.length; i++) {
            m.inc();
            d[i] = evalVDD(domain, f1, f2[i]);
        }
        m.terminate();
        return d;
    }

    @Override
    public double[] evalVDD(Domain domain, DoubleToVector[] f1, DoubleToVector f2) {
        return evalVDD(domain, f1, f2, null);
    }

    @Override
    public double[] evalVDD(Domain domain, DoubleToVector[] f1, DoubleToVector f2, ProgressMonitor monitor) {
        if (ProgressMonitors.isSilent(monitor)) {
            double[] d = new double[f1.length];
            for (int i = 0; i < d.length; i++) {
                d[i] = evalVDD(domain, f1[i], f2);
            }
            return d;
        }
        ProgressMonitor m = monitor.incremental(f1.length);
        double[] d = new double[f1.length];
        for (int i = 0; i < d.length; i++) {
            m.inc();
            d[i] = evalVDD(domain, f1[i], f2);
        }
        m.terminate();
        return d;
    }

    public NumberExpr eval(Domain domain, Expr f1, Expr f2) {
        ExprType f1d = f1.getType();
        ExprType f2d = f2.getType();
        switch (f1d) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                switch (f2d) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE: {
                        return Maths.expr(evalDD(domain, f1.toDD(), f2.toDD())).toNumber();
                    }
                    case COMPLEX_EXPR:
                    case COMPLEX_NBR:
                    case DOUBLE_COMPLEX: {
                        return evalDC(domain, f1.toDC(), f2.toDC());
                    }
                    case DOUBLE_CVECTOR:
                    case DOUBLE_CMATRIX: {
                        return evalDM(domain, f1.toDM(), f2.toDM());
                    }
                }
                break;
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                switch (f2d) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_DOUBLE:
                    case DOUBLE_COMPLEX: {
                        return evalDC(domain, f1.toDC(), f2.toDC());
                    }
                    case DOUBLE_CVECTOR:
                    case DOUBLE_CMATRIX: {
                        return evalDM(domain, f1.toDM(), f2.toDM());
                    }
                }
                break;
            }
            case DOUBLE_CVECTOR:
            case DOUBLE_CMATRIX: {
                switch (f2d) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_DOUBLE:
                    case DOUBLE_COMPLEX:
                    case DOUBLE_CVECTOR:
                    case DOUBLE_CMATRIX: {
                        return evalDM(domain, f1.toDM(), f2.toDM());
                    }
                }
                break;
            }
        }
        throw new IllegalArgumentException("Unsupported scalar product <" + f1d + "," + f2d + ">");
    }

    public NumberExpr eval(Expr f1, Expr f2) {
        return eval(null, f1, f2);
    }

    public ComplexMatrix eval(Expr[] g, Expr[] f, ProgressMonitor monitor) {
        return Maths.scalarProductCache(this, g, f, monitor);
    }

    public ComplexMatrix eval(Vector<Expr> g, Vector<Expr> f, ProgressMonitor monitor) {
        return Maths.scalarProductCache(this, g.toArray(), f.toArray(), monitor);
    }

    public ComplexMatrix eval(Vector<Expr> g, Vector<Expr> f, AxisXY axis, ProgressMonitor monitor) {
        return Maths.scalarProductCache(this, g.toArray(), f.toArray(), axis, monitor);
    }

    public ComplexMatrix eval(Expr[] g, Expr[] f, AxisXY axis, ProgressMonitor monitor) {
        return Maths.scalarProductCache(this, g, f, axis, monitor);
    }

    public boolean isHermitian() {
        return hermitian;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode() * 31 + (hermitian ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractScalarProductOperator that = (AbstractScalarProductOperator) o;

        return hermitian == that.hermitian;
    }
}
