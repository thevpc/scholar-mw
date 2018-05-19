package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Chronometer;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.CacheEnabled;
import net.vpc.scholar.hadrumaths.util.LRUMap;
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
    private LRUMap<Expr, RewriteResult> cache = new LRUMap<Expr, RewriteResult>(Maths.Config.getSimplifierCacheSize());
    private boolean cacheEnabled = true;
    protected List<ExprRewriteListener> rewriteListeners = new ArrayList<>();
    protected List<ExprRewriteFailListener> rewriteFailListeners = new ArrayList<>();

    protected ExprRewriteListener listenerListAdapter = new ExprRewriteListener() {
        @Override
        public void onRewriteExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue) {
            for (ExprRewriteListener rewriteListener : rewriteListeners) {
                rewriteListener.onRewriteExpr(rewriter, oldValue, newValue);
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
        Maths.Config.addConfigChangeListener("cacheEnabled", cacheEnabledListener);
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
            if (Maths.Config.isDevelopmentMode()) {
                PlatformUtils.requireEqualsAndHashCode(e.getClass());
            }
            RewriteResult found = cache.get(e);
            if (found != null) {
                return found;
            }
            RewriteResult r = null;
            if (MAX_REWRITE_TIME_SECONDS > 0) {
                Chronometer c = Maths.chrono();
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
                Chronometer c = Maths.chrono();
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

    public ExprRewriteListener[] getRewriteListeners() {
        return rewriteListeners.toArray(new ExprRewriteListener[rewriteListeners.size()]);
    }

    @Override
    public ExpressionRewriter addRewriteListener(ExprRewriteListener listener) {
        rewriteListeners.add(listener);
        return this;
    }

    @Override
    public ExpressionRewriter removeRewriteListener(ExprRewriteListener listener) {
        rewriteListeners.remove(listener);
        return this;
    }

    public ExprRewriteFailListener[] getRewriteFailListeners() {
        return rewriteFailListeners.toArray(new ExprRewriteFailListener[rewriteFailListeners.size()]);
    }

    @Override
    public ExpressionRewriter addRewriteFailListener(ExprRewriteFailListener listener) {
        rewriteFailListeners.add(listener);
        return this;
    }

    @Override
    public ExpressionRewriter removeRewriteFailListener(ExprRewriteFailListener listener) {
        rewriteFailListeners.remove(listener);
        return this;
    }
}
