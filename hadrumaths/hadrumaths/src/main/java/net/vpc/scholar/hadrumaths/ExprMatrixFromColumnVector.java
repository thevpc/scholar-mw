package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 3/23/17.
 */
public class ExprMatrixFromColumnVector extends MatrixFromColumnVector<Expr> implements ExprMatrix {
    private static final long serialVersionUID = 1L;

    public ExprMatrixFromColumnVector(Vector<Expr> vector) {
        super(vector);
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
                return ExprMatrixFromColumnVector.this.get(row, column).simplify(options);
            }
        });
    }
}
