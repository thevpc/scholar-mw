package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToMatrix;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

//import net.vpc.scholar.math.functions.dfxy.DFunctionVector2D;

/**
 * User: taha Date: 2 juil. 2003 Time: 11:58:07
 */
public interface ScalarProductOperator extends Dumpable {

    double evalDD(DoubleToDouble f1, DoubleToDouble f2);

    double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2);

    double[] evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble[] f2);

    double[] evalDD(Domain domain, DoubleToDouble[] f1, DoubleToDouble f2);

    Complex[] evalDC(Domain domain, DoubleToComplex f1, DoubleToComplex[] f2);

    Complex[] evalDC(Domain domain, DoubleToComplex[] f1, DoubleToComplex f2);

    Complex evalDC(Domain domain, DoubleToComplex f1, DoubleToComplex f2);

    Complex evalDC(DoubleToComplex f1, DoubleToComplex f2);

    Complex evalVDC(Domain domain, DoubleToVector f1, DoubleToVector f2);

    Complex[] evalVDC(Domain domain, DoubleToVector[] f1, DoubleToVector f2);

    Complex[] evalVDC(Domain domain, DoubleToVector f1, DoubleToVector[] f2);

    double evalVDD(Domain domain, DoubleToVector f1, DoubleToVector f2);

    Complex evalDM(Domain domain, DoubleToMatrix f1, DoubleToMatrix f2);

    double[] evalVDD(Domain domain, DoubleToVector f1, DoubleToVector[] f2);

    double[] evalVDD(Domain domain, DoubleToVector[] f1, DoubleToVector f2);

    Complex eval(Domain domain, Expr f1, Expr f2);

    Complex eval(Expr f1, Expr f2);

    TMatrix<Complex> eval(Expr[] g, Expr[] f, ProgressMonitor monitor);

    TMatrix<Complex> eval(TVector<Expr> g, TVector<Expr> f, ProgressMonitor monitor);

    TMatrix<Complex> eval(TVector<Expr> g, TVector<Expr> f, AxisXY axis, ProgressMonitor monitor);

    TMatrix<Complex> eval(Expr[] g, Expr[] f, AxisXY axis, ProgressMonitor monitor);


    //    ExpressionRewriter getExpressionRewriter();
    ExpressionRewriter getExpressionRewriter();

    boolean isHermitian();
}
