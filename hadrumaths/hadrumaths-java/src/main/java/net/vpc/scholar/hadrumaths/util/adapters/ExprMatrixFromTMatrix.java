package net.vpc.scholar.hadrumaths.util.adapters;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 3/23/17.
 */
public class ExprMatrixFromTMatrix<T> extends TMatrixAdapter<T, Expr> implements ExprMatrix {
    private static final long serialVersionUID = 1L;

    public ExprMatrixFromTMatrix(TMatrix<T> matrix) {
        super(matrix, MathsBase.$EXPR);
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
                return ExprMatrixFromTMatrix.this.get(row, column).simplify(options);
            }
        });
    }
}
