package net.vpc.scholar.hadrumaths;

import java.io.File;

public interface TStoreManager<T> {
    void store(T item, File file);

    T load(File file);
}
