package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.*;

public interface ScalarProductCache {

    Matrix toMatrix();

    TVector<Complex> column(int column);

    TVector<Complex> getColumn(int column);

    TVector<Complex> getRow(int row);

    TVector<Complex> row(int row);

    Complex apply(int p, int n);

    Complex gf(int p, int n);

    Complex fg(int n, int p);


    ScalarProductCache evaluate(ScalarProductOperator sp, Expr[] fn, Expr[] gp, AxisXY axis, ProgressMonitor monitor);

}
