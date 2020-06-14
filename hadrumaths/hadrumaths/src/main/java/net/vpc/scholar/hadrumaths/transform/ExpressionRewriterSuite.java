package net.vpc.scholar.hadrumaths.transform;

import net.vpc.common.tson.*;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.cache.CacheEnabled;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 5/5/14.
 */
public class ExpressionRewriterSuite extends AbstractExpressionRewriter {
    public List<ExpressionRewriter> sets = new ArrayList<ExpressionRewriter>();

    public ExpressionRewriterSuite(String name) {
        super(name);
    }

    public <T> void clear() {
        for (ExpressionRewriter set : sets.toArray(new ExpressionRewriter[0])) {
            remove(set);
        }
        sets.clear();
    }

    public void remove(ExpressionRewriter set) {
        sets.remove(set);
        set.removeRewriteSuccessListener(listenerSuccessListAdapter);
        set.removeRewriteFailListener(listenerFailListAdapter);
    }

    public void add(ExpressionRewriter set) {
        sets.add(set);
        set.addRewriteSuccessListener(listenerSuccessListAdapter);
        set.addRewriteFailListener(listenerFailListAdapter);
    }

    public List<ExpressionRewriter> toList() {
        return new ArrayList<ExpressionRewriter>(sets);
    }

    @Override
    public String toString() {
        return "Suite(" + getName() + ")";
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + sets.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExpressionRewriterSuite that = (ExpressionRewriterSuite) o;
        return Objects.equals(sets, that.sets);
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

    @Override
    public RewriteResult rewriteImpl(Expr e, ExprType targetExprType) {
        Expr curr = e;
        boolean modified = false;
        int bestEffort = 0;
        for (ExpressionRewriter set : sets) {
            RewriteResult rr = set.rewrite(curr, targetExprType);
            if (rr.isUnmodified()) {
                bestEffort++;
            } else if (rr.isBestEffort()) {
                modified = true;
                curr = rr.getValue();
                bestEffort++;
            } else {
                modified = true;
                curr = rr.getValue();
            }
        }
        if (modified) {
            if (bestEffort == sets.size()) {
                return RewriteResult.bestEffort(curr);
            }
            return RewriteResult.newVal(curr);
        }
        return RewriteResult.unmodified();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonArrayBuilder obj = Tson.array(getClass().getSimpleName(), new TsonElementBase[]{
                Tson.pair("name", Tson.elem(getName())),
                Tson.pair("itr", Tson.elem(getMaxIterations()))
        });
        for (ExpressionRewriter set : sets) {
            obj.add(context.elem(set));
        }
        return obj.build();
    }
}
