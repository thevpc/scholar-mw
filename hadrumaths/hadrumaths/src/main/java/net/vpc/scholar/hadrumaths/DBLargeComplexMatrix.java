package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.cache.CacheObjectSerializedForm;
import net.vpc.scholar.hadrumaths.io.HFile;

import java.io.IOException;

/**
 * Created by vpc on 4/4/17.
 */
public class DBLargeComplexMatrix extends LargeComplexMatrix {
    private static final long serialVersionUID = 1L;

    public DBLargeComplexMatrix(long id, int rows, int columns, DBLargeComplexMatrixFactory factory) {
        super(id, rows, columns, factory);
    }

    @Override
    public CacheObjectSerializedForm createCacheObjectSerializedForm(HFile file) throws IOException {
        boolean sparse = getLargeFactory().isSparse();
        Complex defaultValue = getLargeFactory().getDefaultValue();
        LargeComplexMatrixFactory cacheFactory = createLargeMatrixFactory(sparse, file.getNativeLocalFile(), defaultValue);
        cacheFactory.setResetOnClose(false);
        ComplexMatrix newMatrix = cacheFactory.newMatrix(this);
        long localId = ((LargeComplexMatrix) newMatrix).getLargeMatrixId();
        cacheFactory.close();

        return new MatrixSer(localId, sparse, defaultValue);
    }

    protected static LargeComplexMatrixFactory createLargeMatrixFactory(boolean sparse, String file, Complex defaultValue) {
        return (LargeComplexMatrixFactory) Maths.Config.getTMatrixFactory(DBLargeComplexMatrixFactory.createLocalId(file, sparse, defaultValue));
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
        public Object deserialize(HFile file) throws IOException {
            LargeComplexMatrixFactory cacheFactory = createLargeMatrixFactory(sparse, file.getNativeLocalFile(), defaultValue);
            cacheFactory.setResetOnClose(false);
            return cacheFactory.findMatrix(localId);
        }
    }

}
