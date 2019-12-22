package net.vpc.scholar.hadrumaths;

public class DefaultExprMatrix extends DefaultTMatrix<Expr> implements ExprMatrix {
    public DefaultExprMatrix(TMatrixModel<Expr> model) {
        super(model, MathsBase.EXPR_VECTOR_SPACE);
    }

    public DefaultExprMatrix(int rows, int cols, TMatrixCell<Expr> model) {
        super(rows, cols, model, MathsBase.EXPR_VECTOR_SPACE);
    }

    @Override
    public ExprMatrix simplify() {
        return simplify(null);
    }

    @Override
    public ExprMatrix simplify(SimplifyOptions options) {
        return (ExprMatrix) getFactory().newMatrix(getRowCount(), getColumnCount(), new TMatrixCell<Expr>() {
            @Override
            public Expr get(int row, int column) {
                return DefaultExprMatrix.this.get(row, column).simplify(options);
            }
        });
    }
}
