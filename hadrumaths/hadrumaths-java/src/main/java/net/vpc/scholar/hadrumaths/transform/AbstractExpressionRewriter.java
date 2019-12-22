package net.vpc.scholar.hadrumaths.transform;

import net.vpc.common.util.Chronometer;
import net.vpc.common.util.LRUMap;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.cache.CacheEnabled;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpc on 1/24/15.
 */
public abstract class AbstractExpressionRewriter implements ExpressionRewriter, CacheEnabled {
    public static int MAX_REWRITE_TIME_SECONDS = 1;
    private LRUMap<Expr, RewriteResult> cache = new LRUMap<Expr, RewriteResult>(MathsBase.Config.getSimplifierCacheSize());
    private boolean cacheEnabled = true;
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
                cache.clear();
            }
        }
    };

    public AbstractExpressionRewriter() {
        MathsBase.Config.addConfigChangeListener("cacheEnabled", cacheEnabledListener);
    }

    @Override
    protected void finalize() throws Throwable {
        MathsBase.Config.removeConfigChangeListener("cacheEnabled", cacheEnabledListener);
        super.finalize();
    }

    @Override
    public boolean isCacheEnabled() {
        return MathsBase.Config.isExpressionWriterCacheEnabled() && cacheEnabled;
    }

    @Override
    public void setCacheEnabled(boolean enabled) {
        this.cacheEnabled = enabled;
    }

    public int getCacheSize() {
        return cache.size();
    }

    @Override
    public void setCacheSize(int size) {
        cache.resize(size);
    }

    public void clearCache() {
        cache.clear();
    }

    public Expr rewriteOrSame(Expr e) {
        Expr t = rewriteOrNull(e);
        return t == null ? e : t;
    }

    @Override
    public Expr rewriteOrNull(Expr e) {
        return rewrite(e).getValueOrNull();
    }

    @Override
    public RewriteResult rewrite(Expr e) {
        if (isCacheEnabled()) {
            if (MathsBase.Config.isDevelopmentMode()) {
                PlatformUtils.requireEqualsAndHashCode(e.getClass());
            }
            RewriteResult found = cache.get(e);
            if (found != null) {
                return found;
            }
            RewriteResult r = null;
            if (MAX_REWRITE_TIME_SECONDS > 0) {
                Chronometer c = MathsBase.chrono();
                r = rewriteImpl(e);
                c.stop();
                if (c.getSeconds() > MAX_REWRITE_TIME_SECONDS) {
                    System.err.println("Expression Rewrite Took too long : " + c.getSeconds() + "s ; " + e + " ==> " + r.getValue());
                }
            } else {
                r = rewriteImpl(e);
            }
            cache.put(e, r);
            return r;
        } else {
            RewriteResult r = null;
            if (MAX_REWRITE_TIME_SECONDS > 0) {
                Chronometer c = MathsBase.chrono();
                r = rewriteImpl(e);
                c.stop();
                if (c.getSeconds() > MAX_REWRITE_TIME_SECONDS) {
                    System.err.println("Expression Rewrite Took too long : " + c.getSeconds() + "s ; " + e + " ==> " + r.getValue());
                }
            } else {
                r = rewriteImpl(e);
            }
            return r;
        }
    }

    public abstract RewriteResult rewriteImpl(Expr e);

    public ExprRewriteSuccessListener[] getRewriteSuccessListeners() {
        return rewriteSuccessListeners.toArray(new ExprRewriteSuccessListener[0]);
    }

    @Override
    public ExpressionRewriter addRewriteSuccessListener(ExprRewriteSuccessListener listener) {
        if(listener!=null) {
            rewriteSuccessListeners.add(listener);
        }
        return this;
    }

    @Override
    public ExpressionRewriter removeRewriteSuccessListener(ExprRewriteSuccessListener listener) {
        if(listener!=null) {
            rewriteSuccessListeners.remove(listener);
        }
        return this;
    }

    public ExprRewriteFailListener[] getRewriteFailListeners() {
        return rewriteFailListeners.toArray(new ExprRewriteFailListener[0]);
    }

    @Override
    public ExpressionRewriter addRewriteFailListener(ExprRewriteFailListener listener) {
        if(listener!=null) {
            rewriteFailListeners.add(listener);
        }
        return this;
    }

    @Override
    public ExpressionRewriter removeRewriteFailListener(ExprRewriteFailListener listener) {
        if(listener!=null) {
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
