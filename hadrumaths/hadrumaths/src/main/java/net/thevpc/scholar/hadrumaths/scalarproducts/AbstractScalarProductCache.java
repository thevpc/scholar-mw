package net.thevpc.scholar.hadrumaths.scalarproducts;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexVector;
import net.thevpc.scholar.hadrumaths.Vector;

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
