package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.reflect.NTypeName;

public abstract class AbstractExprMatrix extends AbstractMatrix<Expr> implements ExprMatrix {
    public AbstractExprMatrix() {
    }

    @Override
    public NTypeName getComponentType() {
        return Maths.$EXPR;
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
                return AbstractExprMatrix.this.get(row, column).simplify(options);
            }
        });
    }

}
