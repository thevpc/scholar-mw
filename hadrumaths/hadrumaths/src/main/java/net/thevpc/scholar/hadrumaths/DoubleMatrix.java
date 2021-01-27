package net.thevpc.scholar.hadrumaths;

public interface DoubleMatrix extends Matrix<Double> {
    double getDouble(int row, int col);

    void set(int row, int col, double val);

    double[] getRowDouble(int row);

    double[] getColumnDouble(int column);
}
