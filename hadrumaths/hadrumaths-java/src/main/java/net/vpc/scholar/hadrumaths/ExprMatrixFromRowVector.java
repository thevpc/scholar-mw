package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class ExprMatrixFromRowVector extends TMatrixFromRowVector<Expr> implements ExprMatrix{
    private static final long serialVersionUID = 1L;

    public ExprMatrixFromRowVector(TVector<Expr> vector) {
        super(vector);
    }

    @Override
    public TypeName<Expr> getComponentType() {
        return MathsBase.$EXPR;
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
                return ExprMatrixFromRowVector.this.get(row,column).simplify(options);
            }
        });
    }
}
