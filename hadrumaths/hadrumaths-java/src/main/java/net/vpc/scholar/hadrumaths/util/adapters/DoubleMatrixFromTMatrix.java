package net.vpc.scholar.hadrumaths.util.adapters;

import net.vpc.scholar.hadrumaths.DoubleMatrix;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.TMatrix;

/**
 * Created by vpc on 3/23/17.
 */
public class DoubleMatrixFromTMatrix<T> extends TMatrixAdapter<T, Double> implements DoubleMatrix {
    private static final long serialVersionUID = 1L;

    public DoubleMatrixFromTMatrix(TMatrix<T> matrix) {
        super(matrix, MathsBase.$DOUBLE);
    }

    @Override
    public double getDouble(int row, int col) {
        return get(row, col);
    }

    @Override
    public void set(int row, int col, double val) {
        set(row, col, (Double) val);
    }

    @Override
    public double[] getRowDouble(int row) {
        double[] all = new double[getColumnCount()];
        for (int i = 0; i < all.length; i++) {
            all[i] = getDouble(row, i);
        }
        return all;
    }

    @Override
    public double[] getColumnDouble(int column) {
        double[] all = new double[getRowCount()];
        for (int i = 0; i < all.length; i++) {
            all[i] = getDouble(i, column);
        }
        return all;
    }

}
