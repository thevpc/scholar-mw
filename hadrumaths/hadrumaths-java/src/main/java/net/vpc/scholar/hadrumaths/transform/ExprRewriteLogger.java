package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.transform.ExprRewriteListener;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;

public class ExprRewriteLogger implements ExprRewriteListener {
    private long invocationCount;
    @Override
    public void onRewriteExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue) {
        invocationCount++;
        System.out.println(rewriter+":: "+oldValue.getClass().getSimpleName()+"->"+newValue.getClass().getSimpleName()+"  :: "+oldValue+"\n\t\t"+newValue+" :: "+oldValue.equals(newValue));
    }

    public long getInvocationCount() {
        return invocationCount;
    }
}
