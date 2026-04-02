package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.reflect.NTypeName;

/**
 * Created by vpc on 3/23/17.
 */
public class ExprMatrixFromRowVector extends MatrixFromRowVector<Expr> implements ExprMatrix {
    private static final long serialVersionUID = 1L;

    public ExprMatrixFromRowVector(Vector<Expr> vector) {
        super(vector);
    }

    @Override
    public NTypeName<Expr> getComponentType() {
        return Maths.$EXPR;
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
                return ExprMatrixFromRowVector.this.get(row, column).simplify(options);
            }
        });
    }
}
