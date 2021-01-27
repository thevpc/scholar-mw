package net.thevpc.scholar.hadrumaths;

public class DefaultExprMatrix extends DefaultMatrix<Expr> implements ExprMatrix {
    public DefaultExprMatrix(MatrixModel<Expr> model) {
        super(model, Maths.EXPR_VECTOR_SPACE);
    }

    public DefaultExprMatrix(int rows, int cols, MatrixCell<Expr> model) {
        super(rows, cols, model, Maths.EXPR_VECTOR_SPACE);
    }

    @Override
    public ExprMatrix simplify() {
        return simplify(null);
    }

    @Override
    public ExprMatrix simplify(SimplifyOptions options) {
        return (ExprMatrix) getFactory().newMatrix(getRowCount(), getColumnCount(), new MatrixCell<Expr>() {
            @Override
            public Expr get(int row, int column) {
                return DefaultExprMatrix.this.get(row, column).simplify(options);
            }
        });
    }
}
