package net.vpc.scholar.hadrumaths;

public interface ExprMatrix extends TMatrix<Expr> {
    TMatrix<Expr> simplify();
}
