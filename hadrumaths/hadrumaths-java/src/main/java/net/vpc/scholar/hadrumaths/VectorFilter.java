package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 8/1/14.
 */
public interface VectorFilter<T> {
    boolean accept(int index, T e);
}
