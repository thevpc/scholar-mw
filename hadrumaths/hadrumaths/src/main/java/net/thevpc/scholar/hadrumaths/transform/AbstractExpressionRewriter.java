package net.thevpc.scholar.hadrumaths.transform;

import net.thevpc.common.util.Chronometer;
import net.thevpc.common.util.LRUMap;
import net.thevpc.common.util.TimeDuration;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.cache.CacheEnabled;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.ExpressionsDebug;
import net.thevpc.scholar.hadrumaths.util.PlatformUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 1/24/15.
 */
public abstract class AbstractExpressionRewriter implements ExpressionRewriter, CacheEnabled {
    public static int MAX_REWRITE_TIME_SECONDS = 1;
    private final LRUMap<Expr, RewriteResult>[] cache = new LRUMap[ExprType.values().length + 1];
    private final String name;
    public int maxIterations = 100;
    public int cacheSize = Maths.Config.getSimplifierCacheSize();
    protected List<ExprRewriteSuccessListener> rewriteSuccessListeners = new ArrayList<>();
    protected List<ExprRewriteFailListener> rewriteFailListeners = new ArrayList<>();
    protected ExprRewriteSuccessListener listenerSuccessListAdapter = new ExprRewriteSuccessListener() {
        @Override
        public void onModifiedExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue, boolean bestEffort) {
            for (ExprRewriteSuccessListener rewriteSuccessListener : rewriteSuccessListeners) {
                rewriteSuccessListener.onModifiedExpr(rewriter, oldValue, newValue, bestEffort);
            }
        }
    };
    protected ExprRewriteFailListener listenerFailListAdapter = new ExprRewriteFailListener() {
        @Override
        public void onUnmodifiedExpr(ExpressionRewriter rewriter, Expr oldValue) {
            for (ExprRewriteFailListener rewriteFailListener : rewriteFailListeners) {
                rewriteFailListener.onUnmodifiedExpr(rewriter, oldValue);
            }
        }
    };
    PropertyChangeListener cacheEnabledListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            boolean b = (boolean) evt.getNewValue();
            if (!b) {
                clearCache();
            }
        }
    };
    private boolean cacheEnabled = true;

    public AbstractExpressionRewriter(String name) {
        Maths.Config.addConfigChangeListener("cacheEnabled", cacheEnabledListener);
        this.name = name;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName(), cacheEnabled, maxIterations, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractExpressionRewriter that = (AbstractExpressionRewriter) o;
        return cacheEnabled == that.cacheEnabled &&
                maxIterations == that.maxIterations &&
                Objects.equals(name, that.name);
    }

    @Override
    protected void finalize() throws Throwable {
        Maths.Config.removeConfigChangeListener("cacheEnabled", cacheEnabledListener);
        super.finalize();
    }

    @Override
    public boolean isCacheEnabled() {
        return Maths.Config.isExpressionWriterCacheEnabled() && cacheEnabled;
    }


    @Override
    public void setCacheEnabled(boolean enabled) {
        this.cacheEnabled = enabled;
    }


    public int getCacheSize() {
        return cacheSize;
    }


    @Override
    public void setCacheSize(int size) {
        this.cacheSize = size;
        for (int i = 0; i < cache.length; i++) {
            if (cache[i] != null) {
                cache[i].resize(size);
            }
        }
    }

    public void clearCache() {
        for (int i = 0; i < cache.length; i++) {
            if (cache[i] != null) {
                cache[i].clear();
                cache[i] = null;
            }
        }
    }

    public Expr rewriteOrSame(Expr e, ExprType targetExprType) {
        Expr t = rewriteOrNull(e, targetExprType);
        return t == null ? e : t;
    }

    @Override
    public Expr rewriteOrNull(Expr e, ExprType targetExprType) {
        RewriteResult e2 = rewrite(e, targetExprType);
        return e2.isUnmodified() ? e : e2.getValue();
    }

    @Override
    public RewriteResult rewrite(Expr e, ExprType targetExprType) {
        if (isCacheEnabled()) {
            if (Maths.Config.isDevelopmentMode()) {
                PlatformUtils.requireEqualsAndHashCode(e.getClass());
            }
            int targetExprTypeOrdinal = targetExprType == null ? 0 : (targetExprType.ordinal() + 1);
            LRUMap<Expr, RewriteResult> cacheItem = cache[targetExprTypeOrdinal];
            if (cacheItem != null) {
                RewriteResult found = cacheItem.get(e);
                if (found != null) {
                    return found;
                }
            }
            RewriteResult r = rewriteFull(e, targetExprType);
            if (cacheItem == null) {
                cache[targetExprTypeOrdinal] = cacheItem = new LRUMap<>(cacheSize);
            }
            cacheItem.put(e, r);

            //TODO DEBUG
            if (ExpressionsDebug.DEBUG) {
//            List<PlatformUtils.Conflict> conflicts = PlatformUtils.resolveConflicts(cacheItem);
//            for (PlatformUtils.Conflict conflict : conflicts) {
//                List<Object> items = conflict.getItems();
//                boolean b = items.get(0).equals(items.get(1));
//            }
            }
            return r;
        } else {
            return rewriteFull(e, targetExprType);
        }
    }

    public RewriteResult rewriteFull(Expr e, ExprType targetExprType) {
        Expr curr = e;
        int maxIterations = this.maxIterations;
        if (maxIterations <= 0) {
            maxIterations = 1;
        }
        boolean simplified = false;
        RewriteResult next = null;
        Expr last = null;
        boolean debugExpressionRewrite = Maths.Config.isDebugExpressionRewrite();
        while (maxIterations > 0) {
//            String msg = DumpManager.getStackDepthWhites()+name+" :: REWRITE "+maxIterations+" :: "+e;
//            System.out.println(msg);
            next = rewriteOnce(curr, targetExprType);
            if (next.isRewritten() /*&& !next.equals(curr)*/) {
                simplified = true;
                if (debugExpressionRewrite) {
                    if (curr.equals(next.getValue())) {
                        next = rewriteOnce(curr, targetExprType);
                        throw new IllegalArgumentException("Expected Expression Unmodified but simplification returned : " + next.getType());
                    }
                }
                if (next.isBestEffort()) {
                    return next;
                }
                last = curr;
                curr = next.getValue();
                maxIterations--;
            } else {
                if (simplified) {
                    if (debugExpressionRewrite) {
                        if (curr.equals(last)) {
                            throw new IllegalArgumentException("Too many iterations. Would returned BestEffort in the last one.");
                        }
                    }
                    return RewriteResult.newVal(curr);
                }
                return next;
            }
        }
        return next;
    }

    public RewriteResult rewriteOnce(Expr e, ExprType targetExprType) {
        if (MAX_REWRITE_TIME_SECONDS > 0) {
            Chronometer c = Maths.chrono();
            RewriteResult r = rewriteImpl(e, targetExprType);
            c.stop();
            TimeDuration duration = c.getDuration();
            if (duration.getSeconds() > MAX_REWRITE_TIME_SECONDS) {
                System.err.println("Expression Rewrite Took too long : " + duration.getSeconds() + "s ; " + e + " ==> " +
                        (r.isUnmodified() ? "?unmodified?" : r.getValue())
                );
            }
            return r;
        } else {
            return rewriteImpl(e, targetExprType);
        }
    }

    public abstract RewriteResult rewriteImpl(Expr e, ExprType targetExprType);

    public ExprRewriteSuccessListener[] getRewriteSuccessListeners() {
        return rewriteSuccessListeners.toArray(new ExprRewriteSuccessListener[0]);
    }

    @Override
    public ExpressionRewriter addRewriteSuccessListener(ExprRewriteSuccessListener listener) {
        if (listener != null) {
            rewriteSuccessListeners.add(listener);
        }
        return this;
    }

    @Override
    public ExpressionRewriter removeRewriteSuccessListener(ExprRewriteSuccessListener listener) {
        if (listener != null) {
            rewriteSuccessListeners.remove(listener);
        }
        return this;
    }

    public ExprRewriteFailListener[] getRewriteFailListeners() {
        return rewriteFailListeners.toArray(new ExprRewriteFailListener[0]);
    }

    @Override
    public ExpressionRewriter addRewriteFailListener(ExprRewriteFailListener listener) {
        if (listener != null) {
            rewriteFailListeners.add(listener);
        }
        return this;
    }

    @Override
    public ExpressionRewriter removeRewriteFailListener(ExprRewriteFailListener listener) {
        if (listener != null) {
            rewriteFailListeners.remove(listener);
        }
        return this;
    }

    @Override
    public ExpressionRewriter addRewriteListener(ExprRewriteListener listener) {
        addRewriteSuccessListener(listener);
        addRewriteFailListener(listener);
        return this;
    }

    @Override
    public ExpressionRewriter removeRewriteListener(ExprRewriteListener listener) {
        removeRewriteSuccessListener(listener);
        removeRewriteFailListener(listener);
        return this;
    }


}
