package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public abstract class AbstractExprMatrix extends AbstractTMatrix<Expr> implements ExprMatrix{
    public AbstractExprMatrix() {
    }

    @Override
    public TypeName getComponentType() {
        return MathsBase.$EXPR;
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
                return AbstractExprMatrix.this.get(row, column).simplify(options);
            }
        });
    }

}
