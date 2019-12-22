package net.vpc.scholar.hadrumaths;

public interface ExprMatrix extends TMatrix<Expr> {
    ExprMatrix simplify();
    ExprMatrix simplify(SimplifyOptions options);
}
