package net.vpc.scholar.hadrumaths.interop.ojalgo;

import net.vpc.scholar.hadrumaths.AbstractMatrix;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.MatrixFactory;
import net.vpc.scholar.hadrumaths.interop.jblas.JBlasMatrixFactory;
import org.ojalgo.matrix.ComplexMatrix;
import org.ojalgo.matrix.MatrixBuilder;
import org.ojalgo.scalar.ComplexNumber;

public class OjalgoMatrix extends AbstractMatrix {
    private ComplexMatrix base;

    public OjalgoMatrix(int rows, int cols) {
        MatrixBuilder<ComplexNumber> b = ComplexMatrix.FACTORY.getBuilder(rows, cols);
        base = (ComplexMatrix) b.build();
    }

    public OjalgoMatrix(ComplexMatrix base) {
        this.base = base;
    }

    @Override
    public Complex get(int row, int col) {
        ComplexNumber complexDouble = base.get(row, col);
        return Complex.valueOf(complexDouble.getReal(), complexDouble.getImaginary());
    }

    @Override
    public void set(int row, int col, Complex val) {
        base.replace(row, col, ComplexNumber.makeRectangular(val.getReal(), val.getImag()));
    }

    @Override
    public int getRowCount() {
        return base.getRowDim();
    }

    @Override
    public int getColumnCount() {
        return base.getColDim();
    }

    @Override
    protected MatrixFactory getDefaultFactory() {
        return JBlasMatrixFactory.INSTANCE;
    }

    @Override
    public Matrix invSolve() {
        return new OjalgoMatrix((ComplexMatrix) base.invert());
    }
}
