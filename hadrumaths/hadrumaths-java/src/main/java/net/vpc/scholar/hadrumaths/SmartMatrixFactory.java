package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 2/5/15.
 */
public class SmartMatrixFactory extends AbstractMatrixFactory {
    public static final SmartMatrixFactory INSTANCE = new SmartMatrixFactory();

    @Override
    public Matrix newMatrix(int rows, int columns) {
        MatrixFactory factory = MemMatrixFactory.INSTANCE;
        if ((rows>100 || columns>100) && !Maths.Config.memoryCanStores(rows * columns * 24L)) {
            factory = Maths.Config.getLargeMatrixFactory();
        }
        Matrix matrix = factory.newMatrix(rows, columns);
        matrix.setFactory(this);
        return matrix;
    }

//    @Override
//    public Matrix newIdentity(int rows, int cols) {
//        return new AbstractUnmodifiableMatrix(rows, cols, this) {
//            @Override
//            public Complex get(int row, int col) {
//                return (row == col) ? Complex.ONE : Complex.ZERO;
//            }
//        };
//    }
//
//    @Override
//    public Matrix newConstant(int rows, int cols, Complex value) {
//        return new AbstractUnmodifiableMatrix(rows, cols, this) {
//            @Override
//            public Complex get(int row, int col) {
//                return value;
//            }
//        };
//    }

    @Override
    public String getId() {
        return "smart";
    }


}
