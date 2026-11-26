package net.thevpc.scholar.hadrumaths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The flat-storage complex matrix implements most operations itself (no virtual get/set in the
 * loops), so a row/column swap silently produces wrong numbers. This checks every operation
 * against the reference matrix, on a NON-square shape where a swap cannot hide.
 */
public class MemRawD1MatrixTest {

    static final int ROWS = 2, COLS = 3;

    /** value(i,j) = (10i + j) + i*(i - j + 1) */
    static Complex v(int i, int j) {
        return Complex.of(10 * i + j, i - j + 1);
    }

    static MemRawD1ComplexMatrix sample() {
        MemRawD1ComplexMatrix m = new MemRawD1ComplexMatrix(ROWS, COLS);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                m.set(i, j, v(i, j));
            }
        }
        return m;
    }

    @Test
    public void getSetAndShape() {
        MemRawD1ComplexMatrix m = sample();
        Assertions.assertEquals(ROWS, m.getRowCount());
        Assertions.assertEquals(COLS, m.getColumnCount());
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Assertions.assertEquals(v(i, j), m.get(i, j), "get(" + i + "," + j + ")");
            }
        }
    }

    @Test
    public void rowsAndColumns() {
        MemRawD1ComplexMatrix m = sample();
        for (int i = 0; i < ROWS; i++) {
            ComplexVector row = m.getRow(i);
            Assertions.assertEquals(COLS, row.size());
            for (int j = 0; j < COLS; j++) {
                Assertions.assertEquals(v(i, j), row.get(j), "row " + i + "[" + j + "]");
            }
        }
        for (int j = 0; j < COLS; j++) {
            ComplexVector col = m.getColumn(j);
            Assertions.assertEquals(ROWS, col.size());
            for (int i = 0; i < ROWS; i++) {
                Assertions.assertEquals(v(i, j), col.get(i), "col " + j + "[" + i + "]");
            }
        }
    }

    @Test
    public void subMatrixAndIndexedRows() {
        MemRawD1ComplexMatrix m = sample();
        ComplexMatrix sub = m.subMatrix(0, 1, 2, 2);
        Assertions.assertEquals(2, sub.getRowCount());
        Assertions.assertEquals(2, sub.getColumnCount());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Assertions.assertEquals(v(i, j + 1), sub.get(i, j), "sub(" + i + "," + j + ")");
            }
        }
        ComplexMatrix rows = m.getMatrix(new int[]{1, 0}, 0, 1);
        Assertions.assertEquals(2, rows.getRowCount());
        Assertions.assertEquals(2, rows.getColumnCount());
        Assertions.assertEquals(v(1, 0), rows.get(0, 0), "indexed row 0");
        Assertions.assertEquals(v(0, 1), rows.get(1, 1), "indexed row 1");
    }

    @Test
    public void transposes() {
        MemRawD1ComplexMatrix m = sample();
        ComplexMatrix t = m.transpose();
        Assertions.assertEquals(COLS, t.getRowCount());
        Assertions.assertEquals(ROWS, t.getColumnCount());
        ComplexMatrix h = m.transposeHermitian();
        ComplexMatrix a = m.arrayTranspose();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Assertions.assertEquals(v(i, j), t.get(j, i), "transpose[" + j + "," + i + "]");
                Assertions.assertEquals(v(i, j).conj(), h.get(j, i), "hermitian[" + j + "," + i + "]");
                Assertions.assertEquals(v(i, j), a.get(j, i), "arrayTranspose[" + j + "," + i + "]");
            }
        }
    }

    @Test
    public void scalarAndElementWiseArithmetic() {
        MemRawD1ComplexMatrix m = sample();
        Complex c = Complex.of(2, -1);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Assertions.assertEquals(v(i, j).mul(c), m.mul(c).get(i, j), "m*c");
                Assertions.assertEquals(v(i, j).div(c), m.div(c).get(i, j), "m/c");
                Assertions.assertEquals(v(i, j).add(c), m.add(c).get(i, j), "m+c");
                Assertions.assertEquals(v(i, j).minus(c), m.sub(c).get(i, j), "m-c");
                Assertions.assertEquals(v(i, j).neg(), m.neg().get(i, j), "neg");
                Assertions.assertEquals(v(i, j).absdblsqr(), m.abssqr().get(i, j).getReal(), 1e-12, "abssqr");
                Assertions.assertEquals(v(i, j).absdbl(), m.abs().get(i, j).getReal(), 1e-12, "abs");
            }
        }
        // getScalar is only meaningful for a 1x1 matrix
        MemRawD1ComplexMatrix one = new MemRawD1ComplexMatrix(1, 1);
        one.set(0, 0, v(1, 1));
        Assertions.assertEquals(v(1, 1), one.getScalar(), "getScalar on a 1x1");
    }

    @Test
    public void matrixArithmetic() {
        MemRawD1ComplexMatrix m = sample();
        MemRawD1ComplexMatrix n = sample();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Assertions.assertEquals(v(i, j).add(n.get(i, j)), m.add(n).get(i, j), "m+n");
                Assertions.assertEquals(v(i, j).minus(n.get(i, j)), m.sub(n).get(i, j), "m-n");
                Assertions.assertEquals(v(i, j).mul(n.get(i, j)), m.dotmul(n).get(i, j), "m.*n");
                Assertions.assertEquals(v(i, j).div(n.get(i, j)), m.dotdiv(n).get(i, j), "m./n");
            }
        }
    }

    @Test
    public void extremaAndArrayCopy() {
        MemRawD1ComplexMatrix m = sample();
        double max = 0, min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                max = Math.max(max, v(i, j).absdbl());
                min = Math.min(min, v(i, j).absdbl());
            }
        }
        Assertions.assertEquals(max, m.maxAbs(), 1e-12);
        Assertions.assertEquals(min, m.minAbs(), 1e-12);
        Complex[][] copy = m.getArrayCopy();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Assertions.assertEquals(v(i, j), copy[i][j], "arrayCopy[" + i + "][" + j + "]");
            }
        }
    }

    @Test
    public void resizeKeepsTheOverlappingCorner() {
        MemRawD1ComplexMatrix m = sample();
        m.resize(3, 2);
        Assertions.assertEquals(3, m.getRowCount());
        Assertions.assertEquals(2, m.getColumnCount());
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < 2; j++) {
                Assertions.assertEquals(v(i, j), m.get(i, j), "resize[" + i + "," + j + "]");
            }
        }
        Assertions.assertEquals(Complex.ZERO, m.get(2, 0), "new cell is zero");
    }

    @Test
    public void cofactorOfAThreeByThree() {
        MemRawD1ComplexMatrix m = new MemRawD1ComplexMatrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m.set(i, j, Complex.of(i * 3 + j, 0));
            }
        }
        MemRawD1ComplexMatrix co = m.coMatrix(1, 1);
        Assertions.assertEquals(2, co.getRowCount());
        Assertions.assertEquals(2, co.getColumnCount());
        // removing row 1 and col 1 from [[0,1,2],[3,4,5],[6,7,8]] leaves [[0,2],[6,8]]
        Assertions.assertEquals(Complex.of(0), co.get(0, 0));
        Assertions.assertEquals(Complex.of(2), co.get(0, 1));
        Assertions.assertEquals(Complex.of(6), co.get(1, 0));
        Assertions.assertEquals(Complex.of(8), co.get(1, 1));
    }
}
