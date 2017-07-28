package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.cache.Evaluator;
import net.vpc.scholar.hadrumaths.cache.PersistenceCache;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

/**
 * Created by vpc on 10/12/16.
 */
public class TestCache {
    public static void main(String[] args) {
        PersistenceCache persistentCache=new PersistenceCache(null,"imen",null);
        persistentCache.setEnabled(true);
        Dumper dumper=new Dumper("calkile",Dumper.Type.SIMPLE);
        dumper.add("theta",Math.PI);

        Object v=persistentCache.evaluate("calkile", null,new Evaluator() {
            @Override
            public Object evaluate(Object[] args) {
                System.out.println("je calcule v = 3");
                return 3;
            }
        },dumper);
        System.out.println(v);
    }
}
