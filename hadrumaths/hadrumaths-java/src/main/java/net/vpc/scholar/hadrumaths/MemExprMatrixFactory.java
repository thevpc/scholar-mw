package net.vpc.scholar.hadrumaths;

public class MemExprMatrixFactory extends AbstractExprMatrixFactory {
    public static final MemExprMatrixFactory INSTANCE = new MemExprMatrixFactory();

    @Override
    public ExprMatrix newMatrix(int rows, int columns) {
        return new MemExprMatrix(rows, columns);
    }
}
