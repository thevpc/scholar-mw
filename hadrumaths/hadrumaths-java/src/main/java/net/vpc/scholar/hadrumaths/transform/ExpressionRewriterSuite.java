package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.util.CacheEnabled;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpc on 5/5/14.
 */
public class ExpressionRewriterSuite extends AbstractExpressionRewriter {
    public List<ExpressionRewriter> sets = new ArrayList<ExpressionRewriter>();
    private String name;

    public ExpressionRewriterSuite(String name) {
        this.name = name;
    }

    @Override
    public RewriteResult rewriteImpl(Expr e) {
        Expr curr = e;
        boolean modified = false;
        int bestEffort=0;
        for (ExpressionRewriter set : sets) {
            RewriteResult rr = set.rewrite(curr);
            if(rr.isUnmodified()){
                bestEffort++;
            }else if(rr.isBestEffort()) {
                modified = true;
                curr = rr.getValue();
                bestEffort++;
            }else{
                modified=true;
                curr = rr.getValue();
            }
        }
        if(modified){
            if(bestEffort==sets.size()){
                return RewriteResult.bestEffort(curr);
            }
            return RewriteResult.newVal(curr);
        }
        return RewriteResult.unmodified(e);
    }

    public void clear() {
        sets.clear();
    }

    public void add(ExpressionRewriter set) {
        sets.add(set);
    }

    public List<ExpressionRewriter> toList() {
        return new ArrayList<ExpressionRewriter>(sets);
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpressionRewriterSuite)) return false;

        ExpressionRewriterSuite that = (ExpressionRewriterSuite) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (sets != null ? !sets.equals(that.sets) : that.sets != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sets != null ? sets.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public void setCacheEnabled(boolean enabled) {
        super.setCacheEnabled(enabled);
        for (ExpressionRewriter x : sets) {
            if (x instanceof CacheEnabled) {
                ((CacheEnabled) x).setCacheEnabled(enabled);
            }
        }
    }

    @Override
    public void clearCache() {
        super.clearCache();
        for (ExpressionRewriter x : sets) {
            if (x instanceof CacheEnabled) {
                ((CacheEnabled) x).clearCache();
            }
        }
    }

}
