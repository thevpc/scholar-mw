package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.CacheEnabled;
import net.vpc.scholar.hadrumaths.util.LRUMap;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by vpc on 1/24/15.
 */
public abstract class AbstractExpressionRewriter implements ExpressionRewriter,CacheEnabled {

    private LRUMap<Expr, RewriteResult> cache = new LRUMap<Expr, RewriteResult>(Maths.Config.getSimplifierCacheSize());
    private boolean cacheEnabled = true;
    PropertyChangeListener cacheEnabledListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            boolean b = (boolean) evt.getNewValue();
            if(!b) {
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
        return Maths.Config.isExpressionWriterCacheEnabled()  && cacheEnabled;
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
        return t==null?e:t;
    }

    @Override
    public Expr rewriteOrNull(Expr e) {
        return rewrite(e).getValueOrNull();
    }

    @Override
    public RewriteResult rewrite(Expr e) {
        if (isCacheEnabled()) {
            if(Maths.Config.isDevelopmentMode()) {
                PlatformUtils.requireEqualsAndHashCode(e.getClass());
            }
            RewriteResult found = cache.get(e);
            if (found != null) {
                return found;
            }
            RewriteResult r = rewriteImpl(e);
            cache.put(e, r);
            return r;
        }else{
            return rewriteImpl(e);
        }
    }

    public abstract RewriteResult rewriteImpl(Expr e) ;
}