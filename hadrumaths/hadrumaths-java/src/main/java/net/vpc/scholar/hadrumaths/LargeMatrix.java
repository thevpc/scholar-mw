package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.cache.CacheObjectSerializerProvider;

/**
 * Created by vpc on 2/5/15.
 */
public abstract class LargeMatrix extends AbstractMatrix implements CacheObjectSerializerProvider {
    private long largeMatrixId;
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

    public LargeMatrix(long id, int rows, int columns, LargeMatrixFactory factory) {
        setFactory(factory);
        this.largeMatrixId = id;
        this.rows = rows;
        this.columns = columns;
    }


    public long getLargeMatrixId() {
        return largeMatrixId;
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public Complex get(int row, int col) {
        return getFactory().get(largeMatrixId, row, col);
    }

    @Override
    public Vector getColumn(int column) {
        return new ArrayVector(getFactory().getColumn(largeMatrixId, column, 0, getRowCount()), false);
    }

    @Override
    public Vector getRow(int row) {
        return new ArrayVector(getFactory().getRow(largeMatrixId, row, 0, getColumnCount()), true);
    }

    @Override
    public void set(int row, int col, Complex val) {
        getFactory().set(largeMatrixId, val, row, col);
    }

    @Override
    public void resize(int rows, int columns) {
        getFactory().resizeMatrix(largeMatrixId, rows, columns);
        this.rows=rows;
        this.columns=columns;
    }

    @Override
    public LargeMatrixFactory getFactory() {
        return (LargeMatrixFactory) super.getFactory();
    }

    @Override
    public void setFactory(TMatrixFactory<Complex> factory) {
        super.setFactory((LargeMatrixFactory) factory);
    }

    @Override
    public void dispose() {
        getFactory().disposeMatrix(largeMatrixId);
    }

    protected MatrixFactory createDefaultFactory() {
        return Maths.Config.getLargeMatrixFactory();
    }


    public String toString() {
        return "LargeMatrix[#" + largeMatrixId + "]{" + getRowCount() + "x" + getColumnCount() + "}@" + getFactory();
    }

}
