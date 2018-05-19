package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.cache.CacheObjectSerializedForm;
import net.vpc.scholar.hadrumaths.util.HFile;

import java.io.IOException;

/**
 * Created by vpc on 4/4/17.
 */
public class DBLargeMatrix extends LargeMatrix {
    private static final long serialVersionUID = 1L;

    public DBLargeMatrix(long id, int rows, int columns, DBLargeMatrixFactory factory) {
        super(id, rows, columns, factory);
    }

    protected static LargeMatrixFactory createLargeMatrixFactory(boolean sparse, String file, Complex defaultValue) {
        return (LargeMatrixFactory) Maths.Config.getTMatrixFactory(DBLargeMatrixFactory.createLocalId(file, sparse, defaultValue));
    }

    @Override
    public CacheObjectSerializedForm createCacheObjectSerializedForm(HFile file) throws IOException {
        boolean sparse = getLargeFactory().isSparse();
        Complex defaultValue = getLargeFactory().getDefaultValue();
        LargeMatrixFactory cacheFactory = createLargeMatrixFactory(sparse, file.getNativeLocalFile(), defaultValue);
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
        public Object deserialize(HFile file) throws IOException {
            LargeMatrixFactory cacheFactory = createLargeMatrixFactory(sparse, file.getNativeLocalFile(), defaultValue);
            cacheFactory.setResetOnClose(false);
            return cacheFactory.findMatrix(localId);
        }
    }

}
