package net.thevpc.scholar.hadrumaths;

public class MemExprMatrix extends MemMatrix<Expr> implements ExprMatrix {
    public MemExprMatrix(Expr[][] elements) {
        super(elements, Maths.EXPR_VECTOR_SPACE);
    }

    public MemExprMatrix(int rows, int cols) {
        super(rows, cols, Maths.EXPR_VECTOR_SPACE);
    }

    @Override
    public ExprMatrix simplify() {
        return simplify(null);
    }

    @Override
    public ExprMatrix simplify(SimplifyOptions options) {
        return (ExprMatrix) Maths.Config.getComplexMatrixFactory(Maths.$EXPR).newMatrix(getRowCount(), getColumnCount(), new MatrixCell<Expr>() {
            @Override
            public Expr get(int row, int column) {
                return MemExprMatrix.this.get(row, column).simplify(options);
            }
        });
    }
}
