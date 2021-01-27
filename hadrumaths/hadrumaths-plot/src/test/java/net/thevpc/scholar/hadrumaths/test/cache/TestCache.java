package net.thevpc.scholar.hadrumaths.test.cache;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.cache.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Created by vpc on 10/12/16.
 */
public class TestCache {

    @Test
    public void testCache() {
        PersistenceCache persistentCache = Maths.persistenceCache().name("imen").build();

        persistentCache.setEnabled(true);
        CacheKey key = CacheKey.obj("cacheExample","theta",Math.PI);
        persistentCache.getObjectCache(key, false).delete();
        MyCacheEvaluator evaluator = new MyCacheEvaluator();
        for (int i = 0; i < 8; i++) {
            Object v = persistentCache.evaluate("cacheExample", null, evaluator, key);
            System.out.println(v);
        }
        Assertions.assertEquals(1, evaluator.getInvocationCount());

        persistentCache.setEnabled(false);
        Assertions.assertNull(persistentCache.getObjectCache(key, false));

        evaluator = new MyCacheEvaluator();
        for (int i = 0; i < 8; i++) {
            Object v = persistentCache.evaluate("cacheExample", null, evaluator, key);
            System.out.println(v);
        }
        Assertions.assertEquals(8, evaluator.getInvocationCount());
    }

    @Test
    public void testNoCache() {
        PersistenceCache persistentCache = PersistenceCacheBuilder.of().name("imen").build();

        persistentCache.setEnabled(false);
        CacheKey key = CacheKey.obj("cacheExample","theta",Math.PI);
        Assertions.assertNull(persistentCache.getObjectCache(key, false));

        MyCacheEvaluator evaluator = new MyCacheEvaluator();
        for (int i = 0; i < 8; i++) {
            Object v = persistentCache.evaluate("cacheExample", null, evaluator, key);
            System.out.println(v);
        }
        Assertions.assertEquals(8, evaluator.getInvocationCount());
    }

    private static class MyCacheEvaluator implements CacheEvaluator {
        public int invocationCount;

        @Override
        public Object evaluate(Object[] args, ProgressMonitor cacheMonitor) {
            invocationCount++;
            System.out.println("am computing v = 3");
            return 3;
        }

        public int getInvocationCount() {
            return invocationCount;
        }
    }
}
