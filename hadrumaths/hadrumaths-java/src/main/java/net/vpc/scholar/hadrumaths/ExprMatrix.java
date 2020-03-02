package net.vpc.scholar.hadrumaths;

public interface ExprMatrix extends Matrix<Expr> {
    ExprMatrix simplify();

    ExprMatrix simplify(SimplifyOptions options);
}
