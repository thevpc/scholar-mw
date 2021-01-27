package net.thevpc.scholar.hadrumaths.scalarproducts;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.*;

public interface ScalarProductCache {

    ComplexMatrix toMatrix();

    ComplexVector column(int column);

    ComplexVector getColumn(int column);

    ComplexVector getRow(int row);

    ComplexVector row(int row);

    Complex apply(int p, int n);

    Complex gf(int p, int n);

    Complex fg(int n, int p);


    ScalarProductCache evaluate(ScalarProductOperator sp, Expr[] fn, Expr[] gp, AxisXY axis, ProgressMonitor monitor);

}
