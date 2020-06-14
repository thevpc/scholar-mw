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
        this.out = out == null ? System.out : out;
    }

    private int getDepth(){
        return 0;//new Throwable().getStackTrace().length;
    }
    @Override
    public void onUnmodifiedExpr(ExpressionRewriter rewriter, Expr oldValue) {
        out = System.out;
        printPrefix();
        out.println(rewriter + " :: [UNMODIFIED] " + oldValue.getClass().getSimpleName() + "->UNMODIFIED" + "  :: " + oldValue);
    }

    protected void printPrefix() {
        int d = getDepth();
        for (int i = 0; i < d; i++) {
            out.print(' ');
        }
    }

    @Override
    public void onModifiedExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue, boolean bestEffort) {
//        System.out.println(rewriter + " :: [MODIFIED] " + (bestEffort ? "(*) " : "") + oldValue.getClass().getSimpleName() + "->" + newValue.getClass().getSimpleName() + "  :: " + oldValue + " ==> " + newValue);
        printPrefix();
        System.out.println(rewriter + " :: " + (bestEffort ? "[BEST_MODIF] " : "[PART_MODIF] ") + oldValue.getClass().getSimpleName() + "->" + newValue.getClass().getSimpleName() + "  :: " + oldValue + " ==> " + newValue);
    }

}
