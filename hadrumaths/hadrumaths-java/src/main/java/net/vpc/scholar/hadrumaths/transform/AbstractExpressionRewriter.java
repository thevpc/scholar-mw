package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.CacheEnabled;
import net.vpc.scholar.hadrumaths.util.LRUMap;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

/**
 * Created by vpc on 1/24/15.
 */
public abstract class AbstractExpressionRewriter implements ExpressionRewriter,CacheEnabled {

    private LRUMap<Expr, RewriteResult> cache = new LRUMap<Expr, RewriteResult>(Maths.Config.getSimplifierCacheSize());
    private boolean cacheEnabled = true;


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