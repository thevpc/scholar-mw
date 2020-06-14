package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public final class TStoreManagerFactory {
    public static <T> StoreManager<T> create(TypeName<T> type) {
        StoreManager<T> t = null;
        if (type.equals(Maths.$MATRIX)) {
            t = (StoreManager<T>) Maths.MATRIX_STORE_MANAGER;
        } else if (type.equals(Maths.$VECTOR)) {
            t = (StoreManager<T>) Maths.VECTOR_STORE_MANAGER;
        } else if (type.getTypeClass().equals(Vector.class)) {
            t = (StoreManager<T>) Maths.TVECTOR_STORE_MANAGER;
        } else if (type.getTypeClass().equals(Matrix.class)) {
            t = (StoreManager<T>) Maths.TMATRIX_STORE_MANAGER;
        } else {
            throw new IllegalArgumentException("Unsupported store type " + type);
        }
        return t;
    }
}
