package net.vpc.scholar.hadrumaths.interop.ojalgo;

import net.vpc.scholar.hadrumaths.AbstractMatrixFactory;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.MatrixFactory;
import org.ojalgo.scalar.ComplexNumber;

public class OjalgoMatrixFactory extends AbstractMatrixFactory {
    public static final MatrixFactory INSTANCE = new OjalgoMatrixFactory();

    private OjalgoMatrixFactory() {
    }

    @Override
    public Matrix newMatrix(int rows, int columns) {
        OjalgoMatrix ojalgoMatrix = new OjalgoMatrix(rows, columns);
        ojalgoMatrix.setFactory(this);
        return ojalgoMatrix;
    }

    @Override
    public String getId() {
        return "ojalgo";
    }
}
