package net.thevpc.scholar.hadrumaths.transform;

import net.thevpc.scholar.hadrumaths.Expr;

import java.io.PrintStream;

public class ExprRewriteLogger implements ExprRewriteListener {
    public static final ExprRewriteLogger INSTANCE = new ExprRewriteLogger(System.out);
    private PrintStream out;

    public ExprRewriteLogger() {
        this(System.err);
    }

    public ExprRewriteLogger(PrintStream out) {
        this.out = out == null ? System.err : out;
    }

    private int getDepth(){
        return 0;//new Throwable().getStackTrace().length;
    }
    @Override
    public void onUnmodifiedExpr(ExpressionRewriter rewriter, Expr oldValue) {
        out = System.err;
        printPrefix();
        out.println(rewriter + " [ExprRewriteLogger] :: [UNMODIFIED] " + oldValue.getClass().getSimpleName() + "->UNMODIFIED" + "  :: " + oldValue);
    }

    protected void printPrefix() {
        int d = getDepth();
        for (int i = 0; i < d; i++) {
            out.print(' ');
        }
    }

    @Override
    public void onModifiedExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue, boolean bestEffort) {
        printPrefix();
        System.out.println(rewriter + " [ExprRewriteLogger] :: " + (bestEffort ? "[BEST_MODIF] " : "[PART_MODIF] ") + oldValue.getClass().getSimpleName() + "->" + newValue.getClass().getSimpleName() + "  :: " + oldValue + " ==> " + newValue);
    }

}
