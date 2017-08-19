package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.scholar.hadrumaths.*;

/**
 * implements CacheObjectSerializerProvider because the inner matrix is not necessary serializable
 */
public abstract class AbstractScalarProductCache implements ScalarProductCache {
    @Override
    public TVector<Complex> column(int column) {
        return getColumn(column);
    }

    @Override
    public TVector<Complex> row(int row) {
        return getRow(row);
    }
}
