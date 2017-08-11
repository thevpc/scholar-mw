package net.vpc.scholar.hadrumaths.interop.ojalgo;

import net.vpc.scholar.hadrumaths.AbstractMatrixFactory;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.MatrixFactory;
import net.vpc.scholar.hadrumaths.interop.jblas.JBlasMatrix;

public class OjalgoMatrixFactory extends AbstractMatrixFactory {
    public static final MatrixFactory INSTANCE =new OjalgoMatrixFactory();

    private OjalgoMatrixFactory() {
    }

    @Override
    public Matrix newMatrix(int rows, int columns) {
        return new OjalgoMatrix(rows,columns);
    }
}
