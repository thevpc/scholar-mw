package net.vpc.scholar.hadrumaths.scalarproducts;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComplexVector;
import net.vpc.scholar.hadrumaths.Vector;

/**
 * implements CacheObjectSerializerProvider because the inner matrix is not necessary serializable
 */
public abstract class AbstractScalarProductCache implements ScalarProductCache {
    @Override
    public ComplexVector column(int column) {
        return getColumn(column);
    }

    @Override
    public ComplexVector row(int row) {
        return getRow(row);
    }
}
