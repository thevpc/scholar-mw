package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;

public interface ScalarProductCache {

    Matrix toMatrix();

    TVector<Complex> column(int column);

    TVector<Complex> getColumn(int column);

    TVector<Complex> getRow(int row);

    TVector<Complex> row(int row);

    Complex apply(int p, int n);

    Complex gf(int p, int n);

    Complex fg(int n, int p);


    ScalarProductCache evaluate(ScalarProductOperator sp, Expr[] fn, Expr[] gp, boolean hermitian, AxisXY axis, ProgressMonitor monitor);

}
