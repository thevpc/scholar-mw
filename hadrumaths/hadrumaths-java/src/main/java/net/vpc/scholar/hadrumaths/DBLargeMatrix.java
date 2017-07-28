package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.cache.CacheObjectSerializedForm;

import java.io.File;
import java.io.IOException;

/**
 * Created by vpc on 4/4/17.
 */
public class DBLargeMatrix extends LargeMatrix {

    public DBLargeMatrix(long id, int rows, int columns, DBLargeMatrixFactory factory) {
        super(id, rows, columns, factory);
    }

    protected static LargeMatrixFactory createLargeMatrixFactory(boolean sparse, File file, Complex defaultValue) {
        return sparse ? DBLargeMatrixFactory.createLocalSparseStorage(null, file, defaultValue)
                : DBLargeMatrixFactory.createLocalStorage(null, file);
    }

    @Override
    public CacheObjectSerializedForm createCacheObjectSerializedForm(File file) throws IOException {
        boolean sparse = getFactory().isSparse();
        Complex defaultValue = getFactory().getDefaultValue();
        LargeMatrixFactory cacheFactory = createLargeMatrixFactory(sparse, file, defaultValue);
        cacheFactory.setResetOnClose(false);
        Matrix newMatrix = cacheFactory.newMatrix(this);
        long localId = ((LargeMatrix) newMatrix).getLargeMatrixId();
        cacheFactory.close();

        return new MatrixSer(localId, sparse, defaultValue);
    }

    private static class MatrixSer implements CacheObjectSerializedForm {
        long localId;
        boolean sparse;
        Complex defaultValue;

        public MatrixSer(long localId, boolean sparse, Complex defaultValue) {
            this.localId = localId;
            this.sparse = sparse;
            this.defaultValue = defaultValue;
        }

        @Override
        public Object deserialize(File file) throws IOException {
            LargeMatrixFactory cacheFactory = createLargeMatrixFactory(sparse, file, defaultValue);
            cacheFactory.setResetOnClose(false);
            return cacheFactory.findMatrix(localId);
        }
    }

}
