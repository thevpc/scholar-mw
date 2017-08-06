package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;

public interface ScalarProductCache {

    Matrix toMatrix();

    Vector column(int column);

    Vector getColumn(int column);

    Vector getRow(int row);

    Vector row(int row);

    Complex apply(int p, int n);

    Complex gf(int p, int n);

    Complex fg(int n, int p);


    void evaluate(ScalarProductOperator sp, Expr[] fn, Expr[] gp, boolean hermitian, AxisXY axis, ComputationMonitor monitor);

}
