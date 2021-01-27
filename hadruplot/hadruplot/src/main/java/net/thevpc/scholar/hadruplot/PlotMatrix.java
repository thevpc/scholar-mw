package net.thevpc.scholar.hadruplot;

import net.thevpc.common.util.ArrayUtils;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;
import net.thevpc.scholar.hadruplot.util.PlotUtils;

import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 11 oct. 2006 11:03:52
 */
public class PlotMatrix implements Serializable, Cloneable {
    Object[][] matrix;
    String[][] cellTitles;
    String[] rowTitles;
    String[] colTitles;
    double[] rows;
    double[] columns;
    String name;

    public PlotMatrix(Object[][] matrix) {
        this(matrix, null, null);
    }

    public PlotMatrix(Object[][] matrix, double[] columns, double[] rows) {
        this.matrix = matrix;
        this.rows = rows != null ? rows : ArrayUtils.dtimes(0.0, matrix.length == 0 ? 0 : matrix.length - 1, matrix.length);
        this.columns = columns != null ? columns : ArrayUtils.dtimes(0.0, matrix.length == 0 ? 0 : (matrix[0].length - 1), matrix.length == 0 ? 0 : (matrix[0].length));
    }

    public double[] getColumns() {
        return columns;
    }

    public Object[][] getMatrix() {
        return matrix;
    }

    public double[] getRows() {
        return rows;
    }

    public void mul(double mul) {
        for (Object[] complexes : matrix) {
            for (int j = 0; j < complexes.length; j++) {
                complexes[j] = PlotConfigManager.Numbers.mul(complexes[j], mul);
            }
        }
    }

    public String getName() {
        return name;
    }

    public PlotMatrix setName(String name) {
        this.name = name;
        return this;
    }

    public PlotMatrix setCellTitles(String[][] titles) {
        this.cellTitles = titles;
        return this;
    }

    public PlotMatrix setRowTitles(String[] titles) {
        this.rowTitles = titles;
        return this;
    }

    public PlotMatrix setColumnTitles(String[] titles) {
        this.colTitles = titles;
        return this;
    }

    public String[][] getCellTitles() {
        return cellTitles;
    }

    public String[] getRowTitles() {
        return rowTitles;
    }

    public String[] getColumnTitles() {
        return colTitles;
    }

    public PlotMatrix getRelativeMatrix(PlotMatrix referenceMatrix) {
        PlotMatrix modeledMatrix = this;
        Object[][] d = referenceMatrix.getMatrix();
        Object[][] m = modeledMatrix.getMatrix();
        if (d.length == m.length && (d.length == 0 || (d.length > 0 && d[0].length == m[0].length))) {//sinon non comparables
            Object[][] c = new Object[d.length][];
            c= PlotUtils.relativeError(d,m);
            return (new PlotMatrix(c, referenceMatrix.getColumns(), referenceMatrix.getRows()).setName("[%]"));
        }
        throw new IllegalArgumentException("Not comparable");
    }

    public PlotMatrix clone() {
        try {
            return (PlotMatrix) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public PlotMatrix appendRows(PlotMatrix other, String firstTitlePrefix, String seconTitlePrefix) {
        Object[][] matrix = new Object[this.matrix.length + other.matrix.length][];
        String[][] cellTitles = new String[(this.cellTitles == null ? 0 : this.cellTitles.length) + (other.cellTitles == null ? 0 : other.cellTitles.length)][];
        String[] rowTitles = new String[(this.rowTitles == null ? 0 : this.rowTitles.length) + (other.rowTitles == null ? 0 : other.rowTitles.length)];
        double[] rows = this.rows;
        double[] columns = new double[(this.columns == null ? 0 : this.columns.length) + (other.columns == null ? 0 : other.columns.length)];
        String name = this.name + ", " + other.name;
        if (this.matrix[0].length == other.matrix[0].length) {
            append(this.matrix, this.matrix.length, other.matrix, other.matrix.length, matrix);
        } else {
            int mm = Math.max(this.matrix[0].length, other.matrix[0].length);
            append(this.matrix, this.matrix.length, other.matrix, other.matrix.length, matrix);
            for (int i = 0; i < matrix.length; i++) {
                Object[] cc = matrix[i];
                if (cc.length < mm) {
                    Object[] cc2 = new Object[mm];
                    System.arraycopy(cc, 0, cc2, 0, cc.length);
                    for (int j = cc.length; j < cc2.length; j++) {
                        cc2[j] = Double.NaN;
                    }
                    matrix[i] = cc2;
                }
            }
        }
        append(this.cellTitles, this.cellTitles == null ? 0 : this.cellTitles.length, other.cellTitles, other.cellTitles == null ? 0 : other.cellTitles.length, cellTitles);
        if (firstTitlePrefix == null && seconTitlePrefix == null) {
            append(this.rowTitles, this.rowTitles == null ? 0 : this.rowTitles.length, other.rowTitles, other.rowTitles == null ? 0 : other.rowTitles.length, rowTitles);
        } else {
            for (int i = 0; i < rowTitles.length; i++) {
                int threshold = this.rowTitles == null ? 0 : this.rowTitles.length;
                if (i < threshold) {
                    rowTitles[i] = (firstTitlePrefix == null ? "" : firstTitlePrefix) + (this.rowTitles[i] == null ? "" : this.rowTitles[i]);
                } else {
                    rowTitles[i] = (seconTitlePrefix == null ? "" : seconTitlePrefix) + (other.rowTitles[i - threshold] == null ? "" : other.rowTitles[i - threshold]);
                }
            }
        }
        append(this.columns, this.columns == null ? 0 : this.columns.length, other.columns, other.columns == null ? 0 : other.columns.length, columns);
        return new PlotMatrix(matrix, columns, rows).setCellTitles(cellTitles).setName(name).setRowTitles(rowTitles);
    }

    private static void append(Object src1, int len1, Object src2, int len2, Object dest) {
        if (src1 != null) {
            System.arraycopy(src1, 0, dest, 0, len1);
        } else {
            len1 = 0;
        }
        if (src2 != null) {
            System.arraycopy(src2, 0, dest, len1, len2);
        }
    }

}
