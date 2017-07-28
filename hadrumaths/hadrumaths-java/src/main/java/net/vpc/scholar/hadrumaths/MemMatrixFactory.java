package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 2/5/15.
 */
public class MemMatrixFactory extends AbstractMatrixFactory{
    public static final MemMatrixFactory INSTANCE=new MemMatrixFactory();
    @Override
    public Matrix newMatrix(int rows, int columns) {
        MemMatrix m = new MemMatrix(rows, columns);
        m.setFactory(this);
        return m;
    }
}
