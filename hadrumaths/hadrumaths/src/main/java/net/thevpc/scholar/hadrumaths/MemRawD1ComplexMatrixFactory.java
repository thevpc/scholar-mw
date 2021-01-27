package net.thevpc.scholar.hadrumaths;

/**
 * Created by vpc on 2/5/15.
 */
public class MemRawD1ComplexMatrixFactory extends AbstractComplexMatrixFactory {
    public static final MemRawD1ComplexMatrixFactory INSTANCE = new MemRawD1ComplexMatrixFactory();

    @Override
    public ComplexMatrix newMatrix(int rows, int columns) {
        ComplexMatrix m = new MemRawD1ComplexMatrix(rows, columns);
        m.setFactory(this);
        return m;
    }

    @Override
    public ComplexMatrix newConstant(int rows, int cols, Complex value) {
        return new AbstractUnmodifiableComplexMatrix(rows, cols, this) {
            @Override
            public Complex get(int row, int col) {
                return value;
            }
        };
    }

    @Override
    public ComplexMatrix newIdentity(int rows, int cols) {
        return new AbstractUnmodifiableComplexMatrix(rows, cols, this) {
            @Override
            public Complex get(int row, int col) {
                return (row == col) ? Complex.ONE : Complex.ZERO;
            }
        };
    }

    @Override
    public String getId() {
        return "complexMem";
    }

}
