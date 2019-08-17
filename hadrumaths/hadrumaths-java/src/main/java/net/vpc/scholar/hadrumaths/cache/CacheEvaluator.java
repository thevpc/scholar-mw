package net.vpc.scholar.hadrumaths.cache;

/**
 * Created by vpc on 8/29/14.
 */
public interface CacheEvaluator {
    default void init(){
    }
    Object evaluate(Object[] args);
}
