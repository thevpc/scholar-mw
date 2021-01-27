package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.cache.CacheObjectSerializerProvider;

/**
 * Created by vpc on 2/5/15.
 */
public abstract class LargeComplexMatrix extends AbstractComplexMatrix implements CacheObjectSerializerProvider {
    private static final long serialVersionUID = 1L;
    private String largeFactoryId;
    private transient LargeComplexMatrixFactory largeFactory;
    private final long largeMatrixId;
    private int rows;
    private int columns;

//    public static void main(String[] args) {
//        try {
//            File file = new File("/home/vpc/zzz/abc");
//
//            Matrix m = Maths.getLargeMatrixFactory().newMatrix("[1 2 3 ; 1 3 2]");
//            Matrix m2 = Maths.getLargeMatrixFactory().newMatrix(m);
//            System.out.println(m.format(null,null));
//            System.out.println(m2.format(null,null));
////            ObjectCache.storeObject(file, m);
//
////            Matrix o = (Matrix) ObjectCache.loadObject(file, null);
////            System.out.println(o.toString(null,null));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public LargeComplexMatrix(long id, int rows, int columns, LargeComplexMatrixFactory factory) {
        setLargeFactory(factory);
        this.largeMatrixId = id;
        this.rows = rows;
        this.columns = columns;
    }

    public long getLargeMatrixId() {
        return largeMatrixId;
    }

    @Override
    public Complex get(int row, int col) {
        return getLargeFactory().get(largeMatrixId, row, col);
    }

    public LargeComplexMatrixFactory getLargeFactory() {
        if (largeFactory == null) {
            return largeFactory = (LargeComplexMatrixFactory) Maths.Config.getTMatrixFactory(largeFactoryId);
        }
        return largeFactory;
    }

    public void setLargeFactory(LargeComplexMatrixFactory largeFactory) {
        this.largeFactory = largeFactory;
        this.largeFactoryId = largeFactory.getId();
        setFactory(largeFactory);
    }

    @Override
    public void set(int row, int col, Complex val) {
        getLargeFactory().set(largeMatrixId, val, row, col);
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    public String toString() {
        return "LargeMatrix[#" + largeMatrixId + "]{" + getRowCount() + "x" + getColumnCount() + "}@" + getFactory();
    }

    @Override
    public ComplexVector getRow(int row) {
        return new DefaultComplexVector(true, getLargeFactory().getRow(largeMatrixId, row, 0, getColumnCount()));
    }

    @Override
    public ComplexVector getColumn(int column) {
        return new DefaultComplexVector(false, getLargeFactory().getColumn(largeMatrixId, column, 0, getRowCount()));
    }

    @Override
    public void dispose() {
        getLargeFactory().disposeMatrix(largeMatrixId);
    }

    @Override
    public void resize(int rows, int columns) {
        getLargeFactory().resizeMatrix(largeMatrixId, rows, columns);
        this.rows = rows;
        this.columns = columns;
    }

}
