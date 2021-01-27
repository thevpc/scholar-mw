package net.thevpc.scholar.hadrumaths.integration;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToMatrix;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;

//import net.thevpc.scholar.math.functions.dfxy.DFunctionVector2D;

/**
 * User: taha Date: 2 juil. 2003 Time: 11:58:07
 */
public interface IntegrationOperator extends HSerializable {

    double evalDD(Domain domain, DoubleToDouble f1);

    double[] evalDD(Domain domain, DoubleToDouble[] f2);

    Complex[] evalDC(Domain domain, DoubleToComplex[] f2);

    Complex[] evalVDC(Domain domain, DoubleToVector[] f1);

    double[] evalVDD(Domain domain, DoubleToVector[] f2);

    Complex eval(Domain domain, Expr f1);

    Complex eval(Expr f1);

    Complex evalVDC(DoubleToVector f1);

    Complex evalVDC(Domain domain, DoubleToVector f1);

    double evalVDD(Domain domain, DoubleToVector f1);

    Complex evalDM(Domain domain, DoubleToMatrix f1);

    double evalDD(DoubleToDouble f1);

    Complex[] eval(Expr[] g, ProgressMonitor monitor);

    Complex[] eval(Vector<Expr> g, ProgressMonitor monitor);

    Complex evalDC(Domain domain, DoubleToComplex f1);

    Complex eval(DoubleToComplex f1);

    Complex evalDC(DoubleToComplex f1);

    ExpressionRewriter getSimplifier();

}
