package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

public final class TStoreManagerFactory {
    public static <T> TStoreManager<T> create(TypeReference<T> type){
        TStoreManager<T> t = null;
        if (type.equals(Maths.$MATRIX)) {
            t = (TStoreManager<T>) Maths.MATRIX_STORE_MANAGER;
        } else if (type.equals(Maths.$VECTOR)) {
            t = (TStoreManager<T>) Maths.VECTOR_STORE_MANAGER;
        } else if (type.getTypeClass().equals(TVector.class)) {
            t = (TStoreManager<T>) Maths.TVECTOR_STORE_MANAGER;
        } else if (type.getTypeClass().equals(TMatrix.class)) {
            t = (TStoreManager<T>) Maths.TMATRIX_STORE_MANAGER;
        } else {
            throw new IllegalArgumentException("Unsupported store type " + type);
        }
        return t;
    }
}
