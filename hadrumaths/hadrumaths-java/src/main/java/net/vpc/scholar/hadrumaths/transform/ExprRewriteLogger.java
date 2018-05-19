package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

import java.io.PrintStream;

public class ExprRewriteLogger implements ExprRewriteListener {
    public static final ExprRewriteLogger INSTANCE = new ExprRewriteLogger(System.out);
    private PrintStream out;

    public ExprRewriteLogger(PrintStream out) {
        this.out = out;
    }

    @Override
    public void onUnmodifiedExpr(ExpressionRewriter rewriter, Expr oldValue) {
        out = System.out;
        out.println(rewriter + " :: " + oldValue.getClass().getSimpleName() + "->UNMODIFIED" + "  :: " + oldValue);
    }

    @Override
    public void onRewriteSuccessExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue) {
        System.out.println(rewriter + " :: " + oldValue.getClass().getSimpleName() + "->" + newValue.getClass().getSimpleName() + "  :: " + oldValue + "\n\t\t" + newValue + " :: " + oldValue.equals(newValue));
    }

}
