package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.reflect.NTypeName;
import net.thevpc.nuts.reflect.NTypeNameDomain;
import net.thevpc.nuts.reflect.NTypeNamePlatformDomain;

public final class TStoreManagerFactory {
    public static <T> StoreManager<T> create(NTypeName<T> type) {
        StoreManager<T> t = null;
        if (type.equals(Maths.$MATRIX)) {
            t = (StoreManager<T>) Maths.MATRIX_STORE_MANAGER;
        } else if (type.equals(Maths.$VECTOR)) {
            t = (StoreManager<T>) Maths.VECTOR_STORE_MANAGER;
        } else if (NTypeNamePlatformDomain.of().getTypeClass(type).equals(Vector.class)) {
            t = (StoreManager<T>) Maths.TVECTOR_STORE_MANAGER;
        } else if (NTypeNamePlatformDomain.of().getTypeClass(type).equals(Matrix.class)) {
            t = (StoreManager<T>) Maths.TMATRIX_STORE_MANAGER;
        } else {
            throw new IllegalArgumentException("Unsupported store type " + type);
        }
        return t;
    }
}
