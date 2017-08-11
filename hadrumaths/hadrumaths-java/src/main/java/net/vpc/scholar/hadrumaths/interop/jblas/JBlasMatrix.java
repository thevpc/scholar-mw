package net.vpc.scholar.hadrumaths.interop.jblas;

import net.vpc.scholar.hadrumaths.*;
import org.jblas.ComplexDouble;
import org.jblas.ComplexDoubleMatrix;

public class JBlasMatrix extends AbstractMatrix {
    private ComplexDoubleMatrix base;

    public JBlasMatrix(int rows, int cols) {
        base=new ComplexDoubleMatrix(rows,cols);
    }

    @Override
    public Complex get(int row, int col) {
        ComplexDouble complexDouble = base.get(row, col);
        return Complex.valueOf(complexDouble.real(), complexDouble.imag());
    }

    @Override
    public void set(int row, int col, Complex val) {
        base.put(row, col, new ComplexDouble(val.getReal(), val.getImag()));
    }

    @Override
    public int getRowCount() {
        return base.getRows();
    }

    @Override
    public int getColumnCount() {
        return base.getColumns();
    }

    @Override
    protected MatrixFactory getDefaultFactory() {
        return JBlasMatrixFactory.INSTANCE;
    }

}
