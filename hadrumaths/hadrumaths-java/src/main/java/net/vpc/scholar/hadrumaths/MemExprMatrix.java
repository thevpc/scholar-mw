package net.vpc.scholar.hadrumaths;

public class MemExprMatrix extends MemTMatrix<Expr> implements ExprMatrix{
    public MemExprMatrix(Expr[][] elements) {
        super(elements, MathsBase.EXPR_VECTOR_SPACE);
    }

    public MemExprMatrix(int rows, int cols) {
        super(rows, cols, MathsBase.EXPR_VECTOR_SPACE);
    }

    @Override
    public ExprMatrix simplify() {
        return simplify(null);
    }

    @Override
    public ExprMatrix simplify(SimplifyOptions options) {
        return (ExprMatrix) MathsBase.Config.getComplexMatrixFactory(MathsBase.$EXPR).newMatrix(getRowCount(), getColumnCount(), new TMatrixCell<Expr>() {
            @Override
            public Expr get(int row, int column) {
                return MemExprMatrix.this.get(row, column).simplify(options);
            }
        });
    }
}
