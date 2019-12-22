package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public final class TStoreManagerFactory {
    public static <T> TStoreManager<T> create(TypeName<T> type){
        TStoreManager<T> t = null;
        if (type.equals(MathsBase.$MATRIX)) {
            t = (TStoreManager<T>) MathsBase.MATRIX_STORE_MANAGER;
        } else if (type.equals(MathsBase.$VECTOR)) {
            t = (TStoreManager<T>) MathsBase.VECTOR_STORE_MANAGER;
        } else if (type.getTypeClass().equals(TVector.class)) {
            t = (TStoreManager<T>) MathsBase.TVECTOR_STORE_MANAGER;
        } else if (type.getTypeClass().equals(TMatrix.class)) {
            t = (TStoreManager<T>) MathsBase.TMATRIX_STORE_MANAGER;
        } else {
            throw new IllegalArgumentException("Unsupported store type " + type);
        }
        return t;
    }
}
