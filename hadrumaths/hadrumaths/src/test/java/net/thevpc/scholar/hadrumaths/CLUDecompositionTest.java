package net.thevpc.scholar.hadrumaths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * The primitive-array, row-parallel LU: residual of solve(), reconstruction P*A = L*U,
 * rectangular input, singular detection, and identical factors on repeated (parallel) runs.
 */
public class CLUDecompositionTest {

    private static ComplexMatrix random(int m, int n, long seed, double diag) {
        Random r = new Random(seed);
        Complex[][] a = new Complex[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = Complex.of(r.nextGaussian() + (i == j ? diag : 0), r.nextGaussian());
            }
        }
        return Maths.matrix(a);
    }

    private static double fro(ComplexMatrix a) {
        double s = 0;
        for (int i = 0; i < a.getRowCount(); i++) {
            for (int j = 0; j < a.getColumnCount(); j++) {
                s += a.get(i, j).absdblsqr();
            }
        }
        return Math.sqrt(s);
    }

    private static ComplexMatrix permuteRows(ComplexMatrix a, int[] p) {
        Complex[][] x = new Complex[p.length][a.getColumnCount()];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < a.getColumnCount(); j++) {
                x[i][j] = a.get(p[i], j);
            }
        }
        return Maths.matrix(x);
    }

    @Test
    public void solveResidualAndReconstruction() {
        // 300 is above the parallel threshold, the small sizes below it
        for (int n : new int[]{1, 2, 7, 64, 300}) {
            ComplexMatrix a = random(n, n, 11 + n, 0);
            ComplexMatrix b = random(n, 3, 17 + n, 0);
            CLUDecomposition lu = new CLUDecomposition(a);
            ComplexMatrix x = lu.solve(b);
            double res = fro(a.mul(x).sub(b)) / fro(b);
            Assertions.assertTrue(res < 1e-11, "n=" + n + " residual " + res);
            double rec = fro(lu.getL().mul(lu.getU()).sub(permuteRows(a, lu.getPivot()))) / fro(a);
            Assertions.assertTrue(rec < 1e-13, "n=" + n + " |LU - PA|/|A| " + rec);
        }
    }

    @Test
    public void matrixSolveUsesIt() {
        ComplexMatrix a = random(40, 40, 3, 5);
        ComplexMatrix b = random(40, 1, 4, 0);
        ComplexMatrix x = a.solve(b);
        Assertions.assertTrue(fro(a.mul(x).sub(b)) / fro(b) < 1e-12);
    }

    @Test
    public void determinantOfKnownMatrix() {
        // [[1+i, 2], [3, 4-i]]: det = (1+i)(4-i) - 6 = 5+3i - 6 = -1+3i
        ComplexMatrix a = Maths.matrix(new Complex[][]{
                {Complex.of(1, 1), Complex.of(2)},
                {Complex.of(3), Complex.of(4, -1)}});
        Complex d = new CLUDecomposition(a).det();
        Assertions.assertEquals(-1, d.getReal(), 1e-12);
        Assertions.assertEquals(3, d.getImag(), 1e-12);
    }

    @Test
    public void singularMatrixIsReported() {
        Complex[][] s = new Complex[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                s[i][j] = j == 1 ? Complex.ZERO : Complex.of(i + j + 1, i - j);
            }
        }
        CLUDecomposition lu = new CLUDecomposition(Maths.matrix(s));
        Assertions.assertFalse(lu.isNonsingular());
        Assertions.assertThrows(RuntimeException.class, () -> lu.solve(Maths.matrix(3, 1)));
    }

    @Test
    public void rectangularTallMatrixFactors() {
        ComplexMatrix a = random(9, 5, 21, 0);
        CLUDecomposition lu = new CLUDecomposition(a);
        ComplexMatrix l = lu.getL();
        ComplexMatrix u = lu.getU();
        double rec = fro(l.mul(u).sub(permuteRows(a, lu.getPivot()))) / fro(a);
        Assertions.assertTrue(rec < 1e-13, "|LU - PA|/|A| " + rec);
    }

    @Test
    public void parallelRunsAreBitIdentical() {
        ComplexMatrix a = random(400, 400, 99, 0);
        ComplexMatrix b = random(400, 2, 98, 0);
        ComplexMatrix x1 = new CLUDecomposition(a).solve(b);
        ComplexMatrix x2 = new CLUDecomposition(a).solve(b);
        for (int i = 0; i < 400; i++) {
            for (int j = 0; j < 2; j++) {
                Assertions.assertEquals(x1.get(i, j), x2.get(i, j));
            }
        }
    }
}
