package net.vpc.scholar.hadrumaths.util.adapters;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 3/23/17.
 */
public class ExprMatrixFromMatrix<T> extends MatrixAdapter<T, Expr> implements ExprMatrix {
    private static final long serialVersionUID = 1L;

    public ExprMatrixFromMatrix(Matrix<T> matrix) {
        super(matrix, Maths.$EXPR);
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
                return ExprMatrixFromMatrix.this.get(row, column).simplify(options);
            }
        });
    }
}
