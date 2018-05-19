package net.vpc.scholar.hadrumaths.interop.ojalgo;

import net.vpc.scholar.hadrumaths.*;

import java.io.File;

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

    @Override
    public Matrix load(File file) throws RuntimeIOException {
        Matrix m = MemMatrixFactory.INSTANCE.newMatrix(1, 1);
        m.read(file);
        return newMatrix(m);
    }
}
