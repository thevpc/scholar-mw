package net.thevpc.scholar.hadrumaths.interop.ojalgo;

import net.thevpc.scholar.hadrumaths.AbstractComplexMatrixFactory;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.ComplexMatrixFactory;
import net.thevpc.scholar.hadrumaths.MemComplexMatrixFactory;

import java.io.File;
import java.io.UncheckedIOException;

public class OjalgoComplexMatrixFactory extends AbstractComplexMatrixFactory {
    public static final ComplexMatrixFactory INSTANCE = new OjalgoComplexMatrixFactory();

    private OjalgoComplexMatrixFactory() {
    }

    @Override
    public ComplexMatrix newMatrix(int rows, int columns) {
        OjalgoComplexMatrix ojalgoMatrix = new OjalgoComplexMatrix(rows, columns);
        ojalgoMatrix.setFactory(this);
        return ojalgoMatrix;
    }

    @Override
    public String getId() {
        return "ojalgo";
    }

    @Override
    public ComplexMatrix load(File file) throws UncheckedIOException {
        ComplexMatrix m = MemComplexMatrixFactory.INSTANCE.newMatrix(1, 1);
        m.read(file);
        return newMatrix(m);
    }
}
