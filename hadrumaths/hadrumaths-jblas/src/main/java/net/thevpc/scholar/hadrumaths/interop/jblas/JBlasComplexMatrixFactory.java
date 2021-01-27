package net.thevpc.scholar.hadrumaths.interop.jblas;

import net.thevpc.scholar.hadrumaths.AbstractComplexMatrixFactory;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.ComplexMatrixFactory;

public class JBlasComplexMatrixFactory extends AbstractComplexMatrixFactory {
    public static final ComplexMatrixFactory INSTANCE = new JBlasComplexMatrixFactory();

    private JBlasComplexMatrixFactory() {
    }

    @Override
    public String getId() {
        return "jblas";
    }

    @Override
    public ComplexMatrix newMatrix(int rows, int columns) {
        JBlasComplexMatrix jBlasMatrix = new JBlasComplexMatrix(rows, columns);
        jBlasMatrix.setFactory(this);
        return jBlasMatrix;
    }
}
