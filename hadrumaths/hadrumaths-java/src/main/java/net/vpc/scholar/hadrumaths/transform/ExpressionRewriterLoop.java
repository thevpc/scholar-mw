package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.cache.CacheEnabled;

/**
 * Created by vpc on 5/5/14.
 */
public class ExpressionRewriterLoop extends AbstractExpressionRewriter {
    private ExpressionRewriter loopAction;
    private String name;
    private int maxIterations;


    public ExpressionRewriterLoop(ExpressionRewriter loopAction, String name, int maxIterations) {
        this.name = name;
        this.loopAction = loopAction;
        this.maxIterations = maxIterations;
    }

    @Override
    public RewriteResult rewriteImpl(Expr e) {
        int max = maxIterations;
        Expr curr = e;
        boolean modified = false;
        boolean bestEffort = false;
        while (max > 0) {
            RewriteResult rr = loopAction.rewrite(curr);
            if (rr.isBestEffort()) {
                curr = rr.getValue();
                bestEffort = true;
                break;
            }
            if (rr.isUnmodified()) {
                bestEffort = true;
                break;
            }
            modified = true;
            curr = rr.getValue();
            max--;
        }
        if (modified) {
            if (bestEffort) {
                return RewriteResult.bestEffort(curr);
            }
            return RewriteResult.newVal(curr);
        }
        return RewriteResult.unmodified(e);
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpressionRewriterLoop)) return false;

        ExpressionRewriterLoop that = (ExpressionRewriterLoop) o;

        if (maxIterations != that.maxIterations) return false;
        if (loopAction != null ? !loopAction.equals(that.loopAction) : that.loopAction != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = loopAction != null ? loopAction.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + maxIterations;
        return result;
    }

    @Override
    public void setCacheEnabled(boolean enabled) {
        super.setCacheEnabled(enabled);
        if (loopAction instanceof CacheEnabled) {
            ((CacheEnabled) loopAction).setCacheEnabled(enabled);
        }
    }


    @Override
    public void clearCache() {
        super.clearCache();
        if (loopAction instanceof CacheEnabled) {
            ((CacheEnabled) loopAction).clearCache();
        }
    }
}
