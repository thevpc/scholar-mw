package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.StringUtils;

import java.io.Closeable;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by vpc on 2/5/15.
 */
public abstract class LargeMatrixFactory extends AbstractMatrixFactory implements Closeable {
    private static Logger log = Logger.getLogger(LargeMatrixFactory.class.getName());
    private boolean resetOnClose;
    private boolean sparse;
    private Complex defaultValue;
    private String id;

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

    public LargeMatrixFactory(String id,boolean sparse, Complex defaultValue) {
        this.id = StringUtils.isEmpty(id)?"large-matrix":id;
        this.sparse = sparse;
        if (defaultValue == null) {
            defaultValue = Complex.ZERO;
        }
        this.defaultValue = defaultValue;

    }


    public abstract void dispose() ;

    public abstract Matrix findMatrix(long id) ;

    public abstract void clear() ;

    public abstract Long getId(Matrix m) ;

    public abstract Complex get(long id, int row, int col) ;

    public abstract Complex[] getRow(long id, int row, int fromCol,int toCol) ;

    public abstract Complex[] getColumn(long id, int column, int fromRow,int toRow) ;

    public abstract void set(long id, Complex val, int row, int col) ;

    public abstract void resizeMatrix(long id, int rows, int columns) ;

    public abstract void disposeMatrix(long id) ;

    @Override
    public String toString() {
        return "LargeMatrixFactory{" +
                ", resetOnClose=" + resetOnClose +
                ", sparse=" + sparse +
                ", defaultValue=" + defaultValue +
                '}';
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    public String getFactoryId(){
        return id;
    }
}
