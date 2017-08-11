package net.vpc.scholar.hadrumaths.interop.jblas;

import net.vpc.scholar.hadrumaths.AbstractMatrixFactory;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.MatrixFactory;

public class JBlasMatrixFactory extends AbstractMatrixFactory {
    public static final MatrixFactory INSTANCE =new JBlasMatrixFactory();

    private JBlasMatrixFactory() {
    }

    @Override
    public Matrix newMatrix(int rows, int columns) {
        return new JBlasMatrix(rows,columns);
    }
}
