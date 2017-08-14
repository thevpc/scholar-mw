package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 2/5/15.
 */
public class MemMatrixFactory extends AbstractMatrixFactory{
    public static final MemMatrixFactory INSTANCE=new MemMatrixFactory();
    @Override
    public Matrix newMatrix(int rows, int columns) {
        Matrix m = new MemMatrix(rows, columns);
//        Matrix m = new MemRawD1Matrix(rows, columns);
        m.setFactory(this);
        return m;
    }

    @Override
    public  Matrix newIdentity(int rows, int cols) {
        return new AbstractUnmodifiableMatrix(rows,cols,this) {
            @Override
            public Complex get(int row, int col) {
                return (row == col) ? Complex.ONE : Complex.ZERO;
            }
        };
    }

    @Override
    public  Matrix newConstant(int rows, int cols, Complex value) {
        return new AbstractUnmodifiableMatrix(rows,cols,this) {
            @Override
            public Complex get(int row, int col) {
                return value;
            }
        };
    }

    @Override
    public String getId() {
        return "mem";
    }
}
