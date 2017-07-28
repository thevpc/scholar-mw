package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.scholar.hadrumaths.*;

/**
 * implements CacheObjectSerializerProvider because the inner matrix is not necessary serializable
 */
public abstract class AbstractScalarProductCache implements ScalarProductCache {
    @Override
    public Vector column(int column) {
        return getColumn(column);
    }

    @Override
    public Vector row(int row) {
        return getRow(row);
    }
}
