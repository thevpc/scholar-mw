package net.vpc.scholar.hadrumaths.test;

import junit.framework.Assert;
import net.vpc.scholar.hadrumaths.cache.CacheEvaluator;
import net.vpc.scholar.hadrumaths.cache.HashValue;
import net.vpc.scholar.hadrumaths.cache.PersistenceCache;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import org.junit.Test;

/**
 * Created by vpc on 10/12/16.
 */
public class TestCache {

    @Test
    public void testCache() {
        PersistenceCache persistentCache = new PersistenceCache(null, "imen", null);

        persistentCache.setEnabled(true);
        Dumper dumper = new Dumper("cacheExample", Dumper.Type.SIMPLE);
        dumper.add("theta", Math.PI);
        persistentCache.getObjectCache(HashValue.valueOf(dumper), false).delete();
        MyCacheEvaluator evaluator = new MyCacheEvaluator();
        for (int i = 0; i < 8; i++) {
            Object v = persistentCache.evaluate("cacheExample", null, evaluator, dumper);
            System.out.println(v);
        }
        Assert.assertEquals(1, evaluator.getInvocationCount());

        persistentCache.setEnabled(false);
        Assert.assertNull(persistentCache.getObjectCache(HashValue.valueOf(dumper), false));

        evaluator = new MyCacheEvaluator();
        for (int i = 0; i < 8; i++) {
            Object v = persistentCache.evaluate("cacheExample", null, evaluator, dumper);
            System.out.println(v);
        }
        Assert.assertEquals(8, evaluator.getInvocationCount());
    }

    @Test
    public void testNoCache() {
        PersistenceCache persistentCache = new PersistenceCache(null, "imen", null);

        persistentCache.setEnabled(false);
        Dumper dumper = new Dumper("cacheExample", Dumper.Type.SIMPLE);
        dumper.add("theta", Math.PI);
        Assert.assertNull(persistentCache.getObjectCache(HashValue.valueOf(dumper), false));

        MyCacheEvaluator evaluator = new MyCacheEvaluator();
        for (int i = 0; i < 8; i++) {
            Object v = persistentCache.evaluate("cacheExample", null, evaluator, dumper);
            System.out.println(v);
        }
        Assert.assertEquals(8, evaluator.getInvocationCount());
    }

    private static class MyCacheEvaluator implements CacheEvaluator {
        public int invocationCount;

        @Override
        public Object evaluate(Object[] args) {
            invocationCount++;
            System.out.println("je calcule v = 3");
            return 3;
        }

        public int getInvocationCount() {
            return invocationCount;
        }
    }
}
