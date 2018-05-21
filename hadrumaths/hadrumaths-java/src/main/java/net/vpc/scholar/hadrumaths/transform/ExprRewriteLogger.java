package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

import java.io.PrintStream;

public class ExprRewriteLogger implements ExprRewriteListener {
    public static final ExprRewriteLogger INSTANCE = new ExprRewriteLogger(System.out);
    private PrintStream out;

    public ExprRewriteLogger() {
        this(System.out);
    }

    public ExprRewriteLogger(PrintStream out) {
        this.out = out==null?System.out:out;
    }

    @Override
    public void onUnmodifiedExpr(ExpressionRewriter rewriter, Expr oldValue) {
        out = System.out;
        out.println(rewriter + " :: [UNMODIFIED] " + oldValue.getClass().getSimpleName() + "->UNMODIFIED" + "  :: " + oldValue);
    }

    @Override
    public void onModifiedExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue, boolean bestEffort) {
        System.out.println(rewriter + " :: [MODIFIED] " + (bestEffort?"(*) ":"")+ oldValue.getClass().getSimpleName() + "->" + newValue.getClass().getSimpleName() + "  :: " + oldValue + " ==> " + newValue);
    }

}
