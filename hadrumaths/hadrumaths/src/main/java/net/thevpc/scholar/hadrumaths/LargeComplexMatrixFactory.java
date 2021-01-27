package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.strings.StringUtils;

import java.io.Closeable;
import java.util.logging.Logger;

/**
 * Created by vpc on 2/5/15.
 */
public abstract class LargeComplexMatrixFactory extends AbstractComplexMatrixFactory implements Closeable {
    private static final Logger log = Logger.getLogger(LargeComplexMatrixFactory.class.getName());
    private boolean resetOnClose;
    private boolean sparse;
    private Complex defaultValue;
    private String id;

    protected LargeComplexMatrixFactory() {

    }

    public LargeComplexMatrixFactory(String id, boolean sparse, Complex defaultValue) {
        init(id, sparse, defaultValue);
    }

    protected void init(String id, boolean sparse, Complex defaultValue) {
        if (id == null) {
            throw new IllegalArgumentException("Id should not be null");
        }
        this.id = StringUtils.isBlank(id) ? "large-matrix" : id;
        this.sparse = sparse;
        if (defaultValue == null) {
            defaultValue = Complex.ZERO;
        }
        this.defaultValue = defaultValue;

    }

    public boolean isSparse() {
        return sparse;
    }

    public Complex getDefaultValue() {
        return defaultValue;
    }

    public boolean isResetOnClose() {
        return resetOnClose;
    }

    public void setResetOnClose(boolean resetOnClose) {
        this.resetOnClose = resetOnClose;
    }

    public abstract void dispose();

    public abstract ComplexMatrix findMatrix(long id);

    public abstract void clear();

    public abstract Long getId(ComplexMatrix m);

    public abstract Complex get(long id, int row, int col);

    public abstract Complex[] getRow(long id, int row, int fromCol, int toCol);

    public abstract Complex[] getColumn(long id, int column, int fromRow, int toRow);

    public abstract void set(long id, Complex val, int row, int col);

    public abstract void resizeMatrix(long id, int rows, int columns);

    public abstract void disposeMatrix(long id);

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    public String getId() {
        return id;
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
    public String toString() {
        return "LargeMatrixFactory{" +
                ", resetOnClose=" + resetOnClose +
                ", sparse=" + sparse +
                ", defaultValue=" + defaultValue +
                '}';
    }
}
